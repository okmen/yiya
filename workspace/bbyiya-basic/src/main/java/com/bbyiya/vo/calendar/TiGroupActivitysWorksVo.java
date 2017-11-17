package com.bbyiya.vo.calendar;

import com.bbyiya.model.TiGroupactivityworks;


public class TiGroupActivitysWorksVo extends TiGroupactivityworks{

	private String submittimestr;
	private String userorderid;
	private Double postage;//邮费
	
	public String getSubmittimestr() {
		return submittimestr;
	}
	public void setSubmittimestr(String submittimestr) {
		this.submittimestr = submittimestr;
	}
	public String getUserorderid() {
		return userorderid;
	}
	public Double getPostage() {
		return postage;
	}
	
	public void setUserorderid(String userorderid) {
		this.userorderid = userorderid;
	}
	public void setPostage(Double postage) {
		this.postage = postage;
	}

	
	
	
	
	
	
}
