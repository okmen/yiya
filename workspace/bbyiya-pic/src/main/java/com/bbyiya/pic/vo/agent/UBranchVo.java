package com.bbyiya.pic.vo.agent;

import java.util.List;

import com.bbyiya.model.RAreaplans;
import com.bbyiya.model.UAgentapplyareas;
import com.bbyiya.model.UBranches;

public class UBranchVo extends UBranches{
	private String proviceName;
	private String cityName;
	private String areaName;
	private String bindphone;
	private List<String> agentArealist;
	private List<UAgentapplyareas> agentapplyArealist;
	private Integer userCount;//发展的用户数
	private Double goodsAmount; //货款金额
	private Double transAmount; //邮费金额
	public String getProviceName() {
		return proviceName;
	}
	public void setProviceName(String proviceName) {
		this.proviceName = proviceName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public List<String> getAgentArealist() {
		return agentArealist;
	}
	public void setAgentArealist(List<String> agentArealist) {
		this.agentArealist = agentArealist;
	}
	public Integer getUserCount() {
		return userCount;
	}
	public void setUserCount(Integer userCount) {
		this.userCount = userCount;
	}
	public Double getGoodsAmount() {
		return goodsAmount;
	}
	public Double getTransAmount() {
		return transAmount;
	}
	public void setGoodsAmount(Double goodsAmount) {
		this.goodsAmount = goodsAmount;
	}
	public void setTransAmount(Double transAmount) {
		this.transAmount = transAmount;
	}
	public List<UAgentapplyareas> getAgentapplyArealist() {
		return agentapplyArealist;
	}
	public void setAgentapplyArealist(List<UAgentapplyareas> agentapplyArealist) {
		this.agentapplyArealist = agentapplyArealist;
	}
	public String getBindphone() {
		return bindphone;
	}
	public void setBindphone(String bindphone) {
		this.bindphone = bindphone;
	}
	
	
}
