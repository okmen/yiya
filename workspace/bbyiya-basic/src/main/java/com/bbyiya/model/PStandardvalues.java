package com.bbyiya.model;

public class PStandardvalues {
    private Long standardvalueid;

    private Long standardid;

    private String standardvalue;

    public Long getStandardvalueid() {
        return standardvalueid;
    }

    public void setStandardvalueid(Long standardvalueid) {
        this.standardvalueid = standardvalueid;
    }

    public Long getStandardid() {
        return standardid;
    }

    public void setStandardid(Long standardid) {
        this.standardid = standardid;
    }

    public String getStandardvalue() {
        return standardvalue;
    }

    public void setStandardvalue(String standardvalue) {
        this.standardvalue = standardvalue == null ? null : standardvalue.trim();
    }
}