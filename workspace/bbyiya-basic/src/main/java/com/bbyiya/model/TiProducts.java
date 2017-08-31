package com.bbyiya.model;

import java.util.Date;

public class TiProducts {
    private Long productid;

    private Integer status;

    private Double price;

    private String defaultimg;

    private String title;

    private Date createtime;

    private Date updatetime;

    private String description;
    private String imgjson;

    private Integer postmodelid;


    private Integer advertcount;

    private String advertdescription;

    private String advertimgjson;
    

    public Long getProductid() {
        return productid;
    }

    public void setProductid(Long productid) {
        this.productid = productid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDefaultimg() {
        return defaultimg;
    }

    public void setDefaultimg(String defaultimg) {
        this.defaultimg = defaultimg == null ? null : defaultimg.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
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
	
	public String getImgjson() {
		return imgjson;
	}

	public void setImgjson(String imgjson) {
		this.imgjson = imgjson;
	}

	public Integer getPostmodelid() {
		return postmodelid;
	}

	public void setPostmodelid(Integer postmodelid) {
		this.postmodelid = postmodelid;
	}

	public Integer getAdvertcount() {
		return advertcount;
	}

	public void setAdvertcount(Integer advertcount) {
		this.advertcount = advertcount;
	}

	public String getAdvertdescription() {
		return advertdescription;
	}

	public void setAdvertdescription(String advertdescription) {
		this.advertdescription = advertdescription;
	}

	public String getAdvertimgjson() {
		return advertimgjson;
	}

	public void setAdvertimgjson(String advertimgjson) {
		this.advertimgjson = advertimgjson;
	}
    
    
}