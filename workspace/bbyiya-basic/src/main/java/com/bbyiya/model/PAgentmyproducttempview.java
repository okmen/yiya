package com.bbyiya.model;

import java.util.Date;

public class PAgentmyproducttempview {
    private Long agentuserid;

    private Integer applycountnew;

    private Integer completecountnew;

    private Integer ordercountnew;

    private Integer cartcountnew;

    private Date updatetime;

    public Long getAgentuserid() {
        return agentuserid;
    }

    public void setAgentuserid(Long agentuserid) {
        this.agentuserid = agentuserid;
    }

    public Integer getApplycountnew() {
        return applycountnew;
    }

    public void setApplycountnew(Integer applycountnew) {
        this.applycountnew = applycountnew;
    }

    public Integer getCompletecountnew() {
        return completecountnew;
    }

    public void setCompletecountnew(Integer completecountnew) {
        this.completecountnew = completecountnew;
    }

    public Integer getOrdercountnew() {
        return ordercountnew;
    }

    public void setOrdercountnew(Integer ordercountnew) {
        this.ordercountnew = ordercountnew;
    }

    public Integer getCartcountnew() {
        return cartcountnew;
    }

    public void setCartcountnew(Integer cartcountnew) {
        this.cartcountnew = cartcountnew;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}