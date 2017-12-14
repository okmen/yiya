package com.bbyiya.service.calendar;

import com.bbyiya.model.TiPromoteradvertinfo;
import com.bbyiya.model.TiPromoteradvertviewlogs;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.github.pagehelper.PageInfo;


public interface ITi_PromoterAdvertService {
	/**
	 * 新增浏览记录（广告的送达数，送达人次，用户记录log）
	 * @param user LoginSuccessResult 登录userInfo
	 * @param advertId
	 */
	void addViews(LoginSuccessResult user,int advertId);
	/**
	 * 新增广告点击记录（点击数，点击人的记录）
	 * @param user
	 * @param advertId
	 */
	void addClicks(LoginSuccessResult user,int advertId);
	/**
	 * 获取分享广告详情
	 * @param advertId
	 * @return
	 */
	TiPromoteradvertinfo getTiPromoteradvertinfo(int advertId);
	/**
	 * 获取广告的用户浏览记录
	 * @param advertId
	 * @param index
	 * @param size
	 * @return
	 */
	PageInfo<TiPromoteradvertviewlogs> findTiPromoteradvertviewlogsPage(int advertId,int index,int size);
}
