package com.bbyiya.vo.user;

public class OtherLoginParam implements java.io.Serializable{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String headImg;//头像
    private Short loginType;//登陆类型
    private String nickName;//用户名
    private String openId;//openID第三方登陆的标识

    public String getHeadImg()
    {
        return headImg;
    }

    public void setHeadImg(String headImg)
    {
        this.headImg = headImg;
    }

    public Short getLoginType()
    {
        return loginType;
    }

    public void setLoginType(Short loginType)
    {
        this.loginType = loginType;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

    
}
