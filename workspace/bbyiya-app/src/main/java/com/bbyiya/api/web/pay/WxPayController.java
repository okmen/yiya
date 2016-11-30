package com.bbyiya.api.web.pay;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.api.service.IPayService;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/wxpay")
public class WxPayController  extends SSOController{
	/**
	 * 第三方支付相关
	 * get the param information of weixinpay  
	 */
	@Resource(name="apiPayService")
	private IPayService payService;
	
	/**
	 * 获取微信支付参数
	 * @param orderno
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getparam")
	public String getparam(String orderno) throws Exception {
		LoginSuccessResult user= super.getLoginUser();
		ReturnModel rq=new ReturnModel();
		if(user!=null){
			rq= payService.getWeiXinPay_Param(user.getUserid(), orderno, "");
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登陆信息已过期，请重新登陆");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
}
