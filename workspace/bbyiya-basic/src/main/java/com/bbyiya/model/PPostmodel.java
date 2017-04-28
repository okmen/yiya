package com.bbyiya.model;

import java.util.Date;

public class PPostmodel {
    private Integer postmodelid;

    private Long userid;

    private Integer type;

    private String name;

    private Double amount;

    private Date createtime;

    public Integer getPostmodelid() {
        return postmodelid;
    }

    public void setPostmodelid(Integer postmodelid) {
        this.postmodelid = postmodelid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
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