package com.bbyiya.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.bbyiya.common.enums.SendMsgEnums;
import com.bbyiya.utils.encrypt.MD5Encrypt;

public class SendSMSByMobile {
//	static String UID = ConfigUtil.getSingleValue("smsuid");
//	static String PWD = ConfigUtil.getSingleValue("smspwd");
//	static String URL = "http://c.kf10000.com/sdk/SMS";
	/**
	 * 云片网 单条短信发送
	 */
	private  static String SINGER_URL="https://sms.yunpian.com/v2/sms/single_send.json";
	/**
	 * 新增模板
	 */
	private static String ADD_URL="https://sms.yunpian.com/v2/tpl/add.json";
	/**
	 * key
	 */
	private static String APIKEY="593936c15fcc5c7824407d9953eb2bef";

	
	/*
	 * 短信改造 cmd 命令字 send 发送短信 uid 帐号 非空 psw 密码 非空 密码需要MD5 32位加密,不区分大小写； subid
	 * 扩展号（6位以内） mobiles 手机号码 多个号码用逗号隔开最多500个号码,建议100个提交一次; msgid 消息编号 整型
	 * 客户端生成，唯一性；每提交一次msgid要不同； msg 消息内容 GBK 编码格式，需要urlencoder；
	 */
	/*
	private static String sendSMS(String mobile, String content) {
		String returnMsg = "";
		Map<String, String> map = new HashMap<String, String>();
		map.put("cmd", "send");
		map.put("uid", UID);
		try {
			map.put("psw", MD5Encrypt.encrypt(PWD));
			map.put("mobiles", mobile);
			map.put("msgid", String.valueOf(Math.random()).substring(2, 6));
			map.put("msg",URLEncoder.encode(content.replace("[", "【").replace("]", "】"), "GBK"));

			returnMsg = HttpRequestHelper.doGetMethod(URL, map);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if ("100".equals(returnMsg))
			return "1";
		else
			return returnMsg;
	}*/
	
	/**
	 * 单条短信发送
	 * @param mobile
	 * @param content
	 * @return
	 */
	public static String sendSMS_yunpian(String mobile, String content) {
		String returnMsg = "";
		Map<String, String> map = new HashMap<String, String>();
		map.put("apikey", APIKEY);
		try {
			map.put("mobile", mobile);
			map.put("text",content);
			returnMsg = HttpRequestHelper.post_httpClient(SINGER_URL, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnMsg;
	}
	/**
	 * 新增云片短信模板
	 * @return
	 */
	public static String addTemp_yunpian() {
		String returnMsg = "";
		Map<String, String> map = new HashMap<String, String>();
		map.put("apikey", APIKEY);
		try {
			String content="【咿呀科技】您的验证码是#code#";
			map.put("tpl_content",content);
			returnMsg = HttpRequestHelper.post_httpClient(ADD_URL, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnMsg;
	}

	/**
	 * 发送短信
	 * 
	 * @param type
	 * @param moblie
	 * @return
	 */
	public static String sendMsg(SendMsgEnums type, String moblie) {
		String result="";
		if (type.equals(SendMsgEnums.register)) {
			String verifyCode = String.valueOf(Math.random()).substring(2, 6);
			result=sendSMS_yunpian(moblie, "【咿呀科技】您的验证码是"+verifyCode);
		}
		return result;
	}
}
