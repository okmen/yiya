package com.bbyiya.model;

public class PStandards {
    private Long standardid;

    private String standardname;

    private Integer isauto;

    private Long userid;

    public Long getStandardid() {
        return standardid;
    }

    public void setStandardid(Long standardid) {
        this.standardid = standardid;
    }

    public String getStandardname() {
        return standardname;
    }

    public void setStandardname(String standardname) {
        this.standardname = standardname == null ? null : standardname.trim();
    }

    public Integer getIsauto() {
        return isauto;
    }

    public void setIsauto(Integer isauto) {
        this.isauto = isauto;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }
}