package com.bbyiya.pic.vo.product;

import java.util.List;

import com.bbyiya.model.DMyproductdiscountmodel;
import com.bbyiya.model.PMyproducttemp;
import com.bbyiya.model.PMyproducttempapply;

public class MyProductTempVo {
	private PMyproducttemp temp;
	private int applyStatus;
	private int isInvited;
	private Long cartId;
	private String reason;
	private List<DMyproductdiscountmodel> discountList;
	public int getIsInvited() {
		return isInvited;
	}
	public void setIsInvited(int isInvited) {
		this.isInvited = isInvited;
	}
	public Long getCartId() {
		return cartId;
	}
	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}
	public PMyproducttemp getTemp() {
		return temp;
	}
	public void setTemp(PMyproducttemp temp) {
		this.temp = temp;
	}
	public int getApplyStatus() {
		return applyStatus;
	}
	public void setApplyStatus(int applyStatus) {
		this.applyStatus = applyStatus;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public List<DMyproductdiscountmodel> getDiscountList() {
		return discountList;
	}
	public void setDiscountList(List<DMyproductdiscountmodel> discountList) {
		this.discountList = discountList;
	}
	
	
	
}
