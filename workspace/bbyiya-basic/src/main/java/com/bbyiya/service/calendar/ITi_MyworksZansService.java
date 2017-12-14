package com.bbyiya.service.calendar;

import java.util.List;

import com.bbyiya.model.TiMyworkszanlogs;
import com.bbyiya.vo.user.LoginSuccessResult;

public interface ITi_MyworksZansService {

	/**
	 * 点赞
	 * @param user
	 * @param workId
	 * @return
	 */
	boolean addZan(LoginSuccessResult user,long workId);
	/**
	 * 获取点赞列表
	 * @param workId
	 * @return
	 */
	List<TiMyworkszanlogs> findZansList(long workId);
}
