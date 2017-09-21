package com.bbyiya.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.OPayorderMapper;
import com.bbyiya.dao.OPayorderwalletdetailsMapper;
import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.UAccountsMapper;
import com.bbyiya.dao.UAccountslogsMapper;
import com.bbyiya.dao.UBranchtransaccountsMapper;
import com.bbyiya.dao.UBranchtransamountlogMapper;
import com.bbyiya.dao.UBranchusersMapper;
import com.bbyiya.dao.UCashlogsMapper;
import com.bbyiya.enums.AccountLogType;
import com.bbyiya.enums.AmountType;
import com.bbyiya.enums.PayOrderTypeEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.OPayorder;
import com.bbyiya.model.OPayorderwalletdetails;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.UAccounts;
import com.bbyiya.model.UAccountslogs;
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
	/*-------------------账户流水----------------------------------------*/
	@Autowired
	private UAccountslogsMapper accountslogsMapper;
	
	@Autowired
	private OPayorderwalletdetailsMapper walletMapper;
	
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
				if(type!=null&&type.intValue()==Integer.parseInt(AmountType.lost.toString())){ 
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
	
	/**
	 * 账户流水
	 */
	public ReturnModel findAcountsLogsPageResult(Long userId, String keywords,Integer type, int index, int size) {
		ReturnModel rq = new ReturnModel();
		PageHelper.startPage(index, size);
		List<UAccountslogs> logs = accountslogsMapper.findAccountsLogs(userId,keywords,type);
		PageInfo<UAccountslogs> resultPage = new PageInfo<UAccountslogs>(logs);
		if (resultPage.getList() != null && resultPage.getList().size() > 0) {
			for (UAccountslogs log : resultPage.getList()) {
				log.setCreatetimestr(DateUtil.getTimeStr(log.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
				//如果是红包收入
				if(log.getType()!=null&&log.getType().intValue()==Integer.parseInt(AccountLogType.get_redPackets.toString())){
					OPayorderwalletdetails walletDetail= walletMapper.selectByPrimaryKey(log.getOrderid());
					if(walletDetail!=null){
						log.setHeadImg(walletDetail.getHeadimg());
						log.setNickName(walletDetail.getNickname());
					}
				}
			}
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(resultPage);
		return rq;
	}
	
	
	/**
	 * 推广流水及总推广收入
	 */
	public ReturnModel findCommissionDetailsPageResult(Long userId, int index, int size) {
		ReturnModel rq = new ReturnModel();
		PageHelper.startPage(index, size);
		List<UAccountslogs> logs = accountslogsMapper.findCommissionDetails(userId, Integer.parseInt(AccountLogType.get_Commission.toString()));
		PageInfo<UAccountslogs> resultPage = new PageInfo<UAccountslogs>(logs);
		if (resultPage.getList() != null && resultPage.getList().size() > 0) {
			for (UAccountslogs log : resultPage.getList()) {
				log.setCreatetimestr(DateUtil.getTimeStr(log.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
			}
		}
		
		Double sumCommission = accountslogsMapper.getSumCommissionDetails(userId, Integer.parseInt(AccountLogType.get_Commission.toString()));
		
		HashMap<String, Object> map=new HashMap<String, Object>();
		map.put("sumCommission", sumCommission);
		map.put("listpage", resultPage);
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(map);
		return rq;
	}
	
	
	/**
	 * 使用红包将钱转移到冻结账户
	 * @param userId
	 * @param totalpricce
	 * @return
	 * @throws Exception
	 */
	public Double transferCashAccountsToFreeze(long userId,Double totalPrice)throws Exception{
		UAccounts accounts=accountsMapper.selectByPrimaryKey(userId);
		Double walletAmount=0.0;
		if(accounts!=null){
			if(accounts.getAvailableamount()==null){
				accounts.setAvailableamount(0.0);
			}
			if(accounts.getFreezecashamount()==null){
				accounts.setFreezecashamount(0.0);
			}
			walletAmount=(totalPrice>accounts.getAvailableamount())? accounts.getAvailableamount():totalPrice;
			//把红包转移到冻结金额
			accounts.setFreezecashamount(accounts.getFreezecashamount()+walletAmount);
			accounts.setAvailableamount(accounts.getAvailableamount()-walletAmount);
			accountsMapper.updateByPrimaryKey(accounts);
			
		}
		return walletAmount;
	}
	/**
	 * 消费冻结金额
	 * @param userId
	 * @param type
	 * @param amount
	 * @param PayId
	 * @param transOrderId 运单号
	 * @return
	 * @throws Exception
	 */
	public boolean add_FreezeCashAccountsLog(long userId,int type,Double amount,String PayId,String transOrderId)throws Exception{
		if(amount==null||userId<=0)
			return false;
		UAccounts accounts=accountsMapper.selectByPrimaryKey(userId);
		//1.流水记录
		UAccountslogs log=new UAccountslogs();
		log.setUserid(userId);
		log.setCreatetime(new Date());
		log.setType(type);
		if (type==Integer.parseInt(AccountLogType.use_payment.toString())) {
			log.setAmount((-1)*Math.abs(amount)); 
			log.setOrderid(PayId); 
			accountslogsMapper.insert(log);
		} else {
			return false;
		}
		//-----------账户冻结金额修正--------------------------------------------------
		if(accounts!=null){
			double totalamount=accounts.getFreezecashamount()==null?0d:accounts.getFreezecashamount();
			accounts.setFreezecashamount(totalamount+log.getAmount()); 
			accountsMapper.updateByPrimaryKeySelective(accounts);
		}else {
			accounts=new UAccounts();
			accounts.setUserid(userId); 
			accounts.setFreezecashamount(log.getAmount());
			accountsMapper.insert(accounts);
		}
		return true;
	}
	
	/**
	 * 
	 * @param userId
	 * @param type
	 * @param amount
	 * @param PayId
	 * @param transOrderId
	 * @return
	 * @throws Exception
	 */
	public boolean add_accountsLog(long userId,int type,Double amount,String PayId,String transOrderId)throws Exception{
		if(amount==null||userId<=0)
			return false;
		UAccounts accounts=accountsMapper.selectByPrimaryKey(userId);
		//1.流水记录
		UAccountslogs log=new UAccountslogs();
		log.setUserid(userId);
		log.setCreatetime(new Date());
		log.setType(type);
		//如果是账户充值
		if(type==Integer.parseInt(AccountLogType.get_recharge.toString())){
			log.setAmount(Math.abs(amount)); 
			log.setOrderid(PayId); 
			accountslogsMapper.insert(log);
		}
		else if (type==Integer.parseInt(AccountLogType.get_redPackets.toString())) {
			log.setAmount(Math.abs(amount)); 
			log.setOrderid(PayId); 
			accountslogsMapper.insert(log);
		}
		else if (type==Integer.parseInt(AccountLogType.get_Commission.toString())) {
			log.setAmount(Math.abs(amount)); 
			log.setOrderid(PayId); 
			accountslogsMapper.insert(log);
		}
		//购物消费
		else if (type==Integer.parseInt(AccountLogType.use_payment.toString())) {
			log.setAmount((-1)*Math.abs(amount)); 
			log.setOrderid(PayId); 
			accountslogsMapper.insert(log);
		}
		//快递消费
		else if (type==Integer.parseInt(AccountLogType.use_freight.toString())) {
			log.setAmount((-1)*Math.abs(amount)); 
			log.setOrderid(transOrderId); 
			accountslogsMapper.insert(log);
		}
		//提现
		else if (type==Integer.parseInt(AccountLogType.use_cashout.toString())) {
			log.setAmount((-1)*Math.abs(amount)); 
			log.setOrderid(PayId); 
			accountslogsMapper.insert(log);
		}else {
			return false;
		}
		//-----------账户金额修正--------------------------------------------------
		if(accounts!=null){
			double totalamount=accounts.getAvailableamount()==null?0d:accounts.getAvailableamount();
			accounts.setAvailableamount(totalamount+log.getAmount()); 
			accountsMapper.updateByPrimaryKeySelective(accounts);
		}else {
			accounts=new UAccounts();
			accounts.setUserid(userId); 
			accounts.setAvailableamount(log.getAmount());
			accountsMapper.insert(accounts);
		}
		return true;
	}
	
}
