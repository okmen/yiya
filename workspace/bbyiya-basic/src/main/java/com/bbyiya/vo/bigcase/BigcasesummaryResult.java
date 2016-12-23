package com.bbyiya.vo.bigcase;

import java.io.Serializable;
import java.util.List;

import com.bbyiya.model.MBigcaseclasstag;

public class BigcasesummaryResult implements Serializable {

	private static final long serialVersionUID = 1L;
	//大事件类别Id
	private Integer casetypeid;

	private Integer groupcount;

	private String content;
	
	private List<MBigcaseclasstag> tags;

	public List<MBigcaseclasstag> getTags() {
		return tags;
	}

	public void setTags(List<MBigcaseclasstag> tags) {
		this.tags = tags;
	}

	public Integer getCasetypeid() {
		return casetypeid;
	}

	public void setCasetypeid(Integer casetypeid) {
		this.casetypeid = casetypeid;
	}

	public Integer getGroupcount() {
		return groupcount;
	}

	public void setGroupcount(Integer groupcount) {
		this.groupcount = groupcount;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
