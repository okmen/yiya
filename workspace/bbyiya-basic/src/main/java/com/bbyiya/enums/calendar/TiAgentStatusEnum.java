package com.bbyiya.enums.calendar;

public enum TiAgentStatusEnum {
	/**
     * 申请中
     */
    applying(0),
    /**
     * 审核通过
     */
    ok(1),
    /**
     * 不通过
     */
    no(2),
   
    /**
     * 退驻
     */
    tuizhu(4)
    ;

    private final int Type;

    private TiAgentStatusEnum(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}
