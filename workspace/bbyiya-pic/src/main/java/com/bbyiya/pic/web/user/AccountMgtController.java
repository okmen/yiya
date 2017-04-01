package com.bbyiya.pic.web.user;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.model.UAccounts;
import com.bbyiya.model.UBranchtransaccounts;
import com.bbyiya.service.IBaseUserAccountService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;
@Controller
@RequestMapping(value = "/user/account")
public class AccountMgtController extends SSOController{
	@Resource(name = "baseUserAccountService")
	private IBaseUserAccountService accountService;
	
	/**
	 * A11 账户信息(余额信息)
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/info")
	public String getAccountInfo() throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			UAccounts accounts= accountService.getUserAccount(user.getUserId());
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(accounts);
			
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 * A12 账户信息-预存款交易流水
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/cashLogs")
	public String getCashLogs(@RequestParam(required = false, defaultValue = "1") int  type,int index,int size) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.branch)){
				rq= accountService.findCashLogs(user.getUserId(),type, index,size);
			}else {
				rq.setStatu(ReturnStatus.SystemError_1);
				rq.setStatusreson("权限不足");
			} 
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * PBS A1代理商运费账户信息(余额信息)
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/branchAccountInfo")
	public String getBranchAccounts() throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			UBranchtransaccounts accounts= accountService.getBranchAccounts(user.getUserId());
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(accounts);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 * PBS A2代理商运费账户交易流水
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/branchTransAccountlog")
	public String getbranchTransAccountLogs(@RequestParam(required = false, defaultValue = "1") int  type,int index,int size) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.branch)){
				rq= accountService.findUBranchTansAmountlog(user.getUserId(),type, index,size);
			}else {
				rq.setStatu(ReturnStatus.SystemError_1);
				rq.setStatusreson("权限不足");
			} 
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
}
