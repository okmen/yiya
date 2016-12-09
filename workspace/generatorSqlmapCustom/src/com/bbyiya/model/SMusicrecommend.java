package com.bbyiya.model;

public class SMusicrecommend {
    private Integer reid;

    private Integer musicid;

    private String linkurl;

    private Integer forday;

    public Integer getReid() {
        return reid;
    }

    public void setReid(Integer reid) {
        this.reid = reid;
    }

    public Integer getMusicid() {
        return musicid;
    }

    public void setMusicid(Integer musicid) {
        this.musicid = musicid;
    }

    public String getLinkurl() {
        return linkurl;
    }

    public void setLinkurl(String linkurl) {
        this.linkurl = linkurl == null ? null : linkurl.trim();
    }

    public Integer getForday() {
        return forday;
    }

    public void setForday(Integer forday) {
        this.forday = forday;
    }
}