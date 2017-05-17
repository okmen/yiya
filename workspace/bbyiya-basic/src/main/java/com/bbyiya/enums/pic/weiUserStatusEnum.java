package com.bbyiya.enums.pic;
/**
 * 流量主状态枚举
 * @author julie
 *
 */
public enum weiUserStatusEnum {
	/**
     * 申请中
     */
    applying(0),
    /**
     * 不通过
     */
    no(2),
    /**
     * 审核通过(成为流量主)
     */
    ok(1),
   /**
    * 删除
    */
    del(4)
    ;

    private final int Type;

    private weiUserStatusEnum(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}
