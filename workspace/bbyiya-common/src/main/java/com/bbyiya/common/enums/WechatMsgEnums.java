package com.bbyiya.common.enums;

public enum WechatMsgEnums {
	/**
     * 购物消费
     */
    payed("mrkX2mBIwQHgsaxeavpn_FFNzei1xHJvptAV7EQoO-E")
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
