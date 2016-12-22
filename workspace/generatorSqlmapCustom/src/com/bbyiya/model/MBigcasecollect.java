package com.bbyiya.model;

import java.util.Date;

public class MBigcasecollect {
    private Long collectid;

    private Long userid;

    private Integer caseid;

    private Date createtime;

    public Long getCollectid() {
        return collectid;
    }

    public void setCollectid(Long collectid) {
        this.collectid = collectid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Integer getCaseid() {
        return caseid;
    }

    public void setCaseid(Integer caseid) {
        this.caseid = caseid;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}