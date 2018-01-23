package com.bbyiya.vo.calendar;
import com.bbyiya.model.TiAgentsapply;

public class TiAgentApplyVo extends TiAgentsapply{
	private String provinceName;
	private String cityName;
	private String areaName;
	private String bindphone;
	private Double availableAmount; //可用余额
	//推广链接地址
	private String promotionUrl;
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
	public String getPromotionUrl() {
		return promotionUrl;
	}
	public void setPromotionUrl(String promotionUrl) {
		this.promotionUrl = promotionUrl;
	}
	
	
	
}
