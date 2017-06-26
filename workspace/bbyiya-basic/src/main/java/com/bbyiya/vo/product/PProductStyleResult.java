package com.bbyiya.vo.product;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class PProductStyleResult implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long styleId;
	private Long productId;
	private String propertyStr;
	private Double price;
	private Double discountAmount;
	private String defaultImg;
	private String description;
	private List<String> detailImgs;
	private String productTitle;
	private Date createtime;
	private String createTimeStr;
	private Integer sellCount;
	
	public Long getStyleId() {
		return styleId;
	}
	public void setStyleId(Long styleId) {
		this.styleId = styleId;
	}
	public String getPropertyStr() {
		return propertyStr;
	}
	public void setPropertyStr(String propertyStr) {
		this.propertyStr = propertyStr;
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
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getDefaultImg() {
		return defaultImg;
	}
	public void setDefaultImg(String defaultImg) {
		this.defaultImg = defaultImg;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getProductTitle() {
		return productTitle;
	}
	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Integer getSellCount() {
		return sellCount;
	}
	public void setSellCount(Integer sellCount) {
		this.sellCount = sellCount;
	}
	public Double getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}
	
	
	

}
