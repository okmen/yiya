package com.bbyiya.enums;

public enum CustomerSourceTypeEnum {
	 /**
     * 客户一对一邀请
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
    other(4) 
    ;

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
