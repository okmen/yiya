package com.bbyiya.web.base;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.bbyiya.utils.ObjectUtil;

public class CookieUtils {

	/**
	 * 获取指定cookie值
	 * 
	 * @param request
	 *            请求实体
	 * @param name
	 *            cookie的键
	 * @return cookie的值
	 */
	public static String getCookieByName(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (ObjectUtil.isEmpty(name)) {
			return null;
		}
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (name.equals(cookie.getName().trim())) {
					return cookie.getValue();
				}
			}
			return null;
		} else {
			return null;
		}
	}

	/**
	 * 设置Cookie
	 * @param response 
	 * @param name cookie 名称
	 * @param value 
	 * @param maxAge 有效时间
	 */
	public static void addCookie(HttpServletResponse response, String name,String value, int maxAge) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		if (maxAge > 0)
			cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}
}
