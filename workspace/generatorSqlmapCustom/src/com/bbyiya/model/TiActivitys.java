package com.bbyiya.model;

import java.util.Date;

public class TiActivitys {
    private Integer actid;

    private String title;

    private Long produceruserid;

    private Long productid;

    private Long styleid;

    private Integer freecount;

    private Integer acttype;

    private Integer extcount;

    private Integer applycount;

    private Integer completecount;

    private Integer status;

    private Date createtime;

    private String remark;

    private String description;

    public Integer getActid() {
        return actid;
    }

    public void setActid(Integer actid) {
        this.actid = actid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Long getProduceruserid() {
        return produceruserid;
    }

    public void setProduceruserid(Long produceruserid) {
        this.produceruserid = produceruserid;
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

    public Integer getFreecount() {
        return freecount;
    }

    public void setFreecount(Integer freecount) {
        this.freecount = freecount;
    }

    public Integer getActtype() {
        return acttype;
    }

    public void setActtype(Integer acttype) {
        this.acttype = acttype;
    }

    public Integer getExtcount() {
        return extcount;
    }

    public void setExtcount(Integer extcount) {
        this.extcount = extcount;
    }

    public Integer getApplycount() {
        return applycount;
    }

    public void setApplycount(Integer applycount) {
        this.applycount = applycount;
    }

    public Integer getCompletecount() {
        return completecount;
    }

    public void setCompletecount(Integer completecount) {
        this.completecount = completecount;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}