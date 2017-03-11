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
    inviting(0),
    /**
     * 接受邀请
     */
    agree(1),
    /**
     * 通过， 交了代理费(成为代理商)
     */
    pass(2),
    /**
     * 编辑好了
     */
    ok(3)
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
