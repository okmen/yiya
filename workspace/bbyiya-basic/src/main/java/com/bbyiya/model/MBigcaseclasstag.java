package com.bbyiya.model;

public class MBigcaseclasstag {
    private Integer tagid;

    private Integer bigcasetypeid;

    private String value;

    public Integer getTagid() {
        return tagid;
    }

    public void setTagid(Integer tagid) {
        this.tagid = tagid;
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