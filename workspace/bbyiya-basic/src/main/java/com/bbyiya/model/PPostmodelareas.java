package com.bbyiya.model;

import java.util.Date;

public class PPostmodelareas {
    private Integer postid;

    private Integer postmodelid;

    private Integer areacode;

    private String areaname;

    private Double amount;

    private Date createtime;

    public Integer getPostid() {
        return postid;
    }

    public void setPostid(Integer postid) {
        this.postid = postid;
    }

    public Integer getPostmodelid() {
        return postmodelid;
    }

    public void setPostmodelid(Integer postmodelid) {
        this.postmodelid = postmodelid;
    }

    public Integer getAreacode() {
        return areacode;
    }

    public void setAreacode(Integer areacode) {
        this.areacode = areacode;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname == null ? null : areaname.trim();
    }


    
    public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}