package com.bbyiya.common.vo.wechatmsg;

public class ShippingParamNew {
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
	
//	/**
//	 * 是否寄到影楼
//	 */
//	private int isPromoterAddress;
	/**
	 * 备注信息
	 */
	private String remark;
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
//	public int getIsPromoterAddress() {
//		return isPromoterAddress;
//	}
//	public void setIsPromoterAddress(int isPromoterAddress) {
//		this.isPromoterAddress = isPromoterAddress;
//	}
	
	
}
