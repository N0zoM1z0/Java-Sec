package com.ofsoft.cms.api.v1;

import com.jfinal.plugin.activerecord.Db;
import com.ofsoft.cms.api.ApiBase;
import com.ofsoft.cms.core.annotation.Action;
import com.ofsoft.cms.core.api.ApiMapping;
import com.ofsoft.cms.core.api.RequestMethod;
import com.ofsoft.cms.core.api.check.ParamsCheck;
import com.ofsoft.cms.core.api.check.ParamsCheckType;
import com.ofsoft.cms.core.utils.IpKit;

import java.util.Map;


/**
 * 评论接口
 *
 * @author OF
 * @date 2019年2月24日
 */
@Action(path = "/comment")
public class CommentApi extends ApiBase {
    /**
     * 获取内容信息
     */
    @ApiMapping(method = RequestMethod.GET)
    @ParamsCheck(
            {@ParamsCheckType(name = "comment_content"), @ParamsCheckType(name = "content_id"),
                    @ParamsCheckType(name = "site_id")})
    public void save() {
        try {
            Map params = getParamsMap();
            params.put("comment_ip", IpKit.getRealIp(getRequest()));
            Db.update(Db.getSqlPara("cms.comment.save", params));
            rendSuccessJson();
        } catch (Exception e) {
            e.printStackTrace();
            rendFailedJson();
        }
    }

}
