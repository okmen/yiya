package com.bbyiya.pic.vo.product;

import java.util.List;

import com.bbyiya.model.PMyproducts;
import com.bbyiya.vo.user.UChildInfoParam;

public class MyProductsResult extends PMyproducts{

	private static final long serialVersionUID = 1L;
	//作品图片数量
	private int count;
	//是否下单
	private int isOrder;
	//被邀请人userId
	private Long inviteUserId;
	//邀请人昵称
	private String myNickName;
	//邀请人身份
	private Long userIdentity;
	private int tempStatus;
	private Long tempCartId;
	
	//参与活动的情况
	private MyProductsTempVo tempVo;
	/**
	 * 分享页不同相册的小图标
	 */
	private String sharepageLcon;
	/**
	 * 作品详细图片
	 */
	private List<MyProductsDetailsResult> detailslist;
	/**
	 * 宝宝生日信息
	 */
	private UChildInfoParam childInfo;
	
	
	/*-------------------------------------------------------*/
	
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
	public List<MyProductsDetailsResult> getDetailslist() {
		return detailslist;
	}
	public void setDetailslist(List<MyProductsDetailsResult> detailslist) {
		this.detailslist = detailslist;
	}
	public String getSharepageLcon() {
		return sharepageLcon;
	}
	public void setSharepageLcon(String sharepageLcon) {
		this.sharepageLcon = sharepageLcon;
	}
	public UChildInfoParam getChildInfo() {
		return childInfo;
	}
	public void setChildInfo(UChildInfoParam childInfo) {
		this.childInfo = childInfo;
	}
	public Long getInviteUserId() {
		return inviteUserId;
	}
	public void setInviteUserId(Long inviteUserId) {
		this.inviteUserId = inviteUserId;
	}
	public String getMyNickName() {
		return myNickName;
	}
	public void setMyNickName(String myNickName) {
		this.myNickName = myNickName;
	}
	public Long getUserIdentity() {
		return userIdentity;
	}
	public void setUserIdentity(Long userIdentity) {
		this.userIdentity = userIdentity;
	}
	public int getTempStatus() {
		return tempStatus;
	}
	public void setTempStatus(int tempStatus) {
		this.tempStatus = tempStatus;
	}
	public Long getTempCartId() {
		return tempCartId;
	}
	public void setTempCartId(Long tempCartId) {
		this.tempCartId = tempCartId;
	}
	public MyProductsTempVo getTempVo() {
		return tempVo;
	}
	public void setTempVo(MyProductsTempVo tempVo) {
		this.tempVo = tempVo;
	}
	
	
}
