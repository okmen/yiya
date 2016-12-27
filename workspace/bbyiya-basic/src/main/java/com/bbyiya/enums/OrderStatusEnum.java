package com.bbyiya.enums;

public enum OrderStatusEnum {
	 /**
     * 等待支付
     */
    noPay(0),
	/**
     * 已支付
     */
    payed(1),
    /**
     * 等待发货 （已支付-》相片上传完成 ）
     */
    waitFoSend(2),
    /**
     * 已发货
     */
    send(3),
    /**
     * 用户已收货
     */
    recived(4)
    ;

    private final int Type;

    private OrderStatusEnum(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}
