package com.bbyiya.vo.order;


import java.util.List;

import com.bbyiya.model.OOrderaddress;
import com.bbyiya.model.OOrderproductdetails;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.model.OUserorders;

public class UserOrderResultVO extends OUserorders{
	private static final long serialVersionUID = 1L;
	
	private String payTimeStr;
	private List<OOrderproductdetails> imglist;
	private OOrderproducts orderproduct;
	private OOrderaddress address;
	
	public List<OOrderproductdetails> getImglist() {
		return imglist;
	}

	public void setImglist(List<OOrderproductdetails> imglist) {
		this.imglist = imglist;
	}

	public OOrderaddress getAddress() {
		return address;
	}

	public void setAddress(OOrderaddress address) {
		this.address = address;
	}

	public String getPayTimeStr() {
		return payTimeStr;
	}

	public void setPayTimeStr(String payTimeStr) {
		this.payTimeStr = payTimeStr;
	}

	public OOrderproducts getOrderproduct() {
		return orderproduct;
	}

	public void setOrderproduct(OOrderproducts orderproduct) {
		this.orderproduct = orderproduct;
	}

	

	
	

}
