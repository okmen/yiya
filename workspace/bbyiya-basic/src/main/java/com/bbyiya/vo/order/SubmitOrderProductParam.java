package com.bbyiya.vo.order;

import java.io.Serializable;

public class SubmitOrderProductParam implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Long productId;
	private Long  styleId;
	private Integer  count;
	private Long cartId;
	private Integer postModelId;
	
	private Long promoterUserId;
	private String contactName;
	private String phone;
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Long getStyleId() {
		return styleId;
	}
	public void setStyleId(Long styleId) {
		this.styleId = styleId;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Long getCartId() {
		return cartId;
	}
	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}
	public Integer getPostModelId() {
		return postModelId;
	}
	public void setPostModelId(Integer postModelId) {
		this.postModelId = postModelId;
	}
	public Long getPromoterUserId() {
		return promoterUserId;
	}
	public void setPromoterUserId(Long promoterUserId) {
		this.promoterUserId = promoterUserId;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
}
