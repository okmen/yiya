package com.bbyiya.service;

import java.util.List;

import com.bbyiya.model.SMusicrecommend;

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
	List<SMusicrecommend> find_SMusicrecommend(Long userId);
}
