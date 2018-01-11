package com.bbyiya.pic.vo.calendar;

import java.util.List;

import com.bbyiya.model.TiMyartsdetails;

public class MyworkDetailsParam {
	private Long workId;
	private String orderproductId;
	private Long styleId;
	private List<TiMyartsdetails> details;
	public Long getWorkId() {
		return workId;
	}
	public void setWorkId(Long workId) {
		this.workId = workId;
	}
	public List<TiMyartsdetails> getDetails() {
		return details;
	}
	public void setDetails(List<TiMyartsdetails> details) {
		this.details = details;
	}
	public String getOrderproductId() {
		return orderproductId;
	}
	public void setOrderproductId(String orderproductId) {
		this.orderproductId = orderproductId;
	}
	public Long getStyleId() {
		return styleId;
	}
	public void setStyleId(Long styleId) {
		this.styleId = styleId;
	}
	
	
}
