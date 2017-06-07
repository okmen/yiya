package com.bbyiya.pic.vo.order.ibs;

import java.io.Serializable;
import java.util.List;

import com.bbyiya.model.OOrderproductphotos;

public class OrderPhotoVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long cartid;
	    
	private String carttitle;
	    
	private String cartauthor;
	
	private List<OOrderproductphotos> photos;

	public Long getCartid() {
		return cartid;
	}

	public void setCartid(Long cartid) {
		this.cartid = cartid;
	}

	public String getCarttitle() {
		return carttitle;
	}

	public void setCarttitle(String carttitle) {
		this.carttitle = carttitle;
	}

	public String getCartauthor() {
		return cartauthor;
	}

	public void setCartauthor(String cartauthor) {
		this.cartauthor = cartauthor;
	}

	public List<OOrderproductphotos> getPhotos() {
		return photos;
	}

	public void setPhotos(List<OOrderproductphotos> photos) {
		this.photos = photos;
	}
	
	
	
}
