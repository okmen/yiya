package com.bbyiya.pic.vo.order;

import java.io.Serializable;

public class OrderPhotoParam implements Serializable{
	private static final long serialVersionUID = 1L;
	//Õº∆¨µÿ÷∑
	private String imageUrl;
	private String printNo;
	private String backImageUrl;
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getPrintNo() {
		return printNo;
	}
	public void setPrintNo(String printNo) {
		this.printNo = printNo;
	}
	public String getBackImageUrl() {
		return backImageUrl;
	}
	public void setBackImageUrl(String backImageUrl) {
		this.backImageUrl = backImageUrl;
	}
	
	
}
