package com.bbyiya.web.base;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.vo.user.LoginSuccessResult;

public class SSOController {
	@Autowired
	HttpServletRequest request;
	@Autowired
	HttpServletResponse response;
	
	private LoginSuccessResult user;
	
	
	
	 /**
     * 微店用户
     */
    public LoginSuccessResult getLoginUser()
    {
        String tiket = request.getParameter("ticket");
        // 判断tiekt是否为空
        if(ObjectUtil.isEmpty(tiket))
        {
            tiket = getCookieByName(request,"ticket");// 获取cookie的tiket的值
            if(ObjectUtil.isEmpty(tiket))
            {
                return null;
            }
        }
        Object userObject = RedisUtil.getObject(tiket);
        if(userObject != null)// 如果存在
        {
            RedisUtil.setExpire(tiket,1800);// 延长时间
            user = (LoginSuccessResult) userObject;
            return user;
        }
        return null;// 用户过期
    }
    
	 /**
     * 直接获取tiekt，没有返回空
     * 
     * @return
     */
    public String getTiket()
    {
        String tiket = request.getParameter("tiket");
        // 判断tiekt是否为空
        if(ObjectUtil.isEmpty(tiket))
        {
            tiket = getCookieByName(request,"tiket");// 获取cookie的tiket的值
            return tiket;
        }
        else{
            return tiket;
        }
    }
    

    /**
     * 获取指定cookie值
     * 
     * @param request
     *            请求实体
     * @param name
     *            cookie的键
     * @return cookie的值
     */
    private String getCookieByName(HttpServletRequest request,String name)
    {
        Cookie[] cookies = request.getCookies();
        if(ObjectUtil.isEmpty(name))
        {
            return null;
        }
        if(cookies != null)
        {
            for(Cookie cookie : cookies)
            {
                if(name.equals(cookie.getName().trim()))
                {
                    return cookie.getValue();
                }
            }
            return null;
        }
        else
        {
            return null;
        }
    }
}
