package com.bbyiya.pic.vo.order.ibs;

import java.io.Serializable;
import java.util.Date;

import com.bbyiya.model.OOrderaddress;

public class OrderVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	//订单号
	private String userorderid;
	//下单人
	private Long userid;

	private Integer status;

	private Long branchuserid;
	//
	private String paytime;
	
	private String expresscom;//快递公司

	private String expressorder;//快递单号
	
	private String expresscode;//物流编码

	private Double postage;//运费
	
	private OOrderaddress address;

	private OrderProductVo orderProduct;

	private String sourcetype;//订单来源 0活动作品  1客户一对一  2 活动码作品  3 其它
	
	private Integer tempid;//活动ID
	
	public String getUserorderid() {
		return userorderid;
	}

	public void setUserorderid(String userorderid) {
		this.userorderid = userorderid;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getBranchuserid() {
		return branchuserid;
	}

	public void setBranchuserid(Long branchuserid) {
		this.branchuserid = branchuserid;
	}

	

	public String getPaytime() {
		return paytime;
	}

	public void setPaytime(String paytime) {
		this.paytime = paytime;
	}

	public OOrderaddress getAddress() {
		return address;
	}

	public void setAddress(OOrderaddress address) {
		this.address = address;
	}

	public OrderProductVo getOrderProduct() {
		return orderProduct;
	}

	public void setOrderProduct(OrderProductVo orderProduct) {
		this.orderProduct = orderProduct;
	}

	public String getExpresscom() {
		return expresscom;
	}

	public void setExpresscom(String expresscom) {
		this.expresscom = expresscom;
	}

	public String getExpressorder() {
		return expressorder;
	}

	public void setExpressorder(String expressorder) {
		this.expressorder = expressorder;
	}

	public Double getPostage() {
		return postage;
	}

	public void setPostage(Double postage) {
		this.postage = postage;
	}

	public String getExpresscode() {
		return expresscode;
	}

	public void setExpresscode(String expresscode) {
		this.expresscode = expresscode;
	}

	
	public String getSourcetype() {
		return sourcetype;
	}

	public void setSourcetype(String sourcetype) {
		this.sourcetype = sourcetype;
	}

	public Integer getTempid() {
		return tempid;
	}

	public void setTempid(Integer tempid) {
		this.tempid = tempid;
	}
	
	
	
	
	
}
