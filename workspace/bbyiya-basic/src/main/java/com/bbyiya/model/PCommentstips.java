package com.bbyiya.model;

import java.util.Date;

public class PCommentstips {
    private Integer tipid;

    private Integer tipclassid;

    private String content;

    private Date createtime;

    public Integer getTipid() {
        return tipid;
    }

    public void setTipid(Integer tipid) {
        this.tipid = tipid;
    }

    public Integer getTipclassid() {
        return tipclassid;
    }

    public void setTipclassid(Integer tipclassid) {
        this.tipclassid = tipclassid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}