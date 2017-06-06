package com.bbyiya.model;

import java.util.Date;

public class OOrderproductphotos {
    private Long odetailid;

    private String orderproductid;

    private String imgurl;

    private String title;

    private String content;

    private String senendes;

    private Integer sort;

    private Date createtime;

    public Long getOdetailid() {
        return odetailid;
    }

    public void setOdetailid(Long odetailid) {
        this.odetailid = odetailid;
    }

    public String getOrderproductid() {
        return orderproductid;
    }

    public void setOrderproductid(String orderproductid) {
        this.orderproductid = orderproductid == null ? null : orderproductid.trim();
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl == null ? null : imgurl.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getSenendes() {
        return senendes;
    }

    public void setSenendes(String senendes) {
        this.senendes = senendes == null ? null : senendes.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}