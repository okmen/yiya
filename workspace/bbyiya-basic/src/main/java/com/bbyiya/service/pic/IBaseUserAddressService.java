package com.bbyiya.service.pic;

import com.bbyiya.model.UUseraddress;
import com.bbyiya.vo.ReturnModel;

public interface IBaseUserAddressService {
	/**
	 * 用户编辑、新增收货地址
	 * @param address
	 * @return
	 */
	ReturnModel addOrEdit_UserAddress(UUseraddress address);
}
