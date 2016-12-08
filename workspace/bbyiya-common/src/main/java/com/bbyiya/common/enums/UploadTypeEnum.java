package com.bbyiya.common.enums;

public enum UploadTypeEnum {
	/**
     * 相册相片
     */
    Product(1),
    /**
     * 用户头像
     */
    HeadImg(2),
    /**
     * 音乐
     */
    Mp3(3),
    /**
     * 视频
     */
    Vidio(4)
    ;

    private final int Type;

    private UploadTypeEnum(int step)
    {

        this.Type = step;
    }

    public String toString()
    {
        return String.valueOf(this.Type);
    }
}
