package com.bbyiya.model;

public class PScenes {
    private Long id;

    private Long productid;

    private String mintitle;

    private String title;

    private String content;

    private String tips;
    
    private String recreason;

    private Integer status;
    
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

    public String getMintitle() {
        return mintitle;
    }

    public void setMintitle(String mintitle) {
        this.mintitle = mintitle == null ? null : mintitle.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips == null ? null : tips.trim();
    }

	public String getRecreason() {
		return recreason;
	}

	public void setRecreason(String recreason) {
		this.recreason = recreason;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
    
}