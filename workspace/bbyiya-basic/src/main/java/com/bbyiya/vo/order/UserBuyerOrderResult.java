package com.bbyiya.vo.order;

import java.io.Serializable;
import com.bbyiya.model.OOrderaddress;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.model.OUserorders;

public class UserBuyerOrderResult extends OUserorders implements Serializable {
	private static final long serialVersionUID = 1L;
	private OOrderproducts product;
	
	private OOrderaddress orderAddress;
	private String workTitle;//作品标题
	private String ordertimestr;
	private String paytimestr;
	private String uploadtimestr;
	
	
	public String getWorkTitle() {
		return workTitle;
	}

	public void setWorkTitle(String workTitle) {
		this.workTitle = workTitle;
	}

	public OOrderproducts getProduct() {
		return product;
	}

	public void setProduct(OOrderproducts product) {
		this.product = product;
	}

	public OOrderaddress getOrderAddress() {
		return orderAddress;
	}

	public void setOrderAddress(OOrderaddress orderAddress) {
		this.orderAddress = orderAddress;
	}

	public String getOrdertimestr() {
		return ordertimestr;
	}

	public void setOrdertimestr(String ordertimestr) {
		this.ordertimestr = ordertimestr;
	}

	public String getPaytimestr() {
		return paytimestr;
	}

	public void setPaytimestr(String paytimestr) {
		this.paytimestr = paytimestr;
	}

	public String getUploadtimestr() {
		return uploadtimestr;
	}

	public void setUploadtimestr(String uploadtimestr) {
		this.uploadtimestr = uploadtimestr;
	}
	
	

	
	
}
