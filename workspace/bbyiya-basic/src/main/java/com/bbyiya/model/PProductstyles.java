package com.bbyiya.model;

import java.io.Serializable;
import java.util.Date;

public class PProductstyles implements Serializable{
	private static final long serialVersionUID = 1L;
    private Long styleid;

    private Long productid;

    private Long userid;

    private String defaultimg;

    private Double oldprice;

    private Double price;

    private Integer status;

    private Integer isdefault;

    private String propertystr;
    private String description;

    private Date createtime;

    private Date updatetime;
    
    private Double agentprice;

    public Long getStyleid() {
        return styleid;
    }

    public void setStyleid(Long styleid) {
        this.styleid = styleid;
    }

    public Long getProductid() {
        return productid;
    }

    public void setProductid(Long productid) {
        this.productid = productid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getDefaultimg() {
        return defaultimg;
    }

    public void setDefaultimg(String defaultimg) {
        this.defaultimg = defaultimg == null ? null : defaultimg.trim();
    }

   

    public Double getOldprice() {
		return oldprice;
	}

	public void setOldprice(Double oldprice) {
		this.oldprice = oldprice;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(Integer isdefault) {
        this.isdefault = isdefault;
    }

    public String getPropertystr() {
        return propertystr;
    }

    public void setPropertystr(String propertystr) {
        this.propertystr = propertystr == null ? null : propertystr.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

	public Double getAgentprice() {
		return agentprice;
	}

	public void setAgentprice(Double agentprice) {
		this.agentprice = agentprice;
	}
    
}