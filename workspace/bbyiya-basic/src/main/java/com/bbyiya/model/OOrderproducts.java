package com.bbyiya.model;

import java.io.Serializable;


public class OOrderproducts implements Serializable{
	private static final long serialVersionUID = 1L;
	
    private String orderproductid;

    private String userorderid;

    private Long productid;

    private Long styleid;

    private Long buyeruserid;

    private String producttitle;

    private Double price;

    private Long branchuserid;

    private Long salesuserid;

    private String propertystr;

    private Integer count;

    public String getOrderproductid() {
        return orderproductid;
    }

    public void setOrderproductid(String orderproductid) {
        this.orderproductid = orderproductid == null ? null : orderproductid.trim();
    }

    public String getUserorderid() {
        return userorderid;
    }

    public void setUserorderid(String userorderid) {
        this.userorderid = userorderid == null ? null : userorderid.trim();
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

    public Long getBuyeruserid() {
        return buyeruserid;
    }

    public void setBuyeruserid(Long buyeruserid) {
        this.buyeruserid = buyeruserid;
    }

    public String getProducttitle() {
        return producttitle;
    }

    public void setProducttitle(String producttitle) {
        this.producttitle = producttitle == null ? null : producttitle.trim();
    }

  

    public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Long getBranchuserid() {
        return branchuserid;
    }

    public void setBranchuserid(Long branchuserid) {
        this.branchuserid = branchuserid;
    }

    public Long getSalesuserid() {
        return salesuserid;
    }

    public void setSalesuserid(Long salesuserid) {
        this.salesuserid = salesuserid;
    }

    public String getPropertystr() {
        return propertystr;
    }

    public void setPropertystr(String propertystr) {
        this.propertystr = propertystr == null ? null : propertystr.trim();
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}