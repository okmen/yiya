package com.bbyiya.common.enums;

public enum WechatMsgEnums {
	/**
     * 购物消费
     */
    payed("mrkX2mBIwQHgsaxeavpn_FFNzei1xHJvptAV7EQoO-E"),
    
    /**
     * 已发货
     */
    send("GtyopaD5uOp43814DXKyFRAdYe95EfIkenxACyz-FFI"),
    /**
     * 发货通知
     */
    sendSimple("YBdS4leiXpT7_sawc7tHpO_wwOKadP741GvJbo1IK0o")
    ;

    private final String templateId;

    private WechatMsgEnums(String step)
    {

        this.templateId = step;
    }

    public String toString()
    {
        return String.valueOf(this.templateId);
    }
}
