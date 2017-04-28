package com.bbyiya.pic.service;

import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.OtherLoginParam;

public interface IPic_UserMgtService {
	
	/**
	 * 第三方登录、注册
	 * @param param
	 * @return
	 */
	ReturnModel otherLogin(OtherLoginParam param);
	/**
	 * 绑定手机号
	 * @param userId
	 * @param pwd
	 * @param phone
	 * @param vcode
	 * @return
	 */
	ReturnModel setPwd(Long userId,String pwd,String phone,String vcode);
}
