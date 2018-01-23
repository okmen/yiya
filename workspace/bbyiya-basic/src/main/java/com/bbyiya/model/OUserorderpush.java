package com.bbyiya.model;

import java.util.Date;

public class OUserorderpush {
    private String userorderid;

    private Integer ispushed;

    private Date pushtime;

    public String getUserorderid() {
        return userorderid;
    }

    public void setUserorderid(String userorderid) {
        this.userorderid = userorderid == null ? null : userorderid.trim();
    }

    public Integer getIspushed() {
        return ispushed;
    }

    public void setIspushed(Integer ispushed) {
        this.ispushed = ispushed;
    }

    public Date getPushtime() {
        return pushtime;
    }

    public void setPushtime(Date pushtime) {
        this.pushtime = pushtime;
    }
}