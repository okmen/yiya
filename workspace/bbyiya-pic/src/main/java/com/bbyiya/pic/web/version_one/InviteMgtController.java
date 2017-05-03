package com.bbyiya.pic.web.version_one;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.pic.service.IPic_myProductService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/myProduct/invite")
public class InviteMgtController  extends SSOController {
	@Resource(name = "pic_myProductService")
	private IPic_myProductService myProductService;
	
	/**
	 * ���� Эͬ�༭ ����
	 * @param province
	 * @param city
	 * @param district
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/sendInvite")
	public String sendInvite(String phone,Long cartId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(phone.equals(user.getMobilePhone())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("���������Լ�Эͬ�༭��");
				return JsonUtil.objectToJsonStr(rq);
			}
			rq=myProductService.sendInvite(user.getUserId(), phone, cartId);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼���ڣ������µ�¼");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * �����ҵ�����
	 * @param phone
	 * @param cartId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/processInvite")
	public String processInvite(Long cartId,Integer status) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=myProductService.processInvite(user.getMobilePhone(), cartId, status);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼���ڣ������µ�¼");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * ����ɨ��ҳ��Ľ�������
	 * @param phone ���������ֻ���
	 * @param cartId ��Ʒcartid
	 * @param verifcode  ��֤��
	 * @param needVerfiCode  �Ƿ���Ҫ��֤�ֻ���֤��
	 * @param version  ��ά��汾��
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/acceptScanQrCodeInvite")
	public String acceptScanQrCodeInvite(String phone,Long cartId,String vcode,Integer needVerfiCode,String version) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=myProductService.acceptScanQrCodeInvite(user.getUserId(),phone,cartId,vcode,needVerfiCode,version);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼���ڣ������µ�¼");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * ����ҽԺɨ��ҳ��Ľ�������
	 * @param phone ���������ֻ���
	 * @param cartId ģ����Ʒcartid	
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/acceptTempScanQrCodeInvite")
	public String acceptTempScanQrCodeInvite(String phone,Long cartId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=myProductService.acceptTempScanQrCodeInvite(user.getUserId(), phone, cartId);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼���ڣ������µ�¼");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * ��ȡ�û���ʾ��Ϣ
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/myUserInfoExp")
	public String myUserInfoExp() throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=myProductService.myUserInfoExp(user.getUserId(), user.getMobilePhone()); 
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼���ڣ������µ�¼");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
}
