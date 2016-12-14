package com.bbyiya.cts.service;

import com.bbyiya.vo.ReturnModel;

public interface IUAdminService {

	/**
	 * ÓÃ»§ÃûµÇÂ¼
	 * @param username
	 * @param pwd
	 * @return
	 */
	ReturnModel loginProcess(String username,String pwd);
}
