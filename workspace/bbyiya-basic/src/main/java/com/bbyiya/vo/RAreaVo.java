package com.bbyiya.vo;

import java.io.Serializable;

public class RAreaVo  implements Serializable {
	private static final long serialVersionUID = 1L;
    private Integer areacode;
    private Integer citycode;
    private Integer provincecode;
	public Integer getAreacode() {
		return areacode;
	}
	public Integer getCitycode() {
		return citycode;
	}
	public Integer getProvincecode() {
		return provincecode;
	}
	public void setAreacode(Integer areacode) {
		this.areacode = areacode;
	}
	public void setCitycode(Integer citycode) {
		this.citycode = citycode;
	}
	public void setProvincecode(Integer provincecode) {
		this.provincecode = provincecode;
	}
    
    
    
}