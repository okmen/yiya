package com.bbyiya.model;

import java.util.Date;

public class TiPromoteradvertviewlogs {
    private Long logid;

    private Integer advertid;

    private Long userid;

    private String nickname;

    private String headimg;

    private Integer viewcount;

    private Date viewtime;

    private String viewtimeStr;
    public Long getLogid() {
        return logid;
    }

    public void setLogid(Long logid) {
        this.logid = logid;
    }

    public Integer getAdvertid() {
        return advertid;
    }

    public void setAdvertid(Integer advertid) {
        this.advertid = advertid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg == null ? null : headimg.trim();
    }

    public Integer getViewcount() {
        return viewcount;
    }

    public void setViewcount(Integer viewcount) {
        this.viewcount = viewcount;
    }

    public Date getViewtime() {
        return viewtime;
    }

    public void setViewtime(Date viewtime) {
        this.viewtime = viewtime;
    }

	public String getViewtimeStr() {
		return viewtimeStr;
	}

	public void setViewtimeStr(String viewtimeStr) {
		this.viewtimeStr = viewtimeStr;
	}
    
}