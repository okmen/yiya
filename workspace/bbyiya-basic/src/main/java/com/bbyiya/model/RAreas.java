package com.bbyiya.model;

import java.io.Serializable;

public class RAreas  implements Serializable {
	private static final long serialVersionUID = 1L;
    private Integer code;

    private String area;

    private Integer citycode;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    public Integer getCitycode() {
        return citycode;
    }

    public void setCitycode(Integer citycode) {
        this.citycode = citycode;
    }
}