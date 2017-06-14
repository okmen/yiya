package com.bbyiya.cts.vo.job;

import java.io.Serializable;
import java.util.Date;

public class JobTime implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Date lastTime;

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

}
