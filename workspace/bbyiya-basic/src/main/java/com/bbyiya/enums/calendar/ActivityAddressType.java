package com.bbyiya.enums.calendar;
/**
 * 控制用户地址可选类型
 * @author Administrator
 *
 */
public enum ActivityAddressType {
	/**
     * 可选影楼/用户自己（可自由选择）
     */
    auto(0),
    /**
     * 只能寄到用户自己
     */
    customerAddr(1),
    /**
     * 只能寄到影楼
     */
    promoterAddr(2)
    ;
    
    private final int Type;

    private ActivityAddressType(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}
