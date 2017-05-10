package com.bbyiya.model;

import java.io.Serializable;
import java.util.Date;

public class OUserorders implements Serializable{
	private static final long serialVersionUID = 1L;
    private String userorderid;

    private Long userid;

    private Double totalprice;

    private Integer status;

    private Long branchuserid;

    private Long orderaddressid;

    private String payid;

    private String remark;

    private Date ordertime;

    private Date paytime;
    
    private Double ordertotalprice;

    private Integer paytype;

    private Integer ordertype;

    private Long agentuserid;

    private Integer isbranch;

    private String expresscom;
    
    private String expresscode;

    private String expressorder;

    private Double postage;
    private Date uploadtime;
    private Integer postmodelid;
    private Date deliverytime;
    
    public String getUserorderid() {
        return userorderid;
    }

    public void setUserorderid(String userorderid) {
        this.userorderid = userorderid == null ? null : userorderid.trim();
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

  

    public Double getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(Double totalprice) {
		this.totalprice = totalprice;
	}

	public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getBranchuserid() {
        return branchuserid;
    }

    public void setBranchuserid(Long branchuserid) {
        this.branchuserid = branchuserid;
    }

    public Long getOrderaddressid() {
        return orderaddressid;
    }

    public void setOrderaddressid(Long orderaddressid) {
        this.orderaddressid = orderaddressid;
    }

    public String getPayid() {
        return payid;
    }

    public void setPayid(String payid) {
        this.payid = payid == null ? null : payid.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(Date ordertime) {
        this.ordertime = ordertime;
    }

    public Date getPaytime() {
        return paytime;
    }

    public void setPaytime(Date paytime) {
        this.paytime = paytime;
    }

	public Double getOrdertotalprice() {
		return ordertotalprice;
	}

	public void setOrdertotalprice(Double ordertotalprice) {
		this.ordertotalprice = ordertotalprice;
	}

	public Integer getPaytype() {
		return paytype;
	}

	public void setPaytype(Integer paytype) {
		this.paytype = paytype;
	}

	public Integer getOrdertype() {
		return ordertype;
	}

	public void setOrdertype(Integer ordertype) {
		this.ordertype = ordertype;
	}

	public Long getAgentuserid() {
		return agentuserid;
	}

	public void setAgentuserid(Long agentuserid) {
		this.agentuserid = agentuserid;
	}

	public Integer getIsbranch() {
		return isbranch;
	}

	public void setIsbranch(Integer isbranch) {
		this.isbranch = isbranch;
	}

	public String getExpresscom() {
		return expresscom;
	}

	public void setExpresscom(String expresscom) {
		this.expresscom = expresscom;
	}

	public String getExpressorder() {
		return expressorder;
	}

	public void setExpressorder(String expressorder) {
		this.expressorder = expressorder;
	}

	public Double getPostage() {
		return postage;
	}

	public void setPostage(Double postage) {
		this.postage = postage;
	}

	public Date getUploadtime() {
		return uploadtime;
	}

	public void setUploadtime(Date uploadtime) {
		this.uploadtime = uploadtime;
	}

	public Integer getPostmodelid() {
		return postmodelid;
	}

	public void setPostmodelid(Integer postmodelid) {
		this.postmodelid = postmodelid;
	}

	public Date getDeliverytime() {
		return deliverytime;
	}

	public void setDeliverytime(Date deliverytime) {
		this.deliverytime = deliverytime;
	}

	public String getExpresscode() {
		return expresscode;
	}

	public void setExpresscode(String expresscode) {
		this.expresscode = expresscode;
	}
    
    
}