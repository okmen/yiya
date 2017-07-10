package com.bbyiya.pic.web.cts;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.pic.service.cts.IScenseService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/cts/scenes")
public class ScenesController extends SSOController {
	
	@Resource(name = "scenseService")
	private IScenseService scenseService;
	/**
	 * 添加场景
	 * @param myproductTempJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addScenes")
	public String addActivityCode(String myScenseJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_admin)||ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_member)){
				rq=scenseService.addorUpdateScense(user.getUserId(), myScenseJson);
			}else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("权限不足！");
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getScenseList")
	public String getScenseList(Integer index,Integer size,String productid,String keywords) throws Exception {
		ReturnModel rq=new ReturnModel();
		if(ObjectUtil.isEmpty(index)) index=0;
		if(ObjectUtil.isEmpty(size)) size=0;
		
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_admin)||ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_member)){
				rq=scenseService.getScenseList(index, size, keywords, productid);
			}else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("权限不足！");
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
		
}
