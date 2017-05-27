package com.bbyiya.pic.vo.product;

import com.bbyiya.model.PMyproducttemp;
import com.bbyiya.model.PMyproducttempapply;

public class MyProductTempVo {
	private PMyproducttemp temp;
//	private PMyproducttempapply apply;
	private int applyStatus;
	private int isInvited;
	private Long cartId;
	private String reason;
	
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
//	public PMyproducttempapply getApply() {
//		return apply;
//	}
//	public void setApply(PMyproducttempapply apply) {
//		this.apply = apply;
//	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
	
}
