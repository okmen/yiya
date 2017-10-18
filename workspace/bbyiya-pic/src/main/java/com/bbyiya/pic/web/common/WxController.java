package com.bbyiya.pic.web.common;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.EErrorsMapper;
import com.bbyiya.dao.UOtherloginMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.EErrors;
import com.bbyiya.pic.utils.WxPublicUtils;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.HttpRequestHelper;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.utils.pay.WxPayUtils;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/wx")
public class WxController extends SSOController {
	@Autowired
	private EErrorsMapper errorMapper;
	@Autowired
	private UOtherloginMapper otherMapper;
	/**
	 * 获取微信config
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getWXconfig")
	public String getWXconfig(String webUrl) throws Exception {
		ReturnModel rq = new ReturnModel();
		
		String jsapiString=WxPublicUtils.getWxApiToken();
		if(!ObjectUtil.isEmpty(jsapiString)){
			rq=WxPublicUtils.getWxConfigNew(jsapiString, webUrl);
		}else {
			rq.setStatu(ReturnStatus.LoginError_3);
			rq.setStatusreson("微信accessToken过期！");
		}
		if(!ReturnStatus.Success.equals(rq.getStatu())){
			addlog(rq.getStatusreson()); 
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 获取微信accessToken
	 * @param reflush
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getAccessToken")
	public String getAccessToken(String reflush) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			//强制获取新的accessToken 并同步redis
			if(ObjectUtil.parseInt(reflush)==2){
				String key="wx_access_token";
				String accessToken = WxPublicUtils.getAccessTokenPost();
				RedisUtil.setString(key, accessToken, 7000);
				String postParam= "key=" + key + "&value=" + accessToken + "&seconds=" + 7000;
				List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
				packageParams.add(new BasicNameValuePair("key", key));
				packageParams.add(new BasicNameValuePair("value", accessToken));// 商户号
				packageParams.add(new BasicNameValuePair("seconds", String.valueOf(7000)));
				//生成签名
				String sign= WxPayUtils.genPackageSign(packageParams);
				postParam+="&sign="+sign;
				String currentDomain = ConfigUtil.getSingleValue("currentDomain");
				String result="";
				if (!ObjectUtil.isEmpty(currentDomain) && currentDomain.contains("photo-net.")) {
					result=HttpRequestHelper.sendPost("https://mpic.bbyiya.com/wx/SynRedist", postParam);
				} else {
					result=HttpRequestHelper.sendPost("https://mpic.bbyiya.net/wx/SynRedist", postParam);
				} 
				
				rq.setStatu(ReturnStatus.Success);
				rq.setBasemodle(accessToken);
				rq.setStatusreson(result);
			}else {
				String accessToken=WxPublicUtils.getAccessToken();
				rq.setStatu(ReturnStatus.Success);
				rq.setBasemodle(accessToken);
			}
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 设置redis
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/SynRedist")
	public String SynRedist(String key,String value,String sign, @RequestParam(required = false, defaultValue = "7000")int seconds) throws Exception {
		ReturnModel rq = new ReturnModel();
		if(!(ObjectUtil.isEmpty(key)||ObjectUtil.isEmpty(value)||ObjectUtil.isEmpty(sign))){
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams.add(new BasicNameValuePair("key", key));
			packageParams.add(new BasicNameValuePair("value", value));// 商户号
			packageParams.add(new BasicNameValuePair("seconds", String.valueOf(seconds)));
			//生成签名
			String signTemp= WxPayUtils.genPackageSign(packageParams);
			if(sign.equals(signTemp)){
				RedisUtil.setString(key, value, seconds);
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("ok（good!）");
			}else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("sign error!");
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 插入错误Log
	 * 
	 * @param msg
	 */
	public void addlog(String msg) {
		EErrors errors = new EErrors();
		errors.setClassname(this.getClass().getName());
		errors.setMsg(msg);
		errors.setCreatetime(new Date()); 
		errorMapper.insert(errors);
	}
}
