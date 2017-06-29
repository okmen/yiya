package com.bbyiya.pic.web.common;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.common.enums.SendMsgEnums;
import com.bbyiya.common.vo.SmsParam;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.user.UserStatusEnum;
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
		rq.setStatu(ReturnStatus.ParamError);
		if(!ObjectUtil.isMobile(phone)){
			rq.setStatusreson("��������ȷ���ֻ��ţ�");
			return JsonUtil.objectToJsonStr(rq); 
		}
		int codeType=ObjectUtil.parseInt(type);
		if(codeType>0){
			if(codeType==(Integer.parseInt(SendMsgEnums.register.toString()))){
				UUsers user= userDao.getUUsersByPhone(phone);
				if(user!=null&&user.getStatus()!=null&&user.getStatus().intValue()==Integer.parseInt(UserStatusEnum.ok.toString())){
					rq.setStatu(ReturnStatus.VcodeError_3); 
					rq.setStatusreson("�ֻ����Ѿ�ע�ᣡ");
					return JsonUtil.objectToJsonStr(rq); 
				}
			}else if (codeType==(Integer.parseInt(SendMsgEnums.backPwd.toString()))) {
				UUsers user= userDao.getUUsersByPhone(phone);
				if(user==null){
					rq.setStatu(ReturnStatus.VcodeError_4);
					rq.setStatusreson("�ֻ���δע�ᣡ");
					return JsonUtil.objectToJsonStr(rq); 
				}
			}
		}else {
			rq.setStatusreson("��������");
			return JsonUtil.objectToJsonStr(rq);
		}
		String result= SendSMSByMobile.sendSmsReturnJson(codeType, phone);
		if(ObjectUtil.isEmpty(result)){
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
				rq.setStatusreson("���ŷ���ʧ��"); //String.valueOf(model.get("msg"))
			}
		}else {
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson(result); 
		}
		return JsonUtil.objectToJsonStr(rq); 
    }
	
	@ResponseBody
    @RequestMapping(value = "/sendMsg2")
    public String sendMsg2(String phone,String type,String amount) throws MapperException
    {
		ReturnModel rq=new ReturnModel();
		SmsParam param=new SmsParam();
		param.setAmount(ObjectUtil.parseDouble(amount));
		int typeInt=ObjectUtil.parseInt(type);
		if(typeInt==6){
			param.setTransName("������");
			param.setTransNum("9543321");
		} 
		boolean result=SendSMSByMobile.sendSmS(ObjectUtil.parseInt(type), phone,param);
		if(!result){
			rq.setStatusreson("��������");
			return JsonUtil.objectToJsonStr(rq);
		}
		JSONObject model = JSONObject.fromObject(result);
		if(model!=null){
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("���ͳɹ�");
		}else {
			rq.setStatu(ReturnStatus.SystemError);
		}
		return JsonUtil.objectToJsonStr(rq); 
    }
}
