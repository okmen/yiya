package com.bbyiya.vo.calendar;
/**
 * 推广者员工活动权限VO
 * @author kevin
 *
 */
public class TiEmployeeActOffVo{
	
	private Long userid;

    private Long promoteruserid;

    private String name;

    private String createtimestr;

    private String phone;
    
    private int actid;//活动ID

    private Integer status;//活动负责二维码推广状态 0不负责，1负责

	public Long getUserid() {
		return userid;
	}

	public Long getPromoteruserid() {
		return promoteruserid;
	}

	public String getName() {
		return name;
	}

	public String getCreatetimestr() {
		return createtimestr;
	}

	public String getPhone() {
		return phone;
	}

	public int getActid() {
		return actid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public void setPromoteruserid(Long promoteruserid) {
		this.promoteruserid = promoteruserid;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCreatetimestr(String createtimestr) {
		this.createtimestr = createtimestr;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setActid(int actid) {
		this.actid = actid;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
   
   
	
	
}
