package com.bbyiya.vo.calendar;

public class TiActivityOrderSubmitParam {
	private Long workId;
	private int count;
	private Long submitUserId;
	private submitType type;
	private String remark;
	
	public Long getWorkId() {
		return workId;
	}
	public void setWorkId(Long workId) {
		this.workId = workId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Long getSubmitUserId() {
		return submitUserId;
	}
	public void setSubmitUserId(Long submitUserId) {
		this.submitUserId = submitUserId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	public submitType getType() {
		return type;
	}
	public void setType(submitType type) {
		this.type = type;
	}


	/**
	 * 活动订单提交方式
	 * @author kevin
	 *
	 */
	public enum submitType{
		/**
		 * 时间到自动提交
		 */
		system(1),
		/**
		 * 用户主动提交（可能自赞还未结束）
		 */
		customer(2),
		/**
		 * 广告主 ibs直接下单
		 */
		promoter(3)
		;
		 private final int Type;

	    private submitType(int step)
	    {

	        this.Type = step;
	    }

	    public String toString()
	    {
	        return String.valueOf(this.Type);
	    }
	}
}


