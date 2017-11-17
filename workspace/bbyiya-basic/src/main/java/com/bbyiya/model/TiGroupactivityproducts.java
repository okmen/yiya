package com.bbyiya.model;

import java.math.BigDecimal;

public class TiGroupactivityproducts {
    private Long id;

    private Integer gactid;

    private Long productid;

    private BigDecimal price;

    private Integer status;

    //--------------非数据库字段-------------------------
    private String title;
    private Double originPrice;
    private String defaultimg;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGactid() {
        return gactid;
    }

    public void setGactid(Integer gactid) {
        this.gactid = gactid;
    }

    public Long getProductid() {
        return productid;
    }

    public void setProductid(Long productid) {
        this.productid = productid;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Double getOriginPrice() {
		return originPrice;
	}

	public void setOriginPrice(Double originPrice) {
		this.originPrice = originPrice;
	}

	public String getDefaultimg() {
		return defaultimg;
	}

	public void setDefaultimg(String defaultimg) {
		this.defaultimg = defaultimg;
	}
    
    
}