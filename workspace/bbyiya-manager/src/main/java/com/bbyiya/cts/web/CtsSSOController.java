package com.bbyiya.cts.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.bbyiya.model.UAdmin;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.web.base.CookieUtils;
/**
 * manager�û���¼��֤����
 * @author Administrator
 *
 */
public class CtsSSOController {
	@Autowired
	HttpServletRequest request;
	/**
	 * cookie������
	 */
	protected static String token="token_user";
	 /**
     * ΢���û�
     */
    public UAdmin getLoginUser()
    {
        String ticket = request.getParameter("ticket");
        // �ж�tiekt�Ƿ�Ϊ��
        if(ObjectUtil.isEmpty(ticket))
        {
        	// ��ȡcookie��tiket��ֵ
            ticket = CookieUtils.getCookieByName(request,token);
            if(ObjectUtil.isEmpty(ticket))
            {
                return null;
            }
        }
        Object userObject = RedisUtil.getObject(ticket);
        if(userObject != null)// �������
        {
            RedisUtil.setExpire(ticket,1800);// �ӳ�ʱ��
            return (UAdmin) userObject;
        }
        return null;// �û�����
    }
}
