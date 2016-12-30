package com.bbyiya.pic.web;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.UUseraddress;
import com.bbyiya.service.pic.IBaseUserAddressService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/user")
public class UserInfoMgtController extends SSOController {
	/**
	 * 登陆、注册 service
	 */
	@Resource(name = "baseUserAddressServiceImpl")
	private IBaseUserAddressService addressService;

	/**
	 * 新增、编辑收货地址
	 * 
	 * @param addrJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addOrEditAddr")
	public String area(String addrJson) throws Exception {
		LoginSuccessResult user = super.getLoginUser();
		ReturnModel rq = new ReturnModel();
		if (user != null) {
			UUseraddress address = (UUseraddress) JsonUtil.jsonStrToObject(addrJson, UUseraddress.class);
			if (address != null) {
				address.setUserid(user.getUserId());
				rq = addressService.addOrEdit_UserAddress(address);
			} else {
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数有误");
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
}
