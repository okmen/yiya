package com.bbyiya.model;

import java.util.Date;
import java.util.List;

public class TiPromoteradvertinfo {
	private Integer advertid;

	private Long promoteruserid;

	private String defaultimg;

	private String description;

	private Integer openapplication;

	private Integer status;

	private Date createtime;

	private Date updatetime;

	private Integer isdefault;

    private Integer readcount;

	private List<TiPromoteradvertimgs> imglist;

	private String updatetimestr;


	public Integer getAdvertid() {
		return advertid;
	}

	public void setAdvertid(Integer advertid) {
		this.advertid = advertid;
	}

	public Long getPromoteruserid() {
		return promoteruserid;
	}

	public void setPromoteruserid(Long promoteruserid) {
		this.promoteruserid = promoteruserid;
	}

	public String getDefaultimg() {
		return defaultimg;
	}

	public void setDefaultimg(String defaultimg) {
		this.defaultimg = defaultimg == null ? null : defaultimg.trim();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description == null ? null : description.trim();
	}

	public Integer getOpenapplication() {
		return openapplication;
	}

	public void setOpenapplication(Integer openapplication) {
		this.openapplication = openapplication;
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

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public List<TiPromoteradvertimgs> getImglist() {
		return imglist;
	}

	public void setImglist(List<TiPromoteradvertimgs> imglist) {
		this.imglist = imglist;
	}

	public String getUpdatetimestr() {
		return updatetimestr;
	}

	public void setUpdatetimestr(String updatetimestr) {
		this.updatetimestr = updatetimestr;
	}

	public Integer getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(Integer isdefault) {
		this.isdefault = isdefault;
	}

	public Integer getReadcount() {
		return readcount;
	}

	public void setReadcount(Integer readcount) {
		this.readcount = readcount;
	}


}