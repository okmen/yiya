package com.bbyiya.model;

import java.util.Date;

public class UBranches {
    private Long branchuserid;

    private String branchcompanyname;

    private Long agentuserid;

    private String username;
    private String contactname;

    private String phone;

    private Integer province;

    private Integer city;

    private Integer area;

    private String streetdetail;

    private String businesslicense;

    private String businessscope;

    private String shopimg;

    private String teamimg;

    private String remark;

    private Integer status;

    private Date createtime;

    private Date processtime;

    public Long getBranchuserid() {
        return branchuserid;
    }

    public void setBranchuserid(Long branchuserid) {
        this.branchuserid = branchuserid;
    }

    public String getBranchcompanyname() {
        return branchcompanyname;
    }

    public void setBranchcompanyname(String branchcompanyname) {
        this.branchcompanyname = branchcompanyname == null ? null : branchcompanyname.trim();
    }

    public Long getAgentuserid() {
        return agentuserid;
    }

    public void setAgentuserid(Long agentuserid) {
        this.agentuserid = agentuserid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
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

    public String getStreetdetail() {
        return streetdetail;
    }

    public void setStreetdetail(String streetdetail) {
        this.streetdetail = streetdetail == null ? null : streetdetail.trim();
    }

    public String getBusinesslicense() {
        return businesslicense;
    }

    public void setBusinesslicense(String businesslicense) {
        this.businesslicense = businesslicense == null ? null : businesslicense.trim();
    }

    public String getBusinessscope() {
        return businessscope;
    }

    public void setBusinessscope(String businessscope) {
        this.businessscope = businessscope == null ? null : businessscope.trim();
    }

    public String getShopimg() {
        return shopimg;
    }

    public void setShopimg(String shopimg) {
        this.shopimg = shopimg == null ? null : shopimg.trim();
    }

    public String getTeamimg() {
        return teamimg;
    }

    public void setTeamimg(String teamimg) {
        this.teamimg = teamimg == null ? null : teamimg.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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

    public Date getProcesstime() {
        return processtime;
    }

    public void setProcesstime(Date processtime) {
        this.processtime = processtime;
    }

	public String getContactname() {
		return contactname;
	}

	public void setContactname(String contactname) {
		this.contactname = contactname;
	}
    
}