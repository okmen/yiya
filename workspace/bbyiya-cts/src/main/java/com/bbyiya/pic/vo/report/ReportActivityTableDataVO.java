package com.bbyiya.pic.vo.report;


public class ReportActivityTableDataVO {
	private static final long serialVersionUID = 1L;
	private String dateTime;
	private Integer applyCount;
	private Integer completeCount;
	public String getDateTime() {
		return dateTime;
	}
	public Integer getApplyCount() {
		return applyCount;
	}
	public Integer getCompleteCount() {
		return completeCount;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public void setApplyCount(Integer applyCount) {
		this.applyCount = applyCount;
	}
	public void setCompleteCount(Integer completeCount) {
		this.completeCount = completeCount;
	}
	
	
	

}
