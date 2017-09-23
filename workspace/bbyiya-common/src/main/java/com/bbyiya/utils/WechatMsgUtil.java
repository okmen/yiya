package com.bbyiya.utils;

import java.util.HashMap;
import java.util.Map;

import com.bbyiya.common.enums.WechatMsgEnums;
import com.bbyiya.common.vo.WechatMsgVo;

public class WechatMsgUtil {

	
	public static void sendMsg(WechatMsgEnums templateId,String accessToken, String openId,WechatMsgVo param){
		try {
			//参与台历活动
			if(templateId.toString().equals(WechatMsgEnums.payed.toString())){
					if(param==null){
						param=new WechatMsgVo();
					}
					Map<String, Object> postParam=new HashMap<String, Object>();
					postParam.put("touser", openId);
					postParam.put("template_id", templateId.toString());
					postParam.put("url", ObjectUtil.isEmpty(param.getLinkUrl())?"http://photo-net.bbyiya.com/#/index":param.getLinkUrl());
					postParam.put("topcolor", "#FF0000");
					Map<String, Object> dataPro=new HashMap<String, Object>();
					dataPro.put("value", param.getProductDes());
					dataPro.put("color", "#173177");
					Map<String, Object> dataRemark=new HashMap<String, Object>();
					dataRemark.put("value", param.getRemark());
					dataRemark.put("color", "#173177");
					Map<String, Object> dataMap=new HashMap<String, Object>();
					dataMap.put("name", dataPro);
					dataMap.put("remark", dataRemark);
					postParam.put("data", dataMap);
					String result=HttpRequestHelper.postJson("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+accessToken, JsonUtil.objectToJsonStr(postParam));
//					System.out.println(result); 
			}else {
//				System.out.println(1);
			}
		} catch (Exception e) {
//			System.out.println(e);
		}
		
	}
}
