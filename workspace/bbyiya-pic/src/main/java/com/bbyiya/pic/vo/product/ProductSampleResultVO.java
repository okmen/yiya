package com.bbyiya.pic.vo.product;

//import com.bbyiya.model.PProducts;
import com.bbyiya.model.PProductsamples;

public class ProductSampleResultVO extends PProductsamples{
	private static final long serialVersionUID = 1L;
	
//	private String description;
	private MyProductsResult myWorks;

	public MyProductsResult getMyWorks() {
		return myWorks;
	}
	public void setMyWorks(MyProductsResult myWorks) {
		this.myWorks = myWorks;
	}
	
	

}
