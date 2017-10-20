package com.bbyiya.enums.calendar;

public enum AddressTypeEnum {
	
	/**
     * 影楼自提
     */
    promoteraddr(0),
    /**
     * 客户地址
     */
    cusaddr(1);
    
    private final int Type;

    private AddressTypeEnum(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}