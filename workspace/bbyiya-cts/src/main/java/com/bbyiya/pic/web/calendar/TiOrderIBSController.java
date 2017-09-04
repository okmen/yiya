package com.bbyiya.pic.web.calendar;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.enums.OrderTypeEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.pic.service.calendar.IIbs_TiOrderMgtService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/ibs/ti_order")
public class TiOrderIBSController extends SSOController {
	
	@Resource(name = "ibs_TiOrderMgtService")
	private IIbs_TiOrderMgtService orderService;
	/**
	 * 本店订单列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/myTiOrderlist")
	public String myTiOrderlist(@RequestParam(required = false, defaultValue = "1") int status,
			@RequestParam(required = false, defaultValue = "") String keywords,int index,int size) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			if (ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.branch)) {
				rq = orderService.findTiMyOrderlist(user.getUserId(),Integer.parseInt(OrderTypeEnum.ti_branchOrder.toString()), status,keywords,index,size);
			} else {
				rq.setStatu(ReturnStatus.SystemError_1);
				rq.setStatusreson("您还不是代理商，没有权限");
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	
	
}
