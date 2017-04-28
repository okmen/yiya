package com.bbyiya.pic.web.notify;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.SortedMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bbyiya.dao.EErrorsMapper;
import com.bbyiya.model.EErrors;
import com.bbyiya.service.IBasePayService;
import com.bbyiya.utils.pay.WxAppPayUtils;
import com.bbyiya.utils.pay.WxPayAppConfig;
import com.bbyiya.utils.pay.WxUtil;
import com.sdicons.json.mapper.MapperException;

@Controller
@RequestMapping(value = "/wxapppay")
public class WxAppNotifyController {
	
	@Resource(name = "basePayServiceImpl")
	private IBasePayService orderMgtService;
	@Autowired
	private EErrorsMapper errorMapper;
	/**
	 * 微信支付回写地址
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException
	 * @throws MapperException
	 */
	@RequestMapping(value = "/WxPayNotify", method = { RequestMethod.POST, RequestMethod.GET })
	public String wxpayNotify(HttpServletRequest request, HttpServletResponse response, Model model){
		
		String msg="1";
		String xmlStr = readReqStr(request);
		SortedMap<String, String> notifymap = WxUtil.xmlToMap(xmlStr);
		if (notifymap == null || notifymap.size() < 1) {
			msg+="2,xml="+xmlStr;
			addlog(msg);
			return "error";
		}
		if (WxUtil.isWXsign(notifymap, WxPayAppConfig.AppSecret)) {
			String result_code = notifymap.get("result_code");
			if ("SUCCESS".equals(result_code)) {
				String quReuslt = queryWxOrder(notifymap.get("transaction_id"), notifymap.get("out_trade_no"));
				if (quReuslt.equals("success")) {
					return "paysuccess";
				}else {
					msg+="3,xml="+xmlStr;
					addlog(msg);
				}
			} else {
				msg+="4,xml="+xmlStr;
				addlog(msg);
			}
		}else {
			msg+="5签名有误,xml="+xmlStr;
			addlog(msg);
		}
		return "error";
	}

	private String queryWxOrder(String transaction_id, String payId) {
		SortedMap<String, String> map =  WxAppPayUtils.queryWxOrder(transaction_id, payId);//WxUtil.xmlToMap(xmlResult);
		if (WxUtil.isWXsign(map, WxPayAppConfig.AppSecret)) {
			if (map != null && map.get("return_code").equals("SUCCESS") && map.get("result_code").equals("SUCCESS") && map.get("trade_state").equals("SUCCESS")) {
				// 会写订单状态 TODO 回写订单状态
				boolean paySuccess = orderMgtService.paySuccessProcess(payId);
				if (paySuccess) {
					return "success";
				} else {
					return "error";
				}
			}
		}
		return "success";
	}
	

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
		errors.setClassname("apppayorderBack");
		errors.setMsg(msg);
		errorMapper.insert(errors);
	}
}
