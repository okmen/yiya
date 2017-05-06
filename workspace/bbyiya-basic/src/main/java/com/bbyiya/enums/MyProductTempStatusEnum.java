package com.bbyiya.enums;

public enum MyProductTempStatusEnum {   
	/**
     * 禁用
     */
    disabled(0),
    /**
     * 启用状态
     */
    enable(1),
    /**
     * 已删除
     */
    del(2);

    private final int Type;

    private MyProductTempStatusEnum(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}
