package com.bbyiya.vo.agent;

public class UBranchUserTempVo{
	private Long userid;

    private String name;

    private String phone;

    private Long branchuserid;

    private Long agentuserid;

    private Integer status;//活动负责状态 0 不负责，1负责

    private String createtime;
    
    private int tempid;//模板
    private int applycount;//报名人数
    private int passcount;//活动通过人数
    private String codeurl;//活动二维码
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Long getBranchuserid() {
		return branchuserid;
	}
	public void setBranchuserid(Long branchuserid) {
		this.branchuserid = branchuserid;
	}
	public Long getAgentuserid() {
		return agentuserid;
	}
	public void setAgentuserid(Long agentuserid) {
		this.agentuserid = agentuserid;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public int getTempid() {
		return tempid;
	}
	public void setTempid(int tempid) {
		this.tempid = tempid;
	}
	
	public int getApplycount() {
		return applycount;
	}
	public void setApplycount(int applycount) {
		this.applycount = applycount;
	}
	public int getPasscount() {
		return passcount;
	}
	public void setPasscount(int passcount) {
		this.passcount = passcount;
	}
	public String getCodeurl() {
		return codeurl;
	}
	public void setCodeurl(String codeurl) {
		this.codeurl = codeurl;
	}
    
    
    
	
	
}
