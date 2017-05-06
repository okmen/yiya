package com.bbyiya.vo.reads;

import java.io.Serializable;

/**
 * 每日读物 model
 * @author Administrator
 *
 */
public class ReadsResult implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String title;

	private String summary;

	private String content;
	
	private String sourceUrl;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}
	
	
}
