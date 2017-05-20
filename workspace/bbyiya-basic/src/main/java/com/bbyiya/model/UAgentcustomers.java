package com.bbyiya.model;

import java.util.Date;

public class UAgentcustomers {
    private Long customerid;

    private Long userid;

    private Long branchuserid;

    private Long agentuserid;

    private String phone;

    private String name;

    private Integer status;

    private String remark;

    private Date createtime;

    private Integer ismarket;

    private Integer sourcetype;

    private Long staffuserid;
    
    public Integer getIsmarket() {
		return ismarket;
	}

	public void setIsmarket(Integer ismarket) {
		this.ismarket = ismarket;
	}

	public Integer getSourcetype() {
		return sourcetype;
	}

	public void setSourcetype(Integer sourcetype) {
		this.sourcetype = sourcetype;
	}

	public Long getStaffuserid() {
		return staffuserid;
	}

	public void setStaffuserid(Long staffuserid) {
		this.staffuserid = staffuserid;
	}

	public Long getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Long customerid) {
        this.customerid = customerid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getBranchuserid() {
        return branchuserid;
    }

    public void setBranchuserid(Long branchuserid) {
        this.branchuserid = branchuserid;
    }

    public Long getAgentuserid() {
        return agentuserid;
    }

    public void setAgentuserid(Long agentuserid) {
        this.agentuserid = agentuserid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}