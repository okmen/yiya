package com.bbyiya.vo.bigcase;

import java.io.Serializable;
import java.util.List;


public class BigcaseResult implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer caseId;
	private String title;
	private Integer typeId;
	private String imageDefault;
	private String summary;

	// 大事件标签列表
	private List<BigcaseTagResult> taglist;
	private List<BigcaseUserImgVO> imglist;

	// 被阅读数量
	private int readCount;
	// 同类的大事件数量
	private int groupCount;
	//是否收藏
	private int isCollected;
	private int isRead;
	
	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

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

	public List<BigcaseUserImgVO> getImglist() {
		return imglist;
	}

	public void setImglist(List<BigcaseUserImgVO> imglist) {
		this.imglist = imglist;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getImageDefault() {
		return imageDefault;
	}

	public void setImageDefault(String imageDefault) {
		this.imageDefault = imageDefault;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public int getIsCollected() {
		return isCollected;
	}

	public void setIsCollected(int isCollected) {
		this.isCollected = isCollected;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

}
