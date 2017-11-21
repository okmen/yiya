package com.bbyiya.pic.vo.calendar;

import java.util.List;

import com.bbyiya.model.TiGroupactivity;
import com.bbyiya.model.TiGroupactivityproducts;

public class GroupActivityAddParam extends TiGroupactivity{
	private List<TiGroupactivityproducts>  productlist;

	public List<TiGroupactivityproducts> getProductlist() {
		return productlist;
	}

	public void setProductlist(List<TiGroupactivityproducts> productlist) {
		this.productlist = productlist;
	}
	
	
}
