package com.bbyiya.pic.web.cts;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.baseUtils.GenUtils;
import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.dao.UAccountsMapper;
import com.bbyiya.dao.UAdminMapper;
import com.bbyiya.dao.UAdminactionlogsMapper;
import com.bbyiya.dao.UBranchtransaccountsMapper;
import com.bbyiya.dao.UBranchtransamountlogMapper;
import com.bbyiya.dao.UCashlogsMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.AccountLogType;
import com.bbyiya.enums.AdminActionType;
import com.bbyiya.enums.AmountType;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.model.UAccounts;
import com.bbyiya.model.UAdmin;
import com.bbyiya.model.UAdminactionlogs;
import com.bbyiya.model.UBranchtransaccounts;
import com.bbyiya.model.UBranchtransamountlog;
import com.bbyiya.model.UCashlogs;
import com.bbyiya.model.UUsers;
import com.bbyiya.service.IBaseUserAccountService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;
import com.sdicons.json.mapper.MapperException;

@Controller
@RequestMapping(value = "/cts/account")
public class AccountController  extends SSOController{

	
	@Autowired
	private UUsersMapper userMapper;
	@Autowired
	private UAccountsMapper accountMapper;
	
	@Autowired
	private UBranchtransaccountsMapper transaccountMapper;
	@Autowired
	private UBranchtransamountlogMapper transaccountlogMapper;
	
	@Autowired
	private UCashlogsMapper cashlogMapper;
	@Autowired
	private UAdminactionlogsMapper actLogMapper;
	@Autowired
	private UAdminMapper adminMapper;
	
	@Resource(name = "baseUserAccountService")
	private IBaseUserAccountService accountService;
	
	/**
	 * cts �û���ֵ
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
		if(user!=null) {
			if(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_admin)||ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_member)){
				UUsers branch= userMapper.getUUsersByUserID(branchuserid);
				if(branch!=null&&ValidateUtils.isIdentity(branch.getIdentity(), UserIdentityEnums.branch)) {
					String payId=GenUtils.getOrderNo(9999l); 
					boolean result=accountService.add_accountsLog(branchuserid, Integer.parseInt(AccountLogType.get_recharge.toString()), amountPrice, payId, "");
					if(result){
						UAccounts accounts=accountMapper.selectByPrimaryKey(branchuserid);
						addActionLog(user.getUserId(),"[�˻���ֵ]�����ɹ�����ֵ��"+amountPrice+"Ԫ����ֵ�û� userId:"+branchuserid);
						rq.setStatusreson("��ֵ�ɹ����˻����ý�"+accounts.getAvailableamount()+"Ԫ!"); 
						rq.setStatu(ReturnStatus.Success);
					}else{
						rq.setStatusreson("��ֵʧ�ܣ�"); 
						rq.setStatu(ReturnStatus.SystemError);
					}
					
				}else {
					rq.setStatu(ReturnStatus.SystemError);
					rq.setStatusreson("���û�����Ӱ¥��ݣ�"); 
				}
			}else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("Ȩ�޲��㣡");
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	public void addActionLog(Long userId,String msg){
		UAdminactionlogs log=new UAdminactionlogs();
		log.setUserid(userId);
		log.setContent(msg);
		UAdmin admin= adminMapper.selectByPrimaryKey(userId);
		if(admin!=null){
			log.setUsername(admin.getUsername());
		}
		log.setType(Integer.parseInt(AdminActionType.chongzhi.toString()));
		log.setCreatetime(new Date());
		actLogMapper.insert(log);
	}
	
	/**
	 * cts �������ʷѳ�ֵ
	 * @param branchuserid
	 * @param amount
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/rechargeTransAccount")
	public String rechargeTransAccount(long branchuserid,String  amount) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user=super.getLoginUser();
		double amountPrice=ObjectUtil.parseDouble(amount);
		if(user!=null) {
			if(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_admin)||ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_member)){
				UUsers branch= userMapper.getUUsersByUserID(branchuserid);
				if(branch!=null&&ValidateUtils.isIdentity(branch.getIdentity(), UserIdentityEnums.branch)) {
					UBranchtransaccounts transaccount=transaccountMapper.selectByPrimaryKey(branchuserid);
					
					if(transaccount!=null){
						transaccount.setAvailableamount(transaccount.getAvailableamount()+amountPrice);
						transaccountMapper.updateByPrimaryKeySelective(transaccount);
					}else {
						transaccount=new UBranchtransaccounts();
						transaccount.setBranchuserid(branchuserid);
						transaccount.setAvailableamount(amountPrice);
						transaccountMapper.insert(transaccount);	
					}
					
					//������־
					String payId=GenUtils.getOrderNo(9999l); 
					UBranchtransamountlog translog=new UBranchtransamountlog();
					translog.setAmount(amountPrice);
					translog.setBranchuserid(branchuserid);
					translog.setCreatetime(new Date());
					translog.setPayid(payId);
					translog.setType(Integer.parseInt(AmountType.get.toString()));
					transaccountlogMapper.insert(translog);

					addActionLog(user.getUserId(),"[�ʷ��˻���ֵ]�����ɹ�����ֵ��"+amountPrice+"Ԫ����ֵ�û� userId:"+branchuserid);
					rq.setStatusreson("��ֵ�ɹ����ʷ��˻����ý�"+transaccount.getAvailableamount()+"Ԫ!"); 
					rq.setStatu(ReturnStatus.Success);
				}else {
					rq.setStatu(ReturnStatus.SystemError);
					rq.setStatusreson("���û�����Ӱ¥��ݣ�"); 
				}
			}else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("Ȩ�޲��㣡");
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

}
