package com.bbyiya.api.web;


import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.common.enums.SendMsgEnums;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.service.IUserLoginService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.SendSMSByMobile;
import com.bbyiya.vo.ReturnModel;
import com.sdicons.json.mapper.MapperException;

@Controller
@RequestMapping(value = "/login")
public class LoginContoller {

	@Resource(name="userLoginService")
	private IUserLoginService loginService;
	
	/**
	 * �����ŵ�½
	 * @param headImg
	 * @param loginType
	 * @param nickName
	 * @param openId
	 * @return
	 * @throws MapperException
	 */
	@ResponseBody
    @RequestMapping(value = "/otherLogin")
    public String otherLogin(String headImg,Short loginType,String nickName,String openId) throws MapperException
    {
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("��½�ɹ�"); 
		return JsonUtil.objectToJsonStr(rq); 
    }
	

	
	/**
	 * ������ע�� �ڶ���
	 * @param pwd
	 * @param upweiid
	 * @param tokent
	 * @return
	 * @throws MapperException
	 */
	@ResponseBody
    @RequestMapping(value = "/registerOthers")
    public String registerOthers(String pwd,Short upweiid,String tokent) throws MapperException
    {
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle("");
		rq.setStatusreson("��½�ɹ�"); 
		return JsonUtil.objectToJsonStr(rq); 
    }
	
	
}
