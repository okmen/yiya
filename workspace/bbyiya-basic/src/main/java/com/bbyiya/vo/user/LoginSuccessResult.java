package com.bbyiya.vo.user;

/**
 * 用户基本信息
 * 用户登陆成功后
 * @author Administrator
 *
 */
public class LoginSuccessResult implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long userId;
	private String userName;
	private String headImg;
	private String mobilePhone;
	
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
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	
	
	
	
}
