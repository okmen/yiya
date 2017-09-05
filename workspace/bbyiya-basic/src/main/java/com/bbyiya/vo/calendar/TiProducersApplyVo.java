package com.bbyiya.vo.calendar;
import java.util.List;

import com.bbyiya.model.TiProducerapplymachines;
import com.bbyiya.model.TiProducersapply;

public class TiProducersApplyVo extends TiProducersapply{
	private String provinceName;
	private String cityName;
	private String areaName;
	private String bindphone;
	private List<TiProducerapplymachines> machines;
	private Double availableAmount; //可用余额
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
	public List<TiProducerapplymachines> getMachines() {
		return machines;
	}
	public void setMachines(List<TiProducerapplymachines> machines) {
		this.machines = machines;
	}
	public Double getAvailableAmount() {
		return availableAmount;
	}
	public void setAvailableAmount(Double availableAmount) {
		this.availableAmount = availableAmount;
	}
	
	
	
}
