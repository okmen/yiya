package com.bbyiya.enums;

public enum AccountLogType {
	/**
     * 货款消费
     */
	use_payment(1),
    /**
     * 充值
     */
    get_recharge(2),
    /**
     * 运费消耗
     */
    use_freight(3);

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
