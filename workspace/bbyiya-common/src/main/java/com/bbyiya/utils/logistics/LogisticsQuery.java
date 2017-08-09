package com.bbyiya.utils.logistics;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.bbyiya.utils.encrypt.MD5Encrypt;
public class LogisticsQuery {
	
	/**
	 * 实时快递查询接口的公司编号(customer )
	 */
	public static String customer = "EBB2665063CB8D9E2E69C5C580531EBC"; //
	/**
	 * 实时快递查询接口的授权密匙(Key)
	 */
	public static String key = "PDaDCgoA1580"; //
	
	public static String getLogisticsQueryByNum(String com,String num){
		String param ="{\"com\":\""+com+"\",\"num\":\""+num+"\"}";
		String sign = MD5Encrypt.encrypt(param+key+customer);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("param",param);
		params.put("sign",sign);
		params.put("customer",customer);
		String resp="";
		try {
			resp = new HttpRequest().postData("http://poll.kuaidi100.com/poll/query.do", params, "utf-8").toString();
			//System.out.println(resp);			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
		
	}
	
	public static void main(String[] args) throws Exception {

		String param ="{\"com\":\"shunfeng\",\"num\":\"602569721121\"}";
		String customer ="EBB2665063CB8D9E2E69C5C580531EBC";
		String key = "PDaDCgoA1580";
		String sign = MD5Encrypt.encrypt(param+key+customer);//MD5.encode(param+key+customer);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("param",param);
		params.put("sign",sign);
		params.put("customer",customer);
		String resp;
		try {
			resp = new HttpRequest().postData("http://poll.kuaidi100.com/poll/query.do", params, "utf-8").toString();
			System.out.println(resp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
