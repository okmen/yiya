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
import com.bbyiya.utils.SendSMSByMobile;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.vo.user.RegisterParam;
import com.bbyiya.web.base.SSOController;
import com.sdicons.json.mapper.MapperException;

@Controller
@RequestMapping(value = "/login")
public class LoginContoller extends SSOController{

	@Resource(name="userLoginService")
	private IUserLoginService loginService;
	
	/**
	 * 账户、用户名、昵称，密码登陆
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
	 * 获取登陆信息
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
			rq.setStatusreson("请重新登陆！");
		}
		return JsonUtil.objectToJsonStr(rq); 
	}
	/**
	 * 第三放登陆
	 * @param headImg
	 * @param loginType
	 * @param nickName
	 * @param openId
	 * @return
	 * @throws MapperException
	 */
	@ResponseBody
    @RequestMapping(value = "/otherLogin")
    public String otherLogin(String headImg,Short loginType,String nickName,String openId) throws MapperException
    {
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("登陆成功"); 
		return JsonUtil.objectToJsonStr(rq); 
    } 
	

	
	/**
	 * 第三方注册 第二步
	 * @param pwd
	 * @param upweiid
	 * @param tokent
	 * @return
	 * @throws MapperException
	 */
	@ResponseBody
    @RequestMapping(value = "/registerOthers")
    public String registerOthers(String pwd,Short upweiid,String tokent) throws MapperException
    {
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle("");
		rq.setStatusreson("登陆成功"); 
		return JsonUtil.objectToJsonStr(rq); 
    }
	
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
