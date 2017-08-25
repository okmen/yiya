package com.bbyiya.pic.vo.activity;

public class AllActivityCountResultVO {
	private static final long serialVersionUID = 1L;
	private Integer goingNum;  //进行中的活动
	private Integer nostartNum;//未开始的活动
	private Integer endingNum; //已结束的活动
	private Integer allNum;	   //活动总数
	private Integer applyNum;	//总报名人数
	private Integer completeNum;	//总完成的人数
	
	private Integer xkzhdApplyNum;//新客资报名活动人数
	private Integer codeApplyNum;//兑换码报名活动人数
	public Integer getGoingNum() {
		return goingNum;
	}
	public Integer getNostartNum() {
		return nostartNum;
	}
	public Integer getEndingNum() {
		return endingNum;
	}
	public Integer getAllNum() {
		return allNum;
	}
	public Integer getApplyNum() {
		return applyNum;
	}
	public Integer getCompleteNum() {
		return completeNum;
	}
	
	public void setGoingNum(Integer goingNum) {
		this.goingNum = goingNum;
	}
	public void setNostartNum(Integer nostartNum) {
		this.nostartNum = nostartNum;
	}
	public void setEndingNum(Integer endingNum) {
		this.endingNum = endingNum;
	}
	public void setAllNum(Integer allNum) {
		this.allNum = allNum;
	}
	public void setApplyNum(Integer applyNum) {
		this.applyNum = applyNum;
	}
	public void setCompleteNum(Integer completeNum) {
		this.completeNum = completeNum;
	}
	public Integer getXkzhdApplyNum() {
		return xkzhdApplyNum;
	}
	public void setXkzhdApplyNum(Integer xkzhdApplyNum) {
		this.xkzhdApplyNum = xkzhdApplyNum;
	}

	public Integer getCodeApplyNum() {
		return codeApplyNum;
	}
	public void setCodeApplyNum(Integer codeApplyNum) {
		this.codeApplyNum = codeApplyNum;
	}
	
	
	

}
