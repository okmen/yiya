package com.bbyiya.pic.web.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.pic.utils.WxPublicUtils;
import com.bbyiya.utils.HttpRequestHelper;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/wx")
public class WxQRcodeController extends SSOController {
	
	/**
	 * 获取微信config
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getQRcode")
	public String getWXconfig(String desc,String id) throws Exception {
		ReturnModel rq = new ReturnModel();
		String url="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+WxPublicUtils.getAccessToken();
		Map<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("expire_seconds", 604800);
		paramMap.put("action_name", "QR_STR_SCENE");
		
		Map<String, Object> sub=new HashMap<String, Object>();
		Map<String, Object> sub2=new HashMap<String, Object>();
		sub2.put("scene_str", "test");
		sub2.put("url", "http://photo-net.bbyiya.com/#/index");
		sub.put("scene", sub2);
		
		paramMap.put("action_info", sub);
		String result= HttpRequestHelper.postJson(url, JsonUtil.objectToJsonStr(paramMap));// post(url, JsonUtil.objectToJsonStr(paramMap));
		rq.setBasemodle(result);  //
		return JsonUtil.objectToJsonStr(rq); 
	}
	
	

}
