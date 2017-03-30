package com.bbyiya.pic.service;

import java.util.List;

import com.bbyiya.pic.vo.order.SearchOrderParam;
import com.bbyiya.pic.vo.order.UserOrderResultVO;
import com.bbyiya.vo.ReturnModel;

public interface IPic_OrderMgtService {

	/**
	 * ��ѯ�����б�CTS�ã�
	 * 
	 * @param param
	 * @return
	 */
	ReturnModel find_orderList(SearchOrderParam param);

	/**
	 * ��ȡ������Ķ�����IBS�ã�
	 * @param branchUserId
	 * @return
	 */
	ReturnModel findAgentOrders(Long branchUserId);
	
	/**
	 * IBS ��Ҫ����ͻ������Ӱ¥�������Ĺ��ܣ�
	 * @param branchUserId
	 * @param userOrderId
	 * @return
	 */
	ReturnModel addCustomer(Long branchUserId, String userOrderId);
	/**
	 * ���궩���б� ��IBSӰ¥�ã�
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
	 * ȡ������
	 * @param orderId
	 * @return
	 */
	ReturnModel cancelOrder(String orderId);
}
