package com.bbyiya.model;

public class PScenebacks {
    private Integer id;

    private Integer sceneid;

    private Long styleid;

    private String imageurl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSceneid() {
        return sceneid;
    }

    public void setSceneid(Integer sceneid) {
        this.sceneid = sceneid;
    }

    public Long getStyleid() {
        return styleid;
    }

    public void setStyleid(Long styleid) {
        this.styleid = styleid;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl == null ? null : imageurl.trim();
    }
}