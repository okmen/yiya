package com.bbyiya.model;

import java.util.Date;

public class UAccountslogs {
    private Long logid;

    private Long userid;

    private Integer type;

    private Double amount;

    private String orderid;

    private Date createtime;
    /*--------------------自定义字段-------------------------*/
    private String createtimestr;
    private String headImg;
	private String nickName;
	private Double ordertotalprice;//订单总金额
    public Long getLogid() {
        return logid;
    }

    public void setLogid(Long logid) {
        this.logid = logid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid == null ? null : orderid.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

	public String getCreatetimestr() {
		return createtimestr;
	}

	public void setCreatetimestr(String createtimestr) {
		this.createtimestr = createtimestr;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Double getOrdertotalprice() {
		return ordertotalprice;
	}

	public void setOrdertotalprice(Double ordertotalprice) {
		this.ordertotalprice = ordertotalprice;
	}
	
	
    
}