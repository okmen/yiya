package com.bbyiya.pic.vo.product;

import java.util.Date;

import com.bbyiya.model.PMyproducts;

/**
 * �ҵ���Ʒ�б�Model��
 * @author Administrator
 *
 */
public class MyProductListVo  extends PMyproducts{

	private static final long serialVersionUID = 1L;
	//�ҵ��ֻ���
	private String myPhone;
	//�������ʱ��
	private Date inviteTime;
	//�������״̬
	private Integer inStatus;
	
	private String myHeadImg;
	private String myNickName;
	private int isMine;
	/**
	 * ������/�������ˣ����Լ�����ͷ��
	 */
	private String otherHeadImg;
	private String otherNickName;
	
	//��Ʒ�༭������
	private int count; 
	//��ƷĬ��ͼƬ
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
