package com.bbyiya.model;

import java.util.Date;

public class TiActivityworks {
    private Long workid;

    private Long userid;

    private Integer actid;

    private Integer status;

    private Integer extcount;

    private Date createtime;

    private Integer addresstype;

    private String reciever;

    private String mobiephone;
    
    private Long orderaddressid;

    private Date submittime;

    private Integer countmore;

    private Double postage;

    public Long getWorkid() {
        return workid;
    }

    public void setWorkid(Long workid) {
        this.workid = workid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Integer getActid() {
        return actid;
    }

    public void setActid(Integer actid) {
        this.actid = actid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getExtcount() {
        return extcount;
    }

    public void setExtcount(Integer extcount) {
        this.extcount = extcount;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getAddresstype() {
        return addresstype;
    }

    public void setAddresstype(Integer addresstype) {
        this.addresstype = addresstype;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever == null ? null : reciever.trim();
    }

    public String getMobiephone() {
        return mobiephone;
    }

    public void setMobiephone(String mobiephone) {
        this.mobiephone = mobiephone == null ? null : mobiephone.trim();
    }

	public Long getOrderaddressid() {
		return orderaddressid;
	}

	public void setOrderaddressid(Long orderaddressid) {
		this.orderaddressid = orderaddressid;
	}

	public Date getSubmittime() {
		return submittime;
	}

	public void setSubmittime(Date submittime) {
		this.submittime = submittime;
	}

	public Integer getCountmore() {
		return countmore;
	}

	public Double getPostage() {
		return postage;
	}

	public void setCountmore(Integer countmore) {
		this.countmore = countmore;
	}

	public void setPostage(Double postage) {
		this.postage = postage;
	}
	
    
}