package com.bbyiya.pic.vo.agent;

public class AgentSearchParam {

	private Long userId;
	private Integer status;
	private String branchcompanyname;
    private String username; 
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getBranchcompanyname() {
		return branchcompanyname;
	}
	public void setBranchcompanyname(String branchcompanyname) {
		this.branchcompanyname = branchcompanyname;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
