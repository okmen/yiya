package com.bbyiya.pic.web;

//import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.common.vo.ResultMsg;
import com.bbyiya.dao.OPayorderMapper;
import com.bbyiya.dao.UOtherloginMapper;
import com.bbyiya.enums.OrderStatusEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.OPayorder;
import com.bbyiya.model.UOtherlogin;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.pay.WxAppPayUtils;
import com.bbyiya.utils.pay.WxPayUtils;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;
import com.sdicons.json.mapper.MapperException;

@Controller
@RequestMapping(value = "/pay")
public class PayController extends SSOController {
	@Autowired
	private OPayorderMapper payMapper;
	@Autowired
	private UOtherloginMapper otherMapper;
	/**
	 * 获取微信支付参数
	 *  
	 * @return
	 * @throws MapperException 
	 */
	@ResponseBody
	@RequestMapping(value = "/getWxPayParam")
	public String getWxPayParam(String payId) throws MapperException {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user=super.getLoginUser();
		if(user!=null){
			UOtherlogin otherlogin= otherMapper.getWxloginByUserId(user.getUserId());
			if(otherlogin!=null){
				String ipAddres=request.getHeader("x-forwarded-for");
				if (ObjectUtil.isEmpty(ipAddres)) {
					ipAddres = request.getHeader("Proxy-Client-IP");
					if(ObjectUtil.isEmpty(ipAddres)){
						ipAddres=request.getHeader("WL-Proxy-Client-IP");
						if(ObjectUtil.isEmpty(ipAddres)){
							ipAddres= request.getRemoteAddr();
						}
					}
				}
				OPayorder payorder= payMapper.selectByPrimaryKey(payId);
				if(payorder!=null){
					if(payorder.getStatus().intValue()==Integer.parseInt(OrderStatusEnum.noPay.toString())){
						ResultMsg msg= WxPayUtils.getWxPayParam(payId, otherlogin.getOpenid(), "", payorder.getTotalprice(), ipAddres);
						if(msg.getStatus()==1){
							rq.setStatu(ReturnStatus.Success);
							rq.setBasemodle(msg.getMsg());
						}else {
							rq.setStatu(ReturnStatus.SystemError);
							rq.setStatusreson(msg.getMsg());
						}
					}
				}
			}else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("微信未登录");
			}
			
		}		
		return JsonUtil.objectToJsonStr(rq); 
	}
	
	
	/**
	 * APP 获取微信支付参数
	 * @param payId
	 * @return
	 * @throws MapperException
	 */
	@ResponseBody
	@RequestMapping(value = "/getWxAPPPayParam")
	public String getWxAPPPayParam(String payId) throws MapperException {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user=super.getLoginUser();
		if(user!=null){
			UOtherlogin otherlogin= otherMapper.getWxloginByUserId(user.getUserId());
			if(otherlogin!=null){
				String ipAddres=request.getHeader("x-forwarded-for");
				if (ObjectUtil.isEmpty(ipAddres)) {
					ipAddres = request.getHeader("Proxy-Client-IP");
					if(ObjectUtil.isEmpty(ipAddres)){
						ipAddres=request.getHeader("WL-Proxy-Client-IP");
						if(ObjectUtil.isEmpty(ipAddres)){
							ipAddres= request.getRemoteAddr();
						}
					}
				}
				OPayorder payorder= payMapper.selectByPrimaryKey(payId);
				if(payorder!=null){
					if(payorder.getStatus().intValue()==Integer.parseInt(OrderStatusEnum.noPay.toString())){
						ResultMsg msg= WxAppPayUtils.getWxPayParam(payId, "", payorder.getTotalprice(), ipAddres);
						if(msg.getStatus()==1){
							rq.setStatu(ReturnStatus.Success);
							rq.setBasemodle(msg.getMsg());
						}else {
							rq.setStatu(ReturnStatus.SystemError);
							rq.setStatusreson(msg.getMsg());
						}
					}
				}
			}else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("微信未登录");
			}
			
		}		
		return JsonUtil.objectToJsonStr(rq); 
	}
}
