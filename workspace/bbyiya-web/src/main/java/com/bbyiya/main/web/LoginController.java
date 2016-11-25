package com.bbyiya.main.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(value = "/")
public class LoginController {

	@RequestMapping(value = "/login")
	public String login(Model model,String userno) throws Exception {
	
		return "login";
	}
	
	@RequestMapping(value = "/register")
	public String register(Model model,String aa) throws Exception {
		return "register";
	}
	
	
}
