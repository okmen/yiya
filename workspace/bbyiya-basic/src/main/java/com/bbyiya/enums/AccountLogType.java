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
    
    /**
     * 提现
     */
    use_cashout(6),
    
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
	get_Commission(5),
	/**
	 * 台历 -货款收入
	 */
	get_ti_payment(9),
	/**
	 * 台历-运费收入
	 */
	get_ti_post(7),
	/**
	 * 佣金收入
	 */
	get_ti_commission(8),
	
	/**
	 * 台历红包收入
	 */
	get_ti_redpaket(10),
	/**
	 * 台历分销业务收入
	 */
	get_ti_groupActInCome(11)
	;
	

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
