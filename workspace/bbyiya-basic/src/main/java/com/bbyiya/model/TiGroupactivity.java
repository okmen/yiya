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

    private Long timespare;

    private Integer browsecount;

    private Integer type;

    private Integer tempid;

    private String titleshare;

    private String titleminshare;
    
    private Integer addresstype;
    
    /*****************************VO*************************/
    private Integer sellercount;//销售量
    private Integer advertbrowsecount;//广告浏览量
    private String createtimestr;
    private double shareratio; //用户分享率
    private double sellratio; //销售转化率
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

	
	public void setSellercount(Integer sellercount) {
		this.sellercount = sellercount;
	}

	
	public Integer getAdvertbrowsecount() {
		return advertbrowsecount;
	}

	public void setAdvertbrowsecount(Integer advertbrowsecount) {
		this.advertbrowsecount = advertbrowsecount;
	}

	public Long getTimespare() {
		return timespare;
	}

	public void setTimespare(Long timespare) {
		this.timespare = timespare;
	}

	public Integer getBrowsecount() {
		return browsecount;
	}

	public void setBrowsecount(Integer browsecount) {
		this.browsecount = browsecount;
	}

	public String getCreatetimestr() {
		return createtimestr;
	}

	public void setCreatetimestr(String createtimestr) {
		this.createtimestr = createtimestr;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getTempid() {
		return tempid;
	}

	public void setTempid(Integer tempid) {
		this.tempid = tempid;
	}

	public String getTitleshare() {
		return titleshare;
	}

	public void setTitleshare(String titleshare) {
		this.titleshare = titleshare;
	}

	public String getTitleminshare() {
		return titleminshare;
	}

	public void setTitleminshare(String titleminshare) {
		this.titleminshare = titleminshare;
	}

	public double getShareratio() {
		return shareratio;
	}

	public double getSellratio() {
		return sellratio;
	}

	public void setShareratio(double shareratio) {
		this.shareratio = shareratio;
	}

	public void setSellratio(double sellratio) {
		this.sellratio = sellratio;
	}

	public Integer getAddresstype() {
		return addresstype;
	}

	public void setAddresstype(Integer addresstype) {
		this.addresstype = addresstype;
	}
    
    
    
}