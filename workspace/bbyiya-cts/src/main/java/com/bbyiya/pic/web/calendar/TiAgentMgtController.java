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
import com.bbyiya.model.TiProducersapply;
import com.bbyiya.model.TiPromotersapply;
import com.bbyiya.model.UUsers;
import com.bbyiya.pic.service.calendar.IIbs_TiAgentMgtService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.calendar.TiAgentSearchParam;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/calendar/agent/")
public class TiAgentMgtController extends SSOController {
	
	@Resource(name = "ibs_TiAgentMgtService")
	private IIbs_TiAgentMgtService agentService;
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
	 *  CTS端代理商申请
	 * @param agentJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/cts_tiagentApply")
	public String cts_tiagentApply(String agentUserId,String agentJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_admin)||ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_member)){
				UUsers agentUsers= userMapper.getUUsersByUserID(ObjectUtil.parseLong(agentUserId));
				if(agentUsers!=null){
					if(!ObjectUtil.isEmpty(agentUsers.getMobilephone())){
						try {
							TiAgentsapply applyInfo=(TiAgentsapply)JsonUtil.jsonStrToObject(agentJson, TiAgentsapply.class);
							rq =agentService.tiagentApply(ObjectUtil.parseLong(agentUserId), applyInfo);
						} catch (Exception e) {
							rq.setStatu(ReturnStatus.ParamError);
							rq.setStatusreson("参数有误101");
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
	 *  CTS端推广者申请
	 * @param agentJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/cts_promoterApply")
	public String cts_promoterApply(String promoterUserId,String promoterJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_admin)||ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_member)){
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
	 * C端生产者申请
	 * @param agentJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/producersApply")
	public String producersApply(String producersJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(!ObjectUtil.isEmpty(user.getMobilePhone())){
				try {
					TiProducersapply applyInfo=(TiProducersapply)JsonUtil.jsonStrToObject(producersJson, TiProducersapply.class);
					if(applyInfo!=null){
						applyInfo.setProduceruserid(user.getUserId()); 
					}
					rq =agentService.applyProducers(user.getUserId(), applyInfo);
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
	 * 代理商审核
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
			
			if(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_member)||ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_admin)){
				rq = agentService.audit_AgentApply(user.getUserId(), agentUserId, status, msg);
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
	 * 生产者审核
	 * @param producersUserId
	 * @param status
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/audit_ProducersApply")
	public String audit_ProducersApply(Long producersUserId, int status, String msg) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			if(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_member)||ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_admin)){
				rq = agentService.audit_ProducersApply(user.getUserId(), producersUserId, status, msg);
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
	
	/**
	 * 代理商申请列表
	 */
	@ResponseBody
	@RequestMapping(value = "/findAgentApplylist")
	public String findAgentApplylist(String userId, String status,String keywords,int index,int size) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			TiAgentSearchParam param=new TiAgentSearchParam();
			Long userid=ObjectUtil.parseLong(userId);
			if(userid>0)
				param.setUserId(userid);
			if(!ObjectUtil.isEmpty(status)){
				param.setStatus(ObjectUtil.parseInt(status)); 
			} 
			if(!ObjectUtil.isEmpty(keywords)){
				param.setCompanyName(keywords);
			} 
			
			if(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_member)||ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_admin)){
				rq = agentService.findTiAgentApplyList(param,index,size);
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
	 * 生产商申请列表
	 */
	@ResponseBody
	@RequestMapping(value = "/findProducersApplylist")
	public String findProducersApplylist(String userId, String status,String keywords,int index,int size) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			TiAgentSearchParam param=new TiAgentSearchParam();
			Long userid=ObjectUtil.parseLong(userId);
			if(userid>0)
				param.setUserId(userid);
			if(!ObjectUtil.isEmpty(status)){
				param.setStatus(ObjectUtil.parseInt(status)); 
			} 
			if(!ObjectUtil.isEmpty(keywords)){
				param.setCompanyName(keywords);
			} 
			
			if(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_member)||ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_admin)){
				rq = agentService.findTiProducersApplyList(param,index,size);
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
	 * 推广者申请列表
	 */
	@ResponseBody
	@RequestMapping(value = "/findPromoterApplylist")
	public String findPromoterApplylist(String agentUserId, String status,String keywords,int index,int size) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			TiAgentSearchParam param=new TiAgentSearchParam();
			Long agentuser=ObjectUtil.parseLong(agentUserId);
			if(agentuser>0)
				param.setAgentUserId(agentuser);
			if(!ObjectUtil.isEmpty(status)){
				param.setStatus(ObjectUtil.parseInt(status)); 
			} 
			if(!ObjectUtil.isEmpty(keywords)){
				param.setCompanyName(keywords);
			} 
			
			if(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_member)||ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.cts_admin)){
				rq = agentService.findTiPromoterApplyList(param,index,size);
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
