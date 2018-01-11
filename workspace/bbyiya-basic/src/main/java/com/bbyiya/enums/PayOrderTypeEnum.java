package com.bbyiya.enums;

public enum PayOrderTypeEnum {
	/**
     * 购物
     */
    gouwu(0),
	/**
     * 供应商货款充值
     */
    chongzhi(2),
    /**
     * 邮费充值
     */
    postage(3) ,
    /**
     * 红包
     */
    redPackets(4),
    /**
     * 台历购物
     */
    ti_gouwu(5),
    /**
     * 台历运费
     */
    ti_postage(6),
    /**
     * 台历红包
     */
    ti_redpaket(7),
    /**
     * 台历团购业务
     */
    ti_groupAct(8),
    /**
     * 活动直接优惠买（多买几本）
     */
    ti_halfPriceBuy(9)
    ;

    private final int Type;

    private PayOrderTypeEnum(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}
