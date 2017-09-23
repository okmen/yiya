package com.bbyiya.pic.web.test;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.baseUtils.GenUtils;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.service.IBasePayService;
import com.bbyiya.service.IBaseUserAccountService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/test")
public class TestOrderController  extends SSOController{
	@Resource(name = "basePayServiceImpl")
	private IBasePayService orderMgtService;
	
	@Resource(name = "baseUserAccountService")
	private IBaseUserAccountService accountService;
	
	@ResponseBody 
	@RequestMapping(value = "/urlparam")
	public String urlparam(String weburl) throws Exception {
		ReturnModel rq = new ReturnModel();
		Map<String, String> map= ObjectUtil.getUrlParam(weburl);
		if(map!=null){
			rq.setBasemodle(map); 
		}
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}
	
	@ResponseBody 
	@RequestMapping(value = "/ss")
	public String templateMessageSend(String key) throws Exception {
		ReturnModel rq = new ReturnModel();
		for(int i=0;i<10;i++){
			String idString= GenUtils.generateUuid_Char8();
			System.out.println(idString);
		}
		rq.setBasemodle(key);
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	@ResponseBody 
	@RequestMapping(value = "/accountlogs")
	public String templateMessageSend(long  userId,int type, int index,int size) throws Exception {
		ReturnModel rq = new ReturnModel();
		rq=accountService.findAcountsLogsPageResult(userId,null, type, index, size);
		
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}
	
	@ResponseBody 
	@RequestMapping(value = "/addAccountlogs")
	public String addAccountlog(long  userId,int type, double amount,String payId,String transNo) throws Exception {
		ReturnModel rq = new ReturnModel();
		if(accountService.add_accountsLog(userId, type, amount, payId, transNo)){
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("success");
		}
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 支付成功测试用
	 * @param payId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody 
	@RequestMapping(value = "/orderpaytest")
	public String orderpaytest(String payId) throws Exception {
		ReturnModel rq = new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		String currentDomain=ConfigUtil.getSingleValue("currentDomain");
		if(!ObjectUtil.isEmpty(currentDomain)&&currentDomain.contains("photo-net.")){
			if(orderMgtService.paySuccessProcess(payId)){
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("pay success!"); 
			}
		}else {
			rq.setStatusreson("false!");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	@ResponseBody 
	@RequestMapping(value = "/orderAmountDistribute")
	public String distributeOrderAmount(String orderId) throws Exception {
		ReturnModel rq = new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		String currentDomain=ConfigUtil.getSingleValue("currentDomain");
		if(!ObjectUtil.isEmpty(currentDomain)&&currentDomain.contains("photo-net.")){
			orderMgtService.distributeOrderAmount(orderId);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("ok!");
		}else { 
			rq.setStatusreson("false!");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
}
