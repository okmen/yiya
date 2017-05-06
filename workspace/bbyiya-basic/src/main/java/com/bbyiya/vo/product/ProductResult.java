package com.bbyiya.vo.product;

import java.io.Serializable;
import java.util.List;

public class ProductResult extends ProductBaseVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<String> desImgs;
	private List<ProductStandardResult> propertyList;
	private List<PProductStyleResult> styleslist;

	public List<PProductStyleResult> getStyleslist() {
		return styleslist;
	}

	public void setStyleslist(List<PProductStyleResult> styleslist) {
		this.styleslist = styleslist;
	}

	public List<ProductStandardResult> getPropertyList() {
		return propertyList;
	}

	public void setPropertyList(List<ProductStandardResult> propertyList) {
		this.propertyList = propertyList;
	}

	public List<String> getDesImgs() {
		return desImgs;
	}

	public void setDesImgs(List<String> desImgs) {
		this.desImgs = desImgs;
	}

}
