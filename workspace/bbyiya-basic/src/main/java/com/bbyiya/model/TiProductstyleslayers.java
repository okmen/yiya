package com.bbyiya.model;

import java.util.Date;

public class TiProductstyleslayers {
    private Long id;

    private Long styleid;

    private String layerimg;

    private Date createtime;

    private Integer sort;

    private String layerimgpreview;

    private Double widthhight;

    private Integer isround;

    private Integer advertcoordid;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStyleid() {
        return styleid;
    }

 

	public Integer getAdvertcoordid() {
		return advertcoordid;
	}

	public void setAdvertcoordid(Integer advertcoordid) {
		this.advertcoordid = advertcoordid;
	}

	public void setStyleid(Long styleid) {
        this.styleid = styleid;
    }

    public String getLayerimg() {
        return layerimg;
    }

    public void setLayerimg(String layerimg) {
        this.layerimg = layerimg == null ? null : layerimg.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getLayerimgpreview() {
        return layerimgpreview;
    }

    public void setLayerimgpreview(String layerimgpreview) {
        this.layerimgpreview = layerimgpreview == null ? null : layerimgpreview.trim();
    }

	public Double getWidthhight() {
		return widthhight;
	}

	public void setWidthhight(Double widthhight) {
		this.widthhight = widthhight;
	}

	public Integer getIsround() {
		return isround;
	}

	public void setIsround(Integer isround) {
		this.isround = isround;
	}
}