package com.bbyiya.enums.user;

public enum UserIdentityEnums {
	/**
     * 代理商
     */
    agent(2),
    /**
     * 影楼
     */
    branch(4),
    
    /**
     * 影楼分销人员
     */
    salesman(8),
    /**
     * 生产端用户
     */
    pbs(16)
    ;

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
