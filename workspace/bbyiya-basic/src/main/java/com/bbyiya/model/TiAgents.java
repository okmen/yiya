package com.bbyiya.model;

import java.util.Date;

public class TiAgents {
    private Long agentuserid;

    private String companyname;

    private String contacts;

    private String mobilephone;

    private Integer status;

    private Date expiretime;

    public Long getAgentuserid() {
        return agentuserid;
    }

    public void setAgentuserid(Long agentuserid) {
        this.agentuserid = agentuserid;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname == null ? null : companyname.trim();
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts == null ? null : contacts.trim();
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone == null ? null : mobilephone.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getExpiretime() {
        return expiretime;
    }

    public void setExpiretime(Date expiretime) {
        this.expiretime = expiretime;
    }
}