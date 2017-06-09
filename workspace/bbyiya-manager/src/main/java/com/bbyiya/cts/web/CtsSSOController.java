package com.bbyiya.cts.web;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import com.bbyiya.cts.vo.admin.AdminLoginSuccessResult;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.baseUtils.CookieUtils;
/**
 * manager�û���¼��֤����
 * @author Administrator
 *
 */
public class CtsSSOController {
	@Autowired
	HttpServletRequest request;
	/**
	 * ����Ա��¼ cookie������
	 */
	protected static String token="token_user";
	 /**
     * ΢���û�
     */
    public AdminLoginSuccessResult getLoginUser()
    {
    	String ticket = request.getHeader("ticket");
		if (ObjectUtil.isEmpty(ticket)) {
			ticket = request.getParameter("ticket");
			// �ж�tiekt�Ƿ�Ϊ��
			if (ObjectUtil.isEmpty(ticket)) {
				// ��ȡcookie��tiket��ֵ
//				ticket = CookieUtils.getCookieByName(request, "ticket");
				ticket = CookieUtils.getCookie_web(request);
				if (ObjectUtil.isEmpty(ticket)) {
					return null;
				}
			}
		}
        Object userObject = RedisUtil.getObject(ticket);
        if(userObject != null)// �������
        {
            RedisUtil.setExpire(ticket,1800);// �ӳ�ʱ��
            return (AdminLoginSuccessResult) userObject;
        }
        return null;// �û�����
    }
}
