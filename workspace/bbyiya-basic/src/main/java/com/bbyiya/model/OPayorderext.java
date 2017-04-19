package com.bbyiya.model;

import java.math.BigDecimal;
import java.util.Date;

public class OPayorderext {
    private String payid;

    private String userorderid;

    private BigDecimal totalprice;

    private Long userid;

    private Long upuserid;

    private Integer status;

    private Date createtime;

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

    public BigDecimal getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(BigDecimal totalprice) {
        this.totalprice = totalprice;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getUpuserid() {
        return upuserid;
    }

    public void setUpuserid(Long upuserid) {
        this.upuserid = upuserid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}