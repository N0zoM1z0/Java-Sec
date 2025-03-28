package com.publiccms.controller.web.cms;

import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.controller.admin.cms.CmsContentAdminController;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryModel;
import com.publiccms.entities.cms.CmsCategoryModelId;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.config.SafeConfigComponent;
import com.publiccms.logic.component.site.LockComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.site.StatisticsComponent;
import com.publiccms.logic.component.template.ModelComponent;
import com.publiccms.logic.service.cms.CmsCategoryModelService;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.views.pojo.entities.ClickStatistics;
import com.publiccms.views.pojo.entities.CmsModel;
import com.publiccms.views.pojo.model.CmsContentParameters;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 
 * ContentController 内容
 *
 */
@Controller
@RequestMapping("content")
public class ContentController {
    @Resource
    private StatisticsComponent statisticsComponent;
    @Resource
    private CmsCategoryModelService categoryModelService;
    @Resource
    private CmsCategoryService categoryService;
    @Resource
    private ModelComponent modelComponent;
    @Resource
    protected LogOperateService logOperateService;
    @Resource
    protected SiteComponent siteComponent;
    @Resource
    private LockComponent lockComponent;
    @Resource
    protected SafeConfigComponent safeConfigComponent;

    /**
     * 保存内容
     * 
     * @param site
     * 
     * @param entity
     * @param user
     * @param draft
     * @param captcha
     * @param attribute
     * @param contentParameters
     * @param returnUrl
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @Csrf
    public String save(@RequestAttribute SysSite site, CmsContent entity, @SessionAttribute SysUser user, Boolean draft,
            String captcha, CmsContentAttribute attribute, @ModelAttribute CmsContentParameters contentParameters,
            String returnUrl, HttpServletRequest request, RedirectAttributes model) {
        returnUrl = safeConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        boolean locked = lockComponent.isLocked(site.getId(), LockComponent.ITEM_TYPE_CONTRIBUTE, String.valueOf(user.getId()),
                null);
        if (ControllerUtils.errorCustom("locked.user", locked, model)) {
            lockComponent.lock(site.getId(), LockComponent.ITEM_TYPE_CONTRIBUTE, String.valueOf(user.getId()), null, true);
            return new StringBuilder(UrlBasedViewResolver.REDIRECT_URL_PREFIX).append(returnUrl).toString();
        }
        if (CommonUtils.notEmpty(captcha)
                || safeConfigComponent.enableCaptcha(site.getId(), SafeConfigComponent.CAPTCHA_MODULE_CONTRIBUTE)) {
            String sessionCaptcha = (String) request.getSession().getAttribute("captcha");
            request.getSession().removeAttribute("captcha");
            if (ControllerUtils.errorCustom("captcha.error", null == sessionCaptcha || !sessionCaptcha.equalsIgnoreCase(captcha),
                    model)) {
                return new StringBuilder(UrlBasedViewResolver.REDIRECT_URL_PREFIX).append(returnUrl).toString();
            }
        }
        CmsCategoryModel categoryModel = categoryModelService
                .getEntity(new CmsCategoryModelId(entity.getCategoryId(), entity.getModelId()));
        if (ControllerUtils.errorNotEmpty("categoryModel", categoryModel, model)
                || ControllerUtils.errorCustom("contribute", null == user, model)) {
            return new StringBuilder(UrlBasedViewResolver.REDIRECT_URL_PREFIX).append(returnUrl).toString();
        }
        CmsCategory category = categoryService.getEntity(entity.getCategoryId());
        if (null != category && (site.getId() != category.getSiteId() || !category.isAllowContribute())) {
            category = null;
        }
        CmsModel cmsModel = modelComponent.getModel(site, entity.getModelId());
        if (ControllerUtils.errorNotEmpty("category", category, model)
                || ControllerUtils.errorNotEmpty("model", cmsModel, model)) {
            return new StringBuilder(UrlBasedViewResolver.REDIRECT_URL_PREFIX).append(returnUrl).toString();
        }
        CmsContentAdminController.initContent(entity, site, cmsModel, draft, false, attribute, false, CommonUtils.getDate());
        if (null != entity.getId()) {
            CmsContent oldEntity = service.getEntity(entity.getId());
            if (null != oldEntity && ControllerUtils.errorNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)
                    && (oldEntity.getUserId() == user.getId() || user.isSuperuser())) {
                entity = service.update(entity.getId(), entity, entity.isOnlyUrl() ? CmsContentAdminController.ignoreProperties
                        : CmsContentAdminController.ignorePropertiesWithUrl);
                if (null != entity.getId()) {
                    logOperateService.save(new LogOperate(site.getId(), user.getId(), user.getDeptId(),
                            LogLoginService.CHANNEL_WEB, "update.content", RequestUtils.getIpAddress(request),
                            CommonUtils.getDate(), JsonUtils.getString(entity)));
                }
            }
        } else {
            entity.setContribute(true);
            entity.setSiteId(site.getId());
            entity.setUserId(user.getId());
            entity.setDisabled(false);
            entity.setClicks(0);
            entity.setComments(0);
            entity.setChilds(0);
            entity.setScores(0);
            entity.setScoreUsers(0);
            entity.setScore(BigDecimal.ZERO);
            service.save(entity);
            if (CommonUtils.notEmpty(entity.getParentId())) {
                service.updateChilds(entity.getParentId(), 1);
            }
            logOperateService.save(new LogOperate(site.getId(), user.getId(), user.getDeptId(), LogLoginService.CHANNEL_WEB,
                    "save.content", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        lockComponent.lock(site.getId(), LockComponent.ITEM_TYPE_CONTRIBUTE, String.valueOf(user.getId()), null, true);
        service.saveTagAndAttribute(site, user.getId(), entity.getId(), contentParameters, cmsModel, category.getExtendId(),
                attribute);
        model.addAttribute("id", entity.getId());
        return new StringBuilder(UrlBasedViewResolver.REDIRECT_URL_PREFIX).append(returnUrl).toString();
    }

    /**
     * 内容链接重定向并计数
     * 
     * @param site
     * @param id
     * @return view name
     */
    @RequestMapping("redirect")
    public String contentRedirect(@RequestAttribute SysSite site, Long id) {
        ClickStatistics contentStatistics = statisticsComponent.contentClicks(site, id);
        if (null != contentStatistics && null != contentStatistics.getUrl()
                && site.getId().equals(contentStatistics.getSiteId())) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + contentStatistics.getUrl();
        } else {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + site.getDynamicPath();
        }
    }

    @Resource
    private CmsContentService service;
}
