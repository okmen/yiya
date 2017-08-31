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
    
    /*-----------实体扩展字段-------------------*/
    /**
     * 优惠类型（1折扣，2现金优惠）
     */
    private Integer discountType;
    /**
     * 具体优惠（如：当discountType=1时 discount=0.5表示5折，当discountType=2时，discount=5表示优惠5元）
     */
    private Double discount;

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

	public Integer getDiscountType() {
		return discountType;
	}

	public void setDiscountType(Integer discountType) {
		this.discountType = discountType;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getImgjson() {
		return imgjson;
	}

	public void setImgjson(String imgjson) {
		this.imgjson = imgjson;
	}
    
    
}