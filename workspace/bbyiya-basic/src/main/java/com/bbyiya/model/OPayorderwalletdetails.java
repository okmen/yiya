package com.bbyiya.model;

import java.util.Date;

public class OPayorderwalletdetails {
    private String payid;

    private Long userid;

    private String headimg;

    private Long foruserid;

    private Double amount;

    private Date paytime;

    private Long cartid;

    public String getPayid() {
        return payid;
    }

    public void setPayid(String payid) {
        this.payid = payid == null ? null : payid.trim();
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg == null ? null : headimg.trim();
    }

    public Long getForuserid() {
        return foruserid;
    }

    public void setForuserid(Long foruserid) {
        this.foruserid = foruserid;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getPaytime() {
        return paytime;
    }

    public void setPaytime(Date paytime) {
        this.paytime = paytime;
    }

    public Long getCartid() {
        return cartid;
    }

    public void setCartid(Long cartid) {
        this.cartid = cartid;
    }
}