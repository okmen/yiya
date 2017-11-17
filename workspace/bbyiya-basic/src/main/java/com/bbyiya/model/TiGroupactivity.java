package com.bbyiya.model;

import java.util.Date;

public class TiGroupactivity {
	private Integer gactid;

    private Long promoteruserid;

    private String linkurl;

    private Integer advertid;

    private String title;

    private String description;

    private String companyname;

    private String logo;

    private String reciver;

    private String mobilephone;

    private Integer province;

    private Integer city;

    private Integer area;

    private String streetdetails;

    private String address;

    private Integer status;

    private Date createtime;

    private Integer praisecount;

    private Double timespare;
    
    /*****************************VO*************************/
    private Integer sellercount;//销售量
    private Integer actclickcount;//活动点击量
    private Integer advertcount;//广告浏览量
    

    public Integer getGactid() {
        return gactid;
    }

    public void setGactid(Integer gactid) {
        this.gactid = gactid;
    }

    public Long getPromoteruserid() {
        return promoteruserid;
    }

    public void setPromoteruserid(Long promoteruserid) {
        this.promoteruserid = promoteruserid;
    }

    public String getLinkurl() {
        return linkurl;
    }

    public void setLinkurl(String linkurl) {
        this.linkurl = linkurl == null ? null : linkurl.trim();
    }

    public Integer getAdvertid() {
        return advertid;
    }

    public void setAdvertid(Integer advertid) {
        this.advertid = advertid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname == null ? null : companyname.trim();
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo == null ? null : logo.trim();
    }

    public String getReciver() {
        return reciver;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver == null ? null : reciver.trim();
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone == null ? null : mobilephone.trim();
    }

    public Integer getProvince() {
        return province;
    }

    public void setProvince(Integer province) {
        this.province = province;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public String getStreetdetails() {
        return streetdetails;
    }

    public void setStreetdetails(String streetdetails) {
        this.streetdetails = streetdetails == null ? null : streetdetails.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
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

    public Integer getPraisecount() {
        return praisecount;
    }

    public void setPraisecount(Integer praisecount) {
        this.praisecount = praisecount;
    }

   

	public Integer getSellercount() {
		return sellercount;
	}

	public Integer getActclickcount() {
		return actclickcount;
	}

	public Integer getAdvertcount() {
		return advertcount;
	}

	public void setSellercount(Integer sellercount) {
		this.sellercount = sellercount;
	}

	public void setActclickcount(Integer actclickcount) {
		this.actclickcount = actclickcount;
	}

	public void setAdvertcount(Integer advertcount) {
		this.advertcount = advertcount;
	}
    public Double getTimespare() {
        return timespare;
    }

    public void setTimespare(Double timespare) {
        this.timespare = timespare;
    }
    
    
}