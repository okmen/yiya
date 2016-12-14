package com.bbyiya.cts.service.impl;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.cts.service.IUAdminService;
import com.bbyiya.vo.ReturnModel;


@Service("userAdminService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class UAdminService implements IUAdminService{
//	@Autowired private 
	
	
	public ReturnModel loginProcess(String username,String pwd){
		return null;
	}
}
