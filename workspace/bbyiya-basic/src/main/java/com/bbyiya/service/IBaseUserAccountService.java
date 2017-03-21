package com.bbyiya.service;

import com.bbyiya.model.UAccounts;
import com.bbyiya.vo.ReturnModel;

public interface IBaseUserAccountService {

	/**
	 * 获取用户账户信息
	 * @param userId
	 * @return
	 */
	UAccounts getUserAccount(Long userId);
	/**
	 * 获取用户 预存款Logs
	 * @param userId
	 * @param index
	 * @param size
	 */
	ReturnModel findCashLogs(Long userId,Integer type, int index,int size);
}
