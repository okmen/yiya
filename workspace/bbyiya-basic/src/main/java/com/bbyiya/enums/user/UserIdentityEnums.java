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
    pbs(16),
    /**
     * 流量主 
     */
    wei(32),
    /**
     * cts管理员
     */
    cts_admin(64),
    /**
     * cts组员
     */
    cts_member(128),
    /*-----------------台历模块-角色身份----------------------------*/
    /**
     * 台历-代理商
     */
    ti_agent(256),
    /**
     * 台历-生产商
     */
    ti_producer(512),
    /**
     * 台历-推广者（影楼）
     */
    ti_promoter(1024),
    /**
     * 员工
     */
    ti_employees(2048)
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
