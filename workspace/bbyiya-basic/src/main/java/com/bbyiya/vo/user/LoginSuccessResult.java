package com.bbyiya.vo.user;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户基本信息
 * 用户登陆成功后
 * @author Administrator
 *
 */
public class LoginSuccessResult  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long userId;
	//用户名
    private String userName;
    //用户手机
    private String mobilePhone;
    //是否绑定手机
    private Integer mobileBind;
    //用户状态
    private Integer status;
    /**
     * 用户身份标示
     */
    private Long identity;
    //头像
    private String headImg;
    //昵称
    private String nickName;
    //邮箱
    private String email;
    //
    private Date createTime;
    //登陆票据
    private String ticket;
    
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public Integer getMobileBind() {
		return mobileBind;
	}
	public void setMobileBind(Integer mobileBind) {
		this.mobileBind = mobileBind;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getIdentity() {
		return identity;
	}
	public void setIdentity(Long identity) {
		this.identity = identity;
	}
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	
	
}
