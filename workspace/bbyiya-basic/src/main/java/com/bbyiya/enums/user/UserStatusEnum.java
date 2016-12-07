package com.bbyiya.enums.user;

public enum UserStatusEnum {
	/**
     * 正常用户
     */
    ok(1),
    /**
     * 未设置密码 （未绑定userId的用户）
     */
    noPwd(2),
    /**
     * 未填写宝宝信息
     */
    noChirlInfo(3);

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
