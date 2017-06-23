package com.bbyiya.model;

import java.util.Date;

public class DMyproductdiscountYiye {
    private Long cartid;

    private String discountjson;

    private Date createtime;

    public Long getCartid() {
        return cartid;
    }

    public void setCartid(Long cartid) {
        this.cartid = cartid;
    }

    public String getDiscountjson() {
        return discountjson;
    }

    public void setDiscountjson(String discountjson) {
        this.discountjson = discountjson == null ? null : discountjson.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}