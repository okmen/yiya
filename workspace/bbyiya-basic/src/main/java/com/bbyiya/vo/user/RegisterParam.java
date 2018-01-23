package com.bbyiya.vo.user;

import java.io.Serializable;

public class RegisterParam implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String username;

	private String password;

	private String mobilephone;

	private String userimg;

	private String nickname;

	private String email;
	
	private String vcode;
	
	private String register_token;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getUserimg() {
		return userimg;
	}

	public void setUserimg(String userimg) {
		this.userimg = userimg;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getVcode() {
		return vcode;
	}

	public void setVcode(String vcode) {
		this.vcode = vcode;
	}

	public String getRegister_token() {
		return register_token;
	}

	public void setRegister_token(String register_token) {
		this.register_token = register_token;
	}
	
	

}
