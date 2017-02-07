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
	 //头像
    private String headImg;
    //昵称
    private String nickName;
    //用户手机
    private String mobilePhone;
    //用户签名
    private String sign;
    //用户生日
    private String birthday;
    
    //用户状态
    private Integer status;
    
    private Integer isTester;
    
    //是否有填写宝宝信息
    private int haveBabyInfo;
    //宝宝信息
    private UChildInfo babyInfo;
    /**
     * 用户身份标示
     */
    private Long identity;
    
    private int isEmployee;
    private int isBranchBussiness;
    //登陆票据
    private String ticket;
    //第三方注册标记
    private String register_token;
	
	
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
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public Integer getIsTester() {
		return isTester;
	}
	public void setIsTester(Integer isTester) {
		this.isTester = isTester;
	}
	public int getIsEmployee() {
		return isEmployee;
	}
	public void setIsEmployee(int isEmployee) {
		this.isEmployee = isEmployee;
	}
	public int getIsBranchBussiness() {
		return isBranchBussiness;
	}
	public void setIsBranchBussiness(int isBranchBussiness) {
		this.isBranchBussiness = isBranchBussiness;
	}
	

	
	
}
