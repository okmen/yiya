package com.bbyiya.vo.user;

import java.io.Serializable;

public class UUserInfoParam implements Serializable {

	private static final long serialVersionUID = 1L;
	//头像
	private String headImg;
	//昵称
	private String nickName;
	//签名
	private String sign;
	//生日
	private String birthday;
	
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	
	
	
}
