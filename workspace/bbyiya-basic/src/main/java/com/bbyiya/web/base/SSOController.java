package com.bbyiya.web.base;

//import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.baseUtils.CookieUtils;

/**
 * 用户登陆验证（farther class）
 * 
 * @author Administrator
 *
 */
public class SSOController {
	@Autowired
	public HttpServletRequest request;
	@Autowired
	public HttpServletResponse response;
	
	/**
	 * 管理员登录 cookie缓存名
	 */
	protected static String PHOTO_TOKEN="pic12_token_user";
	/**
	 * 用户登陆成功后返回类
	 */
	private LoginSuccessResult user;

	/**
	 * 微店用户
	 */
	public LoginSuccessResult getLoginUser() {
		String ticket = getTicket();
		if (ObjectUtil.isEmpty(ticket)) {
			// 获取cookie的tiket的值
//			ticket = CookieUtils.getCookieByName(request, PHOTO_TOKEN);
			ticket = CookieUtils.getCookieBySessionId(request);
			if (ObjectUtil.isEmpty(ticket)) {
				ticket=CookieUtils.getCookie_ibs(request);
				if(ObjectUtil.isEmpty(ticket)){
					return null;
				}
			}
		}
		Object userObject = RedisUtil.getObject(ticket);
		if (userObject != null)// 如果存在
		{
			user = (LoginSuccessResult) userObject;
			return user;
		}
		return null;// 用户过期
	}
	
	/**
	 * web、wap 浏览器端获取用户信息
	 * @return
	 */
	public LoginSuccessResult getWebLoginUser() {
		String ticket = CookieUtils.getCookieBySessionId(request);// CookieUtils.getCookieByName(request, PHOTO_TOKEN);
		if(ObjectUtil.isEmpty(ticket))
			return null;
		Object userObject = RedisUtil.getObject(ticket);
		if (userObject != null)// 如果存在
		{
			user = (LoginSuccessResult) userObject;
			RedisUtil.setExpire(ticket, 86400);// 延长时间
			return user;
		}
		return null;// 用户过期
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public static LoginSuccessResult getLoginUser(HttpServletRequest request) {
		String ticket = getTicket(request);
		// 判断tiekt是否为空
		if (ObjectUtil.isEmpty(ticket)) {
//			ticket = CookieUtils.getCookieByName(request, "ticket");// 获取cookie的tiket的值
			ticket = CookieUtils.getCookieBySessionId(request);
			if (ObjectUtil.isEmpty(ticket)) {
				return null;
			}
		}
		Object userObject = RedisUtil.getObject(ticket);
		if (userObject != null)// 如果存在
		{
			RedisUtil.setExpire(ticket, 86400);// 延长时间
			LoginSuccessResult user = (LoginSuccessResult) userObject;
			return user;
		}
		return null;// 用户过期
	}

	/**
	 * 获取用户登录Ticket
	 * 
	 * @return
	 */
	public String getTicket() {
		String ticket = request.getHeader("ticket");
		if (ObjectUtil.isEmpty(ticket)) {
			ticket = request.getParameter("ticket");
		}
		return ticket;
	}

	public static String getTicket(HttpServletRequest request) {
		String ticket = request.getHeader("ticket");
		if (ObjectUtil.isEmpty(ticket)) {
			ticket = request.getParameter("ticket");
		}
		return ticket;
	}

	public String getIpStr() {
		String ipAddres = request.getHeader("x-forwarded-for");
		if (ObjectUtil.isEmpty(ipAddres)) {
			ipAddres = request.getHeader("Proxy-Client-IP");
			if (ObjectUtil.isEmpty(ipAddres)) {
				ipAddres = request.getHeader("WL-Proxy-Client-IP");
				if (ObjectUtil.isEmpty(ipAddres)) {
					ipAddres = request.getRemoteAddr();
				}
			}
		}
		return ipAddres;
	}
}
