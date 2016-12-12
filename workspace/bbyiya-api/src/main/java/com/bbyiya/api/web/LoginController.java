package com.bbyiya.api.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.service.IUserLoginService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
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

	
	
	
	/**
	 * 手机号、密码登陆
	 * 
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
	 * 获取登陆信息
	 * 
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
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登陆过期，请重新登陆！");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * A01第三方登陆、注册
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
	public String otherLogin(String headImg, String loginType, String nickName, String openId) throws Exception {
		OtherLoginParam param = new OtherLoginParam();
		param.setOpenId(openId);
		param.setLoginType(ObjectUtil.parseInt(loginType));
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
		rq = loginService.addChildInfo(user.getUserId(), child);
		if(rq.getStatu().equals(ReturnStatus.Success)){//成功 设置宝宝信息 =》 更新用户登陆信息
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
//		LoginSuccessResult user = this.getLoginUser();
//		if (user == null) {
//			rq.setStatu(ReturnStatus.LoginError);
//			rq.setStatusreson("登陆过期");
//			return JsonUtil.objectToJsonStr(rq);
//		}
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		list.add(getRalation(1, "爸爸"));
		list.add(getRalation(2, "妈妈"));
		list.add(getRalation(3, "爷爷"));
		list.add(getRalation(4, "奶奶"));
		list.add(getRalation(5, "叔叔"));
		list.add(getRalation(6, "阿姨"));
		rq.setBasemodle(list); 
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	private Map<String, Object> getRalation(int id,String value){
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("id", id);
		map.put("value", value);
		return map;
	}

}
