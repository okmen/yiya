package com.bbyiya.vo.product;

public class ProductSearchParam {
	
	private String title;
	private Long productId; 
	private Long styleid; 
	private String propertystr; 
	private Integer status;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Long getStyleid() {
		return styleid;
	}
	public void setStyleid(Long styleid) {
		this.styleid = styleid;
	}
	public String getPropertystr() {
		return propertystr;
	}
	public void setPropertystr(String propertystr) {
		this.propertystr = propertystr;
	}
	public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
	
	
	
}
