package com.bbyiya.utils.pay;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.encrypt.MD5Encrypt;
import com.bbyiya.utils.pay.config.SubWxPayConfig;

public class WxPaySubUtils {

	
	
	public static Map<String, Object> doInBackground(String ipStr, String openId, double totalPrice, String orderNo, String nonceStr) {
		String urlString = WxPayConfig.WX_URL;
		String entityString = genProductArgs(ipStr,openId, totalPrice, orderNo, nonceStr);
		String msgString = WxUtil.httpsRequest(urlString, entityString);
		Map<String, Object> map = WxUtil.xml2Map(msgString);
		return map;
	}
	
	
	private static String genProductArgs(String ipStr, String openId, double totalprice, String orderNo, String nonceStr) {
		try {
			String pattern = "#0";
			DecimalFormat formatter = new DecimalFormat();
			formatter.applyPattern(pattern);
			String totalFee = formatter.format(totalprice * 100);
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams.add(new BasicNameValuePair("appid", SubWxPayConfig.APPID));
			packageParams.add(new BasicNameValuePair("mch_id", SubWxPayConfig.MCH_ID));// 商户号
			packageParams.add(new BasicNameValuePair("sub_mch_id", SubWxPayConfig.SUB_MCH_ID));// 商户号
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			packageParams.add(new BasicNameValuePair("body", "台历/挂历/年历"));// "订单:"+orderNo
			packageParams.add(new BasicNameValuePair("out_trade_no", orderNo));
			packageParams.add(new BasicNameValuePair("total_fee", totalFee));
			packageParams.add(new BasicNameValuePair("spbill_create_ip", ipStr));
			packageParams.add(new BasicNameValuePair("notify_url", SubWxPayConfig.NOTIFY_URL));
			packageParams.add(new BasicNameValuePair("trade_type", "JSAPI"));
			packageParams.add(new BasicNameValuePair("openid", openId));
			//统一支付 签名
			String sign = genPackageSign(packageParams);
			
			packageParams.add(new BasicNameValuePair("sign", sign));
			String xmlstring = WxUtil.toXml(packageParams);
			
			return xmlstring;

		} catch (Exception e) {

			return null;
		}
	}
	
	public static Map<String, String> get_payParam(String prepay_id,String nonceStr,String timeStamp){
		List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
		packageParams.add(new BasicNameValuePair("appId", SubWxPayConfig.APPID));
		packageParams.add(new BasicNameValuePair("package",  "prepay_id="+prepay_id));
		packageParams.add(new BasicNameValuePair("nonceStr", nonceStr));
		packageParams.add(new BasicNameValuePair("timeStamp", timeStamp));
		packageParams.add(new BasicNameValuePair("signType", "MD5"));
		String sign = genPackageSign(packageParams);

		Map<String, String> map_param = new HashMap<String, String>();
		map_param.put("appId", SubWxPayConfig.APPID);
		map_param.put("package", "prepay_id="+prepay_id);
		map_param.put("nonceStr", nonceStr);
		map_param.put("timeStamp", timeStamp); 
		map_param.put("signType", "MD5");
		map_param.put("paySign", sign); 
		return map_param;
	}
	
	public static SortedMap<String, String> queryWxOrder(String transaction_id, String payId) {
		List<NameValuePair> paramlist = new ArrayList<NameValuePair>();
		paramlist.add(new BasicNameValuePair("appid", SubWxPayConfig.APPID));
		paramlist.add(new BasicNameValuePair("mch_id", SubWxPayConfig.MCH_ID));
		paramlist.add(new BasicNameValuePair("sub_mch_id", SubWxPayConfig.SUB_MCH_ID));
		paramlist.add(new BasicNameValuePair("nonce_str", WxPayUtils.genNonceStr()));
		if(!ObjectUtil.isEmpty(payId)){
			paramlist.add(new BasicNameValuePair("out_trade_no", payId));
		}
		if(!ObjectUtil.isEmpty(transaction_id)){
			paramlist.add(new BasicNameValuePair("transaction_id", transaction_id));
		}
		/*-------------------------------------------------------------*/
		String sign = genPackageSign(paramlist);
		paramlist.add(new BasicNameValuePair("sign", sign));
		String xmlstring = WxUtil.toXml(paramlist);
		String xmlResult = WxUtil.httpsRequest("https://api.mch.weixin.qq.com/pay/orderquery", xmlstring);
		SortedMap<String, String> map = WxUtil.xmlToMap(xmlResult);
		return map;
	}
	
	/**
	 * 生成签名（微信公众号）
	 */
	public static String genPackageSign(List<NameValuePair> params) {
		Collections.sort(params, new Comparator<NameValuePair>() {
			// 重写排序规则
			public int compare(NameValuePair list1, NameValuePair list2) {
				return list1.getName().compareTo(list2.getName());
			}
		});
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(SubWxPayConfig.AppSecret);
		String packageSign = MD5Encrypt.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		return packageSign;
	}
}
