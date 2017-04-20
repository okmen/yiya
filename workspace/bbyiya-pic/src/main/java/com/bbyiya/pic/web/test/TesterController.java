package com.bbyiya.pic.web.test;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.baseUtils.GenUtils;
import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.dao.UAccountsMapper;
import com.bbyiya.dao.UCashlogsMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.AmountType;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.model.UAccounts;
import com.bbyiya.model.UCashlogs;
import com.bbyiya.model.UUsers;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;


@Controller
@RequestMapping(value = "/test")
public class TesterController  extends SSOController{

	@Autowired
	private UAccountsMapper accountMapper;
	@Autowired
	private UCashlogsMapper cashlogMapper;
	@Autowired
	private UUsersMapper userMapper;
	
	@ResponseBody
	@RequestMapping(value = "/send")
	public String download(String url,int count,int pageSize) throws Exception {
		ReturnModel rq = new ReturnModel();
		Date t1=new Date();
		if(count>0){
			ThreadCreateUtil th1=new ThreadCreateUtil(url, pageSize, count);
			int pageCount=count/pageSize;
			for(int i=0;i<pageCount;i++){
				new Thread(th1, "a" + i).start(); 
			}
		}
		Date t2=new Date();
		long timeS= t2.getTime()-t1.getTime();
		float seconds=(float)timeS/1000 ;
		rq.setBasemodle(seconds); 
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 * 用户充值
	 * @param branchuserid
	 * @param amount
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/chongzhi")
	public String chongzhi(long branchuserid,String  amount) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user=super.getLoginUser();
		double amountPrice=ObjectUtil.parseDouble(amount);
		if(user!=null&&user.getUserId().longValue()==35){
			UUsers branch= userMapper.getUUsersByUserID(branchuserid);
			if(branch!=null&&ValidateUtils.isIdentity(branch.getIdentity(), UserIdentityEnums.branch)){
				UAccounts accounts=accountMapper.selectByPrimaryKey(branchuserid);
				if(accounts!=null&&accounts.getAvailableamount()!=null&&accounts.getAvailableamount()>1000){
					if(amountPrice>10){
						rq.setStatu(ReturnStatus.SystemError);
						rq.setStatusreson("该用户账户已有金额"+accounts.getAvailableamount());
						return JsonUtil.objectToJsonStr(rq);
					}
				}
				String payId=GenUtils.getOrderNo(9999l); 
				UCashlogs log=new UCashlogs();
				log.setAmount(amountPrice);
				log.setUserid(branchuserid);
				log.setPayid(payId);
				log.setUsetype(Integer.parseInt(AmountType.get.toString()));//充值
				log.setCreatetime(new Date());
				cashlogMapper.insert(log);
				UCashlogs freeLog=new UCashlogs();
				freeLog.setAmount(amountPrice*2);
				freeLog.setUserid(branchuserid);
				freeLog.setPayid(payId);
				freeLog.setUsetype(Integer.parseInt(AmountType.free.toString()));//充值
				freeLog.setCreatetime(new Date());
				cashlogMapper.insert(freeLog);
				//充值 金额 = 实际金额*3 
				Double totalPriceTemp=amountPrice*3;
				
				if(accounts!=null){
					accounts.setAvailableamount(accounts.getAvailableamount()+totalPriceTemp);
					accountMapper.updateByPrimaryKeySelective(accounts);
				}else {
					accounts=new UAccounts();
					accounts.setUserid(branchuserid);
					accounts.setAvailableamount(totalPriceTemp);
					accountMapper.insert(accounts);
				}
			}
		}
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}
	
}

