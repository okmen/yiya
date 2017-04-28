package com.bbyiya.vo.user;

import com.bbyiya.model.UUseraddress;

public class UUserAddressResult extends UUseraddress{

	private static final long serialVersionUID = 1L;
	
	private String provinceName;
	private String cityName;
	private String areaName;
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	
	
}
