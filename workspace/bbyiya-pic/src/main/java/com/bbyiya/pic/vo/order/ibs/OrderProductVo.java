package com.bbyiya.pic.vo.order.ibs;

import java.io.Serializable;

public class OrderProductVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String producttitle;
	private Double price;
	private String propertystr;
	private Long cartid;
	private String cartTitle;
	private String cartAuthor;
	public String getProducttitle() {
		return producttitle;
	}
	public void setProducttitle(String producttitle) {
		this.producttitle = producttitle;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getPropertystr() {
		return propertystr;
	}
	public void setPropertystr(String propertystr) {
		this.propertystr = propertystr;
	}
	public Long getCartid() {
		return cartid;
	}
	public void setCartid(Long cartid) {
		this.cartid = cartid;
	}
	public String getCartTitle() {
		return cartTitle;
	}
	public void setCartTitle(String cartTitle) {
		this.cartTitle = cartTitle;
	}
	public String getCartAuthor() {
		return cartAuthor;
	}
	public void setCartAuthor(String cartAuthor) {
		this.cartAuthor = cartAuthor;
	}
	
	

}
