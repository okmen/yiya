package com.bbyiya.pic.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.pic.dao.IPic_OrderMgtDao;
import com.bbyiya.pic.service.IPic_OrderMgtService;
import com.bbyiya.pic.vo.order.SearchOrderParam;
import com.bbyiya.pic.vo.order.UserOrderResultVO;
import com.bbyiya.vo.ReturnModel;

@Service("pic_orderMgtService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Pic_OrderMgtServiceImpl implements IPic_OrderMgtService{
	
	@Autowired
	private OUserordersMapper userOrdersMapper;
	@Autowired
	private IPic_OrderMgtDao orderDao;
	/**
	 * 获取订单列表
	 * @param userId
	 * @return
	 */
	public ReturnModel find_orderList(Long userId){
		ReturnModel rq=new ReturnModel();
		return rq;
	}
	
	public ReturnModel find_orderList(SearchOrderParam param){
		ReturnModel rq=new ReturnModel();
		if(param==null)
			param=new SearchOrderParam();
		List<UserOrderResultVO> list=orderDao.findUserOrders(param);
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(list); 
		return rq;
	}
	
}
