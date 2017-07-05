package com.bbyiya.pic.vo.product;

import com.bbyiya.model.PMyproductdetails;

public class MyProductsDetailsResult extends PMyproductdetails {
	private static final long serialVersionUID = 1L;
	private String printcode;
	private String sceneDescription;//场景引导
	private String sceneTitle;
	
	public String getPrintcode() {
		return printcode;
	}

	public void setPrintcode(String printcode) {
		this.printcode = printcode;
	}

	public String getSceneDescription() {
		return sceneDescription;
	}

	public void setSceneDescription(String sceneDescription) {
		this.sceneDescription = sceneDescription;
	}

	public String getSceneTitle() {
		return sceneTitle;
	}

	public void setSceneTitle(String sceneTitle) {
		this.sceneTitle = sceneTitle;
	}

}
