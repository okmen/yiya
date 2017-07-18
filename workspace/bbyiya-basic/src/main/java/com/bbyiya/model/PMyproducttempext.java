package com.bbyiya.model;

public class PMyproducttempext {
    private Long id;

    private Integer tempid;

    private Long styleid;

    private Long productid;
    
    /***********************vo********************/
    private String productimg;
    private String productname;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTempid() {
        return tempid;
    }

    public void setTempid(Integer tempid) {
        this.tempid = tempid;
    }

    public Long getStyleid() {
        return styleid;
    }

    public void setStyleid(Long styleid) {
        this.styleid = styleid;
    }

    public Long getProductid() {
        return productid;
    }

    public void setProductid(Long productid) {
        this.productid = productid;
    }

	public String getProductimg() {
		return productimg;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductimg(String productimg) {
		this.productimg = productimg;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}
    
}