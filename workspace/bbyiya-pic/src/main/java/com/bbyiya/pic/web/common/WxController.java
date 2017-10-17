package com.bbyiya.pic.web.common;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.EErrorsMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.EErrors;
import com.bbyiya.pic.utils.WxPublicUtils;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/wx")
public class WxController extends SSOController {
	@Autowired
	private EErrorsMapper errorMapper;
	/**
	 * 获取微信config
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getWXconfig")
	public String getWXconfig(String webUrl) throws Exception {
		ReturnModel rq = new ReturnModel();
		
		String jsapiString=WxPublicUtils.getWxApiToken();
		if(!ObjectUtil.isEmpty(jsapiString)){
			rq=WxPublicUtils.getWxConfigNew(jsapiString, webUrl);
		}else {
			rq.setStatu(ReturnStatus.LoginError_3);
			rq.setStatusreson("微信accessToken过期！");
		}
		if(!ReturnStatus.Success.equals(rq.getStatu())){
			addlog(rq.getStatusreson()); 
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getAccessToken")
	public String getWXconfig() throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			String accessToken=WxPublicUtils.getWxApiToken();
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(accessToken);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 插入错误Log
	 * 
	 * @param msg
	 */
	public void addlog(String msg) {
		EErrors errors = new EErrors();
		errors.setClassname(this.getClass().getName());
		errors.setMsg(msg);
		errors.setCreatetime(new Date()); 
		errorMapper.insert(errors);
	}
}
