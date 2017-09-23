package com.bbyiya.baseUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.log4j.Logger;

import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.RedisUtil;

public class CookieUtils {

	private static String COOKIE_NAME="photo_Cookie_token";
//	private static Logger logger = Logger.getLogger(CookieUtils.class);
	
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
//		cookie.setPath(request.getContextPath()); 
//		if (maxAge > 0){
//			cookie.setMaxAge(maxAge);
//		}else {
//			cookie.setMaxAge(1800); 
//		}
//		response.addCookie(cookie);
//		RedisUtil.setString(sessionId, value, maxAge); 
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
		String currentDomain=ConfigUtil.getSingleValue("currentApiDomain");
		//如果是测试环境
		if(currentDomain.contains(".net")){
			Cookie cookieNet = new Cookie(COOKIE_NAME, value);
			cookieNet.setDomain(".bbyiya.net");
			cookieNet.setPath("/");
			if (maxAge > 0)
				cookieNet.setMaxAge(maxAge);
			response.addCookie(cookieNet);
		}
		cookie.setDomain(".bbyiya.com");
		cookie.setPath("/");
		if (maxAge > 0)
			cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
		//-----
		RedisUtil.setString(request.getSession().getId(), value, maxAge); 
//		logger.error("sessionId="+request.getSession().getId()+",value="+value);
//		System.out.println("sessionId="+request.getSession().getId()+",value="+value);
	}
	
	public static String getCookie_web(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (COOKIE_NAME.equals(cookie.getName().trim())) {
//					logger.error("getCookie="+request.getSession().getId()+",value="+cookie.getValue());
//					System.out.println("getCookie="+request.getSession().getId()+",value="+cookie.getValue());
					return cookie.getValue();
				}
			}
		}
		
		String token=  RedisUtil.getString(request.getSession().getId());
//		logger.error("getSession="+request.getSession().getId()+",value="+token);
//		System.out.println("getSession="+request.getSession().getId()+",value="+token);
		return token;
		
	}
	
	
}
