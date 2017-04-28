package com.bbyiya.pic.vo.agent;

import java.util.List;

import com.bbyiya.model.UAgentapply;

public class UAgentApplyVo extends UAgentapply{
	private String proviceName;
	private String cityName;
	private String areaName;
	private List<String> agentArealist;
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
	public List<String> getAgentArealist() {
		return agentArealist;
	}
	public void setAgentArealist(List<String> agentArealist) {
		this.agentArealist = agentArealist;
	}
	
	

}
