package com.bbyiya.api.web;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.service.IUserLoginService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/user")
public class UserInfoController extends SSOController {
	/**
	 * µ«¬Ω°¢◊¢≤· service
	 */
	@Resource(name = "userLoginService")
	private IUserLoginService loginService; 
	
	/**
	 * A09 ’“ªÿ√‹¬Î£®÷ÿ÷√√‹¬Î£©
	 * @param mobile
	 * @param vcode
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/updatePwd")
	public String updatePwd(String mobile, String vcode, String pwd) throws Exception {
		return JsonUtil.objectToJsonStr(loginService.updatePWD(mobile,vcode,pwd)); 
	}
}
