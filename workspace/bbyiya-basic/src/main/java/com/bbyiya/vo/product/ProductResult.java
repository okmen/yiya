package com.bbyiya.vo.product;

import java.io.Serializable;
import java.util.List;


public class ProductResult implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long productId;
	private String title;
	private Long branchUserId;
	private String defaultImg;
	private List<String> desImgs;
	private List<ProductStandardResult> propertyList;
	private List<PProductStyleResult> styleslist;
	
	public List<PProductStyleResult> getStyleslist() {
		return styleslist;
	}
	public void setStyleslist(List<PProductStyleResult> styleslist) {
		this.styleslist = styleslist;
	}
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
	public List<ProductStandardResult> getPropertyList() {
		return propertyList;
	}
	public void setPropertyList(List<ProductStandardResult> propertyList) {
		this.propertyList = propertyList;
	}
	public String getDefaultImg() {
		return defaultImg;
	}
	public void setDefaultImg(String defaultImg) {
		this.defaultImg = defaultImg;
	}
	public List<String> getDesImgs() {
		return desImgs;
	}
	public void setDesImgs(List<String> desImgs) {
		this.desImgs = desImgs;
	}
	
	
}
