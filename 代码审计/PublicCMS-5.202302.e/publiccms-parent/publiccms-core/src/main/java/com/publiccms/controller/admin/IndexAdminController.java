package com.publiccms.controller.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.util.UrlPathHelper;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.config.SafeConfigComponent;
import com.publiccms.logic.component.site.SiteComponent;

/**
 * 
 * IndexAdminController 统一分发Controller
 *
 */
@Controller
public class IndexAdminController {
    @Resource
    protected SiteComponent siteComponent;
    @Resource
    protected SafeConfigComponent safeConfigComponent;

    /**
     * 页面请求统一分发
     * 
     * @param request
     * @return view name
     */
    @RequestMapping("/**")
    public String page(HttpServletRequest request) {
        String path = CmsFileUtils.getSafeFileName(UrlPathHelper.defaultInstance.getLookupPathForRequest(request));
        if (CommonUtils.notEmpty(path)) {
            if (CommonConstants.SEPARATOR.equals(path) || path.endsWith(CommonConstants.SEPARATOR)) {
                path += CommonConstants.getDefaultPage();
            }
            int index = path.lastIndexOf(CommonConstants.DOT);
            path = path.substring(0 < path.indexOf(CommonConstants.SEPARATOR) ? 0 : 1, -1 < index ? index : path.length());
        }
        return path;
    }

    /**
     * 修改语言
     * 
     * @param site
     * @param lang
     * @param returnUrl
     * @param request
     * @param response
     * @return view name
     */
    @RequestMapping("changeLocale")
    public String changeLocale(@RequestAttribute SysSite site, String lang, String returnUrl, HttpServletRequest request,
            HttpServletResponse response) {
        if (null != lang) {
            LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
            if (null != localeResolver) {
                localeResolver.setLocale(request, response, StringUtils.parseLocaleString(lang));
            }
        }
        if (CommonUtils.empty(returnUrl)) {
            return CommonConstants.TEMPLATE_DONEANDREFRESH;
        } else {
            returnUrl = safeConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
            return new StringBuilder(UrlBasedViewResolver.REDIRECT_URL_PREFIX).append(returnUrl).toString();
        }
    }
}