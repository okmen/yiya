package com.bbyiya.enums;

public enum PayTypeEnum {

	/**
     * 微信支付（公众号）
     */
    weiXin(1),
	/**
     * 预存款（企业账户）
     */
    yiyaCash(2),
    /**
     * 钱包支付（普通用户）
     */
    walletPay(3)
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
