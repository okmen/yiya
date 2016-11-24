package com.bbyiya.common.enums;

public enum SendMsgEnums {
	/**
     * 用户注册
     */
    register(1),
    /**
     * 用户登陆
     */
    login(2);

    private final int Type;

    private SendMsgEnums(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}
