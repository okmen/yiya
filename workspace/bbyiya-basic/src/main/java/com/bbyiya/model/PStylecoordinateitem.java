package com.bbyiya.model;

import java.math.BigDecimal;

public class PStylecoordinateitem {
    private Long coordid;

    private BigDecimal pointleft;

    private BigDecimal pointtop;

    private BigDecimal pointhight;

    private BigDecimal pointwidth;

    public Long getCoordid() {
        return coordid;
    }

    public void setCoordid(Long coordid) {
        this.coordid = coordid;
    }

    public BigDecimal getPointleft() {
        return pointleft;
    }

    public void setPointleft(BigDecimal pointleft) {
        this.pointleft = pointleft;
    }

    public BigDecimal getPointtop() {
        return pointtop;
    }

    public void setPointtop(BigDecimal pointtop) {
        this.pointtop = pointtop;
    }

    public BigDecimal getPointhight() {
        return pointhight;
    }

    public void setPointhight(BigDecimal pointhight) {
        this.pointhight = pointhight;
    }

    public BigDecimal getPointwidth() {
        return pointwidth;
    }

    public void setPointwidth(BigDecimal pointwidth) {
        this.pointwidth = pointwidth;
    }
}