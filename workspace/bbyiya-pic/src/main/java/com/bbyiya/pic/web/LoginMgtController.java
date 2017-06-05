package com.bbyiya.pic.web;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.baseUtils.CookieUtils;
import com.bbyiya.dao.ULoginlogsMapper;
import com.bbyiya.enums.LoginTypeEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.ULoginlogs;
import com.bbyiya.pic.service.IPic_UserMgtService;
import com.bbyiya.service.IUserLoginService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.vo.user.OtherLoginParam;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/login")
public class LoginMgtController extends SSOController {
	/**
	 * ��½��ע�� service
	 */ 
	@Resource(name = "userLoginService")
	private IUserLoginService loginBaseService; 
	/**
	 * ��½��ע�� service
	 */
	@Resource(name = "pic_userMgtService")
	private IPic_UserMgtService loginService;
	@Autowired
	private ULoginlogsMapper loginLogMapper;
	
	/**
	 * A05 ��ȡ�û���¼��Ϣ
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
			rq.setStatusreson("��½���ڣ������µ�½��");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * �ֻ��ŵ�½
	 * @param phone
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/loginPhone")
	public String loginPhone(String phone, String pwd) throws Exception {
		ReturnModel rqModel = loginBaseService.login(phone, pwd);
		if (ReturnStatus.Success.equals(rqModel.getStatu()) && !ObjectUtil.isEmpty(rqModel.getBasemodle())) {
			addLoginLogAndCookie(rqModel.getBasemodle(),Integer.parseInt(LoginTypeEnum.mobilephone.toString()));
		}
		return JsonUtil.objectToJsonStr(rqModel);
	}
	
	/**
	 * A01 ��������½��ע��
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
	public String otherLogin(String headImg, @RequestParam(required = false, defaultValue = "2") int loginType, String nickName, String openId,String upUid) throws Exception {
		headImg = ObjectUtil.urlDecoder_decode(headImg, "");
		nickName = ObjectUtil.urlDecoder_decode(nickName, "");
		openId = ObjectUtil.urlDecoder_decode(openId, "");
		if (!ObjectUtil.validSqlStr(headImg) || !ObjectUtil.validSqlStr(nickName) || !ObjectUtil.validSqlStr(openId)) {
			ReturnModel rqModel = new ReturnModel();
			rqModel.setStatu(ReturnStatus.ParamError_2);
			rqModel.setStatusreson("�����зǷ��ַ�");
			return JsonUtil.objectToJsonStr(rqModel);
		}
		OtherLoginParam param = new OtherLoginParam();
		param.setOpenId(openId);
		param.setLoginType(loginType);
		if(!ObjectUtil.isEmpty(nickName)){
			nickName=java.net.URLDecoder.decode(nickName,"UTF-8");
			nickName=ObjectUtil.filterUtf8Mb4(nickName);
		}
		param.setNickName(nickName);
		param.setHeadImg(headImg);
		if(ObjectUtil.isEmpty(upUid)){
			param.setUpUserId(ObjectUtil.parseLong(upUid)); 
		} 
		ReturnModel rqModel = loginService.otherLogin(param);
		if (ReturnStatus.Success.equals(rqModel.getStatu()) && !ObjectUtil.isEmpty(rqModel.getBasemodle())) {
			addLoginLogAndCookie(rqModel.getBasemodle(),0);
		}
		return JsonUtil.objectToJsonStr(rqModel);
	}
	

	private void addLoginLogAndCookie(Object obj,int type) {
		try {
			LoginSuccessResult user = (LoginSuccessResult) obj;
			if (user != null) {
				ULoginlogs loginLogs = new ULoginlogs();
				loginLogs.setUserid(user.getUserId());
				loginLogs.setLogintime(new Date());
				loginLogs.setLogintype(type);
				loginLogs.setIpstr(super.getIpStr());
				loginLogs.setNickname(user.getNickName()); 
				loginLogs.setSourcetype(1);// 12photo
				loginLogMapper.insert(loginLogs);
//				CookieUtils.addCookieBySessionId(request, response,user.getTicket(),86400); 
				CookieUtils.addCookie_web(request, response,user.getTicket(),86400);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
