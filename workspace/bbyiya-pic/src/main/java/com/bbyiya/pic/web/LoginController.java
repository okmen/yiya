package com.bbyiya.pic.web;

import java.util.Date;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bbyiya.baseUtils.CookieUtils;
import com.bbyiya.dao.EErrorsMapper;
import com.bbyiya.dao.ULoginlogsMapper;
import com.bbyiya.enums.LoginTypeEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.EErrors;
import com.bbyiya.model.ULoginlogs;
import com.bbyiya.pic.service.IPic_UserMgtService;
import com.bbyiya.pic.vo.LoginTempVo;
import com.bbyiya.service.IUserLoginService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.HttpRequestHelper;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.utils.pay.WxPayConfig;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.vo.user.OtherLoginParam;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/login")
public class LoginController extends SSOController {
	/**
	 * ��½��ע�� service
	 */ 
	@Resource(name = "userLoginService")
	private IUserLoginService loginBaseService; 
	/**
	 * ��½��ע�� service
	 */
	@Resource(name = "pic_userMgtService")
	private IPic_UserMgtService loginService;

	@Autowired
	private ULoginlogsMapper loginLogMapper;
	@Autowired
	private EErrorsMapper errorMapper;
	

	
	/**
	 * ��¼ ��תҳ
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/transfer")
	public String transferPage(String m,String uid) throws Exception {
//		LoginSuccessResult user = super.getLoginUser();
		long branch_userid=ObjectUtil.parseLong(uid);
		if(branch_userid>0||!ObjectUtil.isEmpty(m)){ 
			LoginTempVo loginTemp=new LoginTempVo();
			loginTemp.setUpUserId(branch_userid);
			loginTemp.setLoginTo(ObjectUtil.parseInt(m));
			String keyId= request.getSession().getId();
			RedisUtil.setObject(keyId, loginTemp, 60);
		}
//		int mtype=ObjectUtil.parseInt(m);
//		if(mtype>0){
//			if (user != null) {
//				if(mtype==1){//���Ե�ַ
//					return "redirect:" + ConfigUtil.getSingleValue("loginbackurl_test") ;
//				}
//			} 
//			return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxcc101e7b17ed868e&redirect_uri=https%3A%2F%2Fmpic.bbyiya.com%2Flogin%2FwxLoginTest&response_type=code&scope=snsapi_base#wechat_redirect" ;	
//		}
//		else {//��ʽ��ַ��¼
			return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxcc101e7b17ed868e&redirect_uri=https%3A%2F%2Fmpic.bbyiya.com%2Flogin%2FwxLogin&response_type=code&scope=snsapi_base#wechat_redirect" ;		
//		}
	}
	
	/**
	 * c�˲��԰汾��½���
	 * @param headImg
	 * @param loginType
	 * @param nickName
	 * @param openId
	 * @param upUid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/wxLoginTest")
	public String wxLoginTest(String headImg, @RequestParam(required = false, defaultValue = "2") int loginType, String nickName, String openId,String upUid) throws Exception {
		headImg = ObjectUtil.urlDecoder_decode(headImg, "");
		nickName = ObjectUtil.urlDecoder_decode(nickName, "");
		openId = ObjectUtil.urlDecoder_decode(openId, "");
		if (!ObjectUtil.validSqlStr(headImg) || !ObjectUtil.validSqlStr(nickName) || !ObjectUtil.validSqlStr(openId)) {
			ReturnModel rqModel = new ReturnModel();
			rqModel.setStatu(ReturnStatus.ParamError_2);
			rqModel.setStatusreson("�����зǷ��ַ�");
			return JsonUtil.objectToJsonStr(rqModel);
		}
		OtherLoginParam param = new OtherLoginParam();
		param.setOpenId(openId);
		param.setLoginType(loginType);
		param.setNickName(nickName);
		param.setHeadImg(headImg);
		if(!ObjectUtil.isEmpty(upUid)){
			param.setUpUserId(ObjectUtil.parseLong(upUid)); 
		} 
		ReturnModel rqModel = loginService.otherLogin(param);
		if (ReturnStatus.Success.equals(rqModel.getStatu()) && !ObjectUtil.isEmpty(rqModel.getBasemodle())) {
			addLoginLogAndCookie(rqModel.getBasemodle(),0);
		}
		return "redirect:" + ConfigUtil.getSingleValue("photo-net-url") ;
	}
	
	

	/**
	 * ΢�ŵ�¼
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/wxLogin")
	public String wxLogin(String code, String state) throws Exception {
		if (ObjectUtil.isEmpty(code)) {
			code = request.getParameter("code");
		}
		String urlString = "https://api.weixin.qq.com/sns/oauth2/access_token";
		String dataString = "appid=" + WxPayConfig.APPID + "&secret=" + WxPayConfig.AppSecret + "&code=" + code + "&grant_type=authorization_code";
		String result = HttpRequestHelper.sendPost(urlString, dataString);

		JSONObject model = JSONObject.fromObject(result);
		ReturnModel rqModel = new ReturnModel();
		if (model != null) {
			String openid = String.valueOf(model.get("openid"));
			String access_token = String.valueOf(model.get("access_token"));
			if (!ObjectUtil.isEmpty(openid) && !ObjectUtil.isEmpty(access_token) && !"null".equals(openid) && !"null".equals(access_token)) {

				String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo";
				String data2 = "access_token=" + access_token + "&openid=" + openid;
				String userInfoJson = HttpRequestHelper.sendPost(userInfoUrl, data2);
				JSONObject userJson = JSONObject.fromObject(userInfoJson);
				if (userInfoJson != null) {
					OtherLoginParam param = new OtherLoginParam();
					param.setOpenId(openid);
					param.setLoginType(Integer.parseInt(LoginTypeEnum.weixin.toString()));
					String nickName=String.valueOf(userJson.get("nickname"));
					String headimg=String.valueOf(userJson.get("headimgurl"));
					if(!ObjectUtil.isEmpty(nickName)&&!"null".equals(nickName)){
						param.setNickName(nickName);
					}
					if(!ObjectUtil.isEmpty(headimg)&&!"null".equals(headimg)){
						param.setHeadImg(headimg);
					}
					LoginTempVo logintemp= (LoginTempVo)RedisUtil.getObject(request.getSession().getId());
					if(logintemp!=null){
						if(logintemp.getUpUserId()!=null&&logintemp.getUpUserId()>0){
							param.setUpUserId(logintemp.getUpUserId()); 
						}
						int m=logintemp.getLoginTo()==null?0:logintemp.getLoginTo();
						if(m==1){ //photo���Ե�ַ
							String paramtest="?headImg="+param.getHeadImg()+"&loginType="+param.getLoginType()+"&nickName="+param.getNickName()+"&openId="+param.getOpenId()+"&upUid="+param.getUpUserId();
							//��תmpic���Խӿڵ�ַ��ת
							return "redirect:" + ConfigUtil.getSingleValue("mpic-net-url")+paramtest;
						}
					}
					rqModel = loginService.otherLogin(param);
					if (ReturnStatus.Success.equals(rqModel.getStatu()) && !ObjectUtil.isEmpty(rqModel.getBasemodle())) {
						addLoginLogAndCookie(rqModel.getBasemodle(),Integer.parseInt(LoginTypeEnum.weixin.toString())); 
					}
				} else {
					rqModel.setStatu(ReturnStatus.SystemError);
					rqModel.setStatusreson("��ȡ�û���Ϣʧ��");
				}
			} else {
				rqModel.setStatu(ReturnStatus.ParamError);
				rqModel.setStatusreson(String.valueOf(model.get("errmsg")));
			}
		} else {
			rqModel.setStatu(ReturnStatus.SystemError);
			rqModel.setStatusreson("��ȡ΢�ŵ�¼Ȩ��ʧ��");
		}
		if (rqModel.getStatu().equals(ReturnStatus.Success)) {
			return "redirect:" + ConfigUtil.getSingleValue("loginbackurl") ;
		} else {
			return "redirect:" + ConfigUtil.getSingleValue("loginbackurl") ;
		}
	}
	

	

	private void addLoginLogAndCookie(Object obj,int type) {
		try {
			LoginSuccessResult user = (LoginSuccessResult) obj;
			if (user != null) {
				ULoginlogs loginLogs = new ULoginlogs();
				loginLogs.setUserid(user.getUserId());
				loginLogs.setLogintime(new Date());
				loginLogs.setLogintype(type);
				loginLogs.setIpstr(super.getIpStr());
				loginLogs.setNickname(user.getNickName()); 
				loginLogs.setSourcetype(1);// 12photo
				loginLogMapper.insert(loginLogs);
				CookieUtils.addCookieBySessionId(request, response,user.getTicket(),86400); 
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * �������Log
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
