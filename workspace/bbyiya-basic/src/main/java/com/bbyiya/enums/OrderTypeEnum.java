package com.bbyiya.enums;

public enum OrderTypeEnum {
	 /**
     * 普通订单（C端普通下单）
     */
    nomal(0),
	/**
     * 影楼内部下单（B端用户）
     */
    brachOrder(1),
    /**
     * 台历-普通用户下单
     */
    ti_nomal(2),
    /**
     * 台历-推广者下单
     */
    ti_branchOrder(3)
    ;

    private final int Type;

    private OrderTypeEnum(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}
