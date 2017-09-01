package com.bbyiya.vo.calendar.product;

import java.util.List;

import com.bbyiya.model.TiProducts;
import com.bbyiya.model.TiProductstyles;

public class TiProductResult extends TiProducts{
	
	/*-----------实体扩展字段-------------------*/
    /**
     * 优惠类型（1折扣，2现金优惠）
     */
    private Integer discountType;
    /**
     * 具体优惠（如：当discountType=1时 discount=0.5表示5折，当discountType=2时，discount=5表示优惠5元）
     */
    private Double discount;
    /**
     * 销量
     */
	private int saleCount;
	/**
	 * 评论数
	 */
	private int commentsCount;
	/**
	 * 款式列表
	 * @return
	 */
	private List<TiProductstyles> stylelist;
	
	public int getSaleCount() {
		return saleCount;
	}
	public void setSaleCount(int saleCount) {
		this.saleCount = saleCount;
	}
	public int getCommentsCount() {
		return commentsCount;
	}
	public void setCommentsCount(int commentsCount) {
		this.commentsCount = commentsCount;
	}
	public Integer getDiscountType() {
		return discountType;
	}
	public void setDiscountType(Integer discountType) {
		this.discountType = discountType;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public List<TiProductstyles> getStylelist() {
		return stylelist;
	}
	public void setStylelist(List<TiProductstyles> stylelist) {
		this.stylelist = stylelist;
	}
	
	
}