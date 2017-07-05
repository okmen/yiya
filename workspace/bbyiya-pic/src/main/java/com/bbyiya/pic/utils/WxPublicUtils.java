package com.bbyiya.pic.utils;

import java.security.DigestException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;





import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import net.sf.json.JSONObject;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.HttpRequestHelper;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.utils.encrypt.Sha1Encrypt;
import com.bbyiya.utils.pay.WxPayConfig;
import com.bbyiya.utils.pay.WxPayUtils;
import com.bbyiya.vo.ReturnModel;
/**
 * 微信公众号 全局文件
 * @author Administrator
 *
 */
public class WxPublicUtils {

	//微信access_token
	public static String ACCESS_TOKEN= ConfigUtil.getSingleValue("currentRedisKey-Base")+"_wx_access_token";
	//微信config
	public static String JSAPI_TOKEN=ConfigUtil.getSingleValue("currentRedisKey-Base")+"_wx_jsapi_token";
	//微信access_token 缓存有效时间
	public static int ACCESS_TOKEN_TIMEVAL=7200;
	//微信分享 jsapi_token 有效期10分钟
	public static int JSAPI_TOKEN_TIME=7200;
	

	/**
	 * 获取微信   access_Token
	 * @param userId
	 * @return
	 */
	public static String getAccessToken() {
		String tokens=RedisUtil.getString(ACCESS_TOKEN);
		if(ObjectUtil.isEmpty(tokens)){
			tokens=getAccessTokenPost();
			if(!ObjectUtil.isEmpty(tokens)){
				RedisUtil.setString(ACCESS_TOKEN , tokens, ACCESS_TOKEN_TIMEVAL);
			}
			return tokens;
		}else {
			return tokens;
		}
	}
	
	/**
	 * 获取jsapi
	 * @return
	 */
	public static String getWxApiToken() {
		String wxapi_tokens=RedisUtil.getString(JSAPI_TOKEN);
		if(ObjectUtil.isEmpty(wxapi_tokens)){
			String tokens=getAccessToken();
			if(!ObjectUtil.isEmpty(tokens)){
				String url="https://api.weixin.qq.com/cgi-bin/ticket/getticket";
				String postResult= HttpRequestHelper.sendPost(url, "access_token="+tokens+"&type=jsapi");
				JSONObject model = JSONObject.fromObject(postResult);
				wxapi_tokens=String.valueOf(model.get("ticket"));
				int errCode=ObjectUtil.parseInt(String.valueOf(model.get("errcode")));
				if(errCode==0&&!ObjectUtil.isEmpty(wxapi_tokens)) {
					RedisUtil.setString(JSAPI_TOKEN , wxapi_tokens, JSAPI_TOKEN_TIME);
					return wxapi_tokens;
				}
			}
		}else {
			return wxapi_tokens;
		}
		return "";
	}
	
	public static String getAccessTokenPost() {
		String tokenUrl="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+WxPayConfig.APPID+"&secret="+WxPayConfig.AppSecret;
		String postResult=HttpRequestHelper.sendPost(tokenUrl, "");
		JSONObject modelToken = JSONObject.fromObject(postResult);
		if(modelToken!=null&&modelToken.get("access_token")!=null){
			String	accessToken=modelToken.getString("access_token");
			return accessToken;
		}
		return "";
	}
	
	
	
//	public static ReturnModel getWxConfig(String accessToken,String webUrl){
//		ReturnModel rqModel=new ReturnModel();
//		String url="https://api.weixin.qq.com/cgi-bin/ticket/getticket";
//		String postResult= HttpRequestHelper.sendPost(url, "access_token="+accessToken+"&type=jsapi");
//		JSONObject model = JSONObject.fromObject(postResult);
//		if(model!=null) {
//			int errCode=ObjectUtil.parseInt(String.valueOf(model.get("errcode")));
//			String ticket=String.valueOf(model.get("ticket"));
//			if(errCode==0&&!ObjectUtil.isEmpty(ticket)) {
//				String noString= WxPayUtils.genNonceStr();
//				String timeStr=String.valueOf(WxPayUtils.genTimeStamp());
//				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//				parameters.add(new BasicNameValuePair("noncestr", noString));
//				parameters.add(new BasicNameValuePair("timestamp", timeStr));
//				parameters.add(new BasicNameValuePair("jsapi_ticket", ticket));
//				parameters.add(new BasicNameValuePair("url", webUrl));
//				try {
//					String sign= Sha1Encrypt.SHA1(parameters);
//					Map<String, Object> result=new HashMap<String, Object>();
//					result.put("appId", WxPayConfig.APPID);
//					result.put("timestamp", timeStr);
//					result.put("nonceStr", noString);
//					result.put("signature", sign);
//					result.put("jsApiList",null);
//					rqModel.setStatu(ReturnStatus.Success);
//					rqModel.setBasemodle(result);
//					rqModel.setStatusreson(ticket);
//					return rqModel;
//					
//				} catch (DigestException e) {
//					
//					e.printStackTrace();
//					rqModel.setStatu(ReturnStatus.SystemError);
//					rqModel.setBasemodle(e);
//				}
//			}
//		}
//		RedisUtil.delete(ACCESS_TOKEN);
//		rqModel.setStatu(ReturnStatus.Success);
//		rqModel.setStatusreson("ticket获取失败");
//		rqModel.setBasemodle(postResult);
//		return rqModel;
//	}
	
	/**
	 * 获取微信分享 config
	 * @param jsapi_ticket
	 * @param webUrl
	 * @return
	 */
	public static ReturnModel getWxConfigNew(String jsapi_ticket,String webUrl){
		ReturnModel rqModel = new ReturnModel();
		if (!ObjectUtil.isEmpty(jsapi_ticket)) {
			String noString = WxPayUtils.genNonceStr();
			String timeStr = String.valueOf(WxPayUtils.genTimeStamp());
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("noncestr", noString));
			parameters.add(new BasicNameValuePair("timestamp", timeStr));
			parameters.add(new BasicNameValuePair("jsapi_ticket", jsapi_ticket));
			parameters.add(new BasicNameValuePair("url", webUrl));
			try {
				String sign = Sha1Encrypt.SHA1(parameters);
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("appId", WxPayConfig.APPID);
				result.put("timestamp", timeStr);
				result.put("nonceStr", noString);
				result.put("signature", sign);
				result.put("jsApiList", null);
				rqModel.setStatu(ReturnStatus.Success);
				rqModel.setBasemodle(result);
				rqModel.setStatusreson(jsapi_ticket);
				return rqModel;

			} catch (DigestException e) {
				rqModel.setStatu(ReturnStatus.SystemError);
				rqModel.setBasemodle(e);
				rqModel.setStatusreson(e.getMessage()); 
			}
		}
		RedisUtil.delete(JSAPI_TOKEN);
		RedisUtil.delete(ACCESS_TOKEN); 
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("ticket获取失败");
		return rqModel;
	}
}
