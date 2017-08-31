package com.bbyiya.enums.calendar;
/**
 * 
 * 用户参与情况状态
 *
 */
public enum ActivityWorksStatusEnum {
	/**
     * 已参与,已报名
     */
    apply(0),
	/**
     * 图片已提交
     */
    imagesubmit(1),
    /**
     * 已完成分享
     */
    completeshare(2),
    
    /**
     * 已完成（已下单）
     */
    completeorder(3);
    
    private final int Type;

    private ActivityWorksStatusEnum(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}