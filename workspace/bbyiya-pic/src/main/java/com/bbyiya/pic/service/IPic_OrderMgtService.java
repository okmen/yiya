package com.bbyiya.pic.service;

import java.util.List;

import com.bbyiya.pic.vo.order.SearchOrderParam;
import com.bbyiya.pic.vo.order.UserOrderResultVO;
import com.bbyiya.vo.ReturnModel;

public interface IPic_OrderMgtService {

	/**
	 * 查询订单列表（CTS用）
	 * 
	 * @param param
	 * @return
	 */
	ReturnModel find_orderList(SearchOrderParam param);

	/**
	 * 获取待分配的订单（IBS用）
	 * @param branchUserId
	 * @return
	 */
	ReturnModel findAgentOrders(Long branchUserId);
	
	/**
	 * IBS 我要这个客户（针对影楼合作伙伴的功能）
	 * @param branchUserId
	 * @param userOrderId
	 * @return
	 */
	ReturnModel addCustomer(Long branchUserId, String userOrderId);
	/**
	 * 本店订单列表 （IBS影楼用）
	 * @param branchUserId
	 * @param status
	 * @return
	 */
	ReturnModel findMyOrderlist(Long branchUserId,Integer status);
	/**
	 * 
	 * @param userOrderId
	 */
	ReturnModel getOrderDetail(String userOrderId);

	void downloadImg(List<UserOrderResultVO> orderlist, String basePath);
	/**
	 * 取消订单
	 * @param orderId
	 * @return
	 */
	ReturnModel cancelOrder(String orderId);
}
