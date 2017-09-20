package com.bbyiya.pic.web.notify;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.SortedMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.EErrorsMapper;
import com.bbyiya.model.EErrors;
import com.bbyiya.service.IBasePayService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.pay.WxPaySubUtils;
import com.bbyiya.utils.pay.WxUtil;
import com.bbyiya.utils.pay.config.SubWxPayConfig;
import com.sdicons.json.mapper.MapperException;

@Controller
@RequestMapping(value = "/tiwxpay")
public class WxNotifySubController {
	@Resource(name = "basePayServiceImpl")
	private IBasePayService orderMgtService;
	@Autowired
	private EErrorsMapper errorMapper;
	
	/**
	 * 订单主动查询
	 * @param request
	 * @return
	 * @throws MapperException 
	 */
	@ResponseBody
	@RequestMapping(value = "/wxPayQuery", method = { RequestMethod.POST, RequestMethod.GET })
	public String wxPayQuery(HttpServletRequest request) throws MapperException{
		String payId=request.getParameter("payId"); 
		String transaction_id=request.getParameter("transaction_id");
		SortedMap<String, String> mapResult=WxPaySubUtils.queryWxOrder(transaction_id, payId);
		if (mapResult != null) {
			if (mapResult.get("return_code").equals("SUCCESS")) {
				if(mapResult.get("result_code").equals("SUCCESS")){//
					if(mapResult.get("trade_state").equals("SUCCESS")){//交易成功
						orderMgtService.paySuccessProcess(payId);
					}
				}
			}
		}
		return JsonUtil.objectToJsonStr(mapResult);  	
	}
	
	/**
	 * 微信公众号
	 * 微信支付回写地址
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException
	 * @throws MapperException
	 */
	@RequestMapping(value = "/WxPayNotifySub", method = { RequestMethod.POST, RequestMethod.GET })
	public String wxpayNotify(HttpServletRequest request, HttpServletResponse response, Model model){
		String msg="subpay09192;";
		try {
			String xmlStr = readReqStr(request);
			SortedMap<String, String> notifymap = WxUtil.xmlToMap(xmlStr);
			if (notifymap == null || notifymap.size() < 1) {
				addlog(msg+="2,xml="+xmlStr);
				return "error";
			}
			if (WxUtil.isWXsign(notifymap, SubWxPayConfig.AppSecret)) {
				String result_code = notifymap.get("result_code");
				if ("SUCCESS".equals(result_code)) {
					return queryWxOrder(notifymap.get("transaction_id"), notifymap.get("out_trade_no"));
				} else {
					addlog(msg+="通知xml:"+xmlStr);
					return "error";
				}
			}else {
				addlog(msg+"签名有误,xml="+xmlStr);
			}
		} catch (Exception e) {
			addlog(msg+="error:"+e.getMessage());
		}
		return "error";
	}

	/**
	 * 订单签名验证，支付回写处理
	 * @param transaction_id
	 * @param payId
	 * @return
	 */
	private String queryWxOrder(String transaction_id, String payId) {
		try {
			SortedMap<String, String> map = WxPaySubUtils.queryWxOrder(transaction_id, payId);
			if (map != null && map.get("return_code").equals("SUCCESS") && map.get("result_code").equals("SUCCESS") && map.get("trade_state").equals("SUCCESS")) {
				// 会写订单状态
				orderMgtService.paySuccessProcess(payId);
				return "success";
			} else {
				addlog("subPay:" + payId + "支付失败！json:" + JsonUtil.objectToJsonStr(map));
				return "error";
			}
		} catch (Exception e) {
		}

		return "success";
	}

	
	/**
	 * 接受xml参数 
	 * @param request
	 * @return
	 */
	public static String readReqStr(HttpServletRequest request) {
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != reader) {
					reader.close();
				}
			} catch (IOException e) {

			}
		}
		return sb.toString();
	}
	
	public void addlog(String msg){
		EErrors errors=new EErrors();
		errors.setClassname(this.getClass().getName());
		errors.setMsg(msg);
		errors.setCreatetime(new Date()); 
		errorMapper.insert(errors);
	}

}
