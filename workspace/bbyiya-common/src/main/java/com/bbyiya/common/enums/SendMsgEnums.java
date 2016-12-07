package com.bbyiya.common.enums;

public enum SendMsgEnums {
	/**
     * 用户注册 value= 1
     */
    register(1),
    /**
     * 用户登陆 value =2
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
