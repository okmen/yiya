package com.bbyiya.web.base;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.vo.user.LoginSuccessResult;
/**
 * 用户登陆验证（farther class）
 * @author Administrator
 *
 */
public class SSOController {
	@Autowired
	HttpServletRequest request;
//	@Autowired
//	HttpServletResponse response;
	/**
	 * 用户登陆成功后返回类
	 */
	private LoginSuccessResult user;
	
	
	
	 /**
     * 微店用户
     */
    public LoginSuccessResult getLoginUser()
    {
        String ticket = request.getParameter("ticket");
        // 判断tiekt是否为空
        if(ObjectUtil.isEmpty(ticket))
        {
        	// 获取cookie的tiket的值
            ticket = CookieUtils.getCookieByName(request,"ticket");
            if(ObjectUtil.isEmpty(ticket))
            {
                return null;
            }
        }
        Object userObject = RedisUtil.getObject(ticket);
        if(userObject != null)// 如果存在
        {
            RedisUtil.setExpire(ticket,172800);// 延长时间
            user = (LoginSuccessResult) userObject;
            return user;
        }
        return null;// 用户过期
    }
    

}
