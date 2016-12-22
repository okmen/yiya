package com.bbyiya.api.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.service.IUserInfoMgtService;
import com.bbyiya.service.IUserLoginService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.vo.user.OtherLoginParam;
import com.bbyiya.vo.user.RegisterParam;
import com.bbyiya.vo.user.UChildInfoParam;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/login")
public class LoginController extends SSOController {

	/**
	 * 登陆、注册 service
	 */
	@Resource(name = "userLoginService")
	private IUserLoginService loginService; 
	
	@Resource(name = "userInfoMgtService")
	private IUserInfoMgtService userMgtService; 
	
	
	
	/**
	 * A05登陆（手机号、密码登陆）
	 * 手机号、密码登陆
	 * @param phone手机号
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/loginAjax")
	public String loginAjax(String phone, String pwd) throws Exception {
		ReturnModel rq = loginService.login(phone, pwd);
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * A02退出登录
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/outLogin")
	public String outLogin() throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user=super.getLoginUser();
		if(user!=null){
			RedisUtil.delete(super.getTicket());
		} 
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("成功！");
		return JsonUtil.objectToJsonStr(rq);
	}

	
	/**
	 * A07获取用户登陆信息
	 * 获取登陆信息
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getuser")
	public String getuser() throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(user); 
			RedisUtil.setExpire(super.getTicket(), 604800);// 延长时间
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登陆过期，请重新登陆！");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * A01 第三方登陆、注册
	 * zy 
	 * @param headImg
	 * @param loginType
	 * @param nickName
	 * @param openId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/otherLogin")
	public String otherLogin(String headImg, @RequestParam(required = false, defaultValue = "2")int loginType, String nickName, String openId) throws Exception {
		headImg=ObjectUtil.urlDecoder_decode(headImg, "");//URLDecoder.decode(headImg,"UTF-8");
		nickName=ObjectUtil.urlDecoder_decode(nickName, "");//URLDecoder.decode(nickName,"UTF-8");
		openId=ObjectUtil.urlDecoder_decode(openId, "");//URLDecoder.decode(openId,"UTF-8");
		if(!ObjectUtil.validSqlStr(headImg)||!ObjectUtil.validSqlStr(nickName)||!ObjectUtil.validSqlStr(openId)){
			ReturnModel rqModel=new ReturnModel();
			rqModel.setStatu(ReturnStatus.ParamError_2);
			rqModel.setStatusreson("参数有非法字符");
			return JsonUtil.objectToJsonStr(rqModel);
		}
		OtherLoginParam param = new OtherLoginParam();
		param.setOpenId(openId);
		param.setLoginType(loginType);
		param.setNickName(nickName);
		param.setHeadImg(headImg);
		return JsonUtil.objectToJsonStr(loginService.otherLogin(param));
	}

	/**
	 * A06注册（用手机号、密码注册）
	 * 注册 ps:用户名密码,手机号 注册（如果是第三方注册，必须填写register_token）
	 * 
	 * @param username
	 * @param pwd
	 * @param phone
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/registerAjax")
	public String registerAjax(String username, String pwd, String phone, String vcode, String register_token) throws Exception {
		RegisterParam param = new RegisterParam();
		param.setUsername(username);
		param.setPassword(pwd);
		param.setMobilephone(phone);
		param.setVcode(vcode);
		param.setRegister_token(register_token);
		ReturnModel rq = loginService.register(param);
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * A08 保存宝宝信息
	 * 
	 * @param childInfoJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addChildInfo")
	public String addChildInfo(String childInfoJson) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = this.getLoginUser();
		if (user == null) {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登陆过期，请重新登陆！");
			return JsonUtil.objectToJsonStr(rq);
		}
		if (ObjectUtil.isEmpty(childInfoJson)) {
			rq.setStatu(ReturnStatus.ParamError); 
			rq.setStatusreson("参数不能为空");
			return JsonUtil.objectToJsonStr(rq);
		}
		//宝宝信息参数model
		UChildInfoParam child = (UChildInfoParam) JsonUtil.jsonStrToObject(childInfoJson, UChildInfoParam.class);
		rq = userMgtService.addOrEdit_UChildreninfo(user.getUserId(), child);
		if(rq.getStatu().equals(ReturnStatus.Success)){//成功 设置宝宝信息 =》 更新用户登陆信息
			user.setTicket(super.getTicket()); 
			rq.setBasemodle(loginService.updateLoginSuccessResult(user));  
			rq.setStatusreson("宝宝信息设置成功！");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * A08-1 保存宝宝信息
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getrelation")
	public String getrelation() throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = this.getLoginUser();
		if (user == null) {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登陆过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("relations", ConfigUtil.getMaplist("relations"));
		rq.setBasemodle(map); 
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}
	
	

}
