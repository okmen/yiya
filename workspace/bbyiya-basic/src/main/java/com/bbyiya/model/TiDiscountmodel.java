package com.bbyiya.model;

import java.util.Date;
import java.util.List;

public class TiDiscountmodel {
    private Integer discountid;

    private String title;

    private Integer type;

    private Integer status;

    private Date createtime;
    
    /*-----------------扩展字段------------------------------*/
    
    private List<TiDiscountdetails> details;

    public Integer getDiscountid() {
        return discountid;
    }

    public void setDiscountid(Integer discountid) {
        this.discountid = discountid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

	public List<TiDiscountdetails> getDetails() {
		return details;
	}

	public void setDetails(List<TiDiscountdetails> details) {
		this.details = details;
	}
    
    
}