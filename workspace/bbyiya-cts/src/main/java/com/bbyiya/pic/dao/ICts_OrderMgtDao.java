package com.bbyiya.pic.dao;

import java.util.List;

import com.bbyiya.vo.order.SearchOrderParam;
import com.bbyiya.vo.order.UserOrderResultVO;


public interface ICts_OrderMgtDao {

	
	List<UserOrderResultVO>find_CtsUserOrders(SearchOrderParam param);
}
