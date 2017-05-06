package com.bbyiya.enums;

public enum LoginTypeEnum {
	/**
     * 手机号登陆
     */
	mobilephone(1),
    /**
     * 微信
     */
    weixin(2);

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
