package com.bbyiya.enums.user;

public enum UserStatusEnum {
	/**
     * 正常用户
     */
    ok(1),
    /**
     * 未设置密码 （未绑定userId的用户）
     */
    noPwd(2);

    private final int Type;

    private UserStatusEnum(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}
