package com.bbyiya.web.base;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.bbyiya.utils.ObjectUtil;

public class CookieUtils {

	/**
     * 获取指定cookie值
     * 
     * @param request  请求实体
     * @param name  cookie的键
     * @return cookie的值
     */
    public static String getCookieByName(HttpServletRequest request,String name)
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
