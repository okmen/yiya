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
import com.bbyiya.dao.ULoginlogsMapper;
import com.bbyiya.dao.UUsertesterwxMapper;
import com.bbyiya.enums.LoginTypeEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.ULoginlogs;
import com.bbyiya.model.UUsertesterwx;
import com.bbyiya.pic.service.IPic_UserMgtService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.HttpRequestHelper;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
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
	@Resource(name = "pic_userMgtService")
	private IPic_UserMgtService loginService;

	@Autowired
	private UUsertesterwxMapper testMapper;
	@Autowired
	private ULoginlogsMapper loginLogMapper;

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
			addLoginLog(rqModel.getBasemodle());
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
	 * 中转页
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/transfer")
	public String transferPage() throws Exception {
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			return "redirect:"+ ConfigUtil.getSingleValue("loginbackurl") ;//+ "?ticket=" + ((LoginSuccessResult) rqModel.getBasemodle()).getTicket();	
		} else {
			return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxcc101e7b17ed868e&redirect_uri=https%3A%2F%2Fmpic.bbyiya.com%2Flogin%2FwxLogin&response_type=code&scope=snsapi_base#wechat_redirect" ;	
		}		
	}
	
	

	/**
	 * 测试码验证
	 * 
	 * @param headImg
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/checkTesterCode")
	public String checkLoginCode(String testcode) throws Exception {
		ReturnModel rqModel = new ReturnModel();
		String code = ConfigUtil.getSingleValue("testcode", "value");
		int count = ObjectUtil.parseInt(ConfigUtil.getSingleValue("testcode", "count"));
		Integer usCount = (Integer) RedisUtil.getObject("testCode_" + code);
		if (usCount != null && usCount > count) {
			rqModel.setStatu(ReturnStatus.SystemError);
			rqModel.setStatusreson("不好意思，今天的邀请人数已经满了！");
			return JsonUtil.objectToJsonStr(rqModel);
		} else {
			usCount = usCount == null ? 0 : usCount;
		}
		if (!code.equals(testcode)) {
			rqModel.setStatu(ReturnStatus.SystemError);
			rqModel.setStatusreson("邀请码错误！");
			return JsonUtil.objectToJsonStr(rqModel);
		}
		LoginSuccessResult user = super.getLoginUser();
		if (user != null && (user.getIsTester() == null || user.getIsTester() != 1)) {
			UUsertesterwx tester = new UUsertesterwx();
			tester.setUserid(user.getUserId());
			tester.setCreatetime(new Date());
			Integer countIndex = testMapper.getMaxSort();
			tester.setSort((countIndex == null ? 0 : countIndex) + 1);
			tester.setType(1);
			tester.setStatus(1);
			testMapper.insert(tester);
			user.setIsTester(1);
			// 重新设置用户的登录缓存信息
			RedisUtil.setObject(super.getTicket(), user, 1800);
			rqModel.setStatu(ReturnStatus.Success);
			rqModel.setBasemodle(user);
			// 邀请码数量缓存
			RedisUtil.setObject("testCode_" + code, usCount + 1);
		}
		// loginService.otherLogin(param);
		return JsonUtil.objectToJsonStr(rqModel);
	}

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
		String logs = "code:" + code + ";state:" + state;
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
					logs += "5;";
					OtherLoginParam param = new OtherLoginParam();
					param.setOpenId(openid);
					param.setLoginType(Integer.parseInt(LoginTypeEnum.weixin.toString()));
					param.setNickName(String.valueOf(userJson.get("nickname")));
					param.setHeadImg(String.valueOf(userJson.get("headimgurl")));
					rqModel = loginService.otherLogin(param);
					if (ReturnStatus.Success.equals(rqModel.getStatu()) && !ObjectUtil.isEmpty(rqModel.getBasemodle())) {
						addLoginLog(rqModel.getBasemodle());
					}
					logs += "rqModel=" + JsonUtil.objectToJsonStr(rqModel);
				} else {
					logs += "6;";
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
		RedisUtil.setObject("loginlogs", logs, 6000);
		if (rqModel.getStatu().equals(ReturnStatus.Success)) {
			return "redirect:" + ConfigUtil.getSingleValue("loginbackurl") ;//+ "?ticket=" + ((LoginSuccessResult) rqModel.getBasemodle()).getTicket();
		} else {
			return "/index";// "redirect:http://localhost:9191/";
		}
	}

	private void addLoginLog(Object obj) {
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
				
				CookieUtils.addCookie(response, PHOTO_TOKEN, user.getTicket(),86400); 
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
