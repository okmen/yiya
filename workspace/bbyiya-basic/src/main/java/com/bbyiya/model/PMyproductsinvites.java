package com.bbyiya.model;

import java.util.Date;

public class PMyproductsinvites {
    private Long inviteid;

    private Long cartid;

    private Long userid;

    private String invitephone;

    private Integer status;

    private Date createtime;

    public Long getInviteid() {
        return inviteid;
    }

    public void setInviteid(Long inviteid) {
        this.inviteid = inviteid;
    }

    public Long getCartid() {
        return cartid;
    }

    public void setCartid(Long cartid) {
        this.cartid = cartid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getInvitephone() {
        return invitephone;
    }

    public void setInvitephone(String invitephone) {
        this.invitephone = invitephone == null ? null : invitephone.trim();
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