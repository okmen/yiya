package com.bbyiya.model;

import java.io.Serializable;


public class PStylecoordinateitem  implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * 主键id
	 */
    private Long coordid;
    /**
     * 左边距 px
     */
    private Double pointleft;
    /**
     * 顶点边距（上边距）
     */
    private Double pointtop;
    /**
     * 高度（厚度）
     */
    private Double pointhight;

    /**
     * 宽度
     */
    private Double pointwidth;

    public Long getCoordid() {
        return coordid;
    }

    public void setCoordid(Long coordid) {
        this.coordid = coordid;
    }

	public Double getPointleft() {
		return pointleft;
	}

	public void setPointleft(Double pointleft) {
		this.pointleft = pointleft;
	}

	public Double getPointtop() {
		return pointtop;
	}

	public void setPointtop(Double pointtop) {
		this.pointtop = pointtop;
	}

	public Double getPointhight() {
		return pointhight;
	}

	public void setPointhight(Double pointhight) {
		this.pointhight = pointhight;
	}

	public Double getPointwidth() {
		return pointwidth;
	}

	public void setPointwidth(Double pointwidth) {
		this.pointwidth = pointwidth;
	}

    
}