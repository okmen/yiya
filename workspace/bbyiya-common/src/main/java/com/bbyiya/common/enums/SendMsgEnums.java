package com.bbyiya.common.enums;

public enum SendMsgEnums {
	/**
     * 用户注册 value= 1
     */
    register(1),
    /**
     * 找回密码（重置密码）
     */
    backPwd(2),
    /**
     * 用户登陆 value =2
     */
    login(3),
    
    /**
     * 用户充值
     */
    recharge(4),
    /**
     * 订单支付
     */
    payOrder(5),
    /**
     * 已发货(发货通知)
     */
    delivery(6)
    ;

    private final int Type;

    private SendMsgEnums(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}
