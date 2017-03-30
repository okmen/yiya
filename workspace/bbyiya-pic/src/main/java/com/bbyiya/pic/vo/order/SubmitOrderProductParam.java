package com.bbyiya.pic.vo.order;

import java.io.Serializable;

public class SubmitOrderProductParam implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Long productId;
	private Long  styleId;
	private Integer  count;
	private Long cartId;
	private Integer postModelId;
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
	
	
}
