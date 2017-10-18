package com.bbyiya.model;

import java.util.Date;

public class TiMyworkcustomers {
    private Long workid;

    private Long promoteruserid;

    private String customername;

    private String mobilephone;

    private Integer needsharecount;

    private Integer sharedcount;

    private Double needredpackettotal;

    private Double redpacketamount;

    private Date createtime;

    private Integer status;

    private Integer province;

    private Integer city;

    private Integer district;

    private String streetdetails;

    private String reciever;

    private String recieverphone;

    private String address;

    private Integer addresstype;
    
    /*******************VO*****************************/
    private Integer condition;//条件： 0分享,1收集红包
    private String codeUrl;//二维码链接
    private String createtimestr;//制作时间
    
    
    
    public String getCreatetimestr() {
		return createtimestr;
	}

	public void setCreatetimestr(String createtimestr) {
		this.createtimestr = createtimestr;
	}

	public Integer getCondition() {
		return condition;
	}

	public String getCodeUrl() {
		return codeUrl;
	}

	public void setCondition(Integer condition) {
		this.condition = condition;
	}

	public void setCodeUrl(String codeUrl) {
		this.codeUrl = codeUrl;
	}

	public Long getWorkid() {
        return workid;
    }

    public void setWorkid(Long workid) {
        this.workid = workid;
    }

    public Long getPromoteruserid() {
        return promoteruserid;
    }

    public void setPromoteruserid(Long promoteruserid) {
        this.promoteruserid = promoteruserid;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername == null ? null : customername.trim();
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone == null ? null : mobilephone.trim();
    }

    public Integer getNeedsharecount() {
        return needsharecount;
    }

    public void setNeedsharecount(Integer needsharecount) {
        this.needsharecount = needsharecount;
    }

    public Integer getSharedcount() {
        return sharedcount;
    }

    public void setSharedcount(Integer sharedcount) {
        this.sharedcount = sharedcount;
    }

    public Double getNeedredpackettotal() {
        return needredpackettotal;
    }

    public void setNeedredpackettotal(Double needredpackettotal) {
        this.needredpackettotal = needredpackettotal;
    }

    public Double getRedpacketamount() {
        return redpacketamount;
    }

    public void setRedpacketamount(Double redpacketamount) {
        this.redpacketamount = redpacketamount;
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

    public Integer getDistrict() {
        return district;
    }

    public void setDistrict(Integer district) {
        this.district = district;
    }

    public String getStreetdetails() {
        return streetdetails;
    }

    public void setStreetdetails(String streetdetails) {
        this.streetdetails = streetdetails == null ? null : streetdetails.trim();
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever == null ? null : reciever.trim();
    }

    public String getRecieverphone() {
        return recieverphone;
    }

    public void setRecieverphone(String recieverphone) {
        this.recieverphone = recieverphone == null ? null : recieverphone.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Integer getAddresstype() {
        return addresstype;
    }

    public void setAddresstype(Integer addresstype) {
        this.addresstype = addresstype;
    }
}