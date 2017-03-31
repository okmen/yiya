package com.bbyiya.vo.order;

import java.io.Serializable;

public class UserOrderSubmitRepeatParam implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String userOrderId;
	private int  count;
	private Long addrId;
	private String remark;
	private int orderType;
	private Double postage;
	private int postModelId;
	private Long branchUserId;
	private Long agentUserId;
	
	public String getUserOrderId() {
		return userOrderId;
	}
	public void setUserOrderId(String userOrderId) {
		this.userOrderId = userOrderId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Long getAddrId() {
		return addrId;
	}
	public void setAddrId(Long addrId) {
		this.addrId = addrId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getOrderType() {
		return orderType;
	}
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
	public Double getPostage() {
		return postage;
	}
	public void setPostage(Double postage) {
		this.postage = postage;
	}
	public int getPostModelId() {
		return postModelId;
	}
	public void setPostModelId(int postModelId) {
		this.postModelId = postModelId;
	}
	public Long getBranchUserId() {
		return branchUserId;
	}
	public void setBranchUserId(Long branchUserId) {
		this.branchUserId = branchUserId;
	}
	public Long getAgentUserId() {
		return agentUserId;
	}
	public void setAgentUserId(Long agentUserId) {
		this.agentUserId = agentUserId;
	}
	
	

}
