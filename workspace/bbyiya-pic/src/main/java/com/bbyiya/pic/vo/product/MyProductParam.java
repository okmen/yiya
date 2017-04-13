package com.bbyiya.pic.vo.product;

import java.io.Serializable;
import java.util.List;
import com.bbyiya.model.PMyproductdetails;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.vo.user.UChildInfoParam;

public class MyProductParam extends PMyproducts implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private UChildInfoParam childInfo;
	
	private List<PMyproductdetails> details;

	public List<PMyproductdetails> getDetails() {
		return details;
	}

	public void setDetails(List<PMyproductdetails> details) {
		this.details = details;
	}

	public UChildInfoParam getChildInfo() {
		return childInfo;
	}

	public void setChildInfo(UChildInfoParam childInfo) {
		this.childInfo = childInfo;
	}

	
}
