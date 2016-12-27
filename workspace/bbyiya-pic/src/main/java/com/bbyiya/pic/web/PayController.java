package com.bbyiya.pic.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.common.vo.ResultMsg;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.pay.WxPayUtils;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.web.base.SSOController;
import com.sdicons.json.mapper.MapperException;

@Controller
@RequestMapping(value = "/pay")
public class PayController extends SSOController {

	/**
	 * Éú³É¶©µ¥
	 *  
	 * @return
	 * @throws MapperException 
	 */
	@ResponseBody
	@RequestMapping(value = "/getWxPayParam")
	public String getWxPayParam(String orderNo) throws MapperException {
		String ipAddres=request.getHeader("x-forwarded-for");
		if (ObjectUtil.isEmpty(ipAddres)) {
			ipAddres= request.getRemoteAddr();
		}
		ResultMsg msg= WxPayUtils.getWxPayParam(orderNo, "", 12.25d, ipAddres);
		ReturnModel rq=new ReturnModel();
		if(msg.getStatus()==1){
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(msg.getMsg());
		}else {
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson(msg.getMsg());
		}
		return JsonUtil.objectToJsonStr(rq); 
	}
}
