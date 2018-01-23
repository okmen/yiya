package com.bbyiya.pic.web.calendar;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.pic.service.calendar.IIbs_TiOrderMgtService;
import com.bbyiya.service.calendar.ITi_OrderMgtService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;


@Controller
@RequestMapping(value = "/ibs/ti_order")
public class OrderListMgtController extends SSOController {
	@Resource(name = "ibs_TiOrderMgtService")
	private IIbs_TiOrderMgtService ibstiorderService;
	
	@Resource(name = "tiOrderMgtServiceImpl")
	private  ITi_OrderMgtService basetiorderService;
	
	
	
	/**
	 * 商城购买订单
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getMyCustomerBuylist")
	public String getMyCustomerBuylist(@RequestParam(required = false, defaultValue = "-1") int status,@RequestParam(required = false, defaultValue = "") String keywords,int index,int size) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			if (ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.ti_promoter)) {
				rq = ibstiorderService.findTiOrderBuyList(user.getUserId(),keywords,status<0?null:status,index,size);
			} else {
				rq.setStatu(ReturnStatus.SystemError_1);
				rq.setStatusreson("您还不是活动参与单位，没有权限");
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
}
