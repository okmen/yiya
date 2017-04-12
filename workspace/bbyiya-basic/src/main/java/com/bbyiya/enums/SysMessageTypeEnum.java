package com.bbyiya.enums;

public enum SysMessageTypeEnum {
	/**
     * 发送给pbs类型通知
     */
    pbs(0),
	/**
     * 发送给ibs类型通知
     */
    ibs(2),
    /**
     * 全部可见
     */
    all(3) 
    ;

    private final int Type;

    private SysMessageTypeEnum(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}
