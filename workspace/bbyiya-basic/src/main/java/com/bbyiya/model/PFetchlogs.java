package com.bbyiya.model;

import java.util.Date;

public class PFetchlogs {
    private String url;

    private String remoteurl;

    private Integer status;

    private Date createtime;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getRemoteurl() {
        return remoteurl;
    }

    public void setRemoteurl(String remoteurl) {
        this.remoteurl = remoteurl == null ? null : remoteurl.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}