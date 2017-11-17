package com.bbyiya.enums.calendar;

public enum GroupActWorkStatus {
	/**
     * 申请中
     */
    apply(0),
    
    /**
     * 已支付
     */
    payed(1);

    private final int Type;

    private GroupActWorkStatus(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}
