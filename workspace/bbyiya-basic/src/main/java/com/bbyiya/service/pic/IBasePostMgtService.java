package com.bbyiya.service.pic;

import java.util.List;

import com.bbyiya.model.PPostmodel;

public interface IBasePostMgtService {

	/**
	 * 列出快递方式 以及快递费（供用户选择）
	 * zy（17-03-30）
	 * @param area （收货区域）
	 * @return
	 */
	List<PPostmodel> find_postlist(Integer area) ;
	/**
	 * 通过 快递方式Id,areaId 确定快递费
	 * zy（17-03-30）
	 * @param postModelId 快递方式
	 * @param areaId 地区id
	 * @return
	 */
	PPostmodel getPostmodel(Integer postModelId,Integer areaId);
}
