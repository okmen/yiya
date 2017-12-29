package com.bbyiya.service.calendar;

import com.bbyiya.vo.ReturnModel;

public interface ItiAcitivityMgtService {
	/**
	 * 更新领取名额
	 * @param actId
	 */
	void updateActivitylimitCountByActId(Integer actId);
	/**
	 * 7天未完成分享的 置未名额失效
	 */
	void updateWorkTofailse();
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
