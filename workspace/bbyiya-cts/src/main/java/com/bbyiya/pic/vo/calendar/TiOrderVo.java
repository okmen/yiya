package com.bbyiya.pic.vo.calendar;

import java.io.Serializable;
import java.util.Date;

import com.bbyiya.model.OOrderaddress;

public class TiOrderVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private OOrderaddress address;
	//订单号
	private String userorderid;
	//下单人
	private Long userid;
	
	private String usernickname;//微信昵称

	private Integer status;

	private Long branchuserid;
	
	private String paytime;
	
	private String expresscom;//快递公司

	private String expressorder;//快递单号
	
	private String expresscode;//物流编码

	private Double postage;//运费

	private String producttitle;//产品名称
	private Double price;
	private String propertystr;
	private Long cartid;
	private Integer actid;//活动ID
	public OOrderaddress getAddress() {
		return address;
	}
	public String getUserorderid() {
		return userorderid;
	}
	public Long getUserid() {
		return userid;
	}
	public String getUsernickname() {
		return usernickname;
	}
	public Integer getStatus() {
		return status;
	}
	public Long getBranchuserid() {
		return branchuserid;
	}
	public String getPaytime() {
		return paytime;
	}
	public String getExpresscom() {
		return expresscom;
	}
	public String getExpressorder() {
		return expressorder;
	}
	public String getExpresscode() {
		return expresscode;
	}
	public Double getPostage() {
		return postage;
	}
	public String getProducttitle() {
		return producttitle;
	}
	public Double getPrice() {
		return price;
	}
	public String getPropertystr() {
		return propertystr;
	}
	public Long getCartid() {
		return cartid;
	}
	public Integer getActid() {
		return actid;
	}
	public void setAddress(OOrderaddress address) {
		this.address = address;
	}
	public void setUserorderid(String userorderid) {
		this.userorderid = userorderid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	public void setUsernickname(String usernickname) {
		this.usernickname = usernickname;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public void setBranchuserid(Long branchuserid) {
		this.branchuserid = branchuserid;
	}
	public void setPaytime(String paytime) {
		this.paytime = paytime;
	}
	public void setExpresscom(String expresscom) {
		this.expresscom = expresscom;
	}
	public void setExpressorder(String expressorder) {
		this.expressorder = expressorder;
	}
	public void setExpresscode(String expresscode) {
		this.expresscode = expresscode;
	}
	public void setPostage(Double postage) {
		this.postage = postage;
	}
	public void setProducttitle(String producttitle) {
		this.producttitle = producttitle;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public void setPropertystr(String propertystr) {
		this.propertystr = propertystr;
	}
	public void setCartid(Long cartid) {
		this.cartid = cartid;
	}
	public void setActid(Integer actid) {
		this.actid = actid;
	}
	
	
	
	
	
}
