package com.bbyiya.enums.calendar;

public enum TiActivityTypeEnum {
	
	/**
     * 整体
     */
    toAll(0),
    /**
     * 一对一
     */
    toOne(1),
    /**
     * 兑换码活动
     */
    exchangeCode(2)
    ;
    
    private final int Type;

    private TiActivityTypeEnum(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}