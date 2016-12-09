package com.bbyiya.vo.user;

import java.io.Serializable;

/**
 * 用户基本信息
 * 用户登陆成功后
 * @author Administrator
 *
 */
public class LoginSuccessResult  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long userId;
	
    //用户手机
    private String mobilePhone;
    //用户状态
    private Integer status;
    //是否有填写宝宝信息
    private int haveBabyInfo;
    /**
     * 用户身份标示
     */
    private Long identity;
    //头像
    private String headImg;
    //昵称
    private String nickName;
    //登陆票据
    private String ticket;
    
    //第三方注册标记
    private String register_token;
    //宝宝信息
    private UChildInfo babyInfo;
	
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
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
	
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public UChildInfo getBabyInfo() {
		return babyInfo;
	}
	public void setBabyInfo(UChildInfo babyInfo) {
		this.babyInfo = babyInfo;
	}
	public String getRegister_token() {
		return register_token;
	}
	public void setRegister_token(String register_token) {
		this.register_token = register_token;
	}
	public int getHaveBabyInfo() {
		return haveBabyInfo;
	}
	public void setHaveBabyInfo(int haveBabyInfo) {
		this.haveBabyInfo = haveBabyInfo;
	}
	

	
	
}
