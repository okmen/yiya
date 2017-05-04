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
	 *  ��ȡ������Ʒ�б�
	 * @param orderproductId
	 * @return 
	 */ 
	List<OOrderproductdetails> findOrderProductDetailsByProductOrderId(@Param("orderProductId")String orderproductId);
	
	/**
	 * ��ȡPbs������ ��  
	 * @param param
	 * @return
	 */
	List<PbsUserOrderResultVO> findPbsUserOrders(SearchOrderParam param);
	
	/**
	 * �����˵��Ų��Ҷ�����Ϣ  
	 * @param expressOrder
	 * @author julie
	 * @return
	 */
	List<OUserorders> findUserOrderByExpressOrder(@Param("expressOrder") String expressOrder,@Param("expressCom")String expressCom);
	/**
	 * ������ƷID�õ�����
	 * @param cartid
	 * @return
	 */
	List<OUserorders> findOrderListByCartId(@Param("cartid") Long cartid);
	/**
	 * ������ƷID�õ�δ֧����ͼƬδ�ϴ�����
	 * @param cartid
	 * @return
	 */
	List<OUserorders> findNoPayOrderListByCartId(@Param("cartid") Long cartid);
	
}
