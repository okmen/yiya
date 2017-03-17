package com.bbyiya.pic.web.user;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.UAccounts;
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
	public String getCashLogs() throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq= accountService.findCashLogs(user.getUserId(), 1, 10);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
}
