package com.bbyiya.cts.service;

import com.bbyiya.vo.ReturnModel;

public interface IUAdminService {

	/**
	 * �û�����¼
	 * @param username
	 * @param pwd
	 * @return
	 */
	ReturnModel loginProcess(String username,String pwd);
}
