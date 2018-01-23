package com.bbyiya.cts.service;

import com.bbyiya.vo.ReturnModel;

public interface IUAdminService {

	/**
	 * 用户名登录
	 * @param username
	 * @param pwd
	 * @return
	 */
	ReturnModel loginProcess(String username,String pwd);
}
