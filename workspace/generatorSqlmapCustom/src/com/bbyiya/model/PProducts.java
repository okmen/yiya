package com.bbyiya.model;

import java.math.BigDecimal;
import java.util.Date;

public class PProducts {
    private Long productid;

    private Long userid;

    private String defaultimg;

    private String title;

    private BigDecimal oldprice;

    private BigDecimal price;

    private Date createtime;

    private Date updatetime;

    private Integer status;

    private Integer type;

    private Integer postmodelid;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public BigDecimal getOldprice() {
        return oldprice;
    }

    public void setOldprice(BigDecimal oldprice) {
        this.oldprice = oldprice;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPostmodelid() {
        return postmodelid;
    }

    public void setPostmodelid(Integer postmodelid) {
        this.postmodelid = postmodelid;
    }
}