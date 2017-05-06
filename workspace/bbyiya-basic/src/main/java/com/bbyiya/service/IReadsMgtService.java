package com.bbyiya.service;

import java.util.List;

import com.bbyiya.model.SReadstypes;
import com.bbyiya.vo.reads.ReadsResult;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.github.pagehelper.PageInfo;

public interface IReadsMgtService {
	/**
	 * 获取每日推荐读物
	 * @param user
	 * @return
	 */
	List<ReadsResult> find_DailyReadResultlist(LoginSuccessResult user);

	/**
	 * 读物分类列表 
	 * @return
	 */
	List<SReadstypes> find_SReadstypeslist();
	
	 PageInfo<ReadsResult> find_SReadsPageInfo(int readType,int index,int size);
}
