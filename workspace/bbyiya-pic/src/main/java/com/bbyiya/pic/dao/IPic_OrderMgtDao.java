package com.bbyiya.pic.dao;

import java.util.List;








import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.OOrderproductdetails;
import com.bbyiya.pic.vo.order.PbsSearchOrderParam;
import com.bbyiya.pic.vo.order.PbsUserOrderResultVO;
import com.bbyiya.pic.vo.order.SearchOrderParam;
import com.bbyiya.pic.vo.order.UserOrderResultVO;

public interface IPic_OrderMgtDao {

	List<UserOrderResultVO> findUserOrders(SearchOrderParam param);
	/**
	 * ��ȡ������Ʒ�б�
	 * @param orderproductId
	 * @return
	 */
	List<OOrderproductdetails> findOrderProductDetailsByProductOrderId(@Param("orderProductId")String orderproductId);
	
	/**
	 * ��ȡPbs�����б�
	 * @param param
	 * @return
	 */
	List<PbsUserOrderResultVO> findPbsUserOrders(PbsSearchOrderParam param);
}
