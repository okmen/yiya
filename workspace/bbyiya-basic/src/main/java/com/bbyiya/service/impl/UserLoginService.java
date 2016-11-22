package com.bbyiya.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.UOtherloginMapper;
import com.bbyiya.service.IUserLoginService;
import com.bbyiya.vo.user.OtherLoginParam;

@Service("userLoginService")
@Transactional(rollbackFor={RuntimeException.class, Exception.class})
public class UserLoginService implements IUserLoginService{
	@Autowired
	private UOtherloginMapper otherloginMapper;
	
	
	public void otherLogin(OtherLoginParam param){
		
	}
	
}
