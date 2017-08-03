package com.bbyiya.pic.vo.product;

import java.io.Serializable;
import java.util.List;

import com.bbyiya.model.DMyproductdiscountmodel;

public class MyProductsTempVo implements Serializable{
	private static final long serialVersionUID = 1L;


	/**
	 * 我的申请的状态
	 */
	private int mytempStatus;
	/**
	 * 款式
	 */
	private Long styleId;
	/**
	 * 是否限制完成人数
	 */
	private int isLimitQuotas;
	/**
	 * 剩余免费名额
	 */
	private Integer remainingCount;
	/**
	 * 是否需要评论
	 */
	private int isLimitCommentsCount;
	/**
	 * 完成条件（需要的评论数）
	 */
	private Integer maxcommentCount;
	/**
	 * 已经积攒的 评论数
	 */
	private Integer commentCount;
	/**
	 * 是否需要分享（1需要分享，0 不需要分享）
	 */
	private int needShared;
	/**
	 * 是否已经分享（1已经分享）
	 */
	private int haveShared;
	/**
	 * 众筹金额
	 */
	private Double publicAmountLimit;
	/***
	 * 还需要众筹金额
	 */
	private Double publicAmountNeedMore;
	/**
	 * 活动二维码 
	 */
	private String QRCodeUrl;
	/**
	 * 活动二维码描述
	 */
	private String QRcontent;
	/**
	 * 优惠信息
	 */
	private List<DMyproductdiscountmodel> discountList;
	
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
	public List<DMyproductdiscountmodel> getDiscountList() {
		return discountList;
	}
	public void setDiscountList(List<DMyproductdiscountmodel> discountList) {
		this.discountList = discountList;
	}
	public Long getStyleId() {
		return styleId;
	}
	public void setStyleId(Long styleId) {
		this.styleId = styleId;
	}
	public Double getPublicAmountLimit() {
		return publicAmountLimit;
	}
	public void setPublicAmountLimit(Double publicAmountLimit) {
		this.publicAmountLimit = publicAmountLimit;
	}
	public Double getPublicAmountNeedMore() {
		return publicAmountNeedMore;
	}
	public void setPublicAmountNeedMore(Double publicAmountNeedMore) {
		this.publicAmountNeedMore = publicAmountNeedMore;
	}
	public String getQRCodeUrl() {
		return QRCodeUrl;
	}
	public void setQRCodeUrl(String qRCodeUrl) {
		QRCodeUrl = qRCodeUrl;
	}
	public String getQRcontent() {
		return QRcontent;
	}
	public void setQRcontent(String qRcontent) {
		QRcontent = qRcontent;
	}
	public int getNeedShared() {
		return needShared;
	}
	public void setNeedShared(int needShared) {
		this.needShared = needShared;
	}
	public int getHaveShared() {
		return haveShared;
	}
	public void setHaveShared(int haveShared) {
		this.haveShared = haveShared;
	}
	
	
	
}
