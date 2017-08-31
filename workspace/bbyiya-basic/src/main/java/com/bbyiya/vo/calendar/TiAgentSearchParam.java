package com.bbyiya.vo.calendar;

public class TiAgentSearchParam {
	private Long userId;
	private Long agentUserId;
	private Integer status;
	private String companyName;
    private String contacts;
	public Long getUserId() {
		return userId;
	}
	public Integer getStatus() {
		return status;
	}
	public String getCompanyName() {
		return companyName;
	}
	public String getContacts() {
		return contacts;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	public Long getAgentUserId() {
		return agentUserId;
	}
	public void setAgentUserId(Long agentUserId) {
		this.agentUserId = agentUserId;
	} 
	
	
	
}
