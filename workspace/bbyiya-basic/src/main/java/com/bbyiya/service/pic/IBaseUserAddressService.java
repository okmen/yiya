package com.bbyiya.service.pic;

import com.bbyiya.model.UUseraddress;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.UUserAddressResult;

public interface IBaseUserAddressService {
	/**
	 * 用户编辑、新增收货地址
	 * @param address
	 * @return
	 */
	ReturnModel addOrEdit_UserAddress(UUseraddress address);
	
	/**
	 * 编辑、新增收货地址 返回用户收货地址Id
	 * @param address
	 * @return
	 */
	ReturnModel addOrEdit_UserAddressReturnAddressId(UUseraddress address);
	
	/**
	 * 获取用户收货地址信息 
	 * @param userId 
	 * @param addressId(当 addressId=null 获取用户默认收货地址)
	 * @return
	 */
	UUserAddressResult getUserAddressResult(Long userId, Long addressId);
	
}
