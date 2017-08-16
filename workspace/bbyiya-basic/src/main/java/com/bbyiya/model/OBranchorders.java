package com.bbyiya.model;

import java.util.Date;

public class OBranchorders {
    private String userorderid;

    private Long userid;

    private String customername;

    private Long cartid;

    private Long branchuserid;

    private Long agentuserid;

    private Long productid;

    private Long styleid;
    
    private Long frombranchuserid;
    
    private Date createtime;

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

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername == null ? null : customername.trim();
    }

    public Long getCartid() {
        return cartid;
    }

    public void setCartid(Long cartid) {
        this.cartid = cartid;
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

    public Long getProductid() {
        return productid;
    }

    public void setProductid(Long productid) {
        this.productid = productid;
    }

    public Long getStyleid() {
        return styleid;
    }

    public void setStyleid(Long styleid) {
        this.styleid = styleid;
    }

	public Long getFrombranchuserid() {
		return frombranchuserid;
	}

	public void setFrombranchuserid(Long frombranchuserid) {
		this.frombranchuserid = frombranchuserid;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
    
}