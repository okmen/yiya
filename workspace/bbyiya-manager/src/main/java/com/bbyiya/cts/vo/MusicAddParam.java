package com.bbyiya.cts.vo;

import java.io.Serializable;


public class MusicAddParam implements Serializable{
    private static final long serialVersionUID = 1L;

    private String name;
    private String downloadurl;
    //音乐分组
    private Integer musictype;
    
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDownloadurl() {
		return downloadurl;
	}
	public void setDownloadurl(String downloadurl) {
		this.downloadurl = downloadurl;
	}
	public Integer getMusictype() {
		return musictype;
	}
	public void setMusictype(Integer musictype) {
		this.musictype = musictype;
	}
    
}
