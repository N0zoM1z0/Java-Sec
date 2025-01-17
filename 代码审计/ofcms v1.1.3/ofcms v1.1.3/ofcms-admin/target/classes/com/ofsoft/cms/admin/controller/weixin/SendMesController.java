package com.ofsoft.cms.admin.controller.weixin;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.TemplateData;
import com.jfinal.weixin.sdk.api.TemplateMsgApi;
import com.ofsoft.cms.admin.controller.BaseController;
import com.ofsoft.cms.core.annotation.Action;
import com.ofsoft.cms.core.config.ErrorCode;

/**
 * 微信模板消息推送
 * 
 * @author sht
 * @date 2018年11月16日
 */
@Action(path = "/weixin/sendMes")
public class SendMesController extends BaseController {
	public final static String SUCESS_CODE = "200";
	/**
	 * 推送消息服务
	 */
	public void sendMesService() {
		String id = getPara("id");
		Record record = Db.findById("of_sys_weixin_template", id);
		TemplateData temp = TemplateData.New();
		temp.setTouser("o7xZG0V4HRM2Ew5TpRzMi9xVBRE8");
		temp.setTemplate_id(record.getStr("template_key"));
		// {{first.DATA}} 快递公司：{{delivername.DATA}} 快递单号：{{ordername.DATA}}
		// {{remark.DATA}}
		System.out.println("===content==" + record.getStr("content"));
		String content = record.getStr("content");
		String[] con = content.split("[{{.}}]");
		int k;
		for (k = 2; k < con.length;) {
			System.out.println(con[k] + "======sss=====" + con[k + 1].substring(3));
			temp.add(con[k], con[k + 1].substring(4), "#173177");
			k = k + 5;
		}

		temp.setTopcolor("#173177");
		temp.setUrl("www.shenk.wang");
		String jsonStr = temp.build();
		try {
			ApiResult result = TemplateMsgApi.send(jsonStr);
			System.out.println(result + "==result===" + result.isSucceed());
			if (result.isSucceed()) {
				rendSuccessJson();
			} else {
				rendFailedJson(ErrorCode.get("9999"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			rendFailedJson(ErrorCode.get("9999"));
		}

	}

}
