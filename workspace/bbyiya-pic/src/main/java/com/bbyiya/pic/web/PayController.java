package com.bbyiya.pic.web;

//import javax.servlet.http.HttpServletRequest;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.UOtherloginMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.UOtherlogin;
import com.bbyiya.pic.service.IPic_PayMgtService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;
import com.sdicons.json.mapper.MapperException;

@Controller
@RequestMapping(value = "/pay")
public class PayController extends SSOController {

	@Resource(name = "pic_payMgtService")
	private IPic_PayMgtService payService;
	
	// @Autowired
	// private OPayorderMapper payMapper;
	@Autowired
	private UOtherloginMapper otherMapper;
	
	/**
	 * P01 获取微信支付参数
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
				String ipAddres=super.getIpStr();
				rq=payService.getWxPayParam(payId, otherlogin.getOpenid(), ipAddres);
//				OPayorder payorder= payMapper.selectByPrimaryKey(payId);
//				if(payorder!=null){
//					if(payorder.getStatus().intValue()==Integer.parseInt(OrderStatusEnum.noPay.toString())){
//						ResultMsg msg= WxPayUtils.getWxPayParam(payId, otherlogin.getOpenid(), "", payorder.getTotalprice(), ipAddres);
//						if(msg.getStatus()==1){
//							rq.setStatu(ReturnStatus.Success);
//							rq.setBasemodle(msg.getMsg());
//						}else {
//							rq.setStatu(ReturnStatus.SystemError);
//							rq.setStatusreson(msg.getMsg());
//						}
//					}
//				}
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
				String ipAddres=super.getIpStr();
				rq=payService.getWxAppPayParam(payId, ipAddres);
//				OPayorder payorder= payMapper.selectByPrimaryKey(payId);
//				if(payorder!=null){
//					if(payorder.getStatus().intValue()==Integer.parseInt(OrderStatusEnum.noPay.toString())){
//						ResultMsg msg= WxAppPayUtils.getWxPayParam(payId, "", payorder.getTotalprice(), ipAddres);
//						if(msg.getStatus()==1){
//							rq.setStatu(ReturnStatus.Success);
//							rq.setBasemodle(msg.getMsg());
//						}else {
//							rq.setStatu(ReturnStatus.SystemError);
//							rq.setStatusreson(msg.getMsg());
//						}
//					}
//				}
			}else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("微信未登录");
			}
			
		}		
		return JsonUtil.objectToJsonStr(rq); 
	}
}
