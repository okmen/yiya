package com.bbyiya.pic.web.test;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.baseUtils.GenUtils;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.service.IBasePayService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/test")
public class TestOrderController  extends SSOController{
	private Logger logger = Logger.getLogger(TesterController.class);
	@Resource(name = "basePayServiceImpl")
	private IBasePayService orderMgtService;
	
	@ResponseBody 
	@RequestMapping(value = "/ss")
	public String templateMessageSend(String key) throws Exception {
		ReturnModel rq = new ReturnModel();
		for(int i=0;i<10;i++){
			String idString= GenUtils.generateUuid_Char8();
			System.out.println(idString);
		}
		rq.setBasemodle(key);
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}
	
	@ResponseBody 
	@RequestMapping(value = "/orderpaytest")
	public String orderpaytest(String payId) throws Exception {
		ReturnModel rq = new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		String currentDomain=ConfigUtil.getSingleValue("currentDomain");
		if(!ObjectUtil.isEmpty(currentDomain)&&currentDomain.contains("photo-net.")){
			if(orderMgtService.paySuccessProcess(payId)){
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("pay success!"); 
			}
		}else {
			rq.setStatusreson("²»´æÔÚ");
		}
		return JsonUtil.objectToJsonStr(rq);
	
	}
	
	
	
	
}
