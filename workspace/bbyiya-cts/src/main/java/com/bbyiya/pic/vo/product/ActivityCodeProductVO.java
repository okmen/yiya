package com.bbyiya.pic.vo.product;
import java.util.List;

import com.bbyiya.model.PMyproductactivitycode;
public class ActivityCodeProductVO{
	
	private PMyproductactivitycode code;
	private String invitedName;//客户昵称	
    //宝宝生日
  	private String birthdayStr;
  	
  	private String title;//作品名称

    private String author; //作品作者

    private Long productid; //产品ID

    private Long styleid; //产品款式
    
    private String productTitle;//产品名称
    
  	private int count;  //产品进度数量
  	//我的作品默认图
  	private String headImg;
    //评论数
  	private Integer commentsCount;
  	//是否预产期
  	private Integer isDue;
    //订单号集合
  	private List<String> orderNoList;
  	
  	//活动状态
  	private Integer activeStatus;
  	
  	private String address;
  	
  	private String phone;
   //作品开始编辑时间
  	private String createtimestr;
    //作品最后编辑时间
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
