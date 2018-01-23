package com.bbyiya.vo;

import java.io.Serializable;

public class RegionVo  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer code;
	private String codeName;
	private Integer step;
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	
	
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	public Integer getStep() {
		return step;
	}
	public void setStep(Integer step) {
		this.step = step;
	}
	
	
	
}
