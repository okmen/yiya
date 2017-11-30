package com.bbyiya.model;

import java.util.List;

import com.bbyiya.common.vo.ImageInfo;

public class TiProductshowtemplateinfo {
	private Integer tempinfoid;

    private Integer tempid;

    private Integer showstyleid;

    private String imgjson;

    private Integer cateid;

    private List<ImageInfo> imglist;
    
    /*****************************vo**************************/
    private String tempname;
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

	public Integer getShowstyleid() {
		return showstyleid;
	}

	public void setShowstyleid(Integer showstyleid) {
		this.showstyleid = showstyleid;
	}

	public Integer getCateid() {
		return cateid;
	}

	public void setCateid(Integer cateid) {
		this.cateid = cateid;
	}

	public String getTempname() {
		return tempname;
	}

	public void setTempname(String tempname) {
		this.tempname = tempname;
	}
    
    
}