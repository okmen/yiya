package com.bbyiya.model;

import java.util.Date;

public class PMyproducttempapply {
    private Long tempapplyid;

    private Integer tempid;

    private Long userid;

    private String mobilephone;

    private Integer province;

    private Integer city;

    private Integer area;

    private String street;

    private String adress;

    private Integer isdue;

    private Long companyuserid;

    private Integer status;

    private Date createtime;

    public Long getTempapplyid() {
        return tempapplyid;
    }

    public void setTempapplyid(Long tempapplyid) {
        this.tempapplyid = tempapplyid;
    }

    public Integer getTempid() {
        return tempid;
    }

    public void setTempid(Integer tempid) {
        this.tempid = tempid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street == null ? null : street.trim();
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress == null ? null : adress.trim();
    }

    public Integer getIsdue() {
        return isdue;
    }

    public void setIsdue(Integer isdue) {
        this.isdue = isdue;
    }

    public Long getCompanyuserid() {
        return companyuserid;
    }

    public void setCompanyuserid(Long companyuserid) {
        this.companyuserid = companyuserid;
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