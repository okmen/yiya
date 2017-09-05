package com.bbyiya.model;

import java.io.Serializable;
import java.util.Date;

public class OPayorder implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String payid;

	private String userorderid;

	private Long userid;

	private String prepayid;

	private Integer status;

	private Double totalprice;

	private Date createtime;

	private Date paytime;

	private Date prepaytime;
	
	private Integer paytype;
	
	private Integer ordertype;
	
	private Double walletamount;
	
    private Double cashamount;
    
    private String extobject;
	
	public String getExtobject() {
		return extobject;
	}

	public void setExtobject(String extobject) {
		this.extobject = extobject;
	}

	public String getPayid() {
		return payid;
	}

	public void setPayid(String payid) {
		this.payid = payid == null ? null : payid.trim();
	}

	public String getUserorderid() {
		return userorderid;
	}

	public void setUserorderid(String userorderid) {
		this.userorderid = userorderid == null ? null : userorderid.trim();
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getPrepayid() {
		return prepayid;
	}

	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid == null ? null : prepayid.trim();
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(Double totalprice) {
		this.totalprice = totalprice;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getPaytime() {
		return paytime;
	}

	public void setPaytime(Date paytime) {
		this.paytime = paytime;
	}

	public Date getPrepaytime() {
		return prepaytime;
	}

	public void setPrepaytime(Date prepaytime) {
		this.prepaytime = prepaytime;
	}

	public Integer getPaytype() {
		return paytype;
	}

	public void setPaytype(Integer paytype) {
		this.paytype = paytype;
	}

	public Integer getOrdertype() {
		return ordertype;
	}

	public void setOrdertype(Integer ordertype) {
		this.ordertype = ordertype;
	}

	public Double getWalletamount() {
		return walletamount;
	}

	public void setWalletamount(Double walletamount) {
		this.walletamount = walletamount;
	}

	public Double getCashamount() {
		return cashamount;
	}

	public void setCashamount(Double cashamount) {
		this.cashamount = cashamount;
	}
	
}