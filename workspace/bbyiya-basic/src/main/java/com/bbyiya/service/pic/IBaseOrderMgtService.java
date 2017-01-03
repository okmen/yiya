package com.bbyiya.service.pic;

import java.util.List;

import com.bbyiya.model.OOrderproducts;
import com.bbyiya.vo.ReturnModel;

public interface IBaseOrderMgtService {

	/**
	 * 提交订单（针对单个分销商 ）
	 * @param userId
	 * @param addrId
	 * @param remark
	 * @param prolist
	 * @return
	 */
	ReturnModel submitOrder(Long userId, Long addrId, String remark, List<OOrderproducts> prolist);
	
	ReturnModel submitOrder_singleProduct(Long userId, Long addrId, String remark, OOrderproducts product);
}
