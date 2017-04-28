package com.bbyiya.enums.pic;

public enum AgentStatusEnum {
	/**
     * 申请中
     */
    applying(0),
    /**
     * 审核通过，等待缴费
     */
    pass(3),
    /**
     * 不通过
     */
    no(2),
    /**
     * 交了代理费(成为代理商)
     */
    ok(1)
    ;

    private final int Type;

    private AgentStatusEnum(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}
