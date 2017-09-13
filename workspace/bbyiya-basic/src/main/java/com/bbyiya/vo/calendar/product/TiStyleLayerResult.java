package com.bbyiya.vo.calendar.product;

import com.bbyiya.model.PStylecoordinateitem;
import com.bbyiya.model.TiProductstyleslayers;

public class TiStyleLayerResult extends TiProductstyleslayers{
	
	private String workImgUrl;
	private PStylecoordinateitem imgCoordMod;
	private String printNo;
	private Double widthDhight;
	//是否是广告位
	private int isAdvert;
	//是否有广告
	private int haveAdvert;
	//广告背景图
	private String backImg;
	//广告图
	private String adImg;
	
	public String getWorkImgUrl() {
		return workImgUrl;
	}

	public void setWorkImgUrl(String workImgUrl) {
		this.workImgUrl = workImgUrl;
	}

	public PStylecoordinateitem getImgCoordMod() {
		return imgCoordMod;
	}

	public void setImgCoordMod(PStylecoordinateitem imgCoordMod) {
		this.imgCoordMod = imgCoordMod;
	}

	public String getPrintNo() {
		return printNo;
	}

	public void setPrintNo(String printNo) {
		this.printNo = printNo;
	}

	public Double getWidthDhight() {
		return widthDhight;
	}

	public void setWidthDhight(Double widthDhight) {
		this.widthDhight = widthDhight;
	}

	public int getIsAdvert() {
		return isAdvert;
	}

	public void setIsAdvert(int isAdvert) {
		this.isAdvert = isAdvert;
	}

	public int getHaveAdvert() {
		return haveAdvert;
	}

	public void setHaveAdvert(int haveAdvert) {
		this.haveAdvert = haveAdvert;
	}

	public String getBackImg() {
		return backImg;
	}

	public void setBackImg(String backImg) {
		this.backImg = backImg;
	}

	public String getAdImg() {
		return adImg;
	}

	public void setAdImg(String adImg) {
		this.adImg = adImg;
	}

	

	
	
	
	
	
}
