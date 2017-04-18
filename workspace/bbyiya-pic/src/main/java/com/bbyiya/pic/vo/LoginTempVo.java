package com.bbyiya.pic.vo;

import java.io.Serializable;

public class LoginTempVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long upUserId;
	private Integer loginTo;
	public Long getUpUserId() {
		return upUserId;
	}
	public void setUpUserId(Long upUserId) {
		this.upUserId = upUserId;
	}
	public Integer getLoginTo() {
		return loginTo;
	}
	public void setLoginTo(Integer loginTo) {
		this.loginTo = loginTo;
	}
	
	
	
}
