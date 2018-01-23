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
	 * 查询代理商运费账户流水记录（过期）
	 * @param userId
	 * @param type
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel findUBranchTansAmountlog(Long userId, Integer type, int index,
			int size);
	/**
	 * 账户流水记录 (2017-06-29)
	 * 统一账户
	 * @param userId
	 * @param type
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel findAcountsLogsPageResult(Long userId,String keywords, Integer type, int index, int size) ;
	/**
	 * 账户变动 -新增账户流水
	 * @param userId
	 * @param type
	 * @param amount
	 * @param PayId
	 * @param transOrderId
	 * @return
	 * @throws Exception
	 */
	boolean add_accountsLog(long userId,int type,Double amount,String PayId,String transOrderId)throws Exception;
	/**
	 * 添加冻结金额记录
	 * @param userId
	 * @param type
	 * @param amount
	 * @param PayId
	 * @param transOrderId
	 * @return
	 * @throws Exception
	 */
	boolean add_FreezeCashAccountsLog(long userId, int type, Double amount,
			String PayId, String transOrderId) throws Exception;
	
	/**
	 * 使用红包将钱转移到冻结账户
	 * @param userId
	 * @param totalPrice
	 * @throws Exception
	 */
	Double transferCashAccountsToFreeze(long userId, Double totalPrice)
			throws Exception;
	/**
	 * 影楼推广流水
	 * @param userId
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel findCommissionDetailsPageResult(Long userId, int index, int size);
}
