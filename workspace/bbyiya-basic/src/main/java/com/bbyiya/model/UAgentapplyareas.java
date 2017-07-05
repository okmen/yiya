package com.bbyiya.model;

public class UAgentapplyareas {
    private Long acodeid;

    private Long userid;

    private Integer provincecode;

    private Integer citycode;

    private Integer areacode;

    private String address;

    public Long getAcodeid() {
        return acodeid;
    }

    public void setAcodeid(Long acodeid) {
        this.acodeid = acodeid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Integer getProvincecode() {
        return provincecode;
    }

    public void setProvincecode(Integer provincecode) {
        this.provincecode = provincecode;
    }

    public Integer getCitycode() {
        return citycode;
    }

    public void setCitycode(Integer citycode) {
        this.citycode = citycode;
    }

    public Integer getAreacode() {
        return areacode;
    }

    public void setAreacode(Integer areacode) {
        this.areacode = areacode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }
}