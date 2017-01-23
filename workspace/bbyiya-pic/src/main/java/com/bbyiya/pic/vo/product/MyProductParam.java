package com.bbyiya.pic.vo.product;

import java.io.Serializable;
import java.util.List;

import com.bbyiya.model.PMyproductdetails;
import com.bbyiya.model.PMyproducts;

public class MyProductParam extends PMyproducts implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<PMyproductdetails> details;

	public List<PMyproductdetails> getDetails() {
		return details;
	}

	public void setDetails(List<PMyproductdetails> details) {
		this.details = details;
	}

	
}
