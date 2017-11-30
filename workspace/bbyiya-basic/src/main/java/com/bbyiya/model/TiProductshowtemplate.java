package com.bbyiya.model;

import java.util.List;

public class TiProductshowtemplate {
    private Integer tempid;

    private String tempname;

    private Integer isdefault;

    private List<TiProductshowtemplateinfo> templateinfos;
    public Integer getTempid() {
        return tempid;
    }

    public void setTempid(Integer tempid) {
        this.tempid = tempid;
    }

    public String getTempname() {
        return tempname;
    }

    public void setTempname(String tempname) {
        this.tempname = tempname == null ? null : tempname.trim();
    }

    public Integer getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(Integer isdefault) {
        this.isdefault = isdefault;
    }

	public List<TiProductshowtemplateinfo> getTemplateinfos() {
		return templateinfos;
	}

	public void setTemplateinfos(List<TiProductshowtemplateinfo> templateinfos) {
		this.templateinfos = templateinfos;
	}
    
    
}