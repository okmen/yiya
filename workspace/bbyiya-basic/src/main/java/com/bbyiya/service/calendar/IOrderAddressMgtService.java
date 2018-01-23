package com.bbyiya.service.calendar;

import com.bbyiya.model.OOrderaddress;
import com.bbyiya.vo.address.OrderaddressParam;

public interface IOrderAddressMgtService {

	/**
	 * 根据用户地址userAddressId 新增订单收货地址Id 
	 * @param userAddrId
	 * @return 订单收货地址Id (orderAddressId)
	 */
	 long addOrderAddressReturnOrderAddressId(Long userAddressId);
	 /**
	  * 根据用户地址userAddressId 新增订单收货地址Id 
	  * @param userAddressId
	  * @return 订单收货地址model(OOrderaddress)
	  */
	 OOrderaddress addOrderAddressReturnOOrderaddressModel(Long userAddressId) ;
	 /**
	  * 根据promoterUser的默认地址 新增订单收货地址
	  * @param promoterUserId
	  * @return 订单收货地址model
	  */
	 OOrderaddress addOrderAddressReturnOOrderaddressModel_promoterAddress(Long promoterUserId);
	 /**
	  * 
	  * @param addressParam
	  * @return
	  */
	 OOrderaddress addOrderAddress(OrderaddressParam addressParam);
	 /**
	  * 获取生产商userId
	  * @param districtcode 区code
	  * @param productId
	  * @return 生产商userid
	  */
	 long getProducerUserId(Integer districtcode,Integer cityCode,Integer provinceCode, Long productId);
	 /**
	  * 根据收货地址获取生产商userID
	  * @param orderAddressId
	  * @param productId
	  * @return
	  */
	 long getProducerUserIdByOrderAddressId(Long orderAddressId, Long productId);
}
