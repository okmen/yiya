package com.bbyiya.vo.product;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.sdicons.json.validator.impl.predicates.Str;


public class SecondStandard  implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long  standardId;
    private String standardname;
    private Long styleId;
    private Double price;
    private List<String> detailImgs;
    
//    private List<Map<String, String>> backgroundImgs; 
    

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
	public List<String> getDetailImgs() {
		return detailImgs;
	}
	public void setDetailImgs(List<String> detailImgs) {
		this.detailImgs = detailImgs;
	}
	
	
    
}
