package com.bbyiya.enums;

public enum ProductStatusEnum {
    /**
     * 草稿箱
     */
    drafts(0),
	/**
     * 销售中
     */
    ok(1),
    /**
     * 已删除
     */
    del(2),
    /**
     * 已下架
     */
    offline(4),
    ;

    private final int Type;

    private ProductStatusEnum(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}
