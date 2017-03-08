package com.bbyiya.pic.vo.agent;

import com.bbyiya.model.UBranches;

public class UBranchVo extends UBranches{
	private String proviceName;
	private String cityName;
	private String areaName;
	public String getProviceName() {
		return proviceName;
	}
	public void setProviceName(String proviceName) {
		this.proviceName = proviceName;
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
