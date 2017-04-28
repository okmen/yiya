package com.bbyiya.vo.product;

import java.util.List;

import com.bbyiya.model.PProductdetails;
import com.bbyiya.vo.product.ProductBaseVo;

public class ProductSampleVo extends ProductBaseVo {

	private static final long serialVersionUID = 1L;
	
	
	private List<PProductdetails> sampleItems;


	public List<PProductdetails> getSampleItems() {
		return sampleItems;
	}


	public void setSampleItems(List<PProductdetails> sampleItems) {
		this.sampleItems = sampleItems;
	}




	
	

}
