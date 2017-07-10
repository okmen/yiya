package com.bbyiya.pic.web.cts;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.baseUtils.GenUtils;
import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.pic.service.cts.IUserService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/cts/user")
public class UserMgtController  extends SSOController{

	@Resource(name = "ctsuserService")
	private IUserService userService;
	
	/**
	 * cts 添加内部员工账号
	 * @param branchuserid
	 * @param amount
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addCtsUser")
	public String addCtsUser(String phone) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user=super.getLoginUser();
		if(user!=null) {
			if(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_admin)){
				rq=userService.addCtsUser(user.getUserId(), phone);
			}else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("不是cts管理员，权限不足！");
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	@ResponseBody
	@RequestMapping(value = "/deleteCtsUser")
	public String deleteCtsUser(String userid) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user=super.getLoginUser();
		if(user!=null) {
			if(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_admin)){
				rq=userService.deleteCtsUser(ObjectUtil.parseLong(userid));
			}else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("不是cts管理员，权限不足！");
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	@ResponseBody
	@RequestMapping(value = "/findCtsMemberlist")
	public String findCtsMemberlist(String keywords,int index,int size) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user=super.getLoginUser();
		if(user!=null) {
			if(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_admin)){
				rq=userService.findCtsMemberlist(keywords, index, size);
			}else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("不是cts管理员，权限不足！");
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	
}
