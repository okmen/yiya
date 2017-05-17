package com.bbyiya.model;

import java.util.Date;
import java.util.List;

public class PCommentstemp {
    private Integer tipclassid;

    private Long productid;

    private String tipclassname;

    private Date createtime;
    
    private List<PCommentstips> tips;

    public Integer getTipclassid() {
        return tipclassid;
    }

    public void setTipclassid(Integer tipclassid) {
        this.tipclassid = tipclassid;
    }

    public Long getProductid() {
        return productid;
    }

    public void setProductid(Long productid) {
        this.productid = productid;
    }

    public String getTipclassname() {
        return tipclassname;
    }

    public void setTipclassname(String tipclassname) {
        this.tipclassname = tipclassname == null ? null : tipclassname.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

	public List<PCommentstips> getTips() {
		return tips;
	}

	public void setTips(List<PCommentstips> tips) {
		this.tips = tips;
	}
    
}