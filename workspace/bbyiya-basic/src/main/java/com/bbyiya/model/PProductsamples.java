package com.bbyiya.model;

import java.io.Serializable;
import java.util.Date;

public class PProductsamples  implements Serializable{
	private static final long serialVersionUID = 1L;
    private Integer sampleid;

    private Long productid;

    private String sampleimg;

    private Long cartid;

    private Date createtime;

    private Integer status;

    public Integer getSampleid() {
        return sampleid;
    }

    public void setSampleid(Integer sampleid) {
        this.sampleid = sampleid;
    }

    public Long getProductid() {
        return productid;
    }

    public void setProductid(Long productid) {
        this.productid = productid;
    }

    public String getSampleimg() {
        return sampleimg;
    }

    public void setSampleimg(String sampleimg) {
        this.sampleimg = sampleimg == null ? null : sampleimg.trim();
    }

    public Long getCartid() {
        return cartid;
    }

    public void setCartid(Long cartid) {
        this.cartid = cartid;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}