package com.bbyiya.enums;

public enum AccountLogType {
	
	/*--------------------账户消费-------------------------*/
	/**
     * 购物消费
     */
	use_payment(1),
    /**
     * 运费消耗
     */
    use_freight(3),
    
    /*----------------- 账户收入-------------------------*/
    /**
     * 充值
     */
    get_recharge(2),
    /**
     * 红包获取
     */
    get_redPackets(4),
	/**
	 * 推广 佣金
	 */
	get_Commission(5);
	

    private final int Type;

    private AccountLogType(int step)
    {

        this.Type = step;
    }

    public int getType() {
		return Type;
	}

	public String toString()
    {
        return String.valueOf(this.Type);
    }
}
