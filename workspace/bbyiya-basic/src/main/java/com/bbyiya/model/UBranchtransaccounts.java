package com.bbyiya.model;

import java.math.BigDecimal;

public class UBranchtransaccounts {
    private Long branchuserid;

    private BigDecimal availableamount;

    public Long getBranchuserid() {
        return branchuserid;
    }

    public void setBranchuserid(Long branchuserid) {
        this.branchuserid = branchuserid;
    }

    public BigDecimal getAvailableamount() {
        return availableamount;
    }

    public void setAvailableamount(BigDecimal availableamount) {
        this.availableamount = availableamount;
    }
}