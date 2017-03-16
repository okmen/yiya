package com.bbyiya.pic.web;

import java.util.Date;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.baseUtils.CookieUtils;
import com.bbyiya.dao.EErrorsMapper;
import com.bbyiya.dao.ULoginlogsMapper;
import com.bbyiya.dao.UUsertesterwxMapper;
import com.bbyiya.enums.LoginTypeEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.EErrors;
import com.bbyiya.model.ULoginlogs;
import com.bbyiya.pic.service.IPic_UserMgtService;
import com.bbyiya.service.IUserLoginService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.HttpRequestHelper;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
//import com.bbyiya.utils.pay.WxPayAppConfig;
import com.bbyiya.utils.pay.WxPayConfig;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.vo.user.OtherLoginParam;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/login")
public class LoginController extends SSOController {
	/**
	 * 登陆、注册 service
	 */
	@Resource(name = "userLoginService")
	private IUserLoginService loginBaseService; 
	/**
	 * 登陆、注册 service
	 */
	@Resource(name = "pic_userMgtService")
	private IPic_UserMgtService loginService;

	@Autowired
	private UUsertesterwxMapper testMapper;
	@Autowired
	private ULoginlogsMapper loginLogMapper;
	@Autowired
	private EErrorsMapper errorMapper;
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
		if (ReturnStatus.Success.equals(rqModel.getStatu()) && !ObjectUtil.isEmpty(rqModel.getBasemodle())) {
			addLoginLogAndCookie(rqModel.getBasemodle());
		}
		return JsonUtil.objectToJsonStr(rqModel);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/loginPhone")
	public String loginPhone(String phone, String pwd) throws Exception {
		ReturnModel rqModel = loginBaseService.login(phone, pwd);
		if (ReturnStatus.Success.equals(rqModel.getStatu()) && !ObjectUtil.isEmpty(rqModel.getBasemodle())) {
			addLoginLogAndCookie(rqModel.getBasemodle());
		}
		return JsonUtil.objectToJsonStr(rqModel);
	}

	/**
	 * A05 获取用户登录信息
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
	 * 登录 中转页
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/transfer")
	public String transferPage(String m) throws Exception {
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			int mtype=ObjectUtil.parseInt(m);
			if(mtype==1){
				return "redirect:"+ ConfigUtil.getSingleValue("loginbackurl_test") ;
			}else {
				return "redirect:"+ ConfigUtil.getSingleValue("loginbackurl") ;
			}
		} else {
			return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxcc101e7b17ed868e&redirect_uri=https%3A%2F%2Fmpic.bbyiya.com%2Flogin%2FwxLogin&response_type=code&scope=snsapi_base#wechat_redirect" ;	
		}		
	}
	
	
	
	

	String access_token;
	/**
	 * 微信登录
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/wxLogin")
	public String wxLogin(String code, String state) throws Exception {
		if (ObjectUtil.isEmpty(code)) {
			code = request.getParameter("code");
		}
		String urlString = "https://api.weixin.qq.com/sns/oauth2/access_token";
		String dataString = "appid=" + WxPayConfig.APPID + "&secret=" + WxPayConfig.AppSecret + "&code=" + code + "&grant_type=authorization_code";
		String result = HttpRequestHelper.sendPost(urlString, dataString);

		JSONObject model = JSONObject.fromObject(result);
		ReturnModel rqModel = new ReturnModel();
		if (model != null) {
			String openid = String.valueOf(model.get("openid"));
			access_token = String.valueOf(model.get("access_token"));

			if (!ObjectUtil.isEmpty(openid) && !ObjectUtil.isEmpty(access_token) && !"null".equals(openid) && !"null".equals(access_token)) {

				String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo";
				String data2 = "access_token=" + access_token + "&openid=" + openid;
				String userInfoJson = HttpRequestHelper.sendPost(userInfoUrl, data2);
				JSONObject userJson = JSONObject.fromObject(userInfoJson);
				if (userInfoJson != null) {
					OtherLoginParam param = new OtherLoginParam();
					param.setOpenId(openid);
					param.setLoginType(Integer.parseInt(LoginTypeEnum.weixin.toString()));
					String nickName=String.valueOf(userJson.get("nickname"));
					String headimg=String.valueOf(userJson.get("headimgurl"));
					if(!ObjectUtil.isEmpty(nickName)&&!"null".equals(nickName)){
						param.setNickName(nickName);
					}
					if(!ObjectUtil.isEmpty(headimg)&&!"null".equals(headimg)){
						param.setHeadImg(headimg);
					}
					rqModel = loginService.otherLogin(param);
					if (ReturnStatus.Success.equals(rqModel.getStatu()) && !ObjectUtil.isEmpty(rqModel.getBasemodle())) {
						addLoginLogAndCookie(rqModel.getBasemodle());
					}
					
				} else {
					
					rqModel.setStatu(ReturnStatus.SystemError);
					rqModel.setStatusreson("获取用户信息失败");
				}
			} else {
				rqModel.setStatu(ReturnStatus.ParamError);
				rqModel.setStatusreson(String.valueOf(model.get("errmsg")));
			}
		} else {
			rqModel.setStatu(ReturnStatus.SystemError);
			rqModel.setStatusreson("获取微信登录权限失败");
		}
		if (rqModel.getStatu().equals(ReturnStatus.Success)) {
			return "redirect:" + ConfigUtil.getSingleValue("loginbackurl") ;
		} else {
			return "redirect:" + ConfigUtil.getSingleValue("loginbackurl") ;
		}
	}
	


	private void addLoginLogAndCookie(Object obj) {
		try {
			LoginSuccessResult user = (LoginSuccessResult) obj;
			if (user != null) {
				ULoginlogs loginLogs = new ULoginlogs();
				loginLogs.setUserid(user.getUserId());
				loginLogs.setLogintime(new Date());
				loginLogs.setLogintype(Integer.parseInt(LoginTypeEnum.weixin.toString()));
				loginLogs.setIpstr(super.getIpStr());
				loginLogs.setNickname(user.getNickName()); 
				loginLogs.setSourcetype(1);// 12photo
				loginLogMapper.insert(loginLogs);
				CookieUtils.addCookieBySessionId(request, response,user.getTicket(),86400); 
//				String sid=request.getSession().getId();
//				addlog("C端登录：sessionId="+sid+";val="+user.getTicket() );
//				WxPublicUtils.setAccessToken(user.getUserId(),access_token);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	
	
	/**
	 * 插入错误Log
	 * 
	 * @param msg
	 */
	public void addlog(String msg) {
		EErrors errors = new EErrors();
		errors.setClassname(this.getClass().getName());
		errors.setMsg(msg);
		errorMapper.insert(errors);
	}

}
