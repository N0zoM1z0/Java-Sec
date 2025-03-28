package com.publiccms.controller.web.trade;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.alipay.easysdk.kernel.util.Signer;
import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.api.PaymentGateway;
import com.publiccms.common.api.TradePaymentProcessor;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.trade.TradePayment;
import com.publiccms.entities.trade.TradePaymentHistory;
import com.publiccms.entities.trade.TradeRefund;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.config.SafeConfigComponent;
import com.publiccms.logic.component.paymentgateway.AlipayGatewayComponent;
import com.publiccms.logic.component.paymentgateway.WechatGatewayComponent;
import com.publiccms.logic.component.trade.PaymentGatewayComponent;
import com.publiccms.logic.component.trade.PaymentProcessorComponent;
import com.publiccms.logic.service.trade.TradePaymentHistoryService;
import com.publiccms.logic.service.trade.TradePaymentService;
import com.publiccms.logic.service.trade.TradeRefundService;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("tradePayment")
public class TradePaymentController {
    protected final Log log = LogFactory.getLog(getClass());

    /**
     * @param site
     * @param paymentId
     * @param paymentType
     * @param returnUrl
     * @param request
     * @param response
     * @param model
     * @throws Exception
     */
    @RequestMapping(value = "pay")
    public void pay(@RequestAttribute SysSite site, Long paymentId, String paymentType, String returnUrl,
            HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
        returnUrl = safeConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        TradePayment entity = service.getEntity(paymentId);
        PaymentGateway paymentGateway = gatewayComponent.get(entity.getAccountType());
        if (null == paymentGateway || null == entity
                || ControllerUtils.errorNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
            log.info("pay parameter error");
            response.sendRedirect(returnUrl);
        } else if (!paymentGateway.pay(site, entity, paymentType, returnUrl, response)) {
            log.info("pay error");
            response.sendRedirect(returnUrl);
        }
    }

    /**
     * @param site
     * @param paymentId
     * @param returnUrl
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "cancel")
    @Csrf
    public String cancel(@RequestAttribute SysSite site, Long paymentId, String returnUrl, HttpServletRequest request,
            RedirectAttributes model) throws Exception {
        returnUrl = safeConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        TradePayment entity = service.getEntity(paymentId);
        if (null == entity || ControllerUtils.errorNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
            return new StringBuilder(UrlBasedViewResolver.REDIRECT_URL_PREFIX).append(returnUrl).toString();
        }
        TradePaymentProcessor paymentProcessor = paymentProcessorComponent.get(entity.getTradeType());
        if (null != paymentProcessor && service.cancel(site.getId(), paymentId)) {
            paymentProcessor.cancel(site.getId(), entity);
        }
        return new StringBuilder(UrlBasedViewResolver.REDIRECT_URL_PREFIX).append(returnUrl).toString();
    }

    /**
     * @param site
     * @param out_trade_no
     * @param total_amount
     * @param trade_no
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "notify/alipay")
    @ResponseBody
    public String notifyAlipay(@RequestAttribute SysSite site, long out_trade_no, String total_amount, String trade_no,
            HttpServletRequest request) throws Exception {
        log.info(new StringBuilder("alipay notify out_trade_no:").append(out_trade_no).append(",total_amount:")
                .append(total_amount).append(",trade_no:").append(trade_no).toString());
        Map<String, String> config = configComponent.getConfigData(site.getId(), AlipayGatewayComponent.CONFIG_CODE);
        if (CommonUtils.notEmpty(config)) {
            Map<String, String> params = request.getParameterMap().entrySet().stream()
                    .collect(Collectors.toMap(e -> e.getKey(), e -> StringUtils.join(e.getValue(), ",")));
            try {
                if (Signer.verifyParams(params, config.get(AlipayGatewayComponent.CONFIG_ALIPAY_PUBLIC_KEY))) {
                    try {
                        TradePaymentHistory history = new TradePaymentHistory(site.getId(), out_trade_no, CommonUtils.getDate(),
                                TradePaymentHistoryService.OPERATE_NOTIFY, JsonUtils.getString(params));
                        historyService.save(history);
                        TradePayment payment = service.getEntity(out_trade_no);
                        if (null != payment && payment.getStatus() == TradePaymentService.STATUS_PENDING_PAY
                                && payment.getAmount().toString().equals(total_amount)) {
                            if (service.paid(site.getId(), payment.getId(), trade_no)) {
                                payment = service.getEntity(payment.getId());
                                alipayGatewayComponent.confirmPay(site.getId(), payment);
                            }
                        }
                        return "success";
                    } catch (NumberFormatException e) {
                        log.info(e.getMessage());
                    }
                } else {
                    log.info("response verify error");
                }
            } catch (Exception e) {
                log.info(e.getMessage());
            }
        }
        return "fail";

    }

    /**
     * @param site
     * @param signature
     * @param timestamp
     * @param nonce
     * @param serial
     * @param body
     * @return result
     * @throws Exception
     */
    @RequestMapping(value = "notify/wechat")
    @ResponseBody
    public Map<String, String> notifyWechat(@RequestAttribute SysSite site,
            @RequestHeader(value = "Wechatpay-Signature") String signature,
            @RequestHeader(value = "Wechatpay-Timestamp") String timestamp,
            @RequestHeader(value = "Wechatpay-Nonce") String nonce, @RequestHeader(value = "Wechatpay-Serial") String serial,
            @RequestBody String body) throws Exception {
        log.info(new StringBuilder("wechat notify signature:").append(signature).append(",serial:").append(serial)
                .append(",timestamp:").append(timestamp).append(",nonce:").append(nonce).append(",body:").append(body)
                .toString());
        Map<String, String> config = configComponent.getConfigData(site.getId(), WechatGatewayComponent.CONFIG_CODE);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("code", "FAIL");
        resultMap.put("message", "error");
        if (CommonUtils.notEmpty(config) && CommonUtils.notEmpty(config.get(WechatGatewayComponent.CONFIG_KEY))) {
            byte[] apiV3Key = config.get(WechatGatewayComponent.CONFIG_KEY).getBytes(CommonConstants.DEFAULT_CHARSET);
            Verifier verifier = wechatGatewayComponent.getVerifier(config, apiV3Key);
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(timestamp).append("\n").append(nonce).append("\n").append(body).append("\n");
                if (verifier.verify(serial, sb.toString().getBytes(CommonConstants.DEFAULT_CHARSET), signature)) {
                    Map<String, Object> result = CommonConstants.objectMapper.readValue(body, CommonConstants.objectMapper
                            .getTypeFactory().constructMapLikeType(HashMap.class, String.class, Object.class));
                    @SuppressWarnings("unchecked")
                    Map<String, String> resource = (Map<String, String>) result.get("resource");
                    if (null != resource) {
                        AesUtil decryptor = new AesUtil(apiV3Key);
                        String decodeResult = decryptor.decryptToString(
                                resource.get("associated_data").replaceAll("\"", "").getBytes(CommonConstants.DEFAULT_CHARSET),
                                resource.get("nonce").replaceAll("\"", "").getBytes(CommonConstants.DEFAULT_CHARSET),
                                resource.get("ciphertext"));
                        Map<String, Object> data = CommonConstants.objectMapper.readValue(decodeResult,
                                CommonConstants.objectMapper.getTypeFactory().constructMapLikeType(HashMap.class, String.class,
                                        Object.class));
                        long paymentId = Long.parseLong((String) data.get("out_trade_no"));
                        TradePaymentHistory history = new TradePaymentHistory(site.getId(), paymentId, CommonUtils.getDate(),
                                TradePaymentHistoryService.OPERATE_NOTIFY, decodeResult);
                        historyService.save(history);
                        if ("REFUND.SUCCESS".equalsIgnoreCase((String) result.get("event_type"))) {
                            @SuppressWarnings("unchecked")
                            Map<String, Integer> amount = (Map<String, Integer>) data.get("amount");
                            if ("SUCCESS".equalsIgnoreCase((String) data.get("refund_status")) && null != amount) {
                                TradePayment payment = service.getEntity(paymentId);
                                TradeRefund refund = refundService.getEntity(Long.parseLong((String) data.get("out_refund_no")));
                                if (null != payment && null != refund
                                        && (refund.getStatus() == TradeRefundService.STATUS_PENDING
                                                || refund.getStatus() == TradeRefundService.STATUS_FAIL)
                                        && (refund.getRefundAmount().multiply(new BigDecimal(100))).intValue() == amount
                                                .get("refund")) {
                                    TradePaymentProcessor tradePaymentProcessor = tradePaymentProcessorComponent
                                            .get(payment.getTradeType());
                                    if (null != tradePaymentProcessor && tradePaymentProcessor.refunded(site.getId(), payment)) {
                                        service.refunded(site.getId(), payment.getId());
                                        refundService.updateStatus(site.getId(), refund.getId(), refund.getRefundUserId(), null,
                                                TradeRefundService.STATUS_REFUNDED);
                                        resultMap.put("code", "SUCCESS");
                                        resultMap.put("message", "OK");
                                        log.info("OK");
                                    } else {
                                        log.info("order status update error");
                                        resultMap.put("message", "order status update error");
                                    }
                                } else {
                                    log.info("payment status error");
                                    resultMap.put("message", "payment status error");
                                }
                            } else {
                                log.info("error trade_state");
                                resultMap.put("message", "error trade_state");
                            }
                        } else if ("TRANSACTION.SUCCESS".equalsIgnoreCase((String) result.get("event_type"))) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> amount = (Map<String, Object>) data.get("amount");
                            if ("SUCCESS".equalsIgnoreCase((String) data.get("trade_state")) && null != amount) {
                                TradePayment payment = service.getEntity(paymentId);
                                if (null != payment && payment.getStatus() == TradePaymentService.STATUS_PENDING_PAY
                                        && (payment.getAmount().multiply(new BigDecimal(100)))
                                                .intValue() == (int) amount.get("payer_total")) {
                                    payment = service.getEntity(paymentId);
                                    if (service.paid(site.getId(), payment.getId(), (String) result.get("transaction_id"))) {
                                        if (wechatGatewayComponent.confirmPay(site.getId(), payment)) {
                                            resultMap.put("code", "SUCCESS");
                                            resultMap.put("message", "OK");
                                            log.info("OK");
                                        } else {
                                            log.info("payment confirm error");
                                            resultMap.put("message", "payment confirm error");
                                        }
                                    } else {
                                        log.info("payment status update error");
                                        resultMap.put("message", "payment status update error");
                                    }
                                } else {
                                    log.info("payment status error");
                                    resultMap.put("message", "payment status error");
                                }
                            } else {
                                log.info("error trade_state");
                                resultMap.put("message", "error trade_state");
                            }
                        }
                    } else {
                        log.info("response error empty resource");
                        resultMap.put("message", "response error empty resource");
                    }
                } else {
                    log.info("response verify error");
                    resultMap.put("message", "response verify error");
                }
            } catch (Exception e) {
                log.info(e.getMessage());
                resultMap.put("message", e.getMessage());
            }
        } else {
            log.info("empty config");
            resultMap.put("message", "empty config");
        }
        return resultMap;
    }

    /**
     * @param site
     * @param user
     * @param entity
     * @param returnUrl
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "refund")
    @Csrf
    public String refund(@RequestAttribute SysSite site, @SessionAttribute SysUser user, TradeRefund entity, String returnUrl,
            HttpServletRequest request, RedirectAttributes model) {
        returnUrl = safeConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        if (null != user && ControllerUtils.errorCustom("tradePaymentStatus",
                !service.pendingRefund(site.getId(), entity.getPaymentId()), model)) {
            return new StringBuilder(UrlBasedViewResolver.REDIRECT_URL_PREFIX).append(returnUrl).toString();
        }
        if (null == entity.getId()) {
            entity.setSiteId(site.getId());
            entity.setRefundAmount(null);
            entity.setReply(null);
            entity.setRefundUserId(null);
            entity.setUserId(user.getId());
            entity.setStatus(TradeRefundService.STATUS_PENDING);
            entity.setCreateDate(null);
            refundService.save(entity);
        } else {
            refundService.updateAmound(entity.getId(), user.getId(), entity.getAmount(), entity.getReason());
        }
        return new StringBuilder(UrlBasedViewResolver.REDIRECT_URL_PREFIX).append(returnUrl).toString();
    }

    /**
     * @param site
     *            站点
     * @param user
     *            用户
     * @param refundId
     *            退款id
     * @param returnUrl
     *            重定向页面地址
     * @param request
     * @return 重定向页面地址
     */
    @RequestMapping(value = "cancelRefund")
    @Csrf
    public String cancel(@RequestAttribute SysSite site, @SessionAttribute SysUser user, long refundId, String returnUrl,
            HttpServletRequest request) {
        returnUrl = safeConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        refundService.updateStatus(site.getId(), refundId, null, user.getId(), TradeRefundService.STATUS_CANCELLED);
        return new StringBuilder(UrlBasedViewResolver.REDIRECT_URL_PREFIX).append(returnUrl).toString();
    }

    @Resource
    private PaymentProcessorComponent tradePaymentProcessorComponent;
    @Resource
    private PaymentProcessorComponent paymentProcessorComponent;
    @Resource
    private PaymentGatewayComponent gatewayComponent;
    @Resource
    private WechatGatewayComponent wechatGatewayComponent;
    @Resource
    private AlipayGatewayComponent alipayGatewayComponent;
    @Resource
    protected ConfigComponent configComponent;
    @Resource
    protected SafeConfigComponent safeConfigComponent;
    @Resource
    private TradePaymentService service;
    @Resource
    private TradeRefundService refundService;
    @Resource
    private TradePaymentHistoryService historyService;

}
