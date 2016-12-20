package com.bbyiya.cts.vo.admin;

import java.io.Serializable;

public class AdminLoginSuccessResult implements Serializable{

    private static final long serialVersionUID = 1L;
    private Integer adminid;
    private String username;
    private String ticket_admin;
    
	public Integer getAdminid() {
		return adminid;
	}
	public void setAdminid(Integer adminid) {
		this.adminid = adminid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTicket_admin() {
		return ticket_admin;
	}
	public void setTicket_admin(String ticket_admin) {
		this.ticket_admin = ticket_admin;
	}
    
    
}
