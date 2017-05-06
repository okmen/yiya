package com.bbyiya.enums.pic;
/**
 * 邀请类型
 * @author Administrator
 *
 */
public enum InviteType {
	/**
     * 发送手机号接受邀请
     */
    sendPhoneInvite(0),
	/**
     * 扫描二维码接受邀请
     */
    scanQRInvite(1);
   
    private final int Type;

    private InviteType(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}
