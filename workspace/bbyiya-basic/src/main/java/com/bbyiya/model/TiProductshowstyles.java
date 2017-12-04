package com.bbyiya.model;

import java.util.List;

public class TiProductshowstyles {
    private Integer showstyleid;

    private String stylename;

    private List<TiProductshowtemplateinfo> tempInfoList;
    
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

	public List<TiProductshowtemplateinfo> getTempInfoList() {
		return tempInfoList;
	}

	public void setTempInfoList(List<TiProductshowtemplateinfo> tempInfoList) {
		this.tempInfoList = tempInfoList;
	}
    
    
}