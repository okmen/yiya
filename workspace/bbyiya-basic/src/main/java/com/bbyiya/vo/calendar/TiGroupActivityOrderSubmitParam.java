package com.bbyiya.vo.calendar;

public class TiGroupActivityOrderSubmitParam {
	private Long workId;
	private int count;
	private Long submitUserId;
	private Long orderAddressId;
	private String remark;
	public Long getWorkId() {
		return workId;
	}
	public Long getSubmitUserId() {
		return submitUserId;
	}
	public Long getOrderAddressId() {
		return orderAddressId;
	}
	public String getRemark() {
		return remark;
	}
	public void setWorkId(Long workId) {
		this.workId = workId;
	}
	public void setSubmitUserId(Long submitUserId) {
		this.submitUserId = submitUserId;
	}
	public void setOrderAddressId(Long orderAddressId) {
		this.orderAddressId = orderAddressId;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
}
