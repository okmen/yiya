package com.bbyiya.pic.service.cts;

import com.bbyiya.vo.order.SearchOrderParam;
import com.bbyiya.vo.order.UserOrderResultVO;
import com.github.pagehelper.PageInfo;

public interface ICts_OrderMgtService {

	/**
	 * cts订单列表查询
	 * @param param
	 * @param index
	 * @param size
	 * @return
	 */
	PageInfo<UserOrderResultVO> find_ctsorderList(SearchOrderParam param, int index, int size);
	
	
}
