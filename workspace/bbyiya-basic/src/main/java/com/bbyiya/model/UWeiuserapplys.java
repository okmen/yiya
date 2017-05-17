package com.bbyiya.model;

import java.util.Date;

public class UWeiuserapplys {
    private Long userid;

    private String mobilephone;

    private String name;

    private String certificateimg;

    private Date createtime;

    private Integer status;

    private String remark;

    private Date processtime;
    
    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone == null ? null : mobilephone.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCertificateimg() {
        return certificateimg;
    }

    public void setCertificateimg(String certificateimg) {
        this.certificateimg = certificateimg == null ? null : certificateimg.trim();
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getProcesstime() {
        return processtime;
    }

    public void setProcesstime(Date processtime) {
        this.processtime = processtime;
    }
}