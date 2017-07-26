package com.bbyiya.vo.user;

import java.io.Serializable;

import com.bbyiya.model.UUsers;

public class UUserInfoVo implements Serializable {

	private static final long serialVersionUID = 1L;
	private UUsers user;
	private int isAgent;//是否代理商身份
	private int isBranch;//是否影楼身份
	public UUsers getUser() {
		return user;
	}
	public int getIsAgent() {
		return isAgent;
	}
	public int getIsBranch() {
		return isBranch;
	}
	public void setUser(UUsers user) {
		this.user = user;
	}
	public void setIsAgent(int isAgent) {
		this.isAgent = isAgent;
	}
	public void setIsBranch(int isBranch) {
		this.isBranch = isBranch;
	}
	
	
	
	
}
