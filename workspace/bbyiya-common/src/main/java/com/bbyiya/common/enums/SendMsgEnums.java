package com.bbyiya.common.enums;

public enum SendMsgEnums {
	/**
     * 用户注册 value= 1
     */
    register(1),
    /**
     * 找回密码（重置密码）
     */
    backPwd(2),
    /**
     * 用户登陆 value =2
     */
    login(3)
    
    ;

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
