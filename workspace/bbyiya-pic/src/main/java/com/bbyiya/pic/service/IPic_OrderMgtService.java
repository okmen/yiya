package com.bbyiya.pic.service;

import java.util.List;

import com.bbyiya.model.OOrderproductdetails;
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
	ReturnModel findMyOrderlist(Long branchUserId,Integer status,String keywords,int index,int size);
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
	
	/**
	 * �õ�������Ʒ����Ʒ���飬�����ظ��µ���Ч�����	
	 * @param orderProductId
	 * @return
	 */
	List<OOrderproductdetails> getOrderProductdetails(String orderProductId);
	
	ReturnModel getOrderProductdetailsByUserOrderId(String userOrderId);
	/**
	 * ��ȡ������Ʒԭͼ
	 */
	ReturnModel getOrderPhotos(String userOrderId);
	
	/**
	 * ��ȡ������ƷͼƬ�б�
	 * @param userOrderId
	 * @return
	 */
	ReturnModel findOrderProductPhotosByUserOrderId(String userOrderId);
	/**
	 * pbs�ϳ�ʱ����ͼƬ��Ϣ
	 * @param userOrderId
	 * @return
	 */
	ReturnModel getOrderProductInfoByUserOrderId(String userOrderId);

}
