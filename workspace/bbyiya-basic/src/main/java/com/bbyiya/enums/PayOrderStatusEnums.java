package com.bbyiya.enums;

public enum PayOrderStatusEnums {
	/**
     * 等待支付
     */
    noPay(0),
	/**
     * 已支付
     */
    payed(1),
    /**
     * 失效
     */
    expired(2),
    /**
     * 支付回写失败（错误订单）
     */
    failed(3),
    ;

    private final int Type;

    private PayOrderStatusEnums(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}
