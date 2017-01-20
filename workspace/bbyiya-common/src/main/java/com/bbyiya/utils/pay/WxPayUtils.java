package com.bbyiya.utils.pay;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import com.bbyiya.utils.encrypt.MD5Encrypt;

/**
 * 微信 公众号支付
 * 
 * @author Administrator
 *
 */
public class WxPayUtils {

	/**
	 * 时间串
	 * 
	 * @return
	 */
	public static long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

	/**
	 * 随机字符串
	 * 
	 * @return
	 */
	public static String genNonceStr() {
		Random random = new Random();
		return MD5Encrypt.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes()).toUpperCase();
	}

	/**
	 * 获取支付参数（带签名）
	 * 
	 * @param orderNo
	 * @param prepay_id
	 * @param totalPrice
	 * @param ip
	 * @return
	 */
//	public static ResultMsg getWxPayParam(String orderNo, String openid, String prepay_id, double totalPrice, String ip) {
//		ResultMsg msgResult = new ResultMsg();
//		if (ObjectUtil.isEmpty(orderNo) || ObjectUtil.isEmpty(ip)) {
//			msgResult.setStatus(-2);
//			msgResult.setMsg("参数有误！");
//			return msgResult;
//		}
//		try {
//			String key_prepayId="prepay_id_"+orderNo;
//			prepay_id=RedisUtil.getString(key_prepayId);
//			// 随机字符串
//			String nonceStr = genNonceStr();
//			if (ObjectUtil.isEmpty(prepay_id)) {
//				Map<String, Object> map = doInBackground(ip, openid, totalPrice, orderNo, nonceStr);
//				if (map != null) {
//					if (map.get("return_code").equals("SUCCESS")) {
//						Object prepayID = map.get("prepay_id");
//						if (prepayID != null) {
//							prepay_id = map.get("prepay_id").toString();
//							RedisUtil.setString(key_prepayId, prepay_id); 
//						} else {
//							msgResult.setStatus(-1);
//							msgResult.setMsg(map.get("err_code_des").toString());
//							return msgResult;
//						}
//					} else {
//						msgResult.setStatus(-1);
//						msgResult.setMsg(JsonUtil.objectToJsonStr(map));
//						return msgResult; 
//					}
//				} else {
//					msgResult.setStatus(-1);
//					msgResult.setMsg("系统错误");
//					return msgResult;
//				}
//			}
//			String timeStamp = String.valueOf(genTimeStamp());
//
//			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
//			packageParams.add(new BasicNameValuePair("appId", WxPayConfig.APPID));
//			packageParams.add(new BasicNameValuePair("package",  "prepay_id="+prepay_id));
//			packageParams.add(new BasicNameValuePair("nonceStr", nonceStr));
//			packageParams.add(new BasicNameValuePair("timeStamp", timeStamp));
//			packageParams.add(new BasicNameValuePair("signType", "MD5"));
//			String sign = genPackageSign(packageParams);
//
//			Map<String, String> map_param = new HashMap<String, String>();
//			map_param.put("appId", WxPayConfig.APPID);
//			map_param.put("package", "prepay_id="+prepay_id);
//			map_param.put("nonceStr", nonceStr);
//			map_param.put("timeStamp", timeStamp); 
//			map_param.put("signType", "MD5");
//			map_param.put("paySign", sign);
//
//			msgResult.setStatus(1);
//			msgResult.setMsg(JsonUtil.objectToJsonStr(map_param));
//
//		} catch (Exception e) {
//			// TODO: handle exception
//			msgResult.setStatus(-2);
//			msgResult.setMsg(e.getMessage());
//		}
//		return msgResult;
//	}
	
	public static Map<String, String> get_payParam(String prepay_id,String nonceStr,String timeStamp){
		List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
		packageParams.add(new BasicNameValuePair("appId", WxPayConfig.APPID));
		packageParams.add(new BasicNameValuePair("package",  "prepay_id="+prepay_id));
		packageParams.add(new BasicNameValuePair("nonceStr", nonceStr));
		packageParams.add(new BasicNameValuePair("timeStamp", timeStamp));
		packageParams.add(new BasicNameValuePair("signType", "MD5"));
		String sign = genPackageSign(packageParams);

		Map<String, String> map_param = new HashMap<String, String>();
		map_param.put("appId", WxPayConfig.APPID);
		map_param.put("package", "prepay_id="+prepay_id);
		map_param.put("nonceStr", nonceStr);
		map_param.put("timeStamp", timeStamp); 
		map_param.put("signType", "MD5");
		map_param.put("paySign", sign); 
		return map_param;
	}
	

	/**
	 * 统一下单 获取预付单
	 * @param ipStr
	 * @param openId
	 * @param totalPrice
	 * @param orderNo
	 * @param nonceStr
	 * @return
	 */
	public static Map<String, Object> doInBackground(String ipStr, String openId, double totalPrice, String orderNo, String nonceStr) {
		String urlString = WxPayConfig.WX_URL;
		String entityString = genProductArgs(ipStr,openId, totalPrice, orderNo, nonceStr);
		String msgString = WxUtil.httpsRequest(urlString, entityString);
		Map<String, Object> map = WxUtil.xml2Map(msgString);
//		System.out.println(map.toString()); 
		return map;
	}

	private static String genProductArgs(String ipStr, String openId, double totalprice, String orderNo, String nonceStr) {
		try {
			String pattern = "#0";
			DecimalFormat formatter = new DecimalFormat();
			formatter.applyPattern(pattern);
			String totalFee = formatter.format(totalprice * 100);
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams.add(new BasicNameValuePair("appid", WxPayConfig.APPID));
			packageParams.add(new BasicNameValuePair("mch_id", WxPayConfig.PARNER));// 商户号
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
//			System.out.println(xmlstring); 
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
		sb.append(WxPayConfig.AppSecret);
		String packageSign = MD5Encrypt.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		return packageSign;
	}

}
