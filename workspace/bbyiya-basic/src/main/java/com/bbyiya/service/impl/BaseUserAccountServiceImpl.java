package com.bbyiya.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.UAccountsMapper;
import com.bbyiya.dao.UCashlogsMapper;
import com.bbyiya.model.UAccounts;
import com.bbyiya.model.UCashlogs;
import com.bbyiya.service.IBaseUserAccountService;
import com.bbyiya.vo.ReturnModel;

@Service("baseUserAccountService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class BaseUserAccountServiceImpl implements IBaseUserAccountService {
	@Autowired
	private UAccountsMapper accountsMapper;
	@Autowired
	private UCashlogsMapper cashlogsMapper;
	
	public UAccounts getUserAccount(Long userId){
		UAccounts accounts=accountsMapper.selectByPrimaryKey(userId);
		if(accounts!=null)
			return accounts;
		else {
			accounts=new UAccounts();
			accounts.setUserid(userId);
			accounts.setAvailableamount(0d);
			accountsMapper.insert(accounts);
		}
		return accounts;
	}
	
	public ReturnModel findCashLogs(Long userId,int index,int size){
		ReturnModel rq=new ReturnModel();
		List<UCashlogs> logs= cashlogsMapper.findCashlogsByUserId(userId, null);
		rq.setBasemodle(logs);
		return rq;
	}
	
}
