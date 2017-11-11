package com.bbyiya.pic.web.common;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.EErrorsMapper;
import com.bbyiya.model.EErrors;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.encrypt.aes_wx.WXBizMsgCrypt;
import com.bbyiya.utils.pay.WxPayConfig;
import com.bbyiya.utils.pay.WxUtil;
import com.bbyiya.web.base.SSOController;


@Controller
@RequestMapping(value = "/wxmsg")
public class WxMsgController extends SSOController {

	private static final String EncodingAESKey =ConfigUtil.getPropertyVal("wxmsgEncodingAESKey");
	private static final String token = ConfigUtil.getPropertyVal("wxmsgToken");

//	String msgSignature = "f4814a8cef40c09500ef4326a45722b5221b29cb";
//	String timeStamp = "1510295808";
//	String nonce = "666942195";

	@ResponseBody
	@RequestMapping(value = "/res")
	public String getWXconfig() throws Exception {
		String xmlStr =  super.readReqStr(request); //recivemsgTest();//
		//信息处理工具类
		WXBizMsgCrypt wxCrypt = new WXBizMsgCrypt(token, EncodingAESKey, WxPayConfig.APPID);
		// 微信加密签名
		String msgSignature = request.getParameter("msg_signature");
		// 时间戳
		String timeStamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		try {
			if(ObjectUtil.isEmpty(xmlStr)){
				String echoStr=request.getParameter("echostr");
				String verMsg=wxCrypt.verifyUrl(msgSignature, timeStamp, nonce, echoStr);
				if(!ObjectUtil.isEmpty(verMsg)){
					return echoStr;
				}
				addlog("param:"+request.getParameter("echostr"));
				return echoStr; 
			}
			String msgResultString = wxCrypt.decryptMsg(msgSignature, timeStamp, nonce, xmlStr);
			SortedMap<String, String> recieveMap = WxUtil.xmlToMap(msgResultString);
//			System.out.println(recieveMap); 
			String enRespMsg = wxCrypt.encryptMsg(sendmsg(recieveMap), String.valueOf(WxUtil.genTimeStamp()), WxUtil.genNonceStr());
			return enRespMsg;
		} catch (Exception e) {
			addlog(e.getMessage()+"param:"+xmlStr+";"+msgSignature+","+timeStamp+","+nonce);
			return "";
		}
	}


	/**
	 * 需要回复的xml 
	 * @param map
	 * @return
	 */
	public String sendmsg(SortedMap<String, String> map) {
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("ToUserName", map.get("FromUserName")));
		params.add(new BasicNameValuePair("FromUserName", map.get("ToUserName")));
		params.add(new BasicNameValuePair("CreateTime", String.valueOf(new Date().getTime())));
		String type=map.get("MsgType");
		String replay="";
		if(type.equals("text")){
			params.add(new BasicNameValuePair("MsgType", "text"));
			replay=getReplymsg(map.get("Content"),type);
		}else if (type.equals("event")) {
			if(map.get("Event").equals("subscribe")){
				params.add(new BasicNameValuePair("MsgType", "text"));
				replay="为影像注入故事、用温情引导记录、让分享充满情怀，咿呀科技官方服务号欢迎您~[小咿]";
			}
		}
		if(ObjectUtil.isEmpty(replay))
			return "";
		params.add(new BasicNameValuePair("Content", replay));
		String xmlParamString = WxUtil.toXml(params);
		return xmlParamString;
	}
	
	

	/**
	 * 回复消息的文本内容
	 * @param recMsg
	 * @param type
	 * @return
	 */
	private String getReplymsg(String recMsg,String type){
		if(type.equals("text")){
			if(!ObjectUtil.isEmpty(recMsg)){
				if(recMsg.contains("kkk")){
					return "什么鬼！[小咿]";
				}else if (recMsg.contains("你是谁")) {
					return "咿呀科技！[小咿]";
				}
			}
		}
		return "";
	}

	@Autowired
	private EErrorsMapper errorMapper;
	/**
	 * 插入错误Log
	 * @param msg
	 */
	public void addlog(String msg) {
		try {
			EErrors errors = new EErrors();
			errors.setClassname(this.getClass().getName());
			errors.setMsg(msg);
			errors.setCreatetime(new Date()); 
			errorMapper.insert(errors);
		} catch (Exception e) {
		}
	}
	
	/**
	 * test GetMsg
	 * @return
	 */
	public String recivemsgTest() {
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("ToUserName", "gh_942a090504df"));
		params.add(new BasicNameValuePair("Encrypt", "YwyQiaiyEyrdLnusGIS8K6T55aIAF9hL1u17SSk+PkAux1BVxL270XwJRwxj/UNiaUBswP3x51YfDJBFyEvQJ3EvsOoUrrUPH+2hJI9YINz+eMB+P/k2+9W8VHc06DlGJtl8oSNkrHIa453DvvbHiuUDxhiK1fg1LL8yF3mLH398wUDbn6Rvy7KZx2Z4REc7nD7he07zSnz4zj1Wjc5WKLxUVq3dE+su/xHkqxVPZeg43cS/srjUbGD0zlep5UKWy95bahapw3MFwRA8QXm08DLsKn0XCa8Qawo50aKA9ovs7KNLpkGazc4K4RnUdVCGO6uYUpVSPw8gprYIWxKZnmL7+FSyzvbTi4NWZweXUdnDz/3YY6ZkTBYBAubK8Gxlh8fFjPK7c2dUHC2UfveJkM7tMQGKCutA+kf8DTCCKJ2B0/Uv2rBQfYNd08U8Z+8GXeWr4ltAaoaME0UL91pceq6b0atRTpA3rnfyGdWbZ/xc60TNtWQOQ/C6CxnkdjRpTyL+GU1um94IgAKzce+meAtKQhW9YHf4+o3MMKyuniWcJzvfXmFw2Zzo0SdcNGhW9ySefs6JvGFVRr3b3sWSgg=="));

		String xmlParamString = WxUtil.toXml(params);
		return xmlParamString;
	}

}
