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

	
	public static String aCCESS_TOKEN_BASE="wx_access_token_all";
	//缓存有效时间
	public static int TIME_VALIDAITY=7200;
	
	

	/**
	 * 获取微信   access_Token
	 * @param userId
	 * @return
	 */
	public static String getAccessToken() {
		String tokens=RedisUtil.getString(aCCESS_TOKEN_BASE);
		if(ObjectUtil.isEmpty(tokens)){
			tokens=getAccessTokenPost();
			if(!ObjectUtil.isEmpty(tokens)){
				RedisUtil.setString(aCCESS_TOKEN_BASE , tokens, 7200);
			}
			return tokens;
		}else {
			return tokens;
		}
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
	
	
	
	public static ReturnModel getWxConfig(String accessToken,String webUrl){
		ReturnModel rqModel=new ReturnModel();
		String url="https://api.weixin.qq.com/cgi-bin/ticket/getticket";
		String postResult= HttpRequestHelper.sendPost(url, "access_token="+accessToken+"&type=jsapi");
		JSONObject model = JSONObject.fromObject(postResult);
		if(model!=null) {
			int errCode=ObjectUtil.parseInt(String.valueOf(model.get("errcode")));
			String ticket=String.valueOf(model.get("ticket"));
			if(errCode==0&&!ObjectUtil.isEmpty(ticket)) {
				String noString= WxPayUtils.genNonceStr();
				String timeStr=String.valueOf(WxPayUtils.genTimeStamp());
				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
				parameters.add(new BasicNameValuePair("noncestr", noString));
				parameters.add(new BasicNameValuePair("timestamp", timeStr));
				parameters.add(new BasicNameValuePair("jsapi_ticket", ticket));
				parameters.add(new BasicNameValuePair("url", webUrl));
				try {
					String sign= Sha1Encrypt.SHA1(parameters);
					Map<String, Object> result=new HashMap<String, Object>();
					result.put("appId", WxPayConfig.APPID);
					result.put("timestamp", timeStr);
					result.put("nonceStr", noString);
					result.put("signature", sign);
					result.put("jsApiList",null);
					rqModel.setStatu(ReturnStatus.Success);
					rqModel.setBasemodle(result);
					rqModel.setStatusreson(ticket);
					return rqModel;
					
				} catch (DigestException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					rqModel.setStatu(ReturnStatus.SystemError);
					rqModel.setBasemodle(e);
				}
			}
		}
		RedisUtil.delete(aCCESS_TOKEN_BASE);
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("ticket获取失败");
		rqModel.setBasemodle(postResult);
		return rqModel;
	}
}
