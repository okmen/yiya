package com.bbyiya.pic.vo.product;

import java.util.List;

import com.bbyiya.model.PMyproducts;

public class MyProductsResult extends PMyproducts{

	private static final long serialVersionUID = 1L;
	//作品图片数量
	private int count;
	//作品默认图片
	private String headImg;
	//是否下单
	private int isOrder;
	/**
	 * 相册的描述（）
	 */
	private String description;
	/**
	 * 分享页不同相册的小图标
	 */
	private String sharepageLcon;
	/**
	 * 作品详细图片
	 */
	private List<MyProductsDetailsResult> detailslist;
	
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	public int getIsOrder() {
		return isOrder;
	}
	public void setIsOrder(int isOrder) {
		this.isOrder = isOrder;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<MyProductsDetailsResult> getDetailslist() {
		return detailslist;
	}
	public void setDetailslist(List<MyProductsDetailsResult> detailslist) {
		this.detailslist = detailslist;
	}
	public String getSharepageLcon() {
		return sharepageLcon;
	}
	public void setSharepageLcon(String sharepageLcon) {
		this.sharepageLcon = sharepageLcon;
	}
	
	
}
