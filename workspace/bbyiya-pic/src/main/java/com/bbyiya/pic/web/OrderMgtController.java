package com.bbyiya.pic.web;

import java.text.DecimalFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.pic.service.IPic_UserMgtService;
import com.bbyiya.pic.vo.order.SubmitOrderProductParam;
import com.bbyiya.service.pic.IBaseOrderMgtService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/order")
public class OrderMgtController extends SSOController {
	
	@Resource(name = "baseOrderMgtServiceImpl")
	private IBaseOrderMgtService orderMgtService; 
	
	
	/**
	 * O01 提交订单（购买）
	 * @param addrId
	 * @param productJsonStr
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/submitOrder")
	public String submitOrder(@RequestParam(required = false, defaultValue = "0") long addrId, String productJsonStr) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null&&user.getUserId()!=null&&user.getUserId()>0){
			if(!ObjectUtil.isEmpty(productJsonStr)){
				SubmitOrderProductParam param=(SubmitOrderProductParam)JsonUtil.jsonStrToObject(productJsonStr, SubmitOrderProductParam.class);
				if(param!=null){
					rq.setBasemodle(getOrderNo(user.getUserId())); 
				}
			}
			rq.setStatu(ReturnStatus.Success);
			
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	public String getOrderNo(Long userId){
		if(userId==null)
			userId=1l;
		long temp=userId%100000;
		DecimalFormat df=new DecimalFormat("0000");
	    String str2=df.format(temp);
		String timeStr=DateUtil.getTimeStr(new Date(), "yyyyMMdd"+str2+"HHmmSSS");
		return timeStr;
	}
}
