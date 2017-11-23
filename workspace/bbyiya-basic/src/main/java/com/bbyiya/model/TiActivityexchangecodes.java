package com.bbyiya.model;

import java.util.Date;

public class TiActivityexchangecodes {
    private String codenum;

    private Integer actid;

    private Date createtime;

    private Date usedtime;

    private Long userid;

    private Integer status;

    public String getCodenum() {
        return codenum;
    }

    public void setCodenum(String codenum) {
        this.codenum = codenum == null ? null : codenum.trim();
    }

    public Integer getActid() {
        return actid;
    }

    public void setActid(Integer actid) {
        this.actid = actid;
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

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}