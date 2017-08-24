package com.bbyiya.pic.vo.order;

public class BranchOrderReportVO {
	private static final long serialVersionUID = 1L;
	
	private Long branchUserId;//代理商ID
	private String companyName;//分店名称
	private Integer totalOrdercount;//总订单数	
	private Double orderPercent;//订单占比
	
	public Long getBranchUserId() {
		return branchUserId;
	}
	public String getCompanyName() {
		return companyName;
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
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public void setTotalOrdercount(Integer totalOrdercount) {
		this.totalOrdercount = totalOrdercount;
	}
	public void setOrderPercent(Double orderPercent) {
		this.orderPercent = orderPercent;
	}
	
	
	

}
