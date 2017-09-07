package com.bbyiya.vo.calendar;
import java.util.List;

import com.bbyiya.model.TiActivityworks;


public class TiActivitysWorkVo extends TiActivityworks{
	private String weiNickName;//微信昵称
	private String createTimestr;
	private List<String> ordernolist;//订单号集合
	private Integer targetextCount;//目标分享数量
	public String getWeiNickName() {
		return weiNickName;
	}
	public String getCreateTimestr() {
		return createTimestr;
	}
	public List<String> getOrdernolist() {
		return ordernolist;
	}
	public void setWeiNickName(String weiNickName) {
		this.weiNickName = weiNickName;
	}
	public void setCreateTimestr(String createTimestr) {
		this.createTimestr = createTimestr;
	}
	public void setOrdernolist(List<String> ordernolist) {
		this.ordernolist = ordernolist;
	}
	public Integer getTargetextCount() {
		return targetextCount;
	}
	public void setTargetextCount(Integer targetextCount) {
		this.targetextCount = targetextCount;
	}
	
	
	
	
	
	
}
