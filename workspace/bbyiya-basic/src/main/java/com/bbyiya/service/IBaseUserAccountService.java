package com.bbyiya.service;

import com.bbyiya.model.UAccounts;
import com.bbyiya.model.UBranchtransaccounts;
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
	/**
	 * 获取代理商邮费账户信息
	 * @param branchUserId
	 * @return
	 */
	UBranchtransaccounts getBranchAccounts(Long branchUserId);
	/**
	 * 查询代理商运费账户流水记录
	 * @param userId
	 * @param type
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel findUBranchTansAmountlog(Long userId, Integer type, int index,
			int size);
}
