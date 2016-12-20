package com.bbyiya.api.web;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.UUserresponses;
import com.bbyiya.service.IUserInfoMgtService;
import com.bbyiya.service.IUserLoginService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.vo.user.UChildInfoParam;
import com.bbyiya.vo.user.UUserInfoParam;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/user")
public class UserInfoController extends SSOController {

	@Resource(name = "userInfoMgtService")
	private IUserInfoMgtService userMgtService;
	@Resource(name = "userLoginService")
	private IUserLoginService loginService;

	/**
	 * A09 找回密码（重置密码）
	 * 
	 * @param mobile
	 * @param vcode
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/updatePwd")
	public String updatePwd(String mobile, String vcode, String pwd) throws Exception {
		return JsonUtil.objectToJsonStr(userMgtService.updatePWD(mobile, vcode, pwd));
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
		// 宝宝信息参数model
		UChildInfoParam child = (UChildInfoParam) JsonUtil.jsonStrToObject(childInfoJson, UChildInfoParam.class);
		rq = userMgtService.addOrEdit_UChildreninfo(user.getUserId(), child);
		if (rq.getStatu().equals(ReturnStatus.Success)) {// 成功 设置宝宝信息 =》
															// 更新用户登陆信息
			rq.setBasemodle(loginService.updateLoginSuccessResult(user));
			rq.setStatusreson("宝宝信息设置成功！");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * 用户信息修改
	 * 
	 * @param mobile
	 * @param vcode
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/updateUserInfo")
	public String updateUserInfo(String userInfoJsonStr) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user == null || user.getUserId() == null || user.getUserId() <= 0) {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期，请重新登录");
			return JsonUtil.objectToJsonStr(rq);
		}
		if (ObjectUtil.isEmpty(userInfoJsonStr)) {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("参数不能为空！");
			return JsonUtil.objectToJsonStr(rq);
		}
		UUserInfoParam param = (UUserInfoParam) JsonUtil.jsonStrToObject(userInfoJsonStr, UUserInfoParam.class);
		if (param != null) {
			rq = userMgtService.editUUsers(user.getUserId(), param);
			user.setTicket(super.getTicket());
			loginService.updateLoginSuccessResult(user);
		} else {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("参数格式不正确！");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * A11 用户意见反馈
	 * @param content
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addResponses")
	public String add_UUserresponses(String content) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user == null || user.getUserId() == null || user.getUserId() <= 0) {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期，请重新登录");
			return JsonUtil.objectToJsonStr(rq);
		}
		UUserresponses param = new UUserresponses();
		param.setUserid(user.getUserId());
		param.setContent(content);
		rq = userMgtService.add_UUserresponses(param);
		return JsonUtil.objectToJsonStr(rq);
	}
}
