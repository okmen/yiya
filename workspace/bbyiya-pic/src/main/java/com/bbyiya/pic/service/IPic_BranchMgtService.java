package com.bbyiya.pic.service;

import com.bbyiya.vo.ReturnModel;

public interface IPic_BranchMgtService {

	/**
	 * ���������жϴ������
	 * @param province
	 * @param city
	 * @param district
	 * @return
	 */
	ReturnModel getBranchAreaPrice(Integer province,Integer city,Integer district);
}
