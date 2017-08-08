package com.bbyiya.enums;

public enum CustomerSourceTypeEnum {
	 /**
     * 影楼一对一邀请
     */
    oneinvite(1),
	/**
     * 异业合作
     */
    temp(2),
    /**
     * 订单分配
     */
    order(3),
    
    /**
     * 其它
     */
    other(4),
    /**
     * 客户邀请影楼员工
     */
    khinvite(5);

    private final int Type;

    private CustomerSourceTypeEnum(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}
