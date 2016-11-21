package com.bbyiya.api.vo.login;

import java.io.Serializable;

public class OtherLoginParam implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String headImg;
	private Integer loginType;
	private String nickName;
	private String openId;
	
	
	
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	public Integer getLoginType() {
		return loginType;
	}
	public void setLoginType(Integer loginType) {
		this.loginType = loginType;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
		
	
	
}
