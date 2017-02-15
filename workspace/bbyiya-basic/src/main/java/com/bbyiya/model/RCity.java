package com.bbyiya.model;

import java.io.Serializable;

public class RCity  implements Serializable {
	private static final long serialVersionUID = 1L;
    private Integer code;

    private String city;

    private Integer provincecode;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public Integer getProvincecode() {
        return provincecode;
    }

    public void setProvincecode(Integer provincecode) {
        this.provincecode = provincecode;
    }
}