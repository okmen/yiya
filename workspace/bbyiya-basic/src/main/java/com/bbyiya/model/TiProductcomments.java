package com.bbyiya.model;

import java.io.Serializable;
import java.util.Date;

public class TiProductcomments implements Serializable {
	private static final long serialVersionUID = 1L;
    private Long id;

    private Long userid;

    private String headimg;

    private Long styleid;

    private Long productid;

    private Date createtime;

    private String content;

    private String imgjson;
    
    private String orderproductid;

    private String nickname;

    private String styledescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getStyledescription() {
		return styledescription;
	}

	public void setStyledescription(String styledescription) {
		this.styledescription = styledescription;
	}

	public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg == null ? null : headimg.trim();
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

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getImgjson() {
        return imgjson;
    }

    public void setImgjson(String imgjson) {
        this.imgjson = imgjson == null ? null : imgjson.trim();
    }

	public String getOrderproductid() {
		return orderproductid;
	}

	public void setOrderproductid(String orderproductid) {
		this.orderproductid = orderproductid;
	}
    
    
}