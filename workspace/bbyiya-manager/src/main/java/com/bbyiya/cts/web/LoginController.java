package com.bbyiya.cts.web;

import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.cts.service.IUAdminService;
import com.bbyiya.cts.vo.admin.AdminLoginSuccessResult;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.baseUtils.CookieUtils;


@Controller
@RequestMapping(value = "/")
public class LoginController  extends CtsSSOController{
	@Autowired
	HttpServletResponse response;
	
	@Resource(name = "userAdminService")
	private IUAdminService adminService; 
	
	@RequestMapping(value = "/login")
	public  String login(Model model) throws Exception {
		return "login";
	}
	/**
	 * 登录成功返回页
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/loginsuccess")
	public  String loginsuccess(Model model) throws Exception {
		AdminLoginSuccessResult user= this.getLoginUser();
		if(user!=null){
			model.addAttribute("msg", user.getUsername());
		}else {
			model.addAttribute("msg", "用户名或密码错误");
			return "login";
		}
		return "msg";
	}
	
	/**
	 * 用户登录
	 * @param model
	 * @param username
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/loginAjax")
	public  String loginAjax(Model model,String username,String pwd) throws Exception {
		ReturnModel rqModel= adminService.loginProcess(username, pwd);
		if(rqModel.getStatu().equals(ReturnStatus.Success)){
			String ticket=UUID.randomUUID().toString();
			RedisUtil.setObject(ticket, rqModel.getBasemodle(), 3600);
			CookieUtils.addCookie(response, token, ticket, 3600);
			rqModel.setStatusreson(ticket); 
		}else {
			String ticket= CookieUtils.getCookieByName(request, token);
			if(!ObjectUtil.isEmpty(ticket)){
				RedisUtil.delete(ticket);
			}
		}
		return JsonUtil.objectToJsonStr(rqModel);
	}
	
	
	@RequestMapping(value = "/register")
	public String register(Model model,String aa) throws Exception {
		return "register";
	}
}
