package com.bbyiya.vo.product;

import java.util.List;
import com.bbyiya.model.PMyproductdetails;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PMyproductsinvites;

public class MyProductResultVo extends PMyproducts{
	private static final long serialVersionUID = 1L;
	//产品数量
	private int count;
	//我的作品默认图
	private String headImg;
	//是否下单
	private int isOrder;
	//是否被邀请的协同编辑
	private int isInvited;
	//邀约人头像
	private String userImg;
	//邀约人昵称
	private String userName;
	
	private PMyproductsinvites inviteModel;
	
	private String description;
	private List<PMyproductdetails> detailslist;
	//宝宝生日
	private String birthdayStr;
	//制作类型名称
	private String productTitle;
	//模板名称 即来源
	private String tempTitle;
	private List<String> orderNoList;
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getIsOrder() {
		return isOrder;
	}
	public void setIsOrder(int isOrder) {
		this.isOrder = isOrder;
	}
	public List<PMyproductdetails> getDetailslist() {
		return detailslist;
	}
	public void setDetailslist(List<PMyproductdetails> detailslist) {
		this.detailslist = detailslist;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	public int getIsInvited() {
		return isInvited;
	}
	public void setIsInvited(int isInvited) {
		this.isInvited = isInvited;
	}
	public String getUserImg() {
		return userImg;
	}
	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public PMyproductsinvites getInviteModel() {
		return inviteModel;
	}
	public void setInviteModel(PMyproductsinvites inviteModel) {
		this.inviteModel = inviteModel;
	}
	public String getBirthdayStr() {
		return birthdayStr;
	}
	public void setBirthdayStr(String birthdayStr) {
		this.birthdayStr = birthdayStr;
	}
	public String getProductTitle() {
		return productTitle;
	}
	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}
	public String getTempTitle() {
		return tempTitle;
	}
	public void setTempTitle(String tempTitle) {
		this.tempTitle = tempTitle;
	}
	public List<String> getOrderNoList() {
		return orderNoList;
	}
	public void setOrderNoList(List<String> orderNoList) {
		this.orderNoList = orderNoList;
	}
	
	

}
