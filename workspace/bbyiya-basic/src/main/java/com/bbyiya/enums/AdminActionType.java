package com.bbyiya.enums;

public enum AdminActionType {
	/**
     * 代理商退驻
     *     */
	agent_quit(1),
	/**
	 * 给供应商充值
	 */
	chongzhi(2);

    private final int Type;

    private AdminActionType(int step)
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
