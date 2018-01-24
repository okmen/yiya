package com.bbyiya.pic.vo.calendar;

public class CalendarActivityAddParam {
	private Integer activityid;  //活动ID
	private String title;	//活动标题
	private Long productid;	//产品ID
	private Long styleId;	//款示ID
	private String description; //详情内容
	private Integer advertid;
	private Integer acttype;	//赠送方式
	private Integer freecount;	//最大参与人数
	private Integer extCount;//目标分享人数
	private String actimg;	//活动图片
	private String qrcode;
	private String qrcodeDesc;
	private Integer autoaddress;
	private String companyname;
	private Integer applylimitcount;
	private Integer zanType;
	//集赞的时间限制（小时）
	private Integer hoursEffective;
	public Integer getActivityid() {
		return activityid;
	}
	public String getTitle() {
		return title;
	}
	public Long getProductid() {
		return productid;
	}
	public Long getStyleId() {
		return styleId;
	}
	public String getDescription() {
		return description;
	}
	public Integer getActtype() {
		return acttype;
	}
	public Integer getFreecount() {
		return freecount;
	}
	public Integer getExtCount() {
		return extCount;
	}
	public void setActivityid(Integer activityid) {
		this.activityid = activityid;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setProductid(Long productid) {
		this.productid = productid;
	}
	public void setStyleId(Long styleId) {
		this.styleId = styleId;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setActtype(Integer acttype) {
		this.acttype = acttype;
	}
	public void setFreecount(Integer freecount) {
		this.freecount = freecount;
	}
	public void setExtCount(Integer extCount) {
		this.extCount = extCount;
	}
	public String getActimg() {
		return actimg;
	}
	public void setActimg(String actimg) {
		this.actimg = actimg;
	}
	public Integer getAutoaddress() {
		return autoaddress;
	}
	public void setAutoaddress(Integer autoaddress) {
		this.autoaddress = autoaddress;
	}
	public Integer getAdvertid() {
		return advertid;
	}
	public void setAdvertid(Integer advertid) {
		this.advertid = advertid;
	}
	public String getQrcode() {
		return qrcode;
	}
	public String getQrcodeDesc() {
		return qrcodeDesc;
	}
	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}
	public void setQrcodeDesc(String qrcodeDesc) {
		this.qrcodeDesc = qrcodeDesc;
	}
	public Integer getApplylimitcount() {
		return applylimitcount;
	}
	public void setApplylimitcount(Integer applylimitcount) {
		this.applylimitcount = applylimitcount;
	}
	public Integer getHoursEffective() {
		return hoursEffective;
	}
	public void setHoursEffective(Integer hoursEffective) {
		this.hoursEffective = hoursEffective;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public Integer getZanType() {
		return zanType;
	}
	public void setZanType(Integer zanType) {
		this.zanType = zanType;
	}
	
	
	
}
