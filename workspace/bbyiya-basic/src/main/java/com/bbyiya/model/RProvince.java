package com.bbyiya.model;

import java.io.Serializable;

public class RProvince implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer code;

	private String province;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province == null ? null : province.trim();
	}
}