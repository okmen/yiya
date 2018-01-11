package com.bbyiya.model;

import java.util.Date;

public class TiActivitys {
    private Integer actid;

    private String title;

    private Long produceruserid;

    private Long productid;

    private Long styleid;

    private Integer freecount;

    private Integer acttype;

    private Integer extcount;

    private Integer applycount;

    private Integer completecount;

    private Integer status;

    private Date createtime;

    private String description;
    
    private String remark;
    
    private String actimg;

    private Integer advertid;

    private Integer autoaddress;

    private String qrcode;

    private String qrcodedesc;
    
    private Integer applyingcount;

    private Integer applylimitcount;

    private Integer isfreestyle;

    private Integer hourseffective;
    
    private String companyname;

    public String getQrcode() {
		return qrcode;
	}

	public String getQrcodedesc() {
		return qrcodedesc;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	public void setQrcodedesc(String qrcodedesc) {
		this.qrcodedesc = qrcodedesc;
	}

	public Integer getActid() {
        return actid;
    }

    public void setActid(Integer actid) {
        this.actid = actid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Long getProduceruserid() {
        return produceruserid;
    }

    public void setProduceruserid(Long produceruserid) {
        this.produceruserid = produceruserid;
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

    public Integer getFreecount() {
        return freecount;
    }

    public void setFreecount(Integer freecount) {
        this.freecount = freecount;
    }

    public Integer getActtype() {
        return acttype;
    }

    public void setActtype(Integer acttype) {
        this.acttype = acttype;
    }

    public Integer getExtcount() {
        return extcount;
    }

    public void setExtcount(Integer extcount) {
        this.extcount = extcount;
    }

    public Integer getApplycount() {
        return applycount;
    }

    public void setApplycount(Integer applycount) {
        this.applycount = applycount;
    }

    public Integer getCompletecount() {
        return completecount;
    }

    public void setCompletecount(Integer completecount) {
        this.completecount = completecount;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getActimg() {
		return actimg;
	}

	public void setActimg(String actimg) {
		this.actimg = actimg;
	}

	public Integer getAdvertid() {
		return advertid;
	}

	public void setAdvertid(Integer advertid) {
		this.advertid = advertid;
	}

	public Integer getAutoaddress() {
		return autoaddress;
	}

	public void setAutoaddress(Integer autoaddress) {
		this.autoaddress = autoaddress;
	}

	public Integer getApplyingcount() {
		return applyingcount;
	}

	public void setApplyingcount(Integer applyingcount) {
		this.applyingcount = applyingcount;
	}

	public Integer getApplylimitcount() {
		return applylimitcount;
	}

	public void setApplylimitcount(Integer applylimitcount) {
		this.applylimitcount = applylimitcount;
	}

	public Integer getIsfreestyle() {
		return isfreestyle;
	}

	public void setIsfreestyle(Integer isfreestyle) {
		this.isfreestyle = isfreestyle;
	}

	public Integer getHourseffective() {
		return hourseffective;
	}

	public void setHourseffective(Integer hourseffective) {
		this.hourseffective = hourseffective;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
    
    
}