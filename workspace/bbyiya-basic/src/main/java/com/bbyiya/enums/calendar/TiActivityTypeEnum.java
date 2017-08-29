package com.bbyiya.enums.calendar;

public enum TiActivityTypeEnum {
	/**
     * 一对一
     */
    toOne(0),
	/**
     * 整体
     */
    toAll(1);
    
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