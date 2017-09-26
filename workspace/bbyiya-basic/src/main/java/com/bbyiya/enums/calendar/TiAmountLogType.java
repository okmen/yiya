package com.bbyiya.enums.calendar;

public enum TiAmountLogType  {
	
	/*--------------------账户消费-------------------------*/
	/**
     * B端充值
     */
	in_charge(1),
    /**
     * c端交易
     */
    in_payment(2),
    
    /**
     * 提现
     */
    out_dispenseCash(6)
	;
	

    private final int Type;

    private TiAmountLogType(int step)
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
