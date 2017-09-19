package com.bbyiya.utils.pay;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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
			packageParams.add(new BasicNameValuePair("body", "yiya-12pic"));// "订单:"+orderNo
			packageParams.add(new BasicNameValuePair("out_trade_no", orderNo));
			packageParams.add(new BasicNameValuePair("total_fee", totalFee));
			packageParams.add(new BasicNameValuePair("spbill_create_ip", ipStr));
			packageParams.add(new BasicNameValuePair("notify_url", WxPayConfig.NOTIFY_URL));
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
