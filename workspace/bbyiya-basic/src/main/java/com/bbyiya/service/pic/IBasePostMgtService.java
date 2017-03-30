package com.bbyiya.service.pic;

import java.util.List;

import com.bbyiya.model.PPostmodel;
import com.bbyiya.vo.ReturnModel;

public interface IBasePostMgtService {
	
	/**
	 * 下单页用-根据收货地址获取邮费信息
	 * zy
	 * v1.0
	 * @param addressId
	 * @return
	 */
	ReturnModel find_postagelist(Long addressId);
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
