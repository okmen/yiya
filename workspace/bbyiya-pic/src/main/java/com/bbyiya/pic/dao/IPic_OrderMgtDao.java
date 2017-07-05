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
	 *  获取订单作品列表
	 * @param orderproductId
	 * @return 
	 */ 
	List<OOrderproductdetails> findOrderProductDetailsByProductOrderId(@Param("orderProductId")String orderproductId);
	
	/**
	 * 获取Pbs订单列 表  
	 * @param param
	 * @return
	 */
	List<PbsUserOrderResultVO> findPbsUserOrders(SearchOrderParam param);
	
	/**
	 * 根据运单号查找订单信息  
	 * @param expressOrder
	 * @author julie
	 * @return
	 */
	List<OUserorders> findUserOrderByExpressOrder(@Param("expressOrder") String expressOrder,@Param("expressCom")String expressCom);
	/**
	 * 根据作品ID得到订单
	 * @param cartid
	 * @return
	 */
	List<OUserorders> findOrderListByCartId(@Param("cartid") Long cartid);
	/**
	 * 根据作品ID得到订单
	 * @param cartid
	 * @return
	 */
	List<OUserorders> findNoPayOrderListByCartId(@Param("cartid") Long cartid);
	/**
	 * 根据作品ID及影楼ID得到订单
	 * @param cartid
	 * @return
	 */
	List<OUserorders> findOrderListByCartIdAndBranchUserID(@Param("cartid") Long cartid,@Param("branchuserid") Long branchuserid);
	
}
