
package com.bbyiya.enums.calendar;

public enum ProducersStatusEnum {
	/**
     * 申请中
     */
    applying(0),
    /**
     * 不通过
     */
    no(2),
    /**
     * 退驻
     */
    tuizhu(4),
    /**
     * 通过
     */
    ok(1);

    private final int Type;

    private ProducersStatusEnum(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}
