package com.bbyiya.model;

import java.util.Date;

public class TiPromoteremployees {
    private Long userid;

    private Long promoteruserid;

    private String name;

    private Integer status;

    private Date createtime;
    
    private String phone;

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getPromoteruserid() {
        return promoteruserid;
    }

    public void setPromoteruserid(Long promoteruserid) {
        this.promoteruserid = promoteruserid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
    
    
}