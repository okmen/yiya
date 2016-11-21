package com.bbyiya.enums;

public enum LoginTypeEnum {
	/**
     * qq登陆
     */
    qq(1),
    /**
     * 微信
     */
    weixin(2),
    /**
     * 新浪
     */
    sina(3),
    /**
     * 百度
     */
    baidu(4),
    /**
     * 用户名
     */
    username(5),
    /**
     * 手机号
     */
    mobilephone(6);

    private final int Type;

    private LoginTypeEnum(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}
