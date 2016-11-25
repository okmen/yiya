package com.bbyiya.api.web;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.common.enums.SendMsgEnums;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.service.IUserLoginService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.SendSMSByMobile;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.vo.user.OtherLoginParam;
import com.bbyiya.vo.user.RegisterParam;
import com.bbyiya.web.base.SSOController;
import com.sdicons.json.mapper.MapperException;

@Controller
@RequestMapping(value = "/login")
public class LoginContoller extends SSOController{

	@Resource(name="userLoginService")
	private IUserLoginService loginService;
	
	/**
	 * �˻����û������ǳƣ������½
	 * @param userno
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/loginAjax")
	public String loginAjax(String userno,String pwd) throws Exception {
		ReturnModel rq=loginService.login(userno, pwd);
		return JsonUtil.objectToJsonStr(rq); 
	}
	
	/**
	 * ��ȡ��½��Ϣ
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getuser")
	public String getuser() throws Exception {
		ReturnModel rq =new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(user);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("�����µ�½��");
		}
		return JsonUtil.objectToJsonStr(rq); 
	}
	/**
	 * ������ ��½ע��
	 * @param headImg
	 * @param loginType
	 * @param nickName
	 * @param openId
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
    @RequestMapping(value = "/otherLogin")
    public String otherLogin(String headImg,String loginType,String nickName,String openId) throws Exception
    {
		OtherLoginParam param =new OtherLoginParam();
		param.setOpenId(openId);
		param.setLoginType(ObjectUtil.parseInt(loginType));
		param.setNickName(nickName);
		param.setHeadImg(headImg); 
		return JsonUtil.objectToJsonStr(loginService.otherLogin(param)); 
    } 
	
	
	/**
	 * ע��
	 * ps:�û�������ע��
	 * @param username
	 * @param pwd
	 * @param phone
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
    @RequestMapping(value = "/registerAjax")
    public String registerAjax(String username,String pwd,String phone) throws Exception 
    {
		RegisterParam param=new RegisterParam();
		param.setUsername(username);
		param.setPassword(pwd);
		param.setMobilephone(phone);
		ReturnModel rq=loginService.register(param);
		return JsonUtil.objectToJsonStr(rq); 
    }
}
