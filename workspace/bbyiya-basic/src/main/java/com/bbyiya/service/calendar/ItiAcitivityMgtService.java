package com.bbyiya.service.calendar;

import com.bbyiya.vo.ReturnModel;

public interface ItiAcitivityMgtService {
	/**
	 * 更新领取名额
	 * @param actId
	 */
	void updateActivitylimitCountByActId(Integer actId);
	/**
	 * 活动时间到，自动下单、自动名额失效
	 */
	void timeToSubmitOrders();
	/**
	 * 直接下单
	 * @param userId
	 * @param workId
	 * @return
	 */
	ReturnModel timeToSubmitOrder(long userId,long workId);
	/**
	 * 作废活动
	 * @param workId
	 * @return
	 */
	ReturnModel updateActivityWorkTofailse(long userId, long workId);
	/**
	 * 是名额有效
	 * @param userId
	 * @param workId
	 * @return
	 */
	ReturnModel invokeActivityWorkStatus(long userId,long workId);
}
