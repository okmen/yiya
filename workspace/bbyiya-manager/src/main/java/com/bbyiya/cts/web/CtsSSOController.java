package com.bbyiya.cts.web;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import com.bbyiya.cts.vo.admin.AdminLoginSuccessResult;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.baseUtils.CookieUtils;
/**
 * manager用户登录验证基类
 * @author Administrator
 *
 */
public class CtsSSOController {
	@Autowired
	HttpServletRequest request;
	/**
	 * 管理员登录 cookie缓存名
	 */
	protected static String token="token_user";
	 /**
     * 微店用户
     */
    public AdminLoginSuccessResult getLoginUser()
    {
    	String ticket = request.getHeader("ticket");
		if (ObjectUtil.isEmpty(ticket)) {
			ticket = request.getParameter("ticket");
			// 判断tiekt是否为空
			if (ObjectUtil.isEmpty(ticket)) {
				// 获取cookie的tiket的值
//				ticket = CookieUtils.getCookieByName(request, "ticket");
				ticket = CookieUtils.getCookie_web(request);
				if (ObjectUtil.isEmpty(ticket)) {
					return null;
				}
			}
		}
        Object userObject = RedisUtil.getObject(ticket);
        if(userObject != null)// 如果存在
        {
            RedisUtil.setExpire(ticket,1800);// 延长时间
            return (AdminLoginSuccessResult) userObject;
        }
        return null;// 用户过期
    }
}
