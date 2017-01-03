package com.bbyiya.vo.order;

import java.io.Serializable;
import java.util.List;

import com.bbyiya.model.OOrderproducts;

public class UserOrderResult implements Serializable {
	private static final long serialVersionUID = 1L;

	private String userOrderId;
	
	private String payId;

	private Double totalprice;

	private Integer status;

	private Long branchUserId;

	private String remark;

	private List<OOrderproducts> prolist;

	public String getUserOrderId() {
		return userOrderId;
	}

	public void setUserOrderId(String userOrderId) {
		this.userOrderId = userOrderId;
	}

	public String getPayId() {
		return payId;
	}

	public void setPayId(String payId) {
		this.payId = payId;
	}

	public Double getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(Double totalprice) {
		this.totalprice = totalprice;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getBranchUserId() {
		return branchUserId;
	}

	public void setBranchUserId(Long branchUserId) {
		this.branchUserId = branchUserId;
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
	
	
}
