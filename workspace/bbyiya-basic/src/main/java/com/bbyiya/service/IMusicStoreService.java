package com.bbyiya.service;

import java.util.List;

import com.bbyiya.model.SMusicrecommend;
import com.bbyiya.vo.music.DailyMusicResult;
import com.bbyiya.vo.user.LoginSuccessResult;

/**
 * 音乐库
 * @author Administrator
 *
 */
public interface IMusicStoreService {

	/**
	 * 根据用户userID获取每日推荐音乐
	 * @param userId
	 * @return
	 */
	List<SMusicrecommend> find_SMusicrecommend(LoginSuccessResult user);
	/**
	 * app 首页每日音乐推荐
	 * @param user
	 * @return
	 */
	List<DailyMusicResult> find_dailyMusiclist(LoginSuccessResult user);
}
