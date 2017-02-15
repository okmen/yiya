package com.bbyiya.pic.web.cts;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.pic.service.IPic_OrderMgtService;
import com.bbyiya.pic.vo.order.SearchOrderParam;
//import com.bbyiya.pic.vo.product.MyProductParam;
//import com.bbyiya.pic.web.common.Json2Objects;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/cts/order")
public class OrderController  extends SSOController {
	@Resource(name = "pic_orderMgtService")
	private IPic_OrderMgtService orderService;
	
	/**
	 * O06订单列表查询
	 * @param myproductJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/findOrderlist")
	public String findOrderlist(String myproductJson) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			SearchOrderParam param= (SearchOrderParam)JsonUtil.jsonStrToObject(myproductJson, SearchOrderParam.class);
			rq=orderService.find_orderList(param);
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	

}
