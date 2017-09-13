package com.bbyiya.model;

public class TiStyleadverts {
    private Long styleid;

    private Long productid;

    private String backimg;

    private String blankimg;

    private Integer imgcoordid;

    public Long getStyleid() {
        return styleid;
    }

    public void setStyleid(Long styleid) {
        this.styleid = styleid;
    }

    public Long getProductid() {
        return productid;
    }

    public void setProductid(Long productid) {
        this.productid = productid;
    }

    public String getBackimg() {
        return backimg;
    }

    public void setBackimg(String backimg) {
        this.backimg = backimg == null ? null : backimg.trim();
    }

    public String getBlankimg() {
        return blankimg;
    }

    public void setBlankimg(String blankimg) {
        this.blankimg = blankimg == null ? null : blankimg.trim();
    }

    public Integer getImgcoordid() {
        return imgcoordid;
    }

    public void setImgcoordid(Integer imgcoordid) {
        this.imgcoordid = imgcoordid;
    }
}