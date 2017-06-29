package com.bbyiya.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

import com.bbyiya.common.enums.MsgStatusEnums;
import com.bbyiya.common.enums.SendMsgEnums;
import com.bbyiya.common.vo.ResultMsg;
import com.bbyiya.common.vo.SmsParam;
/**
 * 短信发送类
 * @author Administrator
 *
 */
public class SendSMSByMobile {
	
	/**
	 * 参考api文档 https://www.yunpian.com/api2.0/sms.html
	 * 
	 * 云片网 单条短信发送
	 */
	private  static String SINGER_URL=ConfigUtil.getSingleValue("yp_single_url");
	/**
	 * 新增模板
	 */
//	private static String ADD_URL=ConfigUtil.getSingleValue("yp_add_url");
	/**
	 * key
	 */
	private static String APIKEY=ConfigUtil.getSingleValue("yp_apikey");

	public static Logger loger = Logger.getLogger(SendSMSByMobile.class);
	
	
	/**
	 * 单条短信发送（验证短信）
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
			loger.error(e);
		}
		return returnMsg;
	}
	
	/**
	 * 通知类短信发送（多个相同类容）
	 * @param mobiles 多个手机号以,隔开，一次不要超过1000条且短信内容条数必须与手机号个数相等
	 * @param content 短信内容，多个短信内容请使用UTF-8做urlencode；使用逗号分隔，一次不要超过1000条且短信内容条数必须与手机号个数相等
	 * @return
	 */
	public static String batchSend(String mobiles, String contents) {
		String returnMsg = "";
		Map<String, String> map = new HashMap<String, String>();
		map.put("apikey", APIKEY);
		try {
			map.put("mobile", mobiles);
			map.put("text",contents);
			returnMsg = HttpRequestHelper.post_httpClient("https://sms.yunpian.com/v2/sms/batch_send.json", map);
//			System.out.println(returnMsg); 
		} catch (Exception e) {
			loger.error(e); 
		}
		return returnMsg;
	}
	
	
	
	
//	/**
//	 * 新增云片短信模板
//	 * @return
//	 */
//	public static String addTemp_yunpian() {
//		String returnMsg = "";
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("apikey", APIKEY);
//		try {
//			String content="【咿呀科技】您的验证码是#code#";
//			map.put("tpl_content",content);
//			returnMsg = HttpRequestHelper.post_httpClient(ADD_URL, map);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return returnMsg;
//	}

	/**
	 * 发送短信
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
		}
		return null;
	}
	
	/**
	 * 短信发送
	 * @param msgType
	 * @param mobile
	 * @param param
	 * @return
	 */
	public static boolean sendSmS(int msgType,String mobile,SmsParam param){
		String msg="";
		//注册验证码
		if(msgType==Integer.parseInt(SendMsgEnums.register.toString())){
			//验证码
			String verifyCode = String.valueOf(Math.random()).substring(2, 6);
			msg="【咿呀科技】您的验证码是"+verifyCode;
			//注册验证码
			String key=mobile+"-"+msgType;
			//验证码5分钟有效
			RedisUtil.setObject(key, verifyCode,300); 
			String returnMsg=sendSMS_yunpian(mobile, msg);
			if(ObjectUtil.isEmpty(returnMsg)){
				return false;
			}
		}
		//用户充值
		else if (msgType==Integer.parseInt(SendMsgEnums.recharge.toString())) {
			msg="【咿呀科技】尊敬的用户，您的帐号于"+DateUtil.getTimeStr(new Date(), "yyyy-MM-dd HH:mm")+"成功充值"+param.getAmount()+"元。（客服电话：13760131762）。";
			batchSend(mobile, msg); 
		}
		//已发货-短信通知
		else if (msgType==Integer.parseInt(SendMsgEnums.delivery.toString())) {
			if(!(param==null||ObjectUtil.isEmpty(param.getTransName())||ObjectUtil.isEmpty(param.getTransNum()))){
				msg="【咿呀科技】尊敬的用户，您在咿呀十二制作购买的相册已发货，快递单号："+param.getTransNum()+"["+param.getTransName()+"]。（客服电话：13760131762）。"; 
				batchSend(mobile, msg); 
			}
		}
		return true;
	}
	
	/**
	 * 短信群发
	 * @param msgType
	 * @param mobiles
	 * @param param
	 * @return
	 */
//	public static String sendSms(int msgType,String[] mobiles,SmsParam param){
//		return "";
//	}
	
	
	/**
	 * 短信发送
	 * @param type SendMsgEnums
	 * @param moblie
	 * @return JSONObject （{code:0 发送成功，msg:失败的原因}）
	 */
//	public static JSONObject sendSmsReturnObject(SendMsgEnums type, String moblie) {
//		if (type.equals(SendMsgEnums.register)) {
//			String verifyCode = String.valueOf(Math.random()).substring(2, 6);
//			String key=moblie+"-"+Integer.parseInt(type.toString());
//			RedisUtil.setObject(key, verifyCode,300); 
//			String resultString= sendSMS_yunpian(moblie, "【咿呀科技】您的验证码是"+verifyCode);
//			return JSONObject.fromObject(resultString);
//		}
//		return null;
//	}
	
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
