package com.bbyiya.pic.service;

import java.util.List;

import com.bbyiya.model.OOrderproductdetails;
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
	ReturnModel findMyOrderlist(Long branchUserId,Integer ordertype,Integer status,String keywords,int index,int size);
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
	
	/**
	 * 得到订单产品的作品详情，用于重复下单的效果浏览	
	 * @param orderProductId
	 * @return
	 */
	List<OOrderproductdetails> getOrderProductdetails(String orderProductId);
	
	ReturnModel getOrderProductdetailsByUserOrderId(String userOrderId);
	/**
	 * 获取订单作品原图
	 */
	ReturnModel getOrderPhotos(String userOrderId);
	
	/**
	 * 获取订单作品图片列表
	 * @param userOrderId
	 * @return
	 */
	ReturnModel findOrderProductPhotosByUserOrderId(String userOrderId);
	/**
	 * pbs合成时订单图片信息
	 * @param userOrderId
	 * @return
	 */
	ReturnModel getOrderProductInfoByUserOrderId(String userOrderId);

}
