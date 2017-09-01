package com.bbyiya.model;

import java.util.Date;

public class TiDiscountdetails {
    private Integer id;

    private Integer discountid;

    private Double discount;

    private Long styleid;

    private Long productid;

    private Date createtime;
    
//    private Integer discountType;
//    
//    private String dicountName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDiscountid() {
        return discountid;
    }

    public void setDiscountid(Integer discountid) {
        this.discountid = discountid;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

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

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

	
    
    
}