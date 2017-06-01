package com.bbyiya.service.pic;

import java.util.List;

import com.bbyiya.model.OOrderproductdetails;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.order.UserOrderSubmitParam;
import com.bbyiya.vo.order.UserOrderSubmitRepeatParam;

public interface IBaseOrderMgtService {

	/**
	 * 提交订单（针对单个分销商 ）
	 * v1.0.0 早期版本
	 * @param userId
	 * @param addrId
	 * @param remark
	 * @param prolist
	 * @return
	 */
	ReturnModel submitOrder_singleProduct(Long userId, Long addrId, String remark, OOrderproducts product);

	/**
	 * 提交订单 2017-3-16
	 * 支持普通下单，内部下单（直接扣款）
	 * @param param
	 * @return
	 */
	ReturnModel submitOrder_new(UserOrderSubmitParam param);
	/**
	 * 再次下单
	 * @param userId
	 * @param param
	 * @return
	 * @throws Exception
	 */
	ReturnModel submitOrder_repeat(Long userId, UserOrderSubmitRepeatParam param) throws Exception;

	/**
	 * 去支付
	 */
	ReturnModel getPayOrderByOrderId(String orderId);

	/**
	 * 保存订单图片，修改订单状态为 待发货
	 * 
	 * @param orderId
	 * @param imagelist
	 * @return
	 */
	ReturnModel saveOrderImages(String orderId, List<OOrderproductdetails> imagelist);

	/**
	 * 获取用户购买订单列表
	 * 
	 * @param userId
	 * @return
	 */
	ReturnModel findOrderlist(Long userId);

	/**
	 * 订单详情
	 * 
	 * @param userId
	 * @param orderId
	 * @return
	 */
	ReturnModel getOrderInfo(Long userId, String orderId);

	/**
	 * 支付订单成功处理
	 * 
	 * @param payId
	 * @return
	 */
//	boolean paySuccessProcess(String payId);
	
	/**
	 * 插入支付信息（账户充值 可用）
	 * @param userId
	 * @param payId
	 * @param userOrderId
	 * @param payOrderType
	 * @param totalPrice
	 * @return
	 */
	boolean addPayOrder(Long userId, String payId, String userOrderId, Integer payOrderType , Double totalPrice);


	
	/**
	 * 获取用户购买订单列表
	 * @param userId
	 * @return
	 */
	ReturnModel findUserOrderlist(Long userId,int index,int size);
	/**
	 * 选择地址下单
	 * @param param
	 * @return
	 */
	ReturnModel submitOrder_IBS(UserOrderSubmitParam param);

}
