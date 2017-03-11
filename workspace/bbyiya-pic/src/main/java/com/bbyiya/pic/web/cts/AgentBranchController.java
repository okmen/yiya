package com.bbyiya.pic.web.cts;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.pic.service.IPic_BranchMgtService;
import com.bbyiya.pic.vo.agent.AgentSearchParam;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/cts/agent")
public class AgentBranchController extends SSOController {
	@Resource(name = "pic_BranchMgtService")
	private IPic_BranchMgtService branchService;

	/**
	 * B01 代理商审核
	 * 
	 * @param agentUserId
	 * @param status
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/audit_AgentApply")
	public String audit_AgentApply(Long agentUserId, int status, String msg) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			
			if(validate(user.getUserId())){
				rq = branchService.audit_AgentApply(user.getUserId(), agentUserId, status, msg);
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
	 * B03 查询代理商申请列表
	 * @param agentUserId
	 * @param status
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/findAgentApplylist")
	public String findAgentApplylist(String userId, String status) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			AgentSearchParam param=new AgentSearchParam();
			Long userid=ObjectUtil.parseLong(userId);
			if(userid>0)
				param.setUserId(userid);
			if(!ObjectUtil.isEmpty(status)){
				param.setStatus(ObjectUtil.parseInt(status)); 
			} 
			
			if(validate(user.getUserId())){
				rq = branchService.findAgentApplyList(param);
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
	 * B04 查询影楼列表
	 * @param userId
	 * @param status
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/findBranchApplylist")
	public String findBranchApplylist(String userId, String status) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			AgentSearchParam param=new AgentSearchParam();
			Long userid=ObjectUtil.parseLong(userId);
			if(userid>0)
				param.setUserId(userid);
			if(!ObjectUtil.isEmpty(status)){
				param.setStatus(ObjectUtil.parseInt(status)); 
			} 
			if(validate(user.getUserId())){
				rq = branchService.findBranchVoList(param);
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
	 * B02 影楼审核
	 * 
	 * @param agentUserId
	 * @param status
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/audit_BranchApply")
	public String audit_BranchApply(Long branchUserId, int status, String msg) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			if(validate(user.getUserId())){
				rq = branchService.audit_BranchApply(user.getUserId(), branchUserId, status, msg);
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
	 * 判断用户代理商申请状态
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getApplyStatus")
	public String getApplyStatus(Integer type,Long userId) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(type!=null&&type==1){
				rq=branchService.getAgentApplyStatusModel(userId);
			}else {
				rq=branchService.getBranchApplyStatusModel(userId);
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	public boolean validate(long userId){
		List<Map<String, String>>users= ConfigUtil.getMaplist("adminUsers");
		if(users!=null&&users.size()>0){
			for (Map<String, String> map : users) {
				if(ObjectUtil.parseLong(map.get("userId"))==userId){
					return true;
				}
			}
		}
		return false;
	}
}
