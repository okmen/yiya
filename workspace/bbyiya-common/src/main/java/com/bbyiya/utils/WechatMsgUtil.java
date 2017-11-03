package com.bbyiya.utils;

import java.util.HashMap;
import java.util.Map;

import com.bbyiya.common.enums.WechatMsgEnums;
import com.bbyiya.common.vo.WechatMsgVo;
import com.bbyiya.common.vo.wechatmsg.ShippingParam;
import com.bbyiya.common.vo.wechatmsg.ShippingParamNew;

public class WechatMsgUtil {

	/**
	 * 微信消息提醒 (订单已支付)
	 * 
	 * @param templateId
	 * @param openId
	 * @param param
	 */
	public static void sendMsg_Payed(String openId, WechatMsgVo param) {
		try {
			if (param == null) {
				param = new WechatMsgVo();
			}
			Map<String, Object> postParam = postParamCommon(WechatMsgEnums.payed, openId, param.getLinkUrl());
			// postParam.put("topcolor", "#FF0000");
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("name", getParamValue(param.getProductDes()));
			dataMap.put("remark", getParamValue(param.getRemark()));
			postParam.put("data", dataMap);
			String result = HttpRequestHelper.postJson("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + AccessTokenUtils.getAccessToken(), JsonUtil.objectToJsonStr(postParam));

		} catch (Exception e) {
		}
	}

	/**
	 * 微信消息提醒 (订单发货信息)
	 * 
	 * @param templateId
	 * @param accessToken
	 * @param openId
	 * @param param
	 */
	public static void sendMsg_Shipping(String openId, ShippingParam param) {
		try {
			if (param == null) {
				param = new ShippingParam();
			}
			Map<String, Object> postParam = postParamCommon(WechatMsgEnums.send, openId, param.getLinkUrl());
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("first", getParamValue("亲，您的订单已发货啦，等待是为了最美的相遇!"));
			dataMap.put("keyword1", getParamValue(param.getOrderId()));
			dataMap.put("keyword2", getParamValue(param.getTransCompany()));
			dataMap.put("keyword3", getParamValue(param.getTransOrderId()));
			dataMap.put("keyword4", getParamValue(String.valueOf(param.getTotalPrice())));
			dataMap.put("keyword5", getParamValue(param.getAddress()));
			dataMap.put("remark", getParamValue(param.getRemark()));
			postParam.put("data", dataMap);
			String result = HttpRequestHelper.postJson("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + AccessTokenUtils.getAccessToken(), JsonUtil.objectToJsonStr(postParam));

		} catch (Exception e) {
		}
	}

	public static void sendMsg_Shipping(String openId, ShippingParamNew param) {
		try {
			if (param == null) {
				param = new ShippingParamNew();
			}
			Map<String, Object> postParam = postParamCommon(WechatMsgEnums.sendSimple, openId, param.getLinkUrl());
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("first", getParamValue("亲，您的订单已发货啦，等待是为了最美的相遇!"));
			dataMap.put("keyword1", getParamValue(param.getOrderId()));
			dataMap.put("keyword2", getParamValue(param.getTransCompany()));
			dataMap.put("keyword3", getParamValue(param.getTransOrderId()));
			dataMap.put("remark", getParamValue(param.getRemark()));
			postParam.put("data", dataMap);
			String result = HttpRequestHelper.postJson("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + AccessTokenUtils.getAccessToken(), JsonUtil.objectToJsonStr(postParam));
//			System.out.println(result);
		} catch (Exception e) {
		}
	}

	private static Map<String, Object> postParamCommon(WechatMsgEnums templateId, String openId, String linkUrl) {
		Map<String, Object> postParam = new HashMap<String, Object>();
		postParam.put("touser", openId);
		postParam.put("template_id", templateId.toString());
		postParam.put("url", ObjectUtil.isEmpty(linkUrl) ? "http://photo-net.bbyiya.com/#/index" : linkUrl);
		return postParam;
	}

	private static Map<String, Object> getParamValue(String value) {
		Map<String, Object> dataPro = new HashMap<String, Object>();
		dataPro.put("value", ObjectUtil.isEmpty(value) ? "点击查看详情" : value);
		dataPro.put("color", "#173177");
		return dataPro;
	}
}
