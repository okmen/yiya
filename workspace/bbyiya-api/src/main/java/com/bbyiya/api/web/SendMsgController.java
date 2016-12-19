package com.bbyiya.api.web;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.common.enums.SendMsgEnums;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.UUsers;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.SendSMSByMobile;
import com.bbyiya.vo.ReturnModel;
import com.sdicons.json.mapper.MapperException;

@Controller
@RequestMapping(value = "/sms")
public class SendMsgController {

	@Autowired
	private UUsersMapper userDao;
	
	/**
	 * ���ŷ���
	 * @param phone
	 * @return
	 * @throws MapperException
	 */
	@ResponseBody
    @RequestMapping(value = "/sendMsg")
    public String sendMsg(String phone,String type) throws MapperException
    {
		ReturnModel rq=new ReturnModel();
		int codeType=ObjectUtil.parseInt(type);
		if(!ObjectUtil.isEmpty(type)){
			if(codeType==(Integer.parseInt(SendMsgEnums.register.toString()))){
				UUsers user= userDao.getUUsersByPhone(phone);
				if(user!=null){
					rq.setStatu(ReturnStatus.SystemError);
					rq.setStatusreson("�ֻ����Ѿ�ע�ᣡ");
					return JsonUtil.objectToJsonStr(rq); 
				}
			}
		}
		String result= SendSMSByMobile.sendSmsReturnJson(codeType, phone);
		if(ObjectUtil.isEmpty(result)){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("��������");
			return JsonUtil.objectToJsonStr(rq);
		}
		JSONObject model = JSONObject.fromObject(result);
		if(model!=null){
			String code=String.valueOf(model.get("code"));
			if(code.equals("0")){
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("���ͳɹ�");
			}else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson(String.valueOf(model.get("msg"))); 
			}
		}else {
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson(result); 
		}
		return JsonUtil.objectToJsonStr(rq); 
    }
}
