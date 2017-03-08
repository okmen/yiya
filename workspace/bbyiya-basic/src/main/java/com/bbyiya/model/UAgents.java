package com.bbyiya.model;

import java.util.Date;

public class UAgents {
    private Long agentuserid;

    private String agentcompanyname;

    private String contactname;
    private String username;

    private String phone;

    private Integer province;

    private Integer city;

    private Integer area;

    private String streetdetail;

    private Integer status;

    private Date createtime;

    private Date processtime;

    public Long getAgentuserid() {
        return agentuserid;
    }

    public void setAgentuserid(Long agentuserid) {
        this.agentuserid = agentuserid;
    }

    public String getAgentcompanyname() {
        return agentcompanyname;
    }

    public void setAgentcompanyname(String agentcompanyname) {
        this.agentcompanyname = agentcompanyname == null ? null : agentcompanyname.trim();
    }

    public String getContactname() {
        return contactname;
    }

    public void setContactname(String contactname) {
        this.contactname = contactname == null ? null : contactname.trim();
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
    
}