package com.bbyiya.enums.calendar;

public enum ActivityStatusEnums {
	/**
     * 活动未开启
     */
    notStart(0),
    /**
     * 活动中
     */
    ok(1),
    /**
     * 活动结束
     */
    end(2)
    ;
    
    private final int Type;

    private ActivityStatusEnums(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}
