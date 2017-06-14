package com.bbyiya.pic.vo.product;

import java.io.Serializable;

public class MyProductsTempVo implements Serializable{
	private static final long serialVersionUID = 1L;

	
	//评论任务
	private Integer maxcommentCount;
	//剩余
	private Integer remainingCount;
	
	private int isLimitQuotas;
	//是否限制评论数
	private int isLimitCommentsCount;
	
	//已完成评论数
	private Integer commentCount;
	
	private int mytempStatus;
	
	public int getIsLimitQuotas() {
		return isLimitQuotas;
	}
	public void setIsLimitQuotas(int isLimitQuotas) {
		this.isLimitQuotas = isLimitQuotas;
	}
	public Integer getMaxcommentCount() {
		return maxcommentCount;
	}
	public void setMaxcommentCount(Integer maxcommentCount) {
		this.maxcommentCount = maxcommentCount;
	}
	public Integer getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}
	public Integer getRemainingCount() {
		return remainingCount;
	}
	public void setRemainingCount(Integer remainingCount) {
		this.remainingCount = remainingCount;
	}
	public int getIsLimitCommentsCount() {
		return isLimitCommentsCount;
	}
	public void setIsLimitCommentsCount(int isLimitCommentsCount) {
		this.isLimitCommentsCount = isLimitCommentsCount;
	}
	public int getMytempStatus() {
		return mytempStatus;
	}
	public void setMytempStatus(int mytempStatus) {
		this.mytempStatus = mytempStatus;
	}
	
	
	
}
