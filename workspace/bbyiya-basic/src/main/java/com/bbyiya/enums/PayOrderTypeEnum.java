package com.bbyiya.enums;

public enum PayOrderTypeEnum {
	/**
     * 购物
     */
    gouwu(0),
	/**
     * 充值
     */
    chongzhi(2),
    ;

    private final int Type;

    private PayOrderTypeEnum(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}
