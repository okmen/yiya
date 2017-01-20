package com.bbyiya.pic.service;

import com.bbyiya.vo.ReturnModel;

public interface IPic_PayMgtService {

	 ReturnModel getWxPayParam(String orderNo, String openid, String ip);
	 
	 ReturnModel getWxAppPayParam(String orderNo, String ip);
}
