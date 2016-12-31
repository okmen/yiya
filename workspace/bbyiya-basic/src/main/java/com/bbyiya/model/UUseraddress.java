package com.bbyiya.model;

import java.io.Serializable;

public class UUseraddress implements Serializable{
	private static final long serialVersionUID = 1L;
	
    private Long addrid;

    private Long userid;

    private String reciver;

    private String phone;

    private Integer province;

    private Integer city;

    private Integer area;

    private String streetdetail;

    private String postcode;

    private Integer isdefault;

    public Long getAddrid() {
        return addrid;
    }

    public void setAddrid(Long addrid) {
        this.addrid = addrid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getReciver() {
        return reciver;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver == null ? null : reciver.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
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

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode == null ? null : postcode.trim();
    }

    public Integer getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(Integer isdefault) {
        this.isdefault = isdefault;
    }
}