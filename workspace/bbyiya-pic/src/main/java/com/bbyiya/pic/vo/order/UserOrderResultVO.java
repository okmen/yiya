package com.bbyiya.pic.vo.order;


import java.util.List;

import com.bbyiya.model.OOrderaddress;
import com.bbyiya.model.OOrderproductdetails;
import com.bbyiya.model.OUserorders;

public class UserOrderResultVO extends OUserorders{
	private static final long serialVersionUID = 1L;
	
	private List<OOrderproductdetails> imglist;
	
	private OOrderaddress address;

	public List<OOrderproductdetails> getImglist() {
		return imglist;
	}

	public void setImglist(List<OOrderproductdetails> imglist) {
		this.imglist = imglist;
	}

	public OOrderaddress getAddress() {
		return address;
	}

	public void setAddress(OOrderaddress address) {
		this.address = address;
	}
	
	

}
