package com.bbyiya.utils;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.bbyiya.common.enums.MsgStatusEnums;
import com.bbyiya.common.enums.SendMsgEnums;
import com.bbyiya.common.vo.ResultMsg;
/**
 * 短信发送类
 * @author Administrator
 *
 */
public class SendSMSByMobile {
//	static String UID = ConfigUtil.getSingleValue("smsuid");
//	static String PWD = ConfigUtil.getSingleValue("smspwd");
//	static String URL = "http://c.kf10000.com/sdk/SMS";
	
	/**
	 * 参考api文档 https://www.yunpian.com/api2.0/sms.html
	 * 
	 * 云片网 单条短信发送
	 */
	private  static String SINGER_URL=ConfigUtil.getSingleValue("yp_single_url");
	/**
	 * 新增模板
	 */
	private static String ADD_URL=ConfigUtil.getSingleValue("yp_add_url");
	/**
	 * key
	 */
	private static String APIKEY=ConfigUtil.getSingleValue("yp_apikey");

	
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
	public static String sendSmsReturnJson(int type, String moblie) {
		if (type>=0) {
			String verifyCode = String.valueOf(Math.random()).substring(2, 6);
			String key=moblie+"-"+type;
			RedisUtil.setObject(key, verifyCode,300); 
			return sendSMS_yunpian(moblie, "【咿呀科技】您的验证码是"+verifyCode);
			
//			return sendSMS_yunpian(moblie, "【咿呀科技】"+verifyCode+"（验证码），"+5+"分钟内有效。如非本人操作，请忽略本短信。"+verifyCode);
		}
		return null;
	}
	
	/**
	 * 短信发送
	 * @param type SendMsgEnums
	 * @param moblie
	 * @return JSONObject （{code:0 发送成功，msg:失败的原因}）
	 */
	public static JSONObject sendSmsReturnObject(SendMsgEnums type, String moblie) {
		if (type.equals(SendMsgEnums.register)) {
			String verifyCode = String.valueOf(Math.random()).substring(2, 6);
			String key=moblie+"-"+Integer.parseInt(type.toString());
			RedisUtil.setObject(key, verifyCode,300); 
			String resultString= sendSMS_yunpian(moblie, "【咿呀科技】您的验证码是"+verifyCode);
			return JSONObject.fromObject(resultString);
		}
		return null;
	}
	
	/**
	 * 验证码验证
	 * @param mobile
	 * @param vcode
	 * @param type
	 * @return
	 */
	public static ResultMsg validateCode(String mobile,String vcode,SendMsgEnums type){
		ResultMsg result=new ResultMsg();
		String key = mobile + "-" + Integer.parseInt(type.toString());
		String vcode_old =String.valueOf( RedisUtil.getObject(key));
		if (ObjectUtil.isEmpty(vcode_old)) {
			result.setStatus(Integer.parseInt(MsgStatusEnums.invalid.toString()));
			result.setMsg("验证码已失效");
			return result;
		}
		if (!vcode.equals(vcode_old)) {
			//验证码 5次错误将过期
			String keyCount="keyConnt_"+key;
			int count=ObjectUtil.parseInt(String.valueOf(RedisUtil.getObject(keyCount)));
			count++;
			if(count>=5){
				RedisUtil.delete(key);
				result.setStatus(Integer.parseInt(MsgStatusEnums.invalid.toString()));
				result.setMsg("验证码已失效");
				return result;
			}
			RedisUtil.setObject(keyCount, count, 3600); 
			result.setStatus(Integer.parseInt(MsgStatusEnums.wrong.toString()));
			result.setMsg("验证码有误");
			return result;
		}
		result.setStatus(Integer.parseInt(MsgStatusEnums.ok.toString()));
		return result;
	}
}
