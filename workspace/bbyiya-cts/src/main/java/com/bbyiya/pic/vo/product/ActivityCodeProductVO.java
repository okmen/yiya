package com.bbyiya.pic.vo.product;
import java.util.List;

import com.bbyiya.model.PMyproductactivitycode;
public class ActivityCodeProductVO{
	
	private PMyproductactivitycode code;
	private String invitedName;//�ͻ��ǳ�	
    //��������
  	private String birthdayStr;
  	
  	private String title;//��Ʒ����

    private String author; //��Ʒ����

    private Long productid; //��ƷID

    private Long styleid; //��Ʒ��ʽ
    
    private String productTitle;//��Ʒ����
    
  	private int count;  //��Ʒ��������
  	//�ҵ���ƷĬ��ͼ
  	private String headImg;
    //������
  	private Integer commentsCount;
  	//�Ƿ�Ԥ����
  	private Integer isDue;
    //�����ż���
  	private List<String> orderNoList;
  	
  	//�״̬
  	private Integer activeStatus;
  	
  	private String address;
  	
  	private String phone;
   //��Ʒ��ʼ�༭ʱ��
  	private String createtimestr;
    //��Ʒ���༭ʱ��
  	private String updatetimestr;


	public String getInvitedName() {
		return invitedName;
	}

	public String getBirthdayStr() {
		return birthdayStr;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public Long getProductid() {
		return productid;
	}

	public Long getStyleid() {
		return styleid;
	}

	public int getCount() {
		return count;
	}

	public String getHeadImg() {
		return headImg;
	}

	public Integer getCommentsCount() {
		return commentsCount;
	}

	public Integer getIsDue() {
		return isDue;
	}

	public List<String> getOrderNoList() {
		return orderNoList;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public String getAddress() {
		return address;
	}

	public void setInvitedName(String invitedName) {
		this.invitedName = invitedName;
	}


	public void setBirthdayStr(String birthdayStr) {
		this.birthdayStr = birthdayStr;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setProductid(Long productid) {
		this.productid = productid;
	}

	public void setStyleid(Long styleid) {
		this.styleid = styleid;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public void setCommentsCount(Integer commentsCount) {
		this.commentsCount = commentsCount;
	}

	public void setIsDue(Integer isDue) {
		this.isDue = isDue;
	}

	public void setOrderNoList(List<String> orderNoList) {
		this.orderNoList = orderNoList;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public PMyproductactivitycode getCode() {
		return code;
	}

	public void setCode(PMyproductactivitycode code) {
		this.code = code;
	}

	public String getCreatetimestr() {
		return createtimestr;
	}

	public String getUpdatetimestr() {
		return updatetimestr;
	}

	public void setCreatetimestr(String createtimestr) {
		this.createtimestr = createtimestr;
	}

	public void setUpdatetimestr(String updatetimestr) {
		this.updatetimestr = updatetimestr;
	}

	public String getProductTitle() {
		return productTitle;
	}

	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
  	
  	
    

    
    
    
	
	
	    
	
}
