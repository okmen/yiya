package com.bbyiya.pic.web;

import java.util.Date;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bbyiya.baseUtils.CookieUtils;
import com.bbyiya.dao.EErrorsMapper;
import com.bbyiya.dao.ULoginlogsMapper;
import com.bbyiya.dao.UUsertesterwxMapper;
import com.bbyiya.enums.LoginTypeEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.EErrors;
import com.bbyiya.model.ULoginlogs;
import com.bbyiya.pic.service.IPic_UserMgtService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.HttpRequestHelper;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.pay.WxPayConfig;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.vo.user.OtherLoginParam;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/login")
public class LoginTransferController extends SSOController {
	/**
	 * 登陆、 注册 service
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
	 * IBS登录 中转页
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ibs/index")
	public String transferPage(String backurl) throws Exception {
		return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxcc101e7b17ed868e&redirect_uri=https%3A%2F%2Fmpic.bbyiya.com%2Flogin%2FwxLogin2&response_type=code&scope=snsapi_base#wechat_redirect";
	}


	/**
	 * IBS 微信登录
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/wxLogin2")
	public String wxLogin2(String code, String state) throws Exception {
		ReturnModel rqModel = getWxLogin(code, state);
		if (rqModel.getStatu().equals(ReturnStatus.Success)) {
			return "redirect:" + ConfigUtil.getSingleValue("loginbackurl_ibs")+ "?ticket=" + ((LoginSuccessResult) rqModel.getBasemodle()).getTicket();
		} else {
			addlog(rqModel.getStatusreson());
			return "/index";
		}
	}

	/**
	 * 微信登录
	 * 
	 * @param code
	 * @param state
	 * @return
	 */
	public ReturnModel getWxLogin(String code, String state) {
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
			String access_token = String.valueOf(model.get("access_token"));
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
					if(!ObjectUtil.isEmpty(nickName)&&!"null".equals(nickName)){
						param.setNickName(nickName);
					}
					String headimgurl=String.valueOf(userJson.get("headimgurl"));
					if(!ObjectUtil.isEmpty(headimgurl)&&!"null".equals(headimgurl)){
						param.setHeadImg(headimgurl);
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
		return rqModel;
	}

	/**
	 * 加入session, cookie
	 * 
	 * @param obj
	 */
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
//				CookieUtils.addCookieBySessionId(request, response, user.getTicket(), 86400);
				CookieUtils.addCookie_web(request, response,user.getTicket(),86400);
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
		errors.setCreatetime(new Date()); 
		errorMapper.insert(errors);
	}

}
