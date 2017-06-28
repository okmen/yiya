package com.bbyiya.pic.vo.product;

import java.util.List;

import com.bbyiya.model.PMyproducts;
import com.bbyiya.vo.user.UChildInfoParam;

public class MyProductsResult extends PMyproducts{

	private static final long serialVersionUID = 1L;
	//��ƷͼƬ����
	private int count;
	//�Ƿ��µ�
	private int isOrder;
	//��������userId
	private Long inviteUserId;
	//�������ǳ�
	private String myNickName;
	//���������
	private Long userIdentity;
	private int tempStatus;
	private Long tempCartId;
	
	//���������
	private MyProductsTempVo tempVo;
	/**
	 * ����ҳ��ͬ����Сͼ��
	 */
	private String sharepageLcon;
	/**
	 * ��Ʒ��ϸͼƬ
	 */
	private List<MyProductsDetailsResult> detailslist;
	/**
	 * ����������Ϣ
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
