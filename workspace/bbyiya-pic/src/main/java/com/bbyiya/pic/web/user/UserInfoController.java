package com.bbyiya.pic.web.user;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.baseUtils.CookieUtils;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.service.IUserInfoMgtService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.utils.encrypt.MD5Encrypt;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.vo.user.UUserInfoParam;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/user/info")
public class UserInfoController  extends SSOController{
	@Resource(name = "userInfoMgtService")
	private IUserInfoMgtService userInfoMgtService;
	@Resource(name = "userInfoMgtService")
	private IUserInfoMgtService userMgtService;
	
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
						String ticket=super.getTicket();
						if(ObjectUtil.isEmpty(ticket)){
							ticket=CookieUtils.getCookieBySessionId(request);
						}
						RedisUtil.setObject(ticket, loginUser, 86400); 
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
	
	/**
	 * ÖØÖÃÃÜÂë
	 * @param phone
	 * @param vcode
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/updatePwd")
	public String updatePwd(String phone, String vcode, String pwd) throws Exception {
		System.out.println(MD5Encrypt.encrypt(pwd)); 
		return JsonUtil.objectToJsonStr(userMgtService.updatePWD(phone, vcode, pwd));
	}

}
