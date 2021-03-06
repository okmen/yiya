package com.bbyiya.pic.web.ibs;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.pic.service.IPic_OrderMgtService;
import com.bbyiya.pic.service.ibs.IIbs_OrderManageService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/ibs/order")
public class OrderIBSController extends SSOController {
	@Resource(name = "pic_orderMgtService")
	private IPic_OrderMgtService orderService;

	@Resource(name = "ibs_OrderManageService")
	private IIbs_OrderManageService ibs_OrderManageService;

	/**
	 * 代分配的订单列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/waitinglist")
	public String waitinglist() throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			rq = orderService.findAgentOrders(user.getUserId());
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * 本店订单列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/myOrderlist")
	public String myOrderlist(@RequestParam(required = false, defaultValue = "2") int status) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			if (ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.branch)) {
				rq = orderService.findMyOrderlist(user.getUserId(), status);
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

	/**
	 * 获取订单详情
	 * 
	 * @param userOrderId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getOrderDetail")
	public String getOrderDetail(String userOrderId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			rq = orderService.getOrderDetail(userOrderId);
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * 我要这个客户
	 * 
	 * @param userOrderId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getMyOrder")
	public String getMyOrder(String userOrderId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			if (ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.branch)) {
				rq = orderService.addCustomer(user.getUserId(), userOrderId);
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

	/**
	 * 根据推荐人userid获取订单列表
	 * @param status
	 * @param index
	 * @param size
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getPayOrderByUpUserId")
	public String getPayOrderByUpUserId(int status, int index, int size) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			rq = ibs_OrderManageService.find_payorderExtByUpUserid(user.getUserId(), status, index, size);
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
}
