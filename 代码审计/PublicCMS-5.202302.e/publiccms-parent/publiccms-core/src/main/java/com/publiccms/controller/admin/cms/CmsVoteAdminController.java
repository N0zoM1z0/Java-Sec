package com.publiccms.controller.admin.cms;

// Generated 2020-3-26 11:46:48 by com.publiccms.common.generator.SourceGenerator

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.cms.CmsVote;
import com.publiccms.entities.cms.CmsVoteItem;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.service.cms.CmsVoteItemService;
import com.publiccms.logic.service.cms.CmsVoteService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.views.pojo.model.CmsVoteParameters;

/**
 *
 * CmsVoteAdminController
 * 
 */
@Controller
@RequestMapping("cmsVote")
public class CmsVoteAdminController {

    private String[] ignoreProperties = new String[] { "id", "siteId", "votes", "createDate", "disabled" };
    private String[] itemIgnoreProperties = new String[] { "id", "voteId", "votes" };

    /**
     * @param site
     * @param admin
     * @param entity
     * @param voteParameters
     * @param request
     * @return operate result
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, CmsVote entity,
            @ModelAttribute CmsVoteParameters voteParameters, HttpServletRequest request) {
        if (null != entity.getId()) {
            entity = service.update(entity.getId(), entity, ignoreProperties);
            voteItemService.update(entity.getId(), voteParameters.getItemList(), itemIgnoreProperties);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "update.cmsVote", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), JsonUtils.getString(entity)));
        } else {
            entity.setSiteId(site.getId());
            service.save(entity);
            for (CmsVoteItem item : voteParameters.getItemList()) {
                item.setVoteId(entity.getId());
            }
            voteItemService.save(voteParameters.getItemList());
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.cmsVote", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param request
     * @param site
     * @param admin
     * @return operate result
     */
    @RequestMapping("delete")
    @Csrf
    public String delete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long[] ids, HttpServletRequest request) {
        if (CommonUtils.notEmpty(ids)) {
            for (CmsVote entity : service.delete(site.getId(), ids)) {
                voteItemService.deleteByVoteId(entity.getId());
            }
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.cmsVote", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(ids, ',')));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    @Resource
    private CmsVoteService service;
    @Resource
    private CmsVoteItemService voteItemService;
    @Resource
    protected LogOperateService logOperateService;
}