package com.bbyiya.vo.talks;

import java.io.Serializable;

public class YiyaTalkBannerModel implements Serializable{
	private static final long serialVersionUID = 1L;

	private Integer id;

    private String title;

    private String imageUrl;

    private String wapUrl;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getWapUrl() {
		return wapUrl;
	}

	public void setWapUrl(String wapUrl) {
		this.wapUrl = wapUrl;
	}


    
}
