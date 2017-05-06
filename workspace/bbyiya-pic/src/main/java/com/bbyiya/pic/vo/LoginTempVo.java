package com.bbyiya.pic.vo;

import java.io.Serializable;

public class LoginTempVo implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long upUserId;
	private Integer loginTo;
	private String redirect_url;
	
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
	public String getRedirect_url() {
		return redirect_url;
	}
	public void setRedirect_url(String redirect_url) {
		this.redirect_url = redirect_url;
	}
	
	
	
}
