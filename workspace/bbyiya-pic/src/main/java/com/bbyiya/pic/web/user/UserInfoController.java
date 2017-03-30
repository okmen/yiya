package com.bbyiya.pic.web.user;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.service.IUserInfoMgtService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.vo.user.UUserInfoParam;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/user/info")
public class UserInfoController  extends SSOController{
	@Resource(name = "userInfoMgtService")
	private IUserInfoMgtService userInfoMgtService;
	
	@ResponseBody
	@RequestMapping(value = "/edit")
	public String getAccountInfo(String userInfoJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			UUserInfoParam param=(UUserInfoParam)JsonUtil.jsonStrToObject(userInfoJson, UUserInfoParam.class);
			if(param!=null){
				rq= userInfoMgtService.editUUsers(user.getUserId(), param);
				if(ReturnStatus.Success.equals(rq.getStatu()) ){
					LoginSuccessResult loginUser= userInfoMgtService.getLoginSuccessResult(user.getUserId());
					if(loginUser!=null){
						RedisUtil.setObject(super.getTicket(), loginUser, 86400); 
					}
				}
			}
			rq.setStatu(ReturnStatus.Success);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("µÇÂ¼¹ýÆÚ");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
}
