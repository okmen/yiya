package com.bbyiya.pic.web.cts;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.enums.user.UserStatusEnum;
import com.bbyiya.model.UAgentapply;
import com.bbyiya.model.UWeiuserapplys;
import com.bbyiya.pic.service.cts.ICts_UWeiUserManageService;
import com.bbyiya.pic.utils.Json2Objects;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.vo.user.UWeiUserSearchParam;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/cts/weiuser")
public class UWeiusersController extends SSOController {
	@Resource(name = "cts_UWeiuserService")
	private ICts_UWeiUserManageService weiUserService;
	
	/**
	 * W01 流量主申请
	 * 
	 * @param agentUserId
	 * @param status
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/weiUserApply")
	public String weiUserApply(String weiUserJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(user.getStatus()!=null&&user.getStatus().intValue()==Integer.parseInt(UserStatusEnum.ok.toString())){
				try {		
					UWeiuserapplys applyInfo=Json2Objects.getParam_UWeiuserapplys(weiUserJson);
					if(applyInfo!=null){
						applyInfo.setMobilephone(user.getMobilePhone());
					}
					rq =weiUserService.applyWeiUser(user.getUserId(), applyInfo);
				} catch (Exception e) {
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("参数有误101");
					return JsonUtil.objectToJsonStr(rq);
				}
			}else {
				rq.setStatu(ReturnStatus.LoginError_2);
				rq.setStatusreson("未完成注册");
				return JsonUtil.objectToJsonStr(rq);
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * W02 流量主审核
	 * 
	 * @param agentUserId
	 * @param status
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/audit_weiUserApply")
	public String audit_weiUserApply(Long weiUserId, int status) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {	
			if(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_member)||ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_admin)){
				rq = weiUserService.audit_weiUserApply(user.getUserId(), weiUserId, status);
			}else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("无权限");
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * W03 查询流量主申请列表
	 * @param weiUserId
	 * @param status
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/findWeiUserApplylist")
	public String findWeiUserApplylist(Integer index,Integer size,String userId,String name,String phone,String status) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			UWeiUserSearchParam param=new UWeiUserSearchParam();
			Long userid=ObjectUtil.parseLong(userId);
			if(userid>0)
				param.setUserId(userid);
			if(!ObjectUtil.isEmpty(status)){
				param.setStatus(ObjectUtil.parseInt(status)); 
			} 
			if(!ObjectUtil.isEmpty(name)){
				param.setName(name);
			}
			if(!ObjectUtil.isEmpty(phone)){
				param.setMobilephone(phone);
			} 	
			if(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_member)||ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_admin)){
				rq = weiUserService.findWeiUserApplylist(param,index,size);
			}else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("无权限");
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * W04 流量主删除
	 * 
	 * @param agentUserId
	 * @param status
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/delete_weiUserApply")
	public String delete_weiUserApply(Long weiUserId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {	
			if(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_member)||ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_admin)){
				rq = weiUserService.delete_weiUserApply(user.getUserId(), weiUserId);
			}else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("无权限");
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	
}
