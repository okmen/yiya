package com.bbyiya.common.vo;

import java.io.Serializable;

public class SmsParam implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * 充值金额
	 */
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
	/**
	 * 账户号（咿呀号）
	 */
	private Long userId;
	/**
	 * 用户信息
	 */
	private String userName;
	/**
	 * 充值方式（0微信充值，1 cts管理员充值）
	 */
	private int rechargeType;
	/**
	 * 异业活动标题
	 */
	private String yiye_title;
	/**
	 * 异业 审核（1表示通过，2表示不通过）
	 */
	private int yiye_status;

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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getYiye_title() {
		return yiye_title;
	}

	public void setYiye_title(String yiye_title) {
		this.yiye_title = yiye_title;
	}

	public int getYiye_status() {
		return yiye_status;
	}

	public void setYiye_status(int yiye_status) {
		this.yiye_status = yiye_status;
	}

	public int getRechargeType() {
		return rechargeType;
	}

	public void setRechargeType(int rechargeType) {
		this.rechargeType = rechargeType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}
