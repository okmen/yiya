package com.bbyiya.model;

public class OProducerordercount {
    private String userorderid;

    private Long produceruserid;

    private Long userid;

    private Integer orderindex;

    private String printindex;

    public String getUserorderid() {
        return userorderid;
    }

    public void setUserorderid(String userorderid) {
        this.userorderid = userorderid == null ? null : userorderid.trim();
    }

    public Long getProduceruserid() {
        return produceruserid;
    }

    public void setProduceruserid(Long produceruserid) {
        this.produceruserid = produceruserid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Integer getOrderindex() {
        return orderindex;
    }

    public void setOrderindex(Integer orderindex) {
        this.orderindex = orderindex;
    }

    public String getPrintindex() {
        return printindex;
    }

    public void setPrintindex(String printindex) {
        this.printindex = printindex == null ? null : printindex.trim();
    }
}