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
	private Date paytime;
	
	private OOrderaddress address;

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

	public Date getPaytime() {
		return paytime;
	}

	public void setPaytime(Date paytime) {
		this.paytime = paytime;
	}

	public OOrderaddress getAddress() {
		return address;
	}

	public void setAddress(OOrderaddress address) {
		this.address = address;
	}
	
	
	
}
