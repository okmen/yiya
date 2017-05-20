package com.bbyiya.enums.pic;

public enum MyProducttempApplyStatusEnum {
	/**
     * 待审核
     */
    apply(0),
	/**
     * 已审核
     */
    ok(1),
    /**
     * 已拒绝
     */
    refuse(2);
    private final int Type;

    private MyProducttempApplyStatusEnum(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}