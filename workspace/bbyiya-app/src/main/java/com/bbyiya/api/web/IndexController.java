package com.bbyiya.api.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.utils.ObjectUtil;

@Controller
@RequestMapping(value = "/")
public class IndexController {
	@Autowired
	private UUsersMapper userMapper;
	
	
	@RequestMapping(value = "/login")
	public String login(Model model,String aa) throws Exception {
		
		userMapper.selectByPrimaryKey(1l);
		
		if(ObjectUtil.isEmpty(aa)){
			return "register";
		}else {
			return "login";
		}
		
	}
}