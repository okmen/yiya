package com.bbyiya.model;

import java.util.Date;

public class DMyproductdiscounts {
    private Long cartid;

    private Long userid;

    private Integer discounttype;

    private Integer status;

    private Double amount;

    private Date createtime;

    private Date usedtime;

    public Long getCartid() {
        return cartid;
    }

    public void setCartid(Long cartid) {
        this.cartid = cartid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Integer getDiscounttype() {
        return discounttype;
    }

    public void setDiscounttype(Integer discounttype) {
        this.discounttype = discounttype;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUsedtime() {
        return usedtime;
    }

    public void setUsedtime(Date usedtime) {
        this.usedtime = usedtime;
    }
}