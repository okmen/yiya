package com.bbyiya.api.web.pay;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/wxpay")
public class WxPayController  extends SSOController{
	
	
	@ResponseBody
	@RequestMapping(value = "/getparam")
	public String getparam(String orderno) throws Exception {
		
		return "";
	}
}
