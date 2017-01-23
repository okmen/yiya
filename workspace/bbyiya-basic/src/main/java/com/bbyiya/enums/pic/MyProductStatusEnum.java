package com.bbyiya.enums.pic;

public enum MyProductStatusEnum {

	/**
     * 制作中
     */
    ok(1),
    /**
     * 已下单
     */
    ordered(2)
    ;

    private final int Type;

    private MyProductStatusEnum(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}
