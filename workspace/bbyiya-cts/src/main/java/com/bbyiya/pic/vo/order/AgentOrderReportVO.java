package com.bbyiya.pic.vo.order;

import java.util.List;

public class AgentOrderReportVO {
	private static final long serialVersionUID = 1L;
	
	private Long agentUserId;//代理商ID
	private Integer totalordercount;//总订单数
	private Integer totalbranchcount;//总分店数
	
	private List<BranchOrderReportVO> branchorder;

	public Long getAgentUserId() {
		return agentUserId;
	}

	public Integer getTotalordercount() {
		return totalordercount;
	}

	public Integer getTotalbranchcount() {
		return totalbranchcount;
	}

	public List<BranchOrderReportVO> getBranchorder() {
		return branchorder;
	}

	public void setAgentUserId(Long agentUserId) {
		this.agentUserId = agentUserId;
	}

	public void setTotalordercount(Integer totalordercount) {
		this.totalordercount = totalordercount;
	}

	public void setTotalbranchcount(Integer totalbranchcount) {
		this.totalbranchcount = totalbranchcount;
	}

	public void setBranchorder(List<BranchOrderReportVO> branchorder) {
		this.branchorder = branchorder;
	}
	
	
	
	
	

}
