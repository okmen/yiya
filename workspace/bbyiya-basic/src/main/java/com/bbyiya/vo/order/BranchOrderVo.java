package com.bbyiya.vo.order;

import java.io.Serializable;




import com.bbyiya.model.OOrderaddress;

public class BranchOrderVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	//订单号
	private String userorderid;
	
	//客户UserId
    private Long userid;

    //客户姓名
    private String customername;
    
    private Long cartid;

    private Long branchuserid;

    private Long agentuserid;

    private Long productid;

    private Long styleid;
    
	//制作类型 作品标题
	private String producttitle;
	
	private String phone;
	
	private String babybirthday;
	
	private String address;

	private Integer tempid;//活动ID
	
	private String branchname;
	//订单状态
	private Integer status;
	//来源影楼名称
	private Long frombranchuserid;
	public String getUserorderid() {
		return userorderid;
	}
	public Long getUserid() {
		return userid;
	}
	public String getCustomername() {
		return customername;
	}
	public Long getCartid() {
		return cartid;
	}
	public Long getBranchuserid() {
		return branchuserid;
	}
	public Long getAgentuserid() {
		return agentuserid;
	}
	public Long getProductid() {
		return productid;
	}
	public Long getStyleid() {
		return styleid;
	}
	
	public String getProducttitle() {
		return producttitle;
	}
	public String getPhone() {
		return phone;
	}
	public String getBabybirthday() {
		return babybirthday;
	}
	public String getAddress() {
		return address;
	}
	public Integer getTempid() {
		return tempid;
	}
	public Integer getStatus() {
		return status;
	}
	public void setUserorderid(String userorderid) {
		this.userorderid = userorderid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	public void setCustomername(String customername) {
		this.customername = customername;
	}
	public void setCartid(Long cartid) {
		this.cartid = cartid;
	}
	public void setBranchuserid(Long branchuserid) {
		this.branchuserid = branchuserid;
	}
	public void setAgentuserid(Long agentuserid) {
		this.agentuserid = agentuserid;
	}
	public void setProductid(Long productid) {
		this.productid = productid;
	}
	public void setStyleid(Long styleid) {
		this.styleid = styleid;
	}
	
	public void setProducttitle(String producttitle) {
		this.producttitle = producttitle;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setBabybirthday(String babybirthday) {
		this.babybirthday = babybirthday;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setTempid(Integer tempid) {
		this.tempid = tempid;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getBranchname() {
		return branchname;
	}
	public void setBranchname(String branchname) {
		this.branchname = branchname;
	}
	public Long getFrombranchuserid() {
		return frombranchuserid;
	}
	public void setFrombranchuserid(Long frombranchuserid) {
		this.frombranchuserid = frombranchuserid;
	}
	
	
}
