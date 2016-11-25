package com.bbyiya.service;

import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.RegisterParam;

public interface IUserLoginService {

	/**
	 * 用户登陆（用户名密码登陆）
	 * @param userno
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	ReturnModel login(String userno, String pwd) throws Exception;
	/**
	 * 用户注册
	 * @param param
	 * @return
	 * @throws Exception
	 */
	ReturnModel register(RegisterParam param) throws Exception ;
}
