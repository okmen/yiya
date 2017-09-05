package com.bbyiya.pic.web.calendar;


import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.enums.user.UserStatusEnum;
import com.bbyiya.model.TiAgentsapply;
import com.bbyiya.model.TiPromotersapply;
import com.bbyiya.model.UUsers;
import com.bbyiya.service.calendar.ITi_AgentMgtService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/calendar/agent/")
public class TiAgentApplyMgtController extends SSOController {
	
	@Resource(name = "ti_AgentMgtService")
	private ITi_AgentMgtService agentService;
	@Autowired
	private UUsersMapper userMapper;
	/**
	 *  C端代理商申请
	 * @param agentJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/tiagentApply")
	public String tiagentApply(String agentJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(user.getStatus()!=null&&user.getStatus().intValue()==Integer.parseInt(UserStatusEnum.ok.toString())){
				try {
					TiAgentsapply applyInfo=(TiAgentsapply)JsonUtil.jsonStrToObject(agentJson, TiAgentsapply.class);
					rq =agentService.tiagentApply(user.getUserId(), applyInfo);
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
	 *  C端推广者申请
	 * @param agentJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/promoterApply")
	public String promoterApply(String promoterJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(!ObjectUtil.isEmpty(user.getMobilePhone())){
				try {
					TiPromotersapply applyInfo=(TiPromotersapply)JsonUtil.jsonStrToObject(promoterJson, TiPromotersapply.class);
					if(applyInfo!=null){
						applyInfo.setPromoteruserid(user.getUserId()); 
					}
					rq =agentService.applyPromoter(user.getUserId(), applyInfo);
				} catch (Exception e) {
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("参数有误101");
					System.out.println(e); 
					return JsonUtil.objectToJsonStr(rq);
				}
			}else {
				rq.setStatu(ReturnStatus.LoginError_2);
				rq.setStatusreson("未完成注册,请先绑定手机号！");
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
	 *  IBS端推广者申请
	 * @param agentJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/ibs_promoterApply")
	public String ibs_promoterApply(String promoterUserId,String promoterJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.ti_promoter)||ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.ti_employees)){
				UUsers promoterUsers= userMapper.getUUsersByUserID(ObjectUtil.parseLong(promoterUserId));
				if(promoterUsers!=null){
					if(!ObjectUtil.isEmpty(promoterUsers.getMobilephone())){
						try {
							
							TiPromotersapply applyInfo=(TiPromotersapply)JsonUtil.jsonStrToObject(promoterJson, TiPromotersapply.class);
							if(applyInfo!=null){
								applyInfo.setPromoteruserid(user.getUserId()); 
							}
							rq =agentService.applyPromoter(user.getUserId(), applyInfo);
						} catch (Exception e) {
							rq.setStatu(ReturnStatus.ParamError);
							rq.setStatusreson("参数有误101");
							System.out.println(e); 
							return JsonUtil.objectToJsonStr(rq);
						}
					}else {
						rq.setStatu(ReturnStatus.ParamError);
						rq.setStatusreson("用户未绑定手机号！");
					}
				}
			}else {
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("无此权限");
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
	 * 推广者审核
	 * @param promoterUserId
	 * @param status
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/audit_PromoterApply")
	public String audit_PromoterApply(Long promoterUserId, int status, String msg) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			
			if(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_member)||ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_admin)){
				rq = agentService.audit_PromoterApply(user.getUserId(), promoterUserId, status, msg);
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
	 * C端判断用户代理商申请状态
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getApplyStatus")
	public String getApplyStatus(Integer type) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(type!=null&&type==1){
				rq=agentService.getAgentApplyStatusModel(user.getUserId());
			}else if(type!=null&&type==2){
				rq=agentService.getPromoterApplyStatusModel(user.getUserId());
			}else {
				rq=agentService.getproducersApplyStatusModel(user.getUserId());
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	
	
	
	
}
