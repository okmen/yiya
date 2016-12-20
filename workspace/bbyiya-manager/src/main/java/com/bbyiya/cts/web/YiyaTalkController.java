package com.bbyiya.cts.web;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.cts.vo.admin.AdminLoginSuccessResult;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.MYiyabanner;
import com.bbyiya.service.IYiyaTalkService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;

@Controller
@RequestMapping(value = "/talks")
public class YiyaTalkController extends CtsSSOController {
	@Resource(name = "yiyaTalkService")
	private IYiyaTalkService talkService;

	/**
	 * 咿呀说 banner 新增修改
	 * @param model
	 * @param talkbannerJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addOrEdit_TalkBanner")
	public String addOrEdit_TalkBanner(Model model, String talkbannerJson) throws Exception {
		ReturnModel rqModel = new ReturnModel();
		AdminLoginSuccessResult user = this.getLoginUser();
		if (user != null) {
			if (!ObjectUtil.isEmpty(talkbannerJson)) {
				MYiyabanner param = (MYiyabanner) JsonUtil.jsonStrToObject(talkbannerJson, MYiyabanner.class);
				if (param != null) {
					rqModel = talkService.addOrEdit_yiyaTalkBanner(user.getAdminid(), param);
					return JsonUtil.objectToJsonStr(rqModel);
				}
			}
			rqModel.setStatu(ReturnStatus.ParamError_1);
			rqModel.setStatusreson("参数有误");
		} else {
			rqModel.setStatu(ReturnStatus.LoginError);
			rqModel.setStatusreson("登录过期");
		} 
		return JsonUtil.objectToJsonStr(rqModel);
	}
}
