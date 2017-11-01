package com.bbyiya.utils;

import net.sf.json.JSONObject;

import com.bbyiya.common.vo.wechatmsg.WxUserInfo;

public class WechatUtils {

	/**
	 * 获取用户微信信息
	 * @param openId
	 * @return
	 */
	public static WxUserInfo getUserInfo(String openId){
		String userInfoUrl = "https://api.weixin.qq.com/cgi-bin/user/info";
		String dataParam = "access_token=" + AccessTokenUtils.getAccessToken() + "&openid=" + openId;
		String resultStr= HttpRequestHelper.sendPost(userInfoUrl, dataParam);
		
		JSONObject userJson = JSONObject.fromObject(resultStr);
		if (userJson != null) {
			WxUserInfo resultInfo=new WxUserInfo();
			int subscribe=ObjectUtil.parseInt(userJson.getString("subscribe"));
			resultInfo.setSubscribe(subscribe);
			resultInfo.setOpenid(openId);
		
			if(!ObjectUtil.isEmpty(userJson.get("nickname"))){
				String nickname=ObjectUtil.filterEmojiNew(String.valueOf(userJson.get("nickname")));
				resultInfo.setNickname(nickname);
			}
			if(!ObjectUtil.isEmpty(userJson.get("headimgurl"))){
				resultInfo.setHeadimgurl(userJson.getString("headimgurl"));  
			}
			if(!ObjectUtil.isEmpty(userJson.get("unionid"))){
				resultInfo.setUnionid(userJson.getString("unionid"));
			}
			if(!ObjectUtil.isEmpty(userJson.get("remark"))){
				resultInfo.setRemark(userJson.getString("remark"));
			}
			if(subscribe==1){ 
				resultInfo.setSex(ObjectUtil.parseInt(String.valueOf(userJson.get("sex"))));
				resultInfo.setProvince(String.valueOf(userJson.get("province"))); 
				resultInfo.setCountry(String.valueOf(userJson.get("country")));
				resultInfo.setCity(String.valueOf(userJson.get("city")));
				resultInfo.setLanguage(String.valueOf(userJson.get("language")));
				resultInfo.setSubscribe_time(ObjectUtil.parseLong(String.valueOf(userJson.get("subscribe_time"))));  
			} 
			return resultInfo;
		}
		return null;
	}
	
	
	/**
	 * 用户是否关注 咿呀科技 公众号
	 * @param openId
	 * @return
	 */
	public static boolean  booleanAttention(String openId){
		WxUserInfo userInfo= getUserInfo(openId);
		if(userInfo!=null&&userInfo.getSubscribe()==1){
			return true;
		}
		return false;
	}
	
}
