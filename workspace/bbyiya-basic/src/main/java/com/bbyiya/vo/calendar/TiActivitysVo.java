package com.bbyiya.vo.calendar;
import com.bbyiya.model.TiActivitys;


public class TiActivitysVo extends TiActivitys{
	private String productName; //产品名称
	private String createTimestr;//创建时间字符串
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCreateTimestr() {
		return createTimestr;
	}

	public void setCreateTimestr(String createTimestr) {
		this.createTimestr = createTimestr;
	}
	
	
	
	
}
