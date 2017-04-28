package com.bbyiya.enums.pic;
/**
 * 协同编辑状态
 * @author Administrator
 *
 */
public enum InviteStatus {
	/**
     * 发送邀请中
     */
    inviting(1),
    /**
     * 接受邀请
     */
    agree(3),
    /**
     *  忽略邀请
     */
    lgnore(2),
    /**
     * 被邀请人已经编辑好了
     */
    ok(4)
    ;

    private final int Type;

    private InviteStatus(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}
