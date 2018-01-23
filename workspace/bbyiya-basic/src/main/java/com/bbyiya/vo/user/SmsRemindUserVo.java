package com.bbyiya.vo.user;

import java.io.Serializable;

/**
 * 需短信通知充值的用户
 * @author kevin
 *
 */
public class SmsRemindUserVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long userid;
	private Integer issend;//是否已发送
	public Long getUserid() {
		return userid;
	}
	public Integer getIssend() {
		return issend;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	public void setIssend(Integer issend) {
		this.issend = issend;
	}
	
	
}
