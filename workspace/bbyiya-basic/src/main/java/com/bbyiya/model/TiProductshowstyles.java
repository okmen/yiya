package com.bbyiya.model;

public class TiProductshowstyles {
    private Integer showstyleid;

    private String stylename;

    public Integer getShowstyleid() {
        return showstyleid;
    }

    public void setShowstyleid(Integer showstyleid) {
        this.showstyleid = showstyleid;
    }

    public String getStylename() {
        return stylename;
    }

    public void setStylename(String stylename) {
        this.stylename = stylename == null ? null : stylename.trim();
    }
}