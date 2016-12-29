package com.bbyiya.vo.product;

import java.io.Serializable;

public class SecondStandard  implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long  standardId;
    private String standardname;
    private Long styleId;
    private Double price;
	public Long getStandardId() {
		return standardId;
	}
	public void setStandardId(Long standardId) {
		this.standardId = standardId;
	}
	public String getStandardname() {
		return standardname;
	}
	public void setStandardname(String standardname) {
		this.standardname = standardname;
	}
	public Long getStyleId() {
		return styleId;
	}
	public void setStyleId(Long styleId) {
		this.styleId = styleId;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
    
}
