package com.bbyiya.common.enums;

public enum MsgStatusEnums {
	/**
     * 验证码可用
     */
    ok(1),
    /**
     * 验证码错误
     */
    wrong(-1),
    /**
     * 验证码失效
     */
    invalid(-2)
    ;

    private final int Type;

    private MsgStatusEnums(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}
