package com.bbyiya.model;

import java.util.Date;

public class OOrderproductdetails {
    private Long oproductdetailid;

    private String orderproductid;

    private String printno;

    private Integer position;

    private String imageurl;

    private Date createtime;
    
    private String backimageurl;

    public Long getOproductdetailid() {
        return oproductdetailid;
    }

    public void setOproductdetailid(Long oproductdetailid) {
        this.oproductdetailid = oproductdetailid;
    }

    public String getOrderproductid() {
        return orderproductid;
    }

    public void setOrderproductid(String orderproductid) {
        this.orderproductid = orderproductid == null ? null : orderproductid.trim();
    }

    public String getPrintno() {
        return printno;
    }

    public void setPrintno(String printno) {
        this.printno = printno == null ? null : printno.trim();
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl == null ? null : imageurl.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

	public String getBackimageurl() {
		return backimageurl;
	}

	public void setBackimageurl(String backimageurl) {
		this.backimageurl = backimageurl;
	}
    
}