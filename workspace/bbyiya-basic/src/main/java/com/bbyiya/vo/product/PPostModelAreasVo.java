package com.bbyiya.vo.product;

import java.util.Date;

public class PPostModelAreasVo {
	
	private Integer postid;

    private Integer postmodelid;
    private String postModelName;

    private Integer areacode;

    private String areaname;

    private Double amount;

    private Date createtime;

	public Integer getPostid() {
		return postid;
	}

	public void setPostid(Integer postid) {
		this.postid = postid;
	}

	public Integer getPostmodelid() {
		return postmodelid;
	}

	public void setPostmodelid(Integer postmodelid) {
		this.postmodelid = postmodelid;
	}

	public String getPostModelName() {
		return postModelName;
	}

	public void setPostModelName(String postModelName) {
		this.postModelName = postModelName;
	}

	public Integer getAreacode() {
		return areacode;
	}

	public void setAreacode(Integer areacode) {
		this.areacode = areacode;
	}

	public String getAreaname() {
		return areaname;
	}

	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	
	
}
