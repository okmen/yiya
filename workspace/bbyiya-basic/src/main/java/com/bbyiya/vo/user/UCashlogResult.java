package com.bbyiya.vo.user;

import com.bbyiya.model.UCashlogs;

public class UCashlogResult extends UCashlogs{

	private Long buyerUserId;
	private String buyerName;
	private String buyerPhone;
	public Long getBuyerUserId() {
		return buyerUserId;
	}
	public void setBuyerUserId(Long buyerUserId) {
		this.buyerUserId = buyerUserId;
	}
	public String getBuyerName() {
		return buyerName;
	}
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	public String getBuyerPhone() {
		return buyerPhone;
	}
	public void setBuyerPhone(String buyerPhone) {
		this.buyerPhone = buyerPhone;
	}
	
	
	
	
	
}
