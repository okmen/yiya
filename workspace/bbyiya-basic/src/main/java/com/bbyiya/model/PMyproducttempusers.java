package com.bbyiya.model;

import java.util.Date;

public class PMyproducttempusers {
    private Long id;

    private Integer tempid;

    private Long userid;

    private Integer status;

    private Integer applycount;

    private Integer passcount;

    private Date createtime;
    
    private String qRcodeUrl;
    
    private Integer tempStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTempid() {
        return tempid;
    }

    public void setTempid(Integer tempid) {
        this.tempid = tempid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getApplycount() {
        return applycount;
    }

    public void setApplycount(Integer applycount) {
        this.applycount = applycount;
    }

    public Integer getPasscount() {
        return passcount;
    }

    public void setPasscount(Integer passcount) {
        this.passcount = passcount;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

	public String getqRcodeUrl() {
		return qRcodeUrl;
	}

	public void setqRcodeUrl(String qRcodeUrl) {
		this.qRcodeUrl = qRcodeUrl;
	}

	public Integer getTempStatus() {
		return tempStatus;
	}

	public void setTempStatus(Integer tempStatus) {
		this.tempStatus = tempStatus;
	}
    
}