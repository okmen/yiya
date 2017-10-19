package com.bbyiya.pic.vo.calendar;

public class GetDiscountParam {
	//
	private Long sourceWorkId;
	/**
	 * 0 影楼活动作品分享 领券 ，1直接参与活动领取（活动结束时领取） 2 代客制作作品领券 
	 */
	private int sourceType;
	private Integer actityId;
	public Long getSourceWorkId() {
		return sourceWorkId;
	}
	public void setSourceWorkId(Long sourceWorkId) {
		this.sourceWorkId = sourceWorkId;
	}
	public int getSourceType() {
		return sourceType;
	}
	public void setSourceType(int sourceType) {
		this.sourceType = sourceType;
	}
	public Integer getActityId() {
		return actityId;
	}
	public void setActityId(Integer actityId) {
		this.actityId = actityId;
	}
	
	
	
}
