package com.bbyiya.model;

public class PStylebackgrounds {
    private Integer id;

    private Long styleid;

    private String backgroundimg;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getStyleid() {
        return styleid;
    }

    public void setStyleid(Long styleid) {
        this.styleid = styleid;
    }

    public String getBackgroundimg() {
        return backgroundimg;
    }

    public void setBackgroundimg(String backgroundimg) {
        this.backgroundimg = backgroundimg == null ? null : backgroundimg.trim();
    }
}