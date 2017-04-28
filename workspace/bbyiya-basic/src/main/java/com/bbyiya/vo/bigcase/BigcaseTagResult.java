package com.bbyiya.vo.bigcase;

import java.io.Serializable;

public class BigcaseTagResult implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer caseId;
	//大事件标签id
	private Integer tagId;
	private String tagName;
	private String tagContent;
	public Integer getCaseId() {
		return caseId;
	}
	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}
	public Integer getTagId() {
		return tagId;
	}
	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public String getTagContent() {
		return tagContent;
	}
	public void setTagContent(String tagContent) {
		this.tagContent = tagContent;
	}
	
	
}
