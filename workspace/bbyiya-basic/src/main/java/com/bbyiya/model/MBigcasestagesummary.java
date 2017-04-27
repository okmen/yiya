package com.bbyiya.model;

import java.io.Serializable;

public class MBigcasestagesummary implements Serializable{

	private static final long serialVersionUID = 1L;
    private Integer id;

    private Integer stageid;

    private Integer casetypeid;

    private Integer groupcount;

    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStageid() {
        return stageid;
    }

    public void setStageid(Integer stageid) {
        this.stageid = stageid;
    }

    public Integer getCasetypeid() {
        return casetypeid;
    }

    public void setCasetypeid(Integer casetypeid) {
        this.casetypeid = casetypeid;
    }

    public Integer getGroupcount() {
        return groupcount;
    }

    public void setGroupcount(Integer groupcount) {
        this.groupcount = groupcount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}