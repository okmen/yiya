package com.bbyiya.vo.user;

import java.io.Serializable;

public class UChildInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	//宝宝昵称
	private String nickName;
	//宝宝生日
	private String birthdayStr;
	//宝宝生日 date
	private String birthday;

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getBirthdayStr() {
		return birthdayStr;
	}

	public void setBirthdayStr(String birthdayStr) {
		this.birthdayStr = birthdayStr;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	
	
}
