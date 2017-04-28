package com.bbyiya.vo.product;

import java.io.Serializable;

public class StyleProperty implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long styleId;
	private Long productId;
	private Long standardId;
	private Long standardValueId;
	private String standardName; 
	private String standardValue;
	public Long getStyleId() {
		return styleId;
	}
	public void setStyleId(Long styleId) {
		this.styleId = styleId;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Long getStandardId() {
		return standardId;
	}
	public void setStandardId(Long standardId) {
		this.standardId = standardId;
	}
	public Long getStandardValueId() {
		return standardValueId;
	}
	public void setStandardValueId(Long standardValueId) {
		this.standardValueId = standardValueId;
	}
	public String getStandardName() {
		return standardName;
	}
	public void setStandardName(String standardName) {
		this.standardName = standardName;
	}
	public String getStandardValue() {
		return standardValue;
	}
	public void setStandardValue(String standardValue) {
		this.standardValue = standardValue;
	}
	
	
	
	

}
