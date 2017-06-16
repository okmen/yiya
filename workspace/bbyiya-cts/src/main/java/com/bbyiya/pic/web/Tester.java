package com.bbyiya.pic.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.baseUtils.GenUtils;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.web.base.SSOController;


@Controller
@RequestMapping(value = "/test")
public class Tester extends SSOController{


	@ResponseBody 
	@RequestMapping(value = "/randUuid")
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
}
