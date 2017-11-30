package com.bbyiya.enums.calendar;

public enum GroupActivityType {

	/**
     * 普通分销模式
     */
    nomal(0),
    /**
     * 广告模式
     */
    advert(1);
    
    private final int Type;

    private GroupActivityType(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}
