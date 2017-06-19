package com.bbyiya.enums.pic;

public enum ActivityCodeStatusEnum {
	/**
     * 未使用
     */
    notuse(0),
	/**
     * 已使用
     */
    used(1), 
    /**
     * 已删除
     */
    deleted(3);
   

    private final int Type;

    private ActivityCodeStatusEnum(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}