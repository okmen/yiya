package com.bbyiya.model;

public class TiAdvertimgs {
    private Long id;

    private Long productid;

    private Long promoteruserid;

    private String advertimgjson;

    private String advertcontent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductid() {
        return productid;
    }

    public void setProductid(Long productid) {
        this.productid = productid;
    }

    public Long getPromoteruserid() {
        return promoteruserid;
    }

    public void setPromoteruserid(Long promoteruserid) {
        this.promoteruserid = promoteruserid;
    }

    public String getAdvertimgjson() {
        return advertimgjson;
    }

    public void setAdvertimgjson(String advertimgjson) {
        this.advertimgjson = advertimgjson == null ? null : advertimgjson.trim();
    }

	public String getAdvertcontent() {
		return advertcontent;
	}

	public void setAdvertcontent(String advertcontent) {
		this.advertcontent = advertcontent;
	}
    
}