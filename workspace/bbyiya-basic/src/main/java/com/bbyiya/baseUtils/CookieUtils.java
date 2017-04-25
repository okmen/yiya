package com.bbyiya.baseUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

	private static String COOKIE_NAME="photo_Cookie_token";
//	private static String PIC_COOKIE_NAME="Pic_Cookie_token";
	
//	/**
//	 * 通过SessionId设置Cookie
//	 * @param request
//	 * @param response
//	 * @param value
//	 * @param maxAge
//	 */
//	public static void addCookieBySessionId(HttpServletRequest request, HttpServletResponse response, String value, int maxAge) {
//		String sessionId= request.getSession().getId();
//		Cookie cookie = new Cookie(sessionId, value);
//		cookie.setPath("/");
//		if (maxAge > 0)
//			cookie.setMaxAge(maxAge);
//		response.addCookie(cookie);
//	}
//	
//
//	
//	/**
//	 * 通过session获取ticket
//	 * @param request
//	 * @return
//	 */
//	public static String getCookieBySessionId(HttpServletRequest request) {
//		Cookie[] cookies = request.getCookies();
//		String sessionId= request.getSession().getId();
//		if (cookies != null) {
//			for (Cookie cookie : cookies) {
//				if (sessionId.equals(cookie.getName().trim())) {
//					return cookie.getValue();
//				}
//			}
//		} 
//		return null;
//	}
	
	public static void addCookie_web(HttpServletRequest request, HttpServletResponse response, String value, int maxAge) {		
		Cookie cookie = new Cookie(COOKIE_NAME, value);
		cookie.setPath("/");
		if (maxAge > 0)
			cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}
	
	public static String getCookie_web(HttpServletRequest request) {
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
