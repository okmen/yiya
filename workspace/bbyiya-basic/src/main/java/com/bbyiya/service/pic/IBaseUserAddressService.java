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
	 * @param addressId
	 * @return
	 */
	UUserAddressResult getUserAddressResult(Long addressId);
}
