package com.bbyiya.model;

public class MBigcasetag {
    private Integer id;

    private Integer caseid;

    private Integer tagid;

    private String tagcontent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCaseid() {
        return caseid;
    }

    public void setCaseid(Integer caseid) {
        this.caseid = caseid;
    }

    public Integer getTagid() {
        return tagid;
    }

    public void setTagid(Integer tagid) {
        this.tagid = tagid;
    }

    public String getTagcontent() {
        return tagcontent;
    }

    public void setTagcontent(String tagcontent) {
        this.tagcontent = tagcontent == null ? null : tagcontent.trim();
    }
}