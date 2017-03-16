package com.bbyiya.model;

import java.math.BigDecimal;
import java.util.Date;

public class UCashlogs {
    private Long cashlogid;

    private Long userid;

    private Integer usetype;

    private Double amount;

    private String payid;

    private Date createtime;

    public Long getCashlogid() {
        return cashlogid;
    }

    public void setCashlogid(Long cashlogid) {
        this.cashlogid = cashlogid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Integer getUsetype() {
        return usetype;
    }

    public void setUsetype(Integer usetype) {
        this.usetype = usetype;
    }

    
    public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getPayid() {
        return payid;
    }

    public void setPayid(String payid) {
        this.payid = payid == null ? null : payid.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}