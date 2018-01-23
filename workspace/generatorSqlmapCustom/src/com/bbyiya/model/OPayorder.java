package com.bbyiya.model;

import java.util.Date;

public class OPayorder {
    private String payid;

    private String userorderid;

    private Long userid;

    private String prepayid;

    private Integer status;

    private Double totalprice;

    private Date createtime;

    private Date prepaytime;

    private Date paytime;

    private Integer paytype;

    private Integer ordertype;

    private Double walletamount;

    private Double cashamount;

    private String extobject;

    private String extobject2;

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

    public Date getPrepaytime() {
        return prepaytime;
    }

    public void setPrepaytime(Date prepaytime) {
        this.prepaytime = prepaytime;
    }

    public Date getPaytime() {
        return paytime;
    }

    public void setPaytime(Date paytime) {
        this.paytime = paytime;
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

    public String getExtobject() {
        return extobject;
    }

    public void setExtobject(String extobject) {
        this.extobject = extobject == null ? null : extobject.trim();
    }

    public String getExtobject2() {
        return extobject2;
    }

    public void setExtobject2(String extobject2) {
        this.extobject2 = extobject2 == null ? null : extobject2.trim();
    }
}