package com.bbyiya.vo.order;

import java.io.Serializable;
import java.util.List;

import com.bbyiya.model.OOrderaddress;
import com.bbyiya.model.OOrderproducts;

public class UserOrderResult implements Serializable {
	private static final long serialVersionUID = 1L;

	private String userOrderId;
	private String payId;
	private Double totalprice;
	private Double postage;
	private Double orderTotalPrice;
	private Integer status;
	private Long branchUserId;
	private String remark;
	private String orderTimeStr;
	private String payTimeStr;
	//快递公司
	private String expressName;
	//快递单号
	private String expressOrderNo;
	public String getPayTimeStr() {
		return payTimeStr;
	}

	public void setPayTimeStr(String payTimeStr) {
		this.payTimeStr = payTimeStr;
	}

	private String shareUrl;
	private Long cartId;
	private Integer payType;
	private List<OOrderproducts> prolist;
	
	private OOrderaddress orderAddress;

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

	public String getOrderTimeStr() {
		return orderTimeStr;
	}

	public void setOrderTimeStr(String orderTimeStr) {
		this.orderTimeStr = orderTimeStr;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public OOrderaddress getOrderAddress() {
		return orderAddress;
	}

	public void setOrderAddress(OOrderaddress orderAddress) {
		this.orderAddress = orderAddress;
	}

	public Long getCartId() {
		return cartId;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Double getPostage() {
		return postage;
	}

	public void setPostage(Double postage) {
		this.postage = postage;
	}

	public Double getOrderTotalPrice() {
		return orderTotalPrice;
	}

	public void setOrderTotalPrice(Double orderTotalPrice) {
		this.orderTotalPrice = orderTotalPrice;
	}

	public String getExpressName() {
		return expressName;
	}

	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}

	public String getExpressOrderNo() {
		return expressOrderNo;
	}

	public void setExpressOrderNo(String expressOrderNo) {
		this.expressOrderNo = expressOrderNo;
	}
	
	
}
