package com.bbyiya.baseUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

	private static String COOKIE_NAME="Ibs_Cookie_token";
//	/**
//	 * 获取指定cookie值
//	 * 
//	 * @param request  请求实体
//	 * @param name cookie的键
//	 * @return cookie的值
//	 */
//	public static String getCookieByName(HttpServletRequest request, String name) {
//		Cookie[] cookies = request.getCookies();
//		if (ObjectUtil.isEmpty(name)) {
//			return null;
//		}
//		String sessionId= request.getSession().getId();
//		System.out.println(sessionId); 
//		if (cookies != null) {
//			for (Cookie cookie : cookies) {
//				if (name.equals(cookie.getName().trim())) {
//					return cookie.getValue();
//				}
//			}
//			return null;
//		} else {
//			return null;
//		}
//	}
//
//	/**
//	 * 设置Cookie
//	 * @param response 
//	 * @param name cookie 名称
//	 * @param value 
//	 * @param maxAge 有效时间
//	 */
//	public static void addCookie(HttpServletResponse response, String name,String value, int maxAge) {
//		Cookie cookie = new Cookie(name, value);
//		cookie.setPath("/");
//		if (maxAge > 0)
//			cookie.setMaxAge(maxAge);
//		response.addCookie(cookie);
//	}
	
	/**
	 * 通过SessionId设置Cookie
	 * @param request
	 * @param response
	 * @param value
	 * @param maxAge
	 */
	public static void addCookieBySessionId(HttpServletRequest request, HttpServletResponse response, String value, int maxAge) {
		String sessionId= request.getSession().getId();
		Cookie cookie = new Cookie(sessionId, value);
		cookie.setPath("/");
		if (maxAge > 0)
			cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}
	
	public static void addCookie_ibs(HttpServletRequest request, HttpServletResponse response, String value, int maxAge) {		
		Cookie cookie = new Cookie(COOKIE_NAME, value);
		cookie.setPath("/");
		if (maxAge > 0)
			cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}
	
	/**
	 * 通过session获取ticket
	 * @param request
	 * @return
	 */
	public static String getCookieBySessionId(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		String sessionId= request.getSession().getId();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (sessionId.equals(cookie.getName().trim())) {
					return cookie.getValue();
				}
			}
		} 
		return null;
		
	}
	
	
	public static String getCookie_ibs(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (COOKIE_NAME.equals(cookie.getName().trim())) {
					return cookie.getValue();
				}
			}
		} 
		return null;
		
	}
	
}
