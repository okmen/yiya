package com.bbyiya.service;

import java.util.List;

import com.bbyiya.model.SMusicrecommend;
import com.bbyiya.model.SMusicttype;
import com.bbyiya.vo.music.MusicResult;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.github.pagehelper.PageInfo;

/**
 * 音乐库
 * 
 * @author Administrator
 *
 */
public interface IMusicStoreService {

	/**
	 * 根据用户userID获取每日推荐音乐
	 * 
	 * @param userId
	 * @return
	 */
	List<SMusicrecommend> find_SMusicrecommend(LoginSuccessResult user);

	/**
	 * app 首页每日音乐推荐
	 * 
	 * @param user
	 * @return
	 */
	List<MusicResult> find_dailyMusiclist(LoginSuccessResult user);

	/**
	 * 获取乐库列表（按照分类typeId）
	 * 
	 * @param typeId 音乐分类Id
	 * @param index
	 * @param size
	 * @return
	 */
	PageInfo<MusicResult> find_MusicResult(int typeId, int index, int size);

	/**
	 * 获取音乐类型列表
	 * 
	 * @return
	 */
	List<SMusicttype> fint_SMusicttypelist();
}
