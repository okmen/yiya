package com.bbyiya.vo.order;

import java.io.Serializable;

import com.bbyiya.model.OOrderproducts;

public class UserOrderSubmitParam implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long userId;
	private Integer orderType;
	private Long addrId;
	private Long orderAddressId;
	private String remark;
	private Long branchUserId;
	private Long agentUserId;
	private Long cartId;
	private Integer postModelId;
	private Double postPrice;
	private OOrderproducts orderproducts;
	
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Integer getOrderType() {
		return orderType;
	}
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	
	public Long getAddrId() {
		return addrId;
	}
	public void setAddrId(Long addrId) {
		this.addrId = addrId;
	}
	public OOrderproducts getOrderproducts() {
		return orderproducts;
	}
	public void setOrderproducts(OOrderproducts orderproducts) {
		this.orderproducts = orderproducts;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public Long getOrderAddressId() {
		return orderAddressId;
	}
	public void setOrderAddressId(Long orderAddressId) {
		this.orderAddressId = orderAddressId;
	}
	public Long getCartId() {
		return cartId;
	}
	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}
	public Integer getPostModelId() {
		return postModelId;
	}
	public void setPostModelId(Integer postModelId) {
		this.postModelId = postModelId;
	}
	public Double getPostPrice() {
		return postPrice;
	}
	public void setPostPrice(Double postPrice) {
		this.postPrice = postPrice;
	}
	
	
	
}
