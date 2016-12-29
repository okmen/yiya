package com.bbyiya.vo.product;

import java.io.Serializable;

public class ProductResult implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long productId;
	private String title;
	private Long branchUserId;
	
	
	
	
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Long getBranchUserId() {
		return branchUserId;
	}
	public void setBranchUserId(Long branchUserId) {
		this.branchUserId = branchUserId;
	}
	
	
}
