package com.bbyiya.api.web;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.common.enums.SendMsgEnums;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.SendSMSByMobile;
import com.bbyiya.vo.ReturnModel;
import com.sdicons.json.mapper.MapperException;

@Controller
@RequestMapping(value = "/sms")
public class SendMsgController {

	/**
	 * 短信发送
	 * @param phone
	 * @return
	 * @throws MapperException
	 */
	@ResponseBody
    @RequestMapping(value = "/sendMsg")
    public String sendMsg(String phone,String type) throws MapperException
    {
		ReturnModel rq=new ReturnModel();
		String result= SendSMSByMobile.sendMsg(SendMsgEnums.register, phone);
		JSONObject model = JSONObject.fromObject(result);
		if(model!=null){
			String code=String.valueOf(model.get("code"));
			if(code.equals("0")){
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("发送成功");
			}else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson(String.valueOf(model.get("msg"))); 
			}
		}else {
			rq.setStatusreson(result); 
		}
		return JsonUtil.objectToJsonStr(rq); 
    }
}
