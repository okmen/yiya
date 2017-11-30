package com.bbyiya.model;

public class TiProductshowtemplateinfo {
    private Integer tempinfoid;

    private Integer tempid;

    private Long productid;

    private String imgjson;

    public Integer getTempinfoid() {
        return tempinfoid;
    }

    public void setTempinfoid(Integer tempinfoid) {
        this.tempinfoid = tempinfoid;
    }

    public Integer getTempid() {
        return tempid;
    }

    public void setTempid(Integer tempid) {
        this.tempid = tempid;
    }

    public Long getProductid() {
        return productid;
    }

    public void setProductid(Long productid) {
        this.productid = productid;
    }

    public String getImgjson() {
        return imgjson;
    }

    public void setImgjson(String imgjson) {
        this.imgjson = imgjson == null ? null : imgjson.trim();
    }
}