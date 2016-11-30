package com.bbyiya.web.base;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.vo.user.LoginSuccessResult;

/**
 * 用户登陆票据信息验证（ ticket）
 * @author Administrator
 *
 */
public class UserValidate {

	/**
	 * 获取用户登陆信息
	 * @param ticket
	 * @return
	 */
	public static LoginSuccessResult getLoginUser(String ticket) {
		Object userObject = RedisUtil.getObject(ticket);
		if (userObject != null)// 如果存在
		{
			RedisUtil.setExpire(ticket, 1800);// 延长时间
			LoginSuccessResult user = (LoginSuccessResult) userObject;
			return user;
		}
		return null;// 用户过期
	}
	
	/**
	 * 获取用户登陆信息
	 * @param request
	 * @return
	 */
	public static LoginSuccessResult getLoginUser(HttpServletRequest request) {
		String ticket=request.getParameter("ticket");
		// 判断tiekt是否为空
        if(ObjectUtil.isEmpty(ticket))
        {
            ticket = CookieUtils.getCookieByName(request,"ticket");// 获取cookie的tiket的值
            if(ObjectUtil.isEmpty(ticket))
            {
                return null;
            }
        }
		Object userObject = RedisUtil.getObject(ticket);
		if (userObject != null)// 如果存在
		{
			RedisUtil.setExpire(ticket, 1800);// 延长时间
			LoginSuccessResult user = (LoginSuccessResult) userObject;
			return user;
		}
		return null;// 用户过期
	}
	
	
	
}
