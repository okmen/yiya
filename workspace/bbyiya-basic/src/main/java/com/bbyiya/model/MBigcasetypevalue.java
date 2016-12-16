package com.bbyiya.model;

public class MBigcasetypevalue {
    private Integer valueid;

    private Integer bigcasetypeid;

    private String value;

    public Integer getValueid() {
        return valueid;
    }

    public void setValueid(Integer valueid) {
        this.valueid = valueid;
    }

    public Integer getBigcasetypeid() {
        return bigcasetypeid;
    }

    public void setBigcasetypeid(Integer bigcasetypeid) {
        this.bigcasetypeid = bigcasetypeid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }
}