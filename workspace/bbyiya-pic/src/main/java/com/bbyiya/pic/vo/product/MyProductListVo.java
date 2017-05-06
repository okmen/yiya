package com.bbyiya.pic.vo.product;

import java.util.Date;

import com.bbyiya.model.PMyproducts;

/**
 * 我的作品列表（Model）
 * @author Administrator
 *
 */
public class MyProductListVo  extends PMyproducts{

	private static final long serialVersionUID = 1L;
	//我的手机号
	private String myPhone;
	//被邀请的时间
	private Date inviteTime;
	//被邀请的状态
	private Integer inStatus;
	
	private String myHeadImg;
	private String myNickName;
	private int isMine;
	/**
	 * 邀请人/被邀请人（非自己）的头像
	 */
	private String otherHeadImg;
	private String otherNickName;
	
	//作品编辑的数量
	private int count; 
	//作品默认图片
	private String defaultImg;
	
	public String getMyPhone() {
		return myPhone;
	}
	public void setMyPhone(String myPhone) {
		this.myPhone = myPhone;
	}
	public Date getInviteTime() {
		return inviteTime;
	}
	public void setInviteTime(Date inviteTime) {
		this.inviteTime = inviteTime;
	}
	public Integer getInStatus() {
		return inStatus;
	}
	public void setInStatus(Integer inStatus) {
		this.inStatus = inStatus;
	}
	public String getMyHeadImg() {
		return myHeadImg;
	}
	public void setMyHeadImg(String myHeadImg) {
		this.myHeadImg = myHeadImg;
	}
	public String getMyNickName() {
		return myNickName;
	}
	public void setMyNickName(String myNickName) {
		this.myNickName = myNickName;
	}
	public int getIsMine() {
		return isMine;
	}
	public void setIsMine(int isMine) {
		this.isMine = isMine;
	}
	public String getOtherHeadImg() {
		return otherHeadImg;
	}
	public void setOtherHeadImg(String otherHeadImg) {
		this.otherHeadImg = otherHeadImg;
	}
	public String getOtherNickName() {
		return otherNickName;
	}
	public void setOtherNickName(String otherNickName) {
		this.otherNickName = otherNickName;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getDefaultImg() {
		return defaultImg;
	}
	public void setDefaultImg(String defaultImg) {
		this.defaultImg = defaultImg;
	}
	

	
	
}
