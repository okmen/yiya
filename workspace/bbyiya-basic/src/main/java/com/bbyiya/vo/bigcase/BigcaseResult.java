package com.bbyiya.vo.bigcase;

import java.util.List;

import com.bbyiya.model.MBigcase;

public class BigcaseResult extends MBigcase{
	
	private static final long serialVersionUID = 1L;
	//大事件标签列表
	private List<BigcaseTagResult> taglist;
	//被阅读数量
	private int readCount;
	//同类的大事件数量
	private int groupCount;
	
	public List<BigcaseTagResult> getTaglist() {
		return taglist;
	}
	public void setTaglist(List<BigcaseTagResult> taglist) {
		this.taglist = taglist;
	}
	public int getReadCount() {
		return readCount;
	}
	public void setReadCount(int readCount) {
		this.readCount = readCount;
	}
	public int getGroupCount() {
		return groupCount;
	}
	public void setGroupCount(int groupCount) {
		this.groupCount = groupCount;
	}
	
	
}
