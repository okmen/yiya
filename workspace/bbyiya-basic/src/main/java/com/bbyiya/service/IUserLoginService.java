package com.bbyiya.service;

import com.bbyiya.vo.ReturnModel;

public interface IUserLoginService {

	/**
	 * 用户登陆（用户名密码登陆）
	 * @param userno
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	ReturnModel login(String userno, String pwd) throws Exception;
}
