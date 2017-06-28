package com.bbyiya.common.vo;

import java.io.Serializable;

public class SmsParam implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Double amount;
	/**
	 * 订单号
	 */
	private String userOrderId;
	/**
	 * 物流名称
	 */
	private String transName;
	/**
	 * 物流单号
	 */
	private String transNum;

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getUserOrderId() {
		return userOrderId;
	}

	public void setUserOrderId(String userOrderId) {
		this.userOrderId = userOrderId;
	}

	public String getTransName() {
		return transName;
	}

	public void setTransName(String transName) {
		this.transName = transName;
	}

	public String getTransNum() {
		return transNum;
	}

	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}
	
	
}
