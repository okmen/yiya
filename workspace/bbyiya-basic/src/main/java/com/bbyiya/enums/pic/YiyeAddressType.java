package com.bbyiya.enums.pic;

public enum YiyeAddressType {
	/**
     * 用户自己收货
     */
    customer(0),
	/**
     * 影楼地址
     */
    branchSelf(1),
    /**
     * 门店自选
     */
    branchList(2);
   
    private final int Type;

    private YiyeAddressType(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}
