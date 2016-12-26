package com.bbyiya.enums.user;

public enum UserIdentityEnums {
	/**
     * 分销商/影楼
     */
    branch(2),
    /**
     * 顶级分销人
     */
    topUser(4);

    private final int Type;

    private UserIdentityEnums(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}
