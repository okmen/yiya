package com.bbyiya.enums;

public enum MyProductTempType {
	/**
     * 普通类型
     */
    normal(0),
    /**
     * 指定手机号类型
     */
    mobile(1),
    /**
     * 活动码 邀请
     */
    code(2);

    private final int Type;

    private MyProductTempType(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}
