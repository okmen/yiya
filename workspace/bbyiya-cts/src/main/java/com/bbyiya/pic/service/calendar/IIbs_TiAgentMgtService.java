package com.bbyiya.pic.service.calendar;

import java.util.List;

import com.bbyiya.model.TiAgentsapply;
import com.bbyiya.model.TiMachinemodel;
import com.bbyiya.model.TiProducersapply;
import com.bbyiya.model.TiPromotersapply;
import com.bbyiya.vo.RAreaVo;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.calendar.TiAgentApplyVo;
import com.bbyiya.vo.calendar.TiAgentSearchParam;
import com.bbyiya.vo.calendar.TiPromoterApplyVo;

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
	ReturnModel applyProducers(Long userId, TiProducersapply applyInfo,List<TiMachinemodel> machinelist);
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
	/**
	 * 获取代理商信息
	 * @param agentUserId
	 * @return
	 */
	TiAgentApplyVo getTiAgentsInfo(Long agentUserId);
	/**
	 * 获取推广者代理信息
	 * @param promoterUserId
	 * @return
	 */
	TiPromoterApplyVo getTiPromoterInfo(Long promoterUserId);
	/**
	 * 修改推广者的收货信息
	 * @param promoteruserid
	 * @param streetdetail
	 * @param name
	 * @param phone
	 * @return
	 */
	ReturnModel editTiPromotersAddress(Long promoteruserid,
			String streetdetail, String name, String phone);
	/**
	 * 修改代理商收货地址
	 * @param agentUserId
	 * @param streetdetail
	 * @param name
	 * @param phone
	 * @return
	 */
	ReturnModel editTiAgentsAddress(Long agentUserId, String streetdetail,
			String name, String phone);
	
	/**
	 * 机器列表
	 * @return
	 */
	ReturnModel findMachinemodelList();
	
	/**
	 * 得到生产商的机型
	 * @param producerUserId
	 * @return
	 */
	ReturnModel findMachineListByProducerUserId(Long producerUserId);
	/**
	 * 生产商产品生产区域设置
	 * @param producerUserId
	 * @param productId
	 * @param arealist
	 * @return
	 */
	ReturnModel setProducerProductAera(Long producerUserId, Long productId,
			List<RAreaVo> arealist);
	
}
