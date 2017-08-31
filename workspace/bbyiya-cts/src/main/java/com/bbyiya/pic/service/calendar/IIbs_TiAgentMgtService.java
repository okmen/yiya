package com.bbyiya.pic.service.calendar;

import com.bbyiya.model.TiAgentsapply;
import com.bbyiya.model.TiProducersapply;
import com.bbyiya.model.TiPromotersapply;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.calendar.TiAgentSearchParam;

public interface IIbs_TiAgentMgtService {
	
	/**
	 * 代理商申请
	 * @param userId
	 * @param applyInfo
	 * @return
	 */
	ReturnModel tiagentApply(Long userId, TiAgentsapply applyInfo);
	/**
	 * 推广者申请
	 * @param userId
	 * @param applyInfo
	 * @return
	 */
	ReturnModel applyPromoter(Long userId, TiPromotersapply applyInfo);
	
	/**
	 * 生产者申请
	 * @param userId
	 * @param applyInfo
	 * @return
	 */
	ReturnModel applyProducers(Long userId, TiProducersapply applyInfo);
	/**
	 * 代理商审核
	 * @param adminId
	 * @param agentUserId
	 * @param status 1通过并已经交费，2不通过，3通过待交费
	 * @param msg
	 * @return
	 */
	ReturnModel audit_AgentApply(Long adminId, Long agentUserId, int status,
			String msg);
	/**
	 * 推广者审核 
	 * @param adminId
	 * @param promoterUserId
	 * @param status
	 * @param msg
	 * @return
	 */
	ReturnModel audit_PromoterApply(Long adminId, Long promoterUserId,
			int status, String msg);
	
	/**
	 * 生产者审核
	 * @param adminId
	 * @param producersUserId
	 * @param status
	 * @param msg
	 * @return
	 */
	ReturnModel audit_ProducersApply(Long adminId, Long producersUserId,
			int status, String msg);
	
	/**
	 * 得到代理商的申请状态信息
	 * @param agentUserId
	 * @return
	 */
	ReturnModel getAgentApplyStatusModel(Long agentUserId);
	/**
	 * 得到推广者的申请状态信息
	 * @param promoterUserId
	 * @return
	 */
	ReturnModel getPromoterApplyStatusModel(Long promoterUserId);
	/**
	 * 得到生产者的申请状态信息
	 * @param producersUserId
	 * @return
	 */
	ReturnModel getproducersApplyStatusModel(Long producersUserId);
	
	/**
	 * 代理商申请的列表
	 * @param param
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel findTiAgentApplyList(TiAgentSearchParam param, int index,
			int size);
	
	/**
	 * 推广者申请列表
	 * @param param
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel findTiPromoterApplyList(TiAgentSearchParam param, int index,
			int size);
	/**
	 * 得到生产商申请列表
	 * @param param
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel findTiProducersApplyList(TiAgentSearchParam param, int index,
			int size);
	
}
