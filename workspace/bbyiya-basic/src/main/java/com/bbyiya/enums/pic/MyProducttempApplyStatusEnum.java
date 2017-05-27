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
    refuse(2),
    
    /**
     * 作品制作完成
     */
    complete(3),
    /**
     * 作品审核不通过
     */
    nopass(4),
    /**
     * 下单审核通过
     */
    pass(5);
    
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