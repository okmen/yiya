package com.bbyiya.common.vo.wechatmsg;

public class ShippingParam {
	/**
	 * 想要跳转的页面地址
	 */
	private String linkUrl;
	/**
	 * 订单编号
	 */
	private String orderId;
	/**
	 * 物流公司
	 */
	private String transCompany;
	/**
	 * 物流单号
	 */
	private String transOrderId;
	/**
	 * 订单价格
	 */
	private Double totalPrice;
	/**
	 * 收货地址
	 */
	private String address;
	/**
	 * 备注信息
	 */
	private String remark;
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getTransCompany() {
		return transCompany;
	}
	public void setTransCompany(String transCompany) {
		this.transCompany = transCompany;
	}
	public String getTransOrderId() {
		return transOrderId;
	}
	public void setTransOrderId(String transOrderId) {
		this.transOrderId = transOrderId;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	
	
}
