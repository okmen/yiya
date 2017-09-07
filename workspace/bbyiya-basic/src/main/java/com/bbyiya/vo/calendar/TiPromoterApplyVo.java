package com.bbyiya.vo.calendar;
import com.bbyiya.model.TiPromotersapply;

public class TiPromoterApplyVo extends TiPromotersapply{
	private String provinceName;
	private String cityName;
	private String areaName;
	private String bindphone;
	private Double availableAmount; //可用余额
	private String agentName;//隶属的代理商名称
	public String getProvinceName() {
		return provinceName;
	}
	public String getCityName() {
		return cityName;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getBindphone() {
		return bindphone;
	}
	public void setBindphone(String bindphone) {
		this.bindphone = bindphone;
	}
	public Double getAvailableAmount() {
		return availableAmount;
	}
	public void setAvailableAmount(Double availableAmount) {
		this.availableAmount = availableAmount;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	
	
	
}
