package com.bbyiya.model;

import java.util.Date;

public class ULoginlogs {
    private Long logid;

    private Long userid;

    private String nickname;

    private Date logintime;

    private Integer logintype;

    private String ipstr;

    private Integer sourcetype;

    public Long getLogid() {
        return logid;
    }

    public void setLogid(Long logid) {
        this.logid = logid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public Date getLogintime() {
        return logintime;
    }

    public void setLogintime(Date logintime) {
        this.logintime = logintime;
    }

    public Integer getLogintype() {
        return logintype;
    }

    public void setLogintype(Integer logintype) {
        this.logintype = logintype;
    }

    public String getIpstr() {
        return ipstr;
    }

    public void setIpstr(String ipstr) {
        this.ipstr = ipstr == null ? null : ipstr.trim();
    }

    public Integer getSourcetype() {
        return sourcetype;
    }

    public void setSourcetype(Integer sourcetype) {
        this.sourcetype = sourcetype;
    }
}