package com.bbyiya.pic.dao;

import java.util.List;









import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.OOrderproductdetails;
import com.bbyiya.model.OUserorders;
import com.bbyiya.pic.vo.order.PbsUserOrderResultVO;
import com.bbyiya.pic.vo.order.SearchOrderParam;
import com.bbyiya.pic.vo.order.UserOrderResultVO;

public interface IPic_OrderMgtDao {

	List<UserOrderResultVO> findUserOrders(SearchOrderParam param);
	/**
	 * 获取订单作品列表
	 * @param orderproductId
	 * @return 
	 */ 
	List<OOrderproductdetails> findOrderProductDetailsByProductOrderId(@Param("orderProductId")String orderproductId);
	
	/**
	 * 获取Pbs订单列表
	 * @param param
	 * @return
	 */
	List<PbsUserOrderResultVO> findPbsUserOrders(SearchOrderParam param);
	
	/**
	 * 根据运单号查找订单信息
	 * @param expressOrder
	 * @return
	 */
	List<OUserorders> findUserOrderByExpressOrder(@Param("expressOrder") String expressOrder,@Param("expressCom")String expressCom);
}
