package com.bbyiya.model;

import java.io.Serializable;
import java.util.Date;

public class UUsers implements Serializable{

	private static final long serialVersionUID = 1L;
	
    private Long userid;

    private String password;

    private String mobilephone;

    private Integer mobilebind;

    private Integer status;

    private Long identity;

    private String userimg;

    private String nickname;

    private String email;

    private Date createtime;
    
    private String createtimestr;

    private String sign;

    private Date birthday;
    
    private Long upuserid;
    
    private Long sourseuserid;
    
    private String birthdaystr;

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone == null ? null : mobilephone.trim();
    }

    public Integer getMobilebind() {
        return mobilebind;
    }

    public void setMobilebind(Integer mobilebind) {
        this.mobilebind = mobilebind;
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

    public String getUserimg() {
        return userimg;
    }

    public void setUserimg(String userimg) {
        this.userimg = userimg == null ? null : userimg.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign == null ? null : sign.trim();
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

	public Long getUpuserid() {
		return upuserid;
	}

	public void setUpuserid(Long upuserid) {
		this.upuserid = upuserid;
	}

	public String getCreatetimestr() {
		return createtimestr;
	}

	public void setCreatetimestr(String createtimestr) {
		this.createtimestr = createtimestr;
	}

	public Long getSourseuserid() {
		return sourseuserid;
	}

	public void setSourseuserid(Long sourseuserid) {
		this.sourseuserid = sourseuserid;
	}

	public String getBirthdaystr() {
		return birthdaystr;
	}

	public void setBirthdaystr(String birthdaystr) {
		this.birthdaystr = birthdaystr;
	}
	
    
}