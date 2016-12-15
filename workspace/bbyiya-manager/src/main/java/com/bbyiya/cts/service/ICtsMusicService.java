package com.bbyiya.cts.service;

import com.bbyiya.cts.vo.MusicAddParam;
import com.bbyiya.model.SMusics;
import com.bbyiya.vo.ReturnModel;
import com.github.pagehelper.Page;

public interface ICtsMusicService {
	/**
	 * 添加音乐到乐库
	 * @param model
	 * @return
	 */
	ReturnModel addOrEdit_SMusics(SMusics model);
	/**
	 * 获取乐库音乐列表（带分页）
	 * @param param 查询参数
	 * @param pageIndex 当前页
	 * @param pageSize 每页条数
	 * @return 
	 */
	Page<SMusics> find_SMusicsResult(MusicAddParam param,int pageIndex,int pageSize);
	
}
