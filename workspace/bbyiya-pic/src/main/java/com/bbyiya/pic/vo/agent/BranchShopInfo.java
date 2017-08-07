package com.bbyiya.pic.vo.agent;

import java.io.Serializable;

public class BranchShopInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String logo;
	private String promotionStr;
	private int mobileBind;
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getPromotionStr() {
		return promotionStr;
	}
	public void setPromotionStr(String promotionStr) {
		this.promotionStr = promotionStr;
	}
	public int getMobileBind() {
		return mobileBind;
	}
	public void setMobileBind(int mobileBind) {
		this.mobileBind = mobileBind;
	}
	
	

}
