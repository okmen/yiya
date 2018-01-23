package com.bbyiya.pic.web.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.enums.OrderStatusEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.OUserorders;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.HttpRequestHelper;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.pay.WxUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/delivery")
public class IOrderSendController extends SSOController{
	
//	private static String PARNER="2018011201";
	
	private static String KEY="gdD0hzX0lAA4V1pxFCBrTTLQBHCd60zA";
	@Autowired
	private OUserordersMapper userorderMapper;
	
	@ResponseBody
	@RequestMapping(value = "/genSign")
	public String genSign(String orderId,String expressCom,String expressOrderNum,String timestamp) throws Exception {
//		ReturnModel rq = new ReturnModel();
		if(ObjectUtil.isEmpty(orderId)||ObjectUtil.isEmpty(timestamp)||ObjectUtil.isEmpty(expressCom)||ObjectUtil.isEmpty(expressOrderNum)){
			return "Parameter incomplete!";
		}
		List<NameValuePair> params =new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("orderId", orderId));
		params.add(new BasicNameValuePair("timestamp", timestamp));
		params.add(new BasicNameValuePair("expressCom", expressCom));
		params.add(new BasicNameValuePair("expressOrderNum", expressOrderNum));
		return WxUtil.genPackageSign(params, KEY);
//		return "";
	}
	
	@ResponseBody
	@RequestMapping(value = "/testSign")
	public String genSign() throws Exception {
	    StringBuilder sb=new StringBuilder();
	    sb.append("<xml>");
	    sb.append("<orderId>2018011512272146468</orderId>");
	    sb.append("<expressCom>tiantian</expressCom>");
	    sb.append("<expressOrderNum>111222</expressOrderNum>");
	    sb.append("<timestamp>32321323</timestamp>");
	    sb.append("<sign>1946B385D6FBA6BEBE25EBF2FD0775F8</sign></xml>");
	    
		String result= HttpRequestHelper.postXml("https://mpic.bbyiy.net/delivery/send", sb.toString());
//	    String result= HttpRequestHelper.postXml("http://localhost:8080/delivery/send", sb.toString());
//		System.out.println(result); 
//		Map<String, Object> mapRrsult=WxUtil.xml2Map(result);
		return result;
	}
	
	/**
	 * 第三方 发货接口
	 * @param expressCode 
	 * @param expressCom
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/send")
	public String send() throws Exception {
//		StringBuilder sb=new StringBuilder();
//		sb.append("<xml>");
		ReturnModel rq = new ReturnModel();
		String xmlParam= super.readReqStr(request);
		SortedMap<String, String> notifymap = WxUtil.xmlToMap(xmlParam);
		if (WxUtil.isWXsign(notifymap, KEY)) {
			String trancm=notifymap.get("expressCom");
			String orderid=notifymap.get("orderId");
			String expressOrderNum=notifymap.get("expressOrderNum");
			if(ObjectUtil.isEmpty(trancm)||ObjectUtil.isEmpty(expressOrderNum)||ObjectUtil.isEmpty(orderid)){
				rq.setStatu(ReturnStatus.ParamError_1);
				rq.setStatusreson("Parameter incomplete!");
				return JsonUtil.objectToJsonStr(rq);
			}
			OUserorders order= userorderMapper.selectByPrimaryKey(orderid);
			if(order!=null){
				if(ObjectUtil.isEmpty(order.getExpresscom())||ObjectUtil.isEmpty(order.getExpressorder())){
					order.setExpresscode(trancm);
					order.setExpressorder(expressOrderNum); 
					order.setExpresscom(getExpressName(trancm)); 
					order.setDeliverytime(new Date()); 
					order.setStatus(Integer.parseInt(OrderStatusEnum.send.toString()));
					userorderMapper.updateByPrimaryKeySelective(order);
					rq.setStatu(ReturnStatus.Success);
					rq.setStatusreson("ok"); 
//					sb.append("<result>1</result>");
//					sb.append("<resultMsg>success</resultMsg>");
				}else{
					rq.setStatu(ReturnStatus.ParamError_1);
					rq.setStatusreson("The order has been shipped!");
//					sb.append("<result>201</result>");
//					sb.append("<resultMsg>The order has been shipped!</resultMsg>");
				}
			}else{
				rq.setStatu(ReturnStatus.ParamError_1);
				rq.setStatusreson("orderId does not exist!");
				rq.setBasemodle(xmlParam); 
//				sb.append("<result>201</result>");
//				sb.append("<resultMsg>orderId does not exist!</resultMsg>");
			}
		}else{
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("sign error!");	
//			sb.append("<result>200</result>");
//			sb.append("<resultMsg>sign error!</resultMsg>");
		}
//		sb.append("</xml>");
//		System.out.println(sb.toString()); 
		return JsonUtil.objectToJsonStr(rq); // sb.toString();//
	}
	
	/**
	 * 查询订单是否发货
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/checkOrder")
	public String checkOrder() throws Exception {
//		StringBuilder sb=new StringBuilder();
//		sb.append("<xml>");
		ReturnModel rq = new ReturnModel();
		String xmlParam= super.readReqStr(request);
		SortedMap<String, String> notifymap = WxUtil.xmlToMap(xmlParam);
		if (WxUtil.isWXsign(notifymap, KEY)) {
			String orderid=notifymap.get("orderId");
			OUserorders order= userorderMapper.selectByPrimaryKey(orderid);
			if(order!=null){
				if(ObjectUtil.isEmpty(order.getExpresscom())||ObjectUtil.isEmpty(order.getExpresscode())){
					rq.setStatu(ReturnStatus.ParamError_2);
					rq.setStatusreson("Unshipped orders!");
//					sb.append("<result>202</result>");
//					sb.append("<resultMsg>Unshipped orders!</resultMsg>");
				}else{
					Map<String, String> map=new HashMap<String, String>();
					map.put("expressCom", order.getExpresscode());
					map.put("expressOrderNum", order.getExpressorder());
					map.put("expressName", order.getExpresscom());
					rq.setStatu(ReturnStatus.Success);
					rq.setStatusreson("ok"); 
					rq.setBasemodle(map);
//					sb.append("<result>1</result>");
//					sb.append("<resultMsg>success</resultMsg>");
				}
			}else{
				rq.setStatu(ReturnStatus.ParamError_1);
				rq.setStatusreson("orderId does not exist!");
//				sb.append("<result>201</result>");
//				sb.append("<resultMsg>orderId does not exist!</resultMsg>");
			}
		}else{
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("sign error!");
//			sb.append("<result>200</result>");
//			sb.append("<resultMsg>sign error!</resultMsg>");
		}
//		sb.append("</xml>");
		return JsonUtil.objectToJsonStr(rq); //sb.toString();//
	}
	
	
	public String getExpressName(String code){
		List<Map<String, String>> maplist=ConfigUtil.getMaplist("postInfo");
		if(maplist!=null&&maplist.size()>0){
			for (Map<String, String> map : maplist) {
				if(map.get("code").equals(code)){
					return map.get("name");
				}
			}
		}
		return "中通快递";
	}
}
