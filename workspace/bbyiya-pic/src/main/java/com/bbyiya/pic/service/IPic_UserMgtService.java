package com.bbyiya.pic.service;

import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.OtherLoginParam;

public interface IPic_UserMgtService {
	
	/**
	 * ��������¼��ע��
	 * @param param
	 * @return
	 */
	ReturnModel otherLogin(OtherLoginParam param);
	/**
	 * ���ֻ���
	 * @param userId
	 * @param pwd
	 * @param phone
	 * @param vcode
	 * @return
	 */
	ReturnModel setPwd(Long userId,String pwd,String phone,String vcode);
}
