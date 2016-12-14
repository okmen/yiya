package com.bbyiya.model;

import java.util.Date;

public class UAdminloginlog {
    private Long logid;

    private Integer adminid;

    private String username;

    private Date createtime;

    public Long getLogid() {
        return logid;
    }

    public void setLogid(Long logid) {
        this.logid = logid;
    }

    public Integer getAdminid() {
        return adminid;
    }

    public void setAdminid(Integer adminid) {
        this.adminid = adminid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}