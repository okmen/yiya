package com.bbyiya.pic.vo.order;

public class BranchOrderReportVO {
	private static final long serialVersionUID = 1L;
	
	private Long agentUserId;//代理商ID
	private Long branchUserId;//代理商ID
	private String branchCompanyName;//分店名称
	private Integer totalOrdercount;//总订单数	
	private Double orderPercent;//订单占比
	
	public Long getBranchUserId() {
		return branchUserId;
	}
	
	public Integer getTotalOrdercount() {
		return totalOrdercount;
	}
	public Double getOrderPercent() {
		return orderPercent;
	}
	public void setBranchUserId(Long branchUserId) {
		this.branchUserId = branchUserId;
	}
	
	public String getBranchCompanyName() {
		return branchCompanyName;
	}

	public void setBranchCompanyName(String branchCompanyName) {
		this.branchCompanyName = branchCompanyName;
	}

	public void setTotalOrdercount(Integer totalOrdercount) {
		this.totalOrdercount = totalOrdercount;
	}
	public void setOrderPercent(Double orderPercent) {
		this.orderPercent = orderPercent;
	}
	public Long getAgentUserId() {
		return agentUserId;
	}
	public void setAgentUserId(Long agentUserId) {
		this.agentUserId = agentUserId;
	}
	
	
	

}
