package com.bbyiya.pic.web.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.pic.utils.WxPublicUtils;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/wx")
public class WxController extends SSOController {
	
	/**
	 * ��ȡ΢��config
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getWXconfig")
	public String getWXconfig(String webUrl) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			String accessToken=WxPublicUtils.getAccessToken(user.getUserId());
			if(!ObjectUtil.isEmpty(accessToken)){//��ȡ
				 rq= WxPublicUtils.getWxConfig(accessToken, webUrl);
			}else {
				rq.setStatu(ReturnStatus.LoginError_3);
				rq.setStatusreson("΢��accessToken���ڣ�");
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��½���ڣ������µ�½��");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
}
