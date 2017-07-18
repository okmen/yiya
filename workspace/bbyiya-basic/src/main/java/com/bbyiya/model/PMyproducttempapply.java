package com.bbyiya.model;

import java.util.Date;

public class PMyproducttempapply {
	private Long tempapplyid;

	private Integer tempid;

	private Long userid;

	private String mobilephone;

	private Integer province;

	private Integer city;

	private Integer area;

	private String street;

	private String adress;

	private Integer isdue;

	private Long companyuserid;

	private Integer status;

	private Date createtime;

	private Date verfiytime;
	
	private Date completetime;

	private Date birthday;
	private Integer isread;
	private String receiver;
	private Long cartid;
	private String reason;
	private Integer sort;
	private Long styleid;

    private Long productid;

	/***************** 添加的VO字段 *************************/

	private String tempName;
//	private Long cartId;
	private String createtimestr;
	private String verfiytimestr;
	private String birthdaystr;
	private String username;// 客户名称
	private String styleImg;

	public Long getTempapplyid() {
		return tempapplyid;
	}

	public void setTempapplyid(Long tempapplyid) {
		this.tempapplyid = tempapplyid;
	}

	public Integer getTempid() {
		return tempid;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public void setTempid(Integer tempid) {
		this.tempid = tempid;
	}

	public Long getUserid() {
		return userid;
	}

	public Long getCartid() {
		return cartid;
	}

	public void setCartid(Long cartid) {
		this.cartid = cartid;
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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street == null ? null : street.trim();
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress == null ? null : adress.trim();
	}

	public Integer getIsdue() {
		return isdue;
	}

	public void setIsdue(Integer isdue) {
		this.isdue = isdue;
	}

	public Long getCompanyuserid() {
		return companyuserid;
	}

	public void setCompanyuserid(Long companyuserid) {
		this.companyuserid = companyuserid;
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

	public Date getVerfiytime() {
		return verfiytime;
	}

	public void setVerfiytime(Date verfiytime) {
		this.verfiytime = verfiytime;
	}

	public String getCreatetimestr() {
		return createtimestr;
	}

	public void setCreatetimestr(String createtimestr) {
		this.createtimestr = createtimestr;
	}

	public String getVerfiytimestr() {
		return verfiytimestr;
	}

	public void setVerfiytimestr(String verfiytimestr) {
		this.verfiytimestr = verfiytimestr;
	}
	
    public Date getCompletetime() {
        return completetime;
    }

    public void setCompletetime(Date completetime) {
        this.completetime = completetime;
    }
    
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getBirthdaystr() {
		return birthdaystr;
	}

	public void setBirthdaystr(String birthdaystr) {
		this.birthdaystr = birthdaystr;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getIsread() {
		return isread;
	}

	public void setIsread(Integer isread) {
		this.isread = isread;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getTempName() {
		return tempName;
	}

	public void setTempName(String tempName) {
		this.tempName = tempName;
	}

//	public Long getCartId() {
//		return cartId;
//	}
//
//	public void setCartId(Long cartId) {
//		this.cartId = cartId;
//	}

	public String getStyleImg() {
		return styleImg;
	}

	public void setStyleImg(String styleImg) {
		this.styleImg = styleImg;
	}
	public Long getStyleid() {
        return styleid;
    }

    public void setStyleid(Long styleid) {
        this.styleid = styleid;
    }

    public Long getProductid() {
        return productid;
    }

    public void setProductid(Long productid) {
        this.productid = productid;
    }

}