package com.bbyiya.vo.user;

public class LoginSuccessResult implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long userId;
	private String userName;
	private String headImg;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	
	
	
	
}
