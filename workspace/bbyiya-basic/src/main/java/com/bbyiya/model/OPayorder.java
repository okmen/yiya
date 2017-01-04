package com.bbyiya.model;

import java.util.Date;

public class OPayorder {
    private String payid;

    private String userorderid;

    private Long userid;

    private Integer status;

    private Double totalprice;

    private Date createtime;

    private Date paytime;

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
}