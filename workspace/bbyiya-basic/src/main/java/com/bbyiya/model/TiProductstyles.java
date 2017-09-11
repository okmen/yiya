package com.bbyiya.model;

public class TiProductstyles {
    private Long styleid;

    private Long productid;

    private Double price;

    private Double promoterprice;

    private Integer imgcount;

    private Integer producttype;

    private String defaultimg;

    private Integer status;

    private String description;
    
    private String imgjson;

    private Double width;

    private Double hight;

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPromoterprice() {
        return promoterprice;
    }

    public void setPromoterprice(Double promoterprice) {
        this.promoterprice = promoterprice;
    }

    public Integer getImgcount() {
        return imgcount;
    }

    public void setImgcount(Integer imgcount) {
        this.imgcount = imgcount;
    }

    public Integer getProducttype() {
        return producttype;
    }

    public void setProducttype(Integer producttype) {
        this.producttype = producttype;
    }

    public String getDefaultimg() {
        return defaultimg;
    }

    public void setDefaultimg(String defaultimg) {
        this.defaultimg = defaultimg == null ? null : defaultimg.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

	public Double getWidth() {
		return width;
	}

	public void setWidth(Double width) {
		this.width = width;
	}

	public Double getHight() {
		return hight;
	}

	public void setHight(Double hight) {
		this.hight = hight;
	}
    
}