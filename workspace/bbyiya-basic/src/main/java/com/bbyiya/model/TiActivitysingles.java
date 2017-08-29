package com.bbyiya.model;

import java.util.Date;

public class TiActivitysingles {
    private Long actsingleid;

    private Integer actid;

    private Long promoteruserid;

    private Long userid;

    private Date createtime;

    private Date getstime;

    private Integer status;

    public Long getActsingleid() {
        return actsingleid;
    }

    public void setActsingleid(Long actsingleid) {
        this.actsingleid = actsingleid;
    }

    public Integer getActid() {
        return actid;
    }

    public void setActid(Integer actid) {
        this.actid = actid;
    }

    public Long getPromoteruserid() {
        return promoteruserid;
    }

    public void setPromoteruserid(Long promoteruserid) {
        this.promoteruserid = promoteruserid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getGetstime() {
        return getstime;
    }

    public void setGetstime(Date getstime) {
        this.getstime = getstime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}