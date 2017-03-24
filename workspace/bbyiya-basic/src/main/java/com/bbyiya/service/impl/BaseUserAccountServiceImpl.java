package com.bbyiya.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.UAccountsMapper;
import com.bbyiya.dao.UBranchusersMapper;
import com.bbyiya.dao.UCashlogsMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.UAccounts;
import com.bbyiya.model.UBranchusers;
import com.bbyiya.model.UCashlogs;
import com.bbyiya.service.IBaseUserAccountService;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.UCashlogResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

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
	
	@Autowired
	private OUserordersMapper userordersMapper;
	@Autowired
	private UBranchusersMapper branchusersMapper;
	public ReturnModel findCashLogs(Long userId,Integer type, int index,int size){
		ReturnModel rq=new ReturnModel();
		PageHelper.startPage(index, size);
//		List<UCashlogs> logs= cashlogsMapper.findCashlogsByUserId(userId, type);
		List<UCashlogResult> logs= cashlogsMapper.findCashlogResultByUserId(userId, type);
		PageInfo<UCashlogResult> resultPage=new PageInfo<UCashlogResult>(logs); 
		if(resultPage.getList()!=null&&resultPage.getList().size()>0){
			for (UCashlogResult log : resultPage.getList()) {
				log.setCreatetimestr(DateUtil.getTimeStr(log.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
				if(type!=null&&type.intValue()==1){
					OUserorders order= userordersMapper.selectByPrimaryKey(log.getPayid());
					if(order!=null){
						log.setBuyerUserId(order.getUserid()); 
						UBranchusers branchusers = branchusersMapper.selectByPrimaryKey(order.getUserid());
						if(branchusers!=null){
							log.setBuyerName(branchusers.getName());
							log.setBuyerPhone(branchusers.getPhone()); 
						}
					}
				}
			
			}
		}
		rq.setStatu(ReturnStatus.Success); 
		rq.setBasemodle(resultPage);
		return rq;
	}
	
}
