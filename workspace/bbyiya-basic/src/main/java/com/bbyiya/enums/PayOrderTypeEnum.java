package com.bbyiya.enums;

public enum PayOrderTypeEnum {
	/**
     * 购物
     */
    gouwu(0),
	/**
     * 供应商货款充值
     */
    chongzhi(2),
    /**
     * 邮费充值
     */
    postage(3) 
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
