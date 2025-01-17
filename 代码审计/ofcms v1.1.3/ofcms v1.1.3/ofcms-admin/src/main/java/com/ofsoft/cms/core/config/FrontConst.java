package com.ofsoft.cms.core.config;

public final class FrontConst {


	/** 站点存放到session中 **/
	public static final String  VERSION= "OFCMS";
	
	//页面路径前缀
    public static final String TEMPLATE_PATE= "/";
    
    //存放站点信息的sessionKey
    public static final String SITE_SESSION = "site";
    
    //freeMarkerRander 指令前缀
    public static final String DIRECTIVE_PREFIX = "of";
	/**  错误页面404 */
	public static String pageError = "/404.html";
}
