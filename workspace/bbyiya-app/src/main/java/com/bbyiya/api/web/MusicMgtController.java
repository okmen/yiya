package com.bbyiya.api.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;


@Controller
@RequestMapping(value = "/")
public class MusicMgtController extends SSOController {

	/**
	 * ��ȡÿ���Ƽ�����
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getindexpage")
	public String getmusicdaylist() throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(user);
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("�����µ�½��");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
}
