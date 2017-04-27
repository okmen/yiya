package com.bbyiya.enums.pic;

public enum BranchStatusEnum {
	/**
     * 申请中
     */
    applying(0),
    /**
     * 不通过
     */
    no(2),
    /**
     * 退驻
     */
    tuizhu(4),
    /**
     * 通过， 交了代理费(成为代理商)
     */
    ok(1)
    ;

    private final int Type;

    private BranchStatusEnum(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}
