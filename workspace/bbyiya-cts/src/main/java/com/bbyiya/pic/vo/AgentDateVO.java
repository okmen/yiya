package com.bbyiya.pic.vo;

import java.util.Date;

public class AgentDateVO {

	/**
	 * 代理商userid
	 */
	private Long agentUserId;
	/**
	 * 代理商 公司名称
	 */
	private String agentCompanyName;
	/**
	 * 活动总报名人数
	 */
	private Integer applyCount;
	/**
	 * 活动总完成人数
	 */
	private Integer completeCount;
	/**
	 * 活动昨日新增报名人数
	 */
	private Integer applyCountNew;
	/**
	 * 活动昨日新增完成人数
	 */
	private Integer completeCountNew;
	/**
	 * 订单总数
	 */
	private Integer orderCount;
	/**
	 * 活动昨日新下单人数
	 */
	private Integer orderCountNew;
	/**
	 * 作品总数
	 */
	private Integer cartCount;
	/**
	 * 昨日新增作品数
	 */
	private Integer cartCountNew;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	
	
	public Long getAgentUserId() {
		return agentUserId;
	}
	public void setAgentUserId(Long agentUserId) {
		this.agentUserId = agentUserId;
	}
	public String getAgentCompanyName() {
		return agentCompanyName;
	}
	public void setAgentCompanyName(String agentCompanyName) {
		this.agentCompanyName = agentCompanyName;
	}
	public Integer getApplyCount() {
		return applyCount;
	}
	public void setApplyCount(Integer applyCount) {
		this.applyCount = applyCount;
	}
	public Integer getCompleteCount() {
		return completeCount;
	}
	public void setCompleteCount(Integer completeCount) {
		this.completeCount = completeCount;
	}
	
	public Integer getApplyCountNew() {
		return applyCountNew;
	}
	public void setApplyCountNew(Integer applyCountNew) {
		this.applyCountNew = applyCountNew;
	}
	public Integer getCompleteCountNew() {
		return completeCountNew;
	}
	public void setCompleteCountNew(Integer completeCountNew) {
		this.completeCountNew = completeCountNew;
	}
	public Integer getOrderCountNew() {
		return orderCountNew;
	}
	public void setOrderCountNew(Integer orderCountNew) {
		this.orderCountNew = orderCountNew;
	}
	public Integer getCartCountNew() {
		return cartCountNew;
	}
	public void setCartCountNew(Integer cartCountNew) {
		this.cartCountNew = cartCountNew;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}
	public Integer getCartCount() {
		return cartCount;
	}
	public void setCartCount(Integer cartCount) {
		this.cartCount = cartCount;
	}
	
	
	
	
}
