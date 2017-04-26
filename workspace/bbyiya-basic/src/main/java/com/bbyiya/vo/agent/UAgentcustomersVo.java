package com.bbyiya.vo.agent;


import com.bbyiya.model.UAgentcustomers;

public class UAgentcustomersVo extends UAgentcustomers{
	private String babyNickName; //宝宝昵称
	private String babyBirthday; //宝宝生日
	private String lastBuyDateStr; // 最近购买日期
	private String createtimeStr; //创建时间字符串类型
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
	

}
