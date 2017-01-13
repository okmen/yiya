package com.bbyiya.service.pic;


import java.util.List;

import com.bbyiya.model.OOrderproductdetails;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.vo.ReturnModel;

public interface IBaseOrderMgtService {

	
//	ReturnModel submitOrder(Long userId, Long addrId, String remark, List<OOrderproducts> prolist);
	/**
	 * 提交订单（针对单个分销商 ）
	 * @param userId
	 * @param addrId
	 * @param remark
	 * @param prolist
	 * @return
	 */
	ReturnModel submitOrder_singleProduct(Long userId, Long addrId, String remark, OOrderproducts product);
	/**
	 * 去支付
	 */
	ReturnModel getPayOrderByOrderId(String orderId);
	/**
	 * 保存订单图片，修改订单状态为 待发货
	 * @param orderId
	 * @param imagelist
	 * @return
	 */
	ReturnModel saveOrderImages(String orderId,List<OOrderproductdetails> imagelist);
	/**
	 * 获取用户购买订单列表
	 * @param userId
	 * @return
	 */
	ReturnModel findOrderlist(Long userId);
	
	/**
	 * 支付订单成功处理
	 * @param payId
	 * @return
	 */
	boolean paySuccessProcess(String payId);
	
	String getStylePropertyStr(Long styleId);
	
	
}
