package com.bbyiya.model;

import java.util.List;

import com.bbyiya.common.vo.ImageInfo;

public class TiProductshowtemplateinfo {
    private Integer tempinfoid;

    private Integer tempid;

    private Long productid;

    private String imgjson;

    private List<ImageInfo> imglist;
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

	public List<ImageInfo> getImglist() {
		return imglist;
	}

	public void setImglist(List<ImageInfo> imglist) {
		this.imglist = imglist;
	}
    
    
}