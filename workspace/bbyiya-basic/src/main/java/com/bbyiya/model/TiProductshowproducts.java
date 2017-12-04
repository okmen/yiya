package com.bbyiya.model;

import java.util.List;

public class TiProductshowproducts {
    private Integer cateid;

    private String catename;

    private List<TiProductshowtemplateinfo> styleList;
    
    public Integer getCateid() {
        return cateid;
    }

    public void setCateid(Integer cateid) {
        this.cateid = cateid;
    }

    public String getCatename() {
        return catename;
    }

    public void setCatename(String catename) {
        this.catename = catename == null ? null : catename.trim();
    }

	public List<TiProductshowtemplateinfo> getStyleList() {
		return styleList;
	}

	public void setStyleList(List<TiProductshowtemplateinfo> styleList) {
		this.styleList = styleList;
	}

	
    
    
}