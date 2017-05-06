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
		String accessToken = WxPublicUtils.getAccessToken();
		if (!ObjectUtil.isEmpty(accessToken)) {// 获取
			rq = WxPublicUtils.getWxConfig(accessToken, webUrl);
		} else {
			rq.setStatu(ReturnStatus.LoginError_3);
			rq.setStatusreson("微信accessToken过期！");
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
