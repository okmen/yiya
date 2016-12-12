package com.bbyiya.api.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.service.IUserLoginService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.vo.user.OtherLoginParam;
import com.bbyiya.vo.user.RegisterParam;
import com.bbyiya.vo.user.UChildInfoParam;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/login")
public class LoginController extends SSOController {

	/**
	 * ��½��ע�� service
	 */
	@Resource(name = "userLoginService")
	private IUserLoginService loginService; 

	
	
	
	/**
	 * �ֻ��š������½
	 * 
	 * @param phone�ֻ���
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/loginAjax")
	public String loginAjax(String phone, String pwd) throws Exception {
		ReturnModel rq = loginService.login(phone, pwd);
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * ��ȡ��½��Ϣ
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getuser")
	public String getuser() throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(user);
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��½���ڣ������µ�½��");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * A01��������½��ע��
	 * zy 
	 * @param headImg
	 * @param loginType
	 * @param nickName
	 * @param openId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/otherLogin")
	public String otherLogin(String headImg, String loginType, String nickName, String openId) throws Exception {
		OtherLoginParam param = new OtherLoginParam();
		param.setOpenId(openId);
		param.setLoginType(ObjectUtil.parseInt(loginType));
		param.setNickName(nickName);
		param.setHeadImg(headImg);
		return JsonUtil.objectToJsonStr(loginService.otherLogin(param));
	}

	/**
	 * A06ע�ᣨ���ֻ��š�����ע�ᣩ
	 * ע�� ps:�û�������,�ֻ��� ע�ᣨ����ǵ�����ע�ᣬ������дregister_token��
	 * 
	 * @param username
	 * @param pwd
	 * @param phone
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/registerAjax")
	public String registerAjax(String username, String pwd, String phone, String vcode, String register_token) throws Exception {
		RegisterParam param = new RegisterParam();
		param.setUsername(username);
		param.setPassword(pwd);
		param.setMobilephone(phone);
		param.setVcode(vcode);
		param.setRegister_token(register_token);
		ReturnModel rq = loginService.register(param);
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * A08 ���汦����Ϣ
	 * 
	 * @param childInfoJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addChildInfo")
	public String addChildInfo(String childInfoJson) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = this.getLoginUser();
		if (user == null) {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��½���ڣ������µ�½��");
			return JsonUtil.objectToJsonStr(rq);
		}
		if (ObjectUtil.isEmpty(childInfoJson)) {
			rq.setStatu(ReturnStatus.ParamError); 
			rq.setStatusreson("��������Ϊ��");
			return JsonUtil.objectToJsonStr(rq);
		}
		//������Ϣ����model
		UChildInfoParam child = (UChildInfoParam) JsonUtil.jsonStrToObject(childInfoJson, UChildInfoParam.class);
		rq = loginService.addChildInfo(user.getUserId(), child);
		if(rq.getStatu().equals(ReturnStatus.Success)){//�ɹ� ���ñ�����Ϣ =�� �����û���½��Ϣ
			rq.setBasemodle(loginService.updateLoginSuccessResult(user));  
			rq.setStatusreson("������Ϣ���óɹ���");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * A08-1 ���汦����Ϣ
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getrelation")
	public String getrelation() throws Exception {
		ReturnModel rq = new ReturnModel();
//		LoginSuccessResult user = this.getLoginUser();
//		if (user == null) {
//			rq.setStatu(ReturnStatus.LoginError);
//			rq.setStatusreson("��½����");
//			return JsonUtil.objectToJsonStr(rq);
//		}
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		list.add(getRalation(1, "�ְ�"));
		list.add(getRalation(2, "����"));
		list.add(getRalation(3, "үү"));
		list.add(getRalation(4, "����"));
		list.add(getRalation(5, "����"));
		list.add(getRalation(6, "����"));
		rq.setBasemodle(list); 
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	private Map<String, Object> getRalation(int id,String value){
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("id", id);
		map.put("value", value);
		return map;
	}

}
