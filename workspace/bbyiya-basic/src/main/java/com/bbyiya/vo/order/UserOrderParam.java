package com.bbyiya.vo.order;

import java.io.Serializable;
import java.util.List;

import com.bbyiya.model.OOrderproducts;

public class UserOrderParam implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long branchUserId;
	private Long agentUserId;
	private Long addrId;
	private Integer orderType;
	private String remark;
	private List<OOrderproducts> prolist;
	public Long getBranchUserId() {
		return branchUserId;
	}
	public void setBranchUserId(Long branchUserId) {
		this.branchUserId = branchUserId;
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
	public List<OOrderproducts> getProlist() {
		return prolist;
	}
	public void setProlist(List<OOrderproducts> prolist) {
		this.prolist = prolist;
	}
	public Integer getOrderType() {
		return orderType;
	}
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	public Long getAgentUserId() {
		return agentUserId;
	}
	public void setAgentUserId(Long agentUserId) {
		this.agentUserId = agentUserId;
	}
	
	
}
