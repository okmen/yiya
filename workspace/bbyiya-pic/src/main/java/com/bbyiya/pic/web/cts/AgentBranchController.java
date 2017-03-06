package com.bbyiya.pic.web.cts;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.pic.service.IPic_BranchMgtService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/cts/agent")
public class AgentBranchController extends SSOController {
	@Resource(name = "pic_BranchMgtService")
	private IPic_BranchMgtService branchService;
	
	/**
	 * 代理商审核
	 * @param agentUserId
	 * @param status
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/audit_AgentApply")
	public String audit_AgentApply(Long agentUserId,int status,String msg) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq =branchService.audit_AgentApply(user.getUserId(), agentUserId, status, msg);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 影楼门店审核
	 * @param agentUserId
	 * @param status
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/audit_BranchApply")
	public String audit_BranchApply(Long branchUserId,int status,String msg) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq =branchService.audit_BranchApply(user.getUserId(), branchUserId, status, msg); 
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
}
