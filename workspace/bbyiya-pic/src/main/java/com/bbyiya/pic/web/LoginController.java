package com.bbyiya.pic.web;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.LoginTypeEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.pic.service.IPic_UserMgtService;
import com.bbyiya.utils.HttpRequestHelper;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.pay.WxPayAppConfig;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.OtherLoginParam;
import com.bbyiya.web.base.SSOController;

@Controller
//@CrossOrigin
@RequestMapping(value = "/login")
public class LoginController extends SSOController {
	/**
	 * 登陆、注册 service
	 */
	@Resource(name = "pic_userMgtService")
	private IPic_UserMgtService loginService;

	
	/**
	 * A01 第三方登陆、注册
	 * 
	 * @param headImg
	 * @param loginType
	 * @param nickName
	 * @param openId
	 * @return
	 * @throws Exception
	 */
	
	@ResponseBody
	@RequestMapping(value = "/otherLogin")
	public String otherLogin(String headImg, @RequestParam(required = false, defaultValue = "2") int loginType, String nickName, String openId) throws Exception {
		headImg = ObjectUtil.urlDecoder_decode(headImg, "");
		nickName = ObjectUtil.urlDecoder_decode(nickName, "");
		openId = ObjectUtil.urlDecoder_decode(openId, "");
		if (!ObjectUtil.validSqlStr(headImg) || !ObjectUtil.validSqlStr(nickName) || !ObjectUtil.validSqlStr(openId)) {
			ReturnModel rqModel = new ReturnModel();
			rqModel.setStatu(ReturnStatus.ParamError_2);
			rqModel.setStatusreson("参数有非法字符");
			return JsonUtil.objectToJsonStr(rqModel);
		}
		OtherLoginParam param = new OtherLoginParam();
		param.setOpenId(openId);
		param.setLoginType(loginType);
		param.setNickName(nickName);
		param.setHeadImg(headImg);
		ReturnModel rqModel = loginService.otherLogin(param);
		return JsonUtil.objectToJsonStr(rqModel);
	}

	/**
	 * 微信登录
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/wxLogin")
	public String wxLogin(String code) throws Exception {
		String urlString = "https://api.weixin.qq.com/sns/oauth2/access_token";
		String dataString = "appid=" + WxPayAppConfig.APPID + "&secret=" + WxPayAppConfig.AppSecret + "&code=" + code + "&grant_type=authorization_code";
		String result = HttpRequestHelper.sendPost(urlString, dataString);
		JSONObject model = JSONObject.fromObject(result);
		ReturnModel rqModel =new ReturnModel();
		if (model != null) {
			String openid=String.valueOf(model.get("openid"));
			String access_token=String.valueOf(model.get("access_token"));
			if(!ObjectUtil.isEmpty(openid)&&!ObjectUtil.isEmpty(access_token)&&!"null".equals(openid)&&!"null".equals(access_token)){
				String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo";
				String data2="access_token="+access_token+"&openid="+openid; 
				String userInfoJson=HttpRequestHelper.sendPost(userInfoUrl, data2);
				JSONObject userJson = JSONObject.fromObject(userInfoJson);
				if(userInfoJson!=null){
					OtherLoginParam param=new OtherLoginParam();
					param.setOpenId(openid);
					param.setLoginType(Integer.parseInt(LoginTypeEnum.weixin.toString()));
					param.setNickName( String.valueOf(userJson.get("nickname")));
					param.setHeadImg(String.valueOf(userJson.get("headimgurl")));
					rqModel = loginService.otherLogin(param);
				}else {
					rqModel.setStatu(ReturnStatus.SystemError);
					rqModel.setStatusreson("获取用户信息失败"); 
				}
			}else {
				rqModel.setStatu(ReturnStatus.ParamError);
				rqModel.setStatusreson(String.valueOf(model.get("errmsg"))); 
			}
		}else {
			rqModel.setStatu(ReturnStatus.SystemError);
			rqModel.setStatusreson("获取微信登录权限失败");
		}
		return JsonUtil.objectToJsonStr(rqModel);
	}

}
