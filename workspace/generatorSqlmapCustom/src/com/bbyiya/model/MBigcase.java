package com.bbyiya.model;

import java.util.Date;

public class MBigcase {
    private Integer caseid;

    private String title;

    private Integer typeid;

    private Date createtime;

    private String imagedefault;

    public Integer getCaseid() {
        return caseid;
    }

    public void setCaseid(Integer caseid) {
        this.caseid = caseid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Integer getTypeid() {
        return typeid;
    }

    public void setTypeid(Integer typeid) {
        this.typeid = typeid;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getImagedefault() {
        return imagedefault;
    }

    public void setImagedefault(String imagedefault) {
        this.imagedefault = imagedefault == null ? null : imagedefault.trim();
    }
}