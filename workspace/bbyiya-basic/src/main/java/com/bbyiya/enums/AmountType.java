package com.bbyiya.enums;

public enum AmountType {
	/**
     * 消费
     */
	lost(1),
    /**
     * 充值
     */
    get(2),
    /**
     * 赠送
     */
	free(3);

    private final int Type;

    private AmountType(int step)
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
