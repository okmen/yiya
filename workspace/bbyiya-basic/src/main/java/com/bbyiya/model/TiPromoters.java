package com.bbyiya.model;

import java.util.Date;

public class TiPromoters {
    private Long promoteruserid;

    private String companyname;

    private String contacts;

    private String mobilephone;

    private Integer province;

    private Integer city;

    private Integer area;

    private String streetdetails;

    private Integer status;

    private Date expiretime;

    private String remark;

    private Long agentuserid;

    private Long produceruserid;

    public Long getPromoteruserid() {
        return promoteruserid;
    }

    public void setPromoteruserid(Long promoteruserid) {
        this.promoteruserid = promoteruserid;
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

    public String getStreetdetails() {
        return streetdetails;
    }

    public void setStreetdetails(String streetdetails) {
        this.streetdetails = streetdetails == null ? null : streetdetails.trim();
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Long getAgentuserid() {
        return agentuserid;
    }

    public void setAgentuserid(Long agentuserid) {
        this.agentuserid = agentuserid;
    }

    public Long getProduceruserid() {
        return produceruserid;
    }

    public void setProduceruserid(Long produceruserid) {
        this.produceruserid = produceruserid;
    }
}