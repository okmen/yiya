package com.bbyiya.vo.product;

import java.io.Serializable;
import java.util.List;

public class ProductStandardResult implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long  standardId;

    private String standardName;
    
//    private List<String> backgroundImgs;

    private List<SecondStandard> subList;
    
    
    

    public Long getStandardId() {
		return standardId;
	}
	public void setStandardId(Long standardId) {
		this.standardId = standardId;
	}
	
	
	public String getStandardName() {
		return standardName;
	}
	public void setStandardName(String standardName) {
		this.standardName = standardName;
	}
	public List<SecondStandard> getSubList() {
		return subList;
	}
	public void setSubList(List<SecondStandard> subList) {
		this.subList = subList;
	}
	



}
