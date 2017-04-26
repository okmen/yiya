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
	 * 发送 协同编辑 邀请
	 * @param province
	 * @param city
	 * @param district
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/sendInvite")
	public String getBranchAreaPrice(String phone,Long cartId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(phone.equals(user.getMobilePhone())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("不能邀请自己协同编辑！");
				return JsonUtil.objectToJsonStr(rq);
			}
			rq=myProductService.sendInvite(user.getUserId(), phone, cartId);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期，请重新登录");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 处理我的邀请
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
			rq.setStatusreson("登录过期，请重新登录");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 
	 * @param phone 被邀请人手机号
	 * @param cartId 作品cartid
	 * @param inviteUserId 邀请人用户ID
	 * @param verifcode  验证码
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/acceptScanQrCodeInvite")
	public String acceptScanQrCodeInvite(String phone,Long cartId,Long inviteUserId,Integer verifcode) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			//rq=myProductService.processInvite(user.getMobilePhone(), cartId, status);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期，请重新登录");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 获取用户提示信息
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
			rq.setStatusreson("登录过期，请重新登录");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
}
