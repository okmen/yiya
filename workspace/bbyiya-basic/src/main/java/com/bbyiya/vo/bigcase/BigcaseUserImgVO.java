package com.bbyiya.vo.bigcase;

import java.io.Serializable;

public class BigcaseUserImgVO implements Serializable {

	private static final long serialVersionUID = 1L;

    private Integer position;

    private String imageurl;
    
    private String content;

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
    
    
}
