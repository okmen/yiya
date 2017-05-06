package com.bbyiya.model;

public class SReadstypes {
    private Integer readtypeid;

    private String name;

    private Integer sort;

    private String des;

    public Integer getReadtypeid() {
        return readtypeid;
    }

    public void setReadtypeid(Integer readtypeid) {
        this.readtypeid = readtypeid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des == null ? null : des.trim();
    }
}