package com.bbyiya.pic.service;

import com.bbyiya.vo.ReturnModel;

public interface IPic_BranchMgtService {

	/**
	 * 根据区域判断代理费用
	 * @param province
	 * @param city
	 * @param district
	 * @return
	 */
	ReturnModel getBranchAreaPrice(Integer province,Integer city,Integer district);
}
