package com.bbyiya.vo.agent;


import java.util.List;

import com.bbyiya.model.UAgentcustomers;

public class UAgentcustomersVo extends UAgentcustomers{
	private String babyNickName; //宝宝昵称
	private String babyBirthday; //宝宝生日
	private String lastBuyDateStr; // 最近购买日期
	private String createtimeStr; //创建时间字符串类型
	private Integer cartCount ; //制作的作品数
	private List<String> cartIdList; //作品ID集合
	private String sourcename;//客户来源名称
	public String getBabyNickName() {
		return babyNickName;
	}
	public void setBabyNickName(String babyNickName) {
		this.babyNickName = babyNickName;
	}
	public String getBabyBirthday() {
		return babyBirthday;
	}
	public void setBabyBirthday(String babyBirthday) {
		this.babyBirthday = babyBirthday;
	}
	public String getLastBuyDateStr() {
		return lastBuyDateStr;
	}
	public void setLastBuyDateStr(String lastBuyDateStr) {
		this.lastBuyDateStr = lastBuyDateStr;
	}
	public String getCreatetimeStr() {
		return createtimeStr;
	}
	public void setCreatetimeStr(String createtimeStr) {
		this.createtimeStr = createtimeStr;
	}
	public Integer getCartCount() {
		return cartCount;
	}
	public void setCartCount(Integer cartCount) {
		this.cartCount = cartCount;
	}
	public List<String> getCartIdList() {
		return cartIdList;
	}
	public void setCartIdList(List<String> cartIdList) {
		this.cartIdList = cartIdList;
	}
	public String getSourcename() {
		return sourcename;
	}
	public void setSourcename(String sourcename) {
		this.sourcename = sourcename;
	}
	

}
