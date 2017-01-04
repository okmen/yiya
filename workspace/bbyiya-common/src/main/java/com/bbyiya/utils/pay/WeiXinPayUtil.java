package com.bbyiya.utils.pay;

import java.io.UnsupportedEncodingException;
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

import com.bbyiya.common.vo.ResultMsg;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.encrypt.MD5Encrypt;
public class WeiXinPayUtil {
	
	private static long genTimeStamp() 
	{
		return System.currentTimeMillis() / 1000;
	}
	private  static String genNonceStr() {
		Random random = new Random();
		return MD5Encrypt.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes()).toUpperCase();
	}
	/**
	 * 获取微信支付参数
	 * @param orderNo
	 * @param totalPrice
	 * @param ip
	 * @return
	 */
	public static ResultMsg getWeixinPayParam(String orderNo,String prepay_id,double totalPrice,String ip,int typeState)
	{
		ResultMsg msgResult=new ResultMsg();
		try {
			String	nonceStr = genNonceStr();
			if("".equals(prepay_id))
			{
				Map<String, Object> map= doInBackground(ip,totalPrice,orderNo,nonceStr, typeState);
				if(map!=null)
				{
					if(map.get("return_code").equals("SUCCESS"))
					{
							Object prepayID=map.get("prepay_id");
							if(prepayID!=null)
							{
								prepay_id=map.get("prepay_id").toString();
							}
							else //
							{
								msgResult.setStatus(-1);
								msgResult.setMsg( map.get("err_code_des").toString());
								return msgResult;
							}
							
					}else {
							msgResult.setStatus(-1);
							msgResult.setMsg( map.get("return_msg").toString());
							return msgResult;
					}
					
				} else {
					msgResult.setStatus(-1);
					msgResult.setMsg("系统错误");
					return msgResult;
				}
			}
			String timeStamp = String.valueOf(genTimeStamp()) ;
			
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams.add(new BasicNameValuePair("appid",WeiXinPayAppConfig.APPID));
			packageParams.add(new BasicNameValuePair("partnerid",WeiXinPayAppConfig.PARNER));// 商户号
			packageParams.add(new BasicNameValuePair("prepayid", prepay_id));
			packageParams.add(new BasicNameValuePair("package", "Sign=WXPay"));
			packageParams.add(new BasicNameValuePair("noncestr", nonceStr));
			packageParams.add(new BasicNameValuePair("timestamp", timeStamp));
			String sign = genPackageSign(packageParams);
			
			
			Map<String, String> map_param=new HashMap<String, String>();
			map_param.put("appid", WeiXinPayAppConfig.APPID);
			map_param.put("partnerId", WeiXinPayAppConfig.PARNER);
			map_param.put("prepayId", prepay_id);
			map_param.put("package", "Sign=WXPay");
			map_param.put("nonceStr", nonceStr);
			map_param.put("timeStamp", timeStamp);
			map_param.put("sign", sign);
			
			msgResult.setStatus(1);
			msgResult.setMsg(JsonUtil.objectToJsonStr(map_param));
			 
		} catch (Exception e) {
			// TODO: handle exception
			msgResult.setStatus(-2);
			msgResult.setMsg(e.getMessage());
		}
		return msgResult;
	}
	 
	private static Map<String, Object> doInBackground(String ipStr,double totalPrice,String orderNo,String nonceStr ,int typeState)
	{
		String urlString=WeiXinPayAppConfig.WX_URL;
		String entityString=genProductArgs(ipStr, totalPrice,orderNo,nonceStr, typeState);
		String msgString= WeiXinUtil.httpPost(urlString, entityString);
		Map<String, Object> map=WeiXinUtil.xml2Map(msgString);
		return map;
	}
	
	private static String genProductArgs(String ipStr,double totalprice,String orderNo,String nonceStr ,int typeState)
	{
		try {
			String pattern = "#0";
			DecimalFormat formatter = new DecimalFormat();  
			formatter.applyPattern(pattern);
			String totalFee = formatter.format(totalprice * 100);
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams.add(new BasicNameValuePair("appid", WeiXinPayAppConfig.APPID));
			packageParams.add(new BasicNameValuePair("mch_id", WeiXinPayAppConfig.PARNER));//商户号
//			packageParams.add(new BasicNameValuePair("device_info", "10000100"));//设备号
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			Map<String, String>map=WeiXinUtil.getOrderTitle(typeState, orderNo);
			String bodyString="",productString="";
			if(map!=null)
			{
				bodyString=map.get("order");
				productString=map.get("product");
			}
			packageParams.add(new BasicNameValuePair("body", bodyString));//"订单:"+orderNo
//			packageParams.add(new BasicNameValuePair("input_charset", "UTF-8")); 
//			packageParams.add(new BasicNameValuePair("detail", "test"+orderNo));
			packageParams.add(new BasicNameValuePair("attach", bodyString));
			packageParams.add(new BasicNameValuePair("out_trade_no",orderNo)); 
//			packageParams.add(new BasicNameValuePair("fee_type","CNY")); 
			packageParams.add(new BasicNameValuePair("total_fee", totalFee));//totalFee
			packageParams.add(new BasicNameValuePair("spbill_create_ip",ipStr));
//			packageParams.add(new BasicNameValuePair("time_start",""));
//			packageParams.add(new BasicNameValuePair("time_expire",""));

			packageParams.add(new BasicNameValuePair("product_id", productString ));
//			packageParams.add(new BasicNameValuePair("openid", "ot59ht9CDGYp4LhO9FOKt7b5ecIM"));//"oaWp-t2N8jWGR_e4X78UPg-L8vrw"			
			
//			packageParams.add(new BasicNameValuePair("goods_tag","订单："+orderNo));
			packageParams.add(new BasicNameValuePair("notify_url", WeiXinPayAppConfig.NOTIFY_URL));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));
			
			String sign = genPackageSign(packageParams);
			
			packageParams.add(new BasicNameValuePair("sign", sign));
		    String xmlstring =toXml(packageParams);
		   
			return xmlstring;

		} catch (Exception e) {
			
			return null;
		}
		

	}
	/**
	 生成签名
	 */
	private static String genPackageSign(List<NameValuePair> params) {
		
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
		sb.append(WeiXinPayAppConfig.APP_SIGN_KEY);
		String packageSign =MD5Encrypt.getMessageDigest(sb.toString().getBytes()).toUpperCase();//.getMessageDigest(sb.toString().getBytes()).toUpperCase();  
		return packageSign;
	}
	
	/**
	 * 生成xml 串
	 * @param params
	 * @return
	 */
	private static String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<"+params.get(i).getName()+">");
			sb.append(params.get(i).getValue());
			sb.append("</"+params.get(i).getName()+">");
		}
		sb.append("</xml>");
//		return sb.toString();
		try {
			return new String(sb.toString().getBytes(),"ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
