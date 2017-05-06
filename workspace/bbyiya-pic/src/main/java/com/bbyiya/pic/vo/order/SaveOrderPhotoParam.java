package com.bbyiya.pic.vo.order;

import java.io.Serializable;
import java.util.List;

public class SaveOrderPhotoParam implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String orderId;
	private List<OrderPhotoParam> imageList;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public List<OrderPhotoParam> getImageList() {
		return imageList;
	}
	public void setImageList(List<OrderPhotoParam> imageList) {
		this.imageList = imageList;
	}
	
	
	
}
