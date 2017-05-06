package com.bbyiya.service;


public interface IRegionService {

	/**
	 * 根据省市区编码获取 省市区名
	 * @param code
	 * @return
	 */
	String getName(Integer code);
	
}
