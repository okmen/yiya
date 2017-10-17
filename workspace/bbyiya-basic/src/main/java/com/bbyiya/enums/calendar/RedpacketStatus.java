package com.bbyiya.enums.calendar;

public enum RedpacketStatus {
	/**
     * 未支付
     */
    nopay(0),
    /**
     * 已支付
     */
    payed(1),
    /**
     * 无效的
     */
    invalid(2)
    
    ;
    
    private final int Type;

    private RedpacketStatus(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}
