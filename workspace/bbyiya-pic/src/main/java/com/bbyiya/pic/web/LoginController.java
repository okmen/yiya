package com.bbyiya.pic.web;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.RegionMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.pic.service.IPic_UserMgtService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.OtherLoginParam;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/login")
public class LoginController  extends SSOController {
	/**
	 * 登陆、注册 service
	 */
	@Resource(name = "pic_userMgtService")
	private IPic_UserMgtService loginService; 
	
	@Autowired
	private RegionMapper regionMapper;
	
	/**
	 * A01 第三方登陆、注册
	 * @param headImg
	 * @param loginType
	 * @param nickName
	 * @param openId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/otherLogin")
	public String otherLogin(String headImg, @RequestParam(required = false, defaultValue = "2")int loginType, String nickName, String openId) throws Exception {
		headImg=ObjectUtil.urlDecoder_decode(headImg, "");
		nickName=ObjectUtil.urlDecoder_decode(nickName, "");
		openId=ObjectUtil.urlDecoder_decode(openId, "");
		if(!ObjectUtil.validSqlStr(headImg)||!ObjectUtil.validSqlStr(nickName)||!ObjectUtil.validSqlStr(openId)){
			ReturnModel rqModel=new ReturnModel();
			rqModel.setStatu(ReturnStatus.ParamError_2);
			rqModel.setStatusreson("参数有非法字符");
			return JsonUtil.objectToJsonStr(rqModel);
		}
		OtherLoginParam param = new OtherLoginParam();
		param.setOpenId(openId);
		param.setLoginType(loginType);
		param.setNickName(nickName);
		param.setHeadImg(headImg);
		ReturnModel rqModel=loginService.otherLogin(param);
		return JsonUtil.objectToJsonStr(rqModel);
	}
	
}
