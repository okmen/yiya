package com.bbyiya.pic.vo.agent;

import java.util.List;

import com.bbyiya.model.RAreaplans;
import com.bbyiya.model.UAgentapply;
import com.bbyiya.model.UAgentapplyareas;

public class UAgentApplyVo extends UAgentapply{
	private String proviceName;
	private String cityName;
	private String areaName;
	private Double goodsAmount; //货款金额
	private Double transAmount; //邮费金额
	private List<UAgentapplyareas> agentapplyArealist;
	private List<String> agentArealist;
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
	public List<String> getAgentArealist() {
		return agentArealist;
	}
	public void setAgentapplyArealist(List<UAgentapplyareas> agentapplyArealist) {
		this.agentapplyArealist = agentapplyArealist;
	}
	public void setAgentArealist(List<String> agentArealist) {
		this.agentArealist = agentArealist;
	}
	
	
}
