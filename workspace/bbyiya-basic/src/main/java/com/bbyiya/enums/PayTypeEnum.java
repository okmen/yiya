package com.bbyiya.enums;

public enum PayTypeEnum {

	/**
     * 微信支付（公众号）
     */
    weiXin(1),
	/**
     * 预存款
     */
    yiyaCash(2)
    ;

    private final int Type;

    private PayTypeEnum(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}
