package com.bbyiya.model;

public class PStylecoordinate {
    private Long id;

    private Long styleid;

    private Integer nocoordid;

    private Integer piccoordid;

    private Integer wordcoordid;

    private Integer type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStyleid() {
        return styleid;
    }

    public void setStyleid(Long styleid) {
        this.styleid = styleid;
    }

    public Integer getNocoordid() {
        return nocoordid;
    }

    public void setNocoordid(Integer nocoordid) {
        this.nocoordid = nocoordid;
    }

    public Integer getPiccoordid() {
        return piccoordid;
    }

    public void setPiccoordid(Integer piccoordid) {
        this.piccoordid = piccoordid;
    }

    public Integer getWordcoordid() {
        return wordcoordid;
    }

    public void setWordcoordid(Integer wordcoordid) {
        this.wordcoordid = wordcoordid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}