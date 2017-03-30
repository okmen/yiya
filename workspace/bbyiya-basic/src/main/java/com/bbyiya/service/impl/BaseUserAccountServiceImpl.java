package com.bbyiya.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.OPayorderMapper;
import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.UAccountsMapper;
import com.bbyiya.dao.UBranchtransaccountsMapper;
import com.bbyiya.dao.UBranchtransamountlogMapper;
import com.bbyiya.dao.UBranchusersMapper;
import com.bbyiya.dao.UCashlogsMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.OPayorder;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.UAccounts;
import com.bbyiya.model.UBranchtransaccounts;
import com.bbyiya.model.UBranchusers;
import com.bbyiya.service.IBaseUserAccountService;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.UBranchTansAmountlogResult;
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
	@Autowired
	private UBranchtransaccountsMapper branchAccountMapper;
	@Autowired
	private UBranchtransamountlogMapper branchTransAmountlogsMapper;
	
	/*---------------------订单模块-------------------------------*/
	@Autowired
	private OUserordersMapper userordersMapper;
	@Autowired
	private UBranchusersMapper branchusersMapper;
	@Autowired 
	private OPayorderMapper payMapper;
	
	/**
	 * 获取用户账户信息
	 */
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
	/**
	 * 获取代理商邮费账户信息
	 */
	public UBranchtransaccounts getBranchAccounts(Long userId){ 
		
		
		UBranchtransaccounts accounts=branchAccountMapper.selectByPrimaryKey(userId);
		if(accounts!=null)
			return accounts;
		else {
			accounts=new UBranchtransaccounts();
			accounts.setBranchuserid(userId);
			accounts.setAvailableamount(0d);
			branchAccountMapper.insert(accounts);
		}
		return accounts;
	}

	/**
	 * 查询账户流水记录
	 */
	public ReturnModel findCashLogs(Long userId,Integer type, int index,int size){
		ReturnModel rq=new ReturnModel();
		PageHelper.startPage(index, size);
		List<UCashlogResult> logs= cashlogsMapper.findCashlogResultByUserId(userId, type);
		PageInfo<UCashlogResult> resultPage=new PageInfo<UCashlogResult>(logs); 
		if(resultPage.getList()!=null&&resultPage.getList().size()>0){
			for (UCashlogResult log : resultPage.getList()) {
				log.setCreatetimestr(DateUtil.getTimeStr(log.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
				if(type!=null&&type.intValue()==1){
					OPayorder payorder=payMapper.selectByPrimaryKey(log.getPayid());
					if(payorder!=null&&!ObjectUtil.isEmpty(payorder.getUserorderid())){
						OUserorders order= userordersMapper.selectByPrimaryKey(payorder.getUserorderid());
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
		}
		rq.setStatu(ReturnStatus.Success); 
		rq.setBasemodle(resultPage);
		return rq;
	}
	
	/**
	 * 查询代理商运费账户流水记录
	 */
	public ReturnModel findUBranchTansAmountlog(Long userId,Integer type, int index,int size){
		ReturnModel rq=new ReturnModel();
		
		
		
		PageHelper.startPage(index, size);
		List<UBranchTansAmountlogResult> logs= branchTransAmountlogsMapper.findUBranchTansAmountlogResultByBranchUserId(userId, type);
		PageInfo<UBranchTansAmountlogResult> resultPage=new PageInfo<UBranchTansAmountlogResult>(logs); 
		
		if(resultPage.getList()!=null&&resultPage.getList().size()>0){
			for (UBranchTansAmountlogResult log : resultPage.getList()) {
				log.setCreatetimestr(DateUtil.getTimeStr(log.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
				if(type!=null&&type.intValue()==1){
					OPayorder payorder=payMapper.selectByPrimaryKey(log.getPayid());
					if(payorder!=null&&!ObjectUtil.isEmpty(payorder.getUserorderid())){
						OUserorders order= userordersMapper.selectByPrimaryKey(payorder.getUserorderid());
						if(order!=null){
							log.setOrderId(order.getUserorderid());
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
