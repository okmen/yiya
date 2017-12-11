package com.bbyiya.model;

import java.io.Serializable;
import java.util.List;

import com.bbyiya.common.vo.ImageInfo;

public class TiProductshowtemplateinfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer tempinfoid;

    private Integer tempid;

    private Integer showstyleid;

    private String imgjson;

    private Integer cateid;

    /*------------------非数据库字段------------------------------*/
    /**
     * 产品类型
     */
    private String cateName;
    /**
     * 显示款式名称
     */
    private String styleName;
    /**
     * 图片列表
     */
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

	public String getCateName() {
		return cateName;
	}

	public void setCateName(String cateName) {
		this.cateName = cateName;
	}

	public String getStyleName() {
		return styleName;
	}

	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}
    
    
}