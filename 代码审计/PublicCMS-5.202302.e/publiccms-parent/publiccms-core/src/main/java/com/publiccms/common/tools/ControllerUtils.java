package com.publiccms.common.tools;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.ui.Model;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.service.sys.SysUserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Controller工具类
 *
 * ControllerUtils
 *
 */
public class ControllerUtils {
	/**
	 * Number Pattern
	 */
	public static final Pattern NUMBER_PATTERN = Pattern.compile("^[0-9]*$");
	/**
	 * UNVALID Pattern
	 */
	public static final Pattern UNVALID_CHARS = Pattern
			.compile("[ 　`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}‘；：”“’。，、？]|\n|\r|\t");

	private static final String VALID_CHARS = "[^\\s\\(\\)<>@,;:\\\\\\\"\\.\\[\\]+]+";
	/**
	 * Email Pattern
	 */
	public static final Pattern EMAIL_PATTERN = Pattern
			.compile("(" + VALID_CHARS + "(\\." + VALID_CHARS + ")*@" + VALID_CHARS + "(\\." + VALID_CHARS + ")*)");

	/**
	 * @param response
	 * @param url
	 */
	public static void redirectPermanently(HttpServletResponse response, String url) {
		response.setHeader("Location", RequestUtils.removeCRLF(url));
		response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
	}

	/**
	 * @param field
	 * @param value
	 * @param model
	 * @return value是否为空
	 */
	public static boolean errorNotEmpty(String field, String value, Model model) {
		if (CommonUtils.empty(value)) {
			model.addAttribute(CommonConstants.ERROR, "verify.notEmpty." + field);
			return true;
		}
		return false;
	}

	/**
	 * @param field
	 * @param value
	 * @param model
	 * @return value是否为空
	 */
	public static boolean errorNotEmpty(String field, String value, Map<String, Object> model) {
		if (CommonUtils.empty(value)) {
			model.put(CommonConstants.ERROR, "verify.notEmpty." + field);
			return true;
		}
		return false;
	}

	/**
	 * @param field
	 * @param value
	 * @param model
	 * @return value是否为true
	 */
	public static boolean errorCustom(String field, boolean value, Model model) {
		if (value) {
			model.addAttribute(CommonConstants.ERROR, "verify.custom." + field);
			return true;
		}
		return false;
	}

	/**
	 * @param field
	 * @param value
	 * @param model
	 * @return value是否为true
	 */
	public static boolean errorCustom(String field, boolean value, Map<String, Object> model) {
		if (value) {
			model.put(CommonConstants.ERROR, "verify.custom." + field);
			return true;
		}
		return false;
	}

	/**
	 * @param field
	 * @param value
	 * @param model
	 * @return value是否为空
	 */
	public static boolean errorNotEmpty(String field, Object value, Model model) {
		if (null == value) {
			model.addAttribute(CommonConstants.ERROR, "verify.notEmpty." + field);
			return true;
		}
		return false;
	}

	/**
	 * @param field
	 * @param value
	 * @param model
	 * @return value是否为空
	 */
	public static boolean errorNotEmpty(String field, Object value, Map<String, Object> model) {
		if (null == value) {
			model.put(CommonConstants.ERROR, "verify.notEmpty." + field);
			return true;
		}
		return false;
	}

	/**
	 * @param field
	 * @param value
	 * @param specific
	 * @param model
	 * @return value是否为空或是否大于等于specific
	 */
	public static boolean errorNotGreaterThen(String field, Integer value, int specific, Model model) {
		if (null == value) {
			model.addAttribute(CommonConstants.ERROR, "verify.notEmpty." + field);
			return true;
		} else if (value >= specific) {
			model.addAttribute(CommonConstants.ERROR, "verify.notGreaterThen." + field);
			return true;
		}
		return false;
	}

	/**
	 * @param field
	 * @param value
	 * @param specific
	 * @param model
	 * @return value是否为空或是否大于等于specific
	 */
	public static boolean errorNotGreaterThen(String field, Integer value, int specific, Map<String, Object> model) {
		if (null == value) {
			model.put(CommonConstants.ERROR, "verify.notEmpty." + field);
			return true;
		} else if (value >= specific) {
			model.put(CommonConstants.ERROR, "verify.notGreaterThen." + field);
			return true;
		}
		return false;
	}

	/**
	 * @param field
	 * @param value
	 * @param specific
	 * @param model
	 * @return value是否为空或是否大于等于specific
	 */
	public static boolean errorNotGreaterThen(String field, Long value, long specific, Map<String, Object> model) {
		if (null == value) {
			model.put(CommonConstants.ERROR, "verify.notEmpty." + field);
			return true;
		} else if (value >= specific) {
			model.put(CommonConstants.ERROR, "verify.notGreaterThen." + field);
			return true;
		}
		return false;
	}

	/**
	 * @param field
	 * @param value
	 * @param specific
	 * @param model
	 * @return value是否为空或长度大于specific
	 */
	public static boolean errorNotLongThen(String field, String value, int specific, Model model) {
		if (null == value) {
			model.addAttribute(CommonConstants.ERROR, "verify.notEmpty." + field);
			return true;
		} else if (value.length() > specific) {
			model.addAttribute(CommonConstants.ERROR, "verify.notLongThen." + field);
			return true;
		}
		return false;
	}
	/**
	 * @param field
	 * @param value
	 * @param specific
	 * @param model
	 * @return value是否为空或长度大于specific
	 */
	public static boolean errorNotLongThen(String field, String value, int specific, Map<String, Object> model) {
		if (null == value) {
			model.put(CommonConstants.ERROR, "verify.notEmpty." + field);
			return true;
		} else if (value.length() > specific) {
			model.put(CommonConstants.ERROR, "verify.notLongThen." + field);
			return true;
		}
		return false;
	}

	/**
	 * @param field
	 * @param value
	 * @param specific
	 * @param model
	 * @return value是否为空或长度小于specific
	 */
	public static boolean errorNotLessThen(String field, Integer value, int specific, Map<String, Object> model) {
		if (null == value) {
			model.put(CommonConstants.ERROR, "verify.notEmpty." + field);
			return true;
		} else if (value < specific) {
			model.put(CommonConstants.ERROR, "verify.notLessThen." + field);
			return true;
		}
		return false;
	}

	/**
	 * @param field
	 * @param value
	 * @param model
	 * @return value是否为空
	 */
	public static boolean errorNotEquals(String field, Object value, Model model) {
		if (null == value) {
			model.addAttribute(CommonConstants.ERROR, "verify.notEquals." + field);
			return true;
		}
		return false;
	}

	/**
	 * @param field
	 * @param value
	 * @param model
	 * @return value是否为空
	 */
	public static boolean errorNotEquals(String field, Object value, Map<String, Object> model) {
		if (null == value) {
			model.put(CommonConstants.ERROR, "verify.notEquals." + field);
			return true;
		}
		return false;
	}

	/**
	 * @param field
	 * @param value
	 * @param model
	 * @return value是否为空
	 */
	public static boolean errorNotExist(String field, Object value, Model model) {
		if (null == value) {
			model.addAttribute(CommonConstants.ERROR, "verify.notExist." + field);
			return true;
		}
		return false;
	}

	/**
	 * @param field
	 * @param value
	 * @param model
	 * @return value是否为空
	 */
	public static boolean errorNotExist(String field, Object value, Map<String, Object> model) {
		if (null == value) {
			model.put(CommonConstants.ERROR, "verify.notExist." + field);
			return true;
		}
		return false;
	}

	/**
	 * @param field
	 * @param value
	 * @param model
	 * @return value是否不为空
	 */
	public static boolean errorHasExist(String field, Object value, Model model) {
		if (null != value) {
			model.addAttribute(CommonConstants.ERROR, "verify.hasExist." + field);
			return true;
		}
		return false;
	}
	
	/**
	 * @param field
	 * @param value
	 * @param model
	 * @return value是否不为空
	 */
	public static boolean errorHasExist(String field, Object value, Map<String, Object> model) {
		if (null != value) {
			model.put(CommonConstants.ERROR, "verify.hasExist." + field);
			return true;
		}
		return false;
	}

	/**
	 * @param field
	 * @param value
	 * @param value2
	 * @param model
	 * @return value1是否不为空并等于value2
	 */
	public static boolean errorEquals(String field, Long value, Long value2, Model model) {
		if (CommonUtils.notEmpty(value) && value.equals(value2)) {
			model.addAttribute(CommonConstants.ERROR, "verify.equals." + field);
			return true;
		}
		return false;
	}
	
	/**
	 * @param field
	 * @param value
	 * @param value2
	 * @param model
	 * @return value1是否不为空并等于value2
	 */
	public static boolean errorEquals(String field, Long value, Long value2, Map<String,Object> model) {
		if (CommonUtils.notEmpty(value) && value.equals(value2)) {
			model.put(CommonConstants.ERROR, "verify.equals." + field);
			return true;
		}
		return false;
	}

	/**
	 * @param field
	 * @param value1
	 * @param value2
	 * @param model
	 * @return value1是否不为空并不等于value2
	 */
	public static boolean errorNotEquals(String field, String value1, String value2, Map<String, Object> model) {
		if (CommonUtils.notEmpty(value1) && !value1.equals(value2)) {
			model.put(CommonConstants.ERROR, "verify.notEquals." + field);
			return true;
		}
		return false;
	}

	/**
	 * @param field
	 * @param value1
	 * @param value2
	 * @param model
	 * @return value1是否不为空并不等于value2
	 */
	public static boolean errorNotEquals(String field, String value1, String value2, Model model) {
		if (CommonUtils.notEmpty(value1) && !value1.equals(value2)) {
			model.addAttribute(CommonConstants.ERROR, "verify.notEquals." + field);
			return true;
		}
		return false;
	}

	/**
	 * @param field
	 * @param value1
	 * @param value2
	 * @param model
	 * @return value1是否不为空并不等于value2
	 */
	public static boolean errorNotEquals(String field, Integer value1, Integer value2, Map<String, Object> model) {
		if (CommonUtils.notEmpty(value1) && !value1.equals(value2)) {
			model.put(CommonConstants.ERROR, "verify.notEquals." + field);
			return true;
		}
		return false;
	}

	/**
	 * @param field
	 * @param value1
	 * @param value2
	 * @param model
	 * @return value1是否不为空并不等于value2
	 */
	public static boolean errorNotEquals(String field, Integer value1, Integer value2, Model model) {
		if (CommonUtils.notEmpty(value1) && !value1.equals(value2)) {
			model.addAttribute(CommonConstants.ERROR, "verify.notEquals." + field);
			return true;
		}
		return false;
	}

	/**
	 * @param field
	 * @param value1
	 * @param value2
	 * @param model
	 * @return value1是否不为空并不等于value2
	 */
	public static boolean errorNotEquals(String field, Long value1, Long value2, Map<String, Object> model) {
		if (CommonUtils.notEmpty(value1) && !value1.equals(value2)) {
			model.put(CommonConstants.ERROR, "verify.notEquals." + field);
			return true;
		}
		return false;
	}

	/**
	 * @param field
	 * @param value1
	 * @param value2
	 * @param model
	 * @return value1是否不为空并不等于value2
	 */
	public static boolean errorNotEquals(String field, Long value1, Long value2, Model model) {
		if (CommonUtils.notEmpty(value1) && !value1.equals(value2)) {
			model.addAttribute(CommonConstants.ERROR, "verify.notEquals." + field);
			return true;
		}
		return false;
	}

	/**
	 * @param field
	 * @param value1
	 * @param value2
	 * @param model
	 * @return value1是否不为空并不等于value2
	 */
	public static boolean errorNotEquals(String field, Short value1, Short value2, Map<String, Object> model) {
		if (CommonUtils.notEmpty(value1) && !value1.equals(value2)) {
			model.put(CommonConstants.ERROR, "verify.notEquals." + field);
			return true;
		}
		return false;
	}

	/**
	 * @param field
	 * @param value1
	 * @param value2
	 * @param model
	 * @return value1是否不为空并不等于value2
	 */
	public static boolean errorNotEquals(String field, Short value1, Short value2, Model model) {
		if (CommonUtils.notEmpty(value1) && !value1.equals(value2)) {
			model.addAttribute(CommonConstants.ERROR, "verify.notEquals." + field);
			return true;
		}
		return false;
	}

	/**
	 * @param request
	 * @return SysSite
	 */
	public static SysSite getSiteFromAttribute(HttpServletRequest request) {
		return (SysSite) request.getAttribute(CommonConstants.getAttributeSite());
	}

	/**
	 * @param session
	 * @return SysUser
	 */
	public static SysUser getUserFromSession(HttpSession session) {
		if (null != session) {
			return (SysUser) session.getAttribute(CommonConstants.getSessionUser());
		} else {
			return null;
		}
	}

	/**
	 * @param session
	 * @return Date
	 */
	public static Long getUserTimeFromSession(HttpSession session) {
		if (null != session) {
			return (Long) session.getAttribute(CommonConstants.getSessionUserTime());
		} else {
			return null;
		}
	}

	/**
	 * @param session
	 * @param user
	 */
	public static void setUserToSession(HttpSession session, SysUser user) {
		session.setAttribute(CommonConstants.getSessionUser(), user);
		session.setAttribute(CommonConstants.getSessionUserTime(), System.currentTimeMillis());
	}

	/**
	 * @param contextPath
	 * @param scheme
	 * @param session
	 * @param response
	 */
	public static void clearUserToSession(String contextPath, String scheme, HttpSession session,
			HttpServletResponse response) {
		RequestUtils.cancleCookie(contextPath, scheme, response, CommonConstants.getCookiesUser(), null);
		session.removeAttribute(CommonConstants.getSessionUser());
	}

	/**
	 * @param session
	 */
	public static void clearUserTimeToSession(HttpSession session) {
		session.removeAttribute(CommonConstants.getSessionUserTime());
	}

	/**
	 * @param session
	 * @return SysUser
	 */
	public static SysUser getAdminFromSession(HttpSession session) {
		return (SysUser) session.getAttribute(CommonConstants.getSessionAdmin());
	}

	/**
	 * @param session
	 * @param user
	 */
	public static void setAdminToSession(HttpSession session, SysUser user) {
		session.setAttribute(CommonConstants.getSessionAdmin(), user);
	}

	/**
	 * @param contextPath
	 * @param scheme
	 * @param session
	 * @param response
	 */
	public static void clearAdminToSession(String contextPath, String scheme, HttpSession session,
			HttpServletResponse response) {
		RequestUtils.cancleCookie(contextPath, scheme, response, CommonConstants.getCookiesAdmin(), null);
		session.removeAttribute(CommonConstants.getSessionAdmin());
	}

	/**
	 * @param request
	 * @return
	 */
	public static String getWebToken(HttpServletRequest request) {
		return getToken(request, CommonConstants.getCookiesUser());
	}

	/**
	 * @param request
	 * @return
	 */
	public static String getAdminToken(HttpServletRequest request) {
		return getToken(request, CommonConstants.getCookiesAdmin());
	}

	/**
	 * @param user
	 * @param content
	 * @return has content permission
	 */
	public static boolean hasContentPermissions(SysUser user, CmsContent content) {
		return SysUserService.CONTENT_PERMISSIONS_ALL == user.getContentPermissions()
				|| SysUserService.CONTENT_PERMISSIONS_SELF == user.getContentPermissions()
						&& content.getUserId() == user.getId()
				|| SysUserService.CONTENT_PERMISSIONS_DEPT == user.getContentPermissions()
						&& (null != content.getDeptId() && content.getDeptId().equals(user.getDeptId()));
	}

	public static boolean ipNotEquals(String ip, SysUser user) {
		return null == ip || null == user || !ip.equalsIgnoreCase(user.getLastLoginIp());
	}

	/**
	 * @param request
	 * @param cookiesName
	 * @return
	 */
	private static String getToken(HttpServletRequest request, String cookiesName) {
		Cookie userCookie = RequestUtils.getCookie(request.getCookies(), cookiesName);
		if (null != userCookie && CommonUtils.notEmpty(userCookie.getValue())) {
			String value = userCookie.getValue();
			if (null != value) {
				String[] userData = value.split(CommonConstants.getCookiesUserSplit());
				if (userData.length > 1) {
					return userData[1];
				}
			}
		}
		return null;
	}

	/**
	 * @param value
	 * @return boolean
	 */
	public static boolean notValid(String value) {
		Matcher m = UNVALID_CHARS.matcher(value);
		if (null == value || 0 == value.length() || m.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * @param field
	 * @param value
	 * @param model
	 * @return boolean
	 */
	public static boolean errorNotEMail(String field, String value, Model model) {
		if (notEMail(value)) {
			model.addAttribute(CommonConstants.ERROR, "verify.notEmail." + field);
			return true;
		}
		return false;
	}

	/**
	 * @param field
	 * @param value
	 * @param model
	 * @return boolean
	 */
	public static boolean errorNotEMail(String field, String value, Map<String, Object> model) {
		if (notEMail(value)) {
			model.put(CommonConstants.ERROR, "verify.notEmail." + field);
			return true;
		}
		return false;
	}

	/**
	 * @param field
	 * @param value
	 * @param model
	 * @return boolean
	 */
	public static boolean errorNotUserName(String field, String value, Model model) {
		if (notValid(value)) {
			model.addAttribute(CommonConstants.ERROR, "verify.notUserName." + field);
			return true;
		}
		return false;
	}
	
	/**
	 * @param field
	 * @param value
	 * @param model
	 * @return boolean
	 */
	public static boolean errorNotUserName(String field, String value, Map<String, Object> model) {
		if (notValid(value)) {
			model.put(CommonConstants.ERROR, "verify.notUserName." + field);
			return true;
		}
		return false;
	}

	/**
	 * @param field
	 * @param value
	 * @param model
	 * @return boolean
	 */
	public static boolean errorNotNickname(String field, String value, Model model) {
		if (notValid(value)) {
			model.addAttribute(CommonConstants.ERROR, "verify.notNickname." + field);
			return true;
		}
		return false;
	}
	
	/**
	 * @param field
	 * @param value
	 * @param model
	 * @return boolean
	 */
	public static boolean errorNotNickname(String field, String value, Map<String, Object> model) {
		if (notValid(value)) {
			model.put(CommonConstants.ERROR, "verify.notNickname." + field);
			return true;
		}
		return false;
	}

	/**
	 * @param value
	 * @return boolean
	 */
	public static boolean notEMail(String value) {
		Matcher m = EMAIL_PATTERN.matcher(value);
		if (!m.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * @param value
	 * @return boolean
	 */
	public static boolean notNumber(String value) {
		Matcher m = NUMBER_PATTERN.matcher(value);
		if (!m.matches()) {
			return true;
		}
		return false;
	}
}
