package com.bbyiya.model;

import java.util.Date;

public class TiAgentsapply {
    private Long agentuserid;

    private String companyname;

    private String contacts;

    private String mobilephone;

    private Integer status;

    private Date createtime;

    private Date processtime;

    private String reason;

    private String remark;

    private Integer province;

    private Integer city;

    private Integer area;

    private String streetdetail;

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

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getProcesstime() {
        return processtime;
    }

    public void setProcesstime(Date processtime) {
        this.processtime = processtime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getProvince() {
        return province;
    }

    public void setProvince(Integer province) {
        this.province = province;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public String getStreetdetail() {
        return streetdetail;
    }

    public void setStreetdetail(String streetdetail) {
        this.streetdetail = streetdetail == null ? null : streetdetail.trim();
    }
}