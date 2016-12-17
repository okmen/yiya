package com.bbyiya.vo.bigcase;

import java.util.List;

import com.bbyiya.model.MBigcaseclasstag;
import com.bbyiya.model.MBigcasesummary;

public class BigcasesummaryResult extends MBigcasesummary{
	private static final long serialVersionUID = 1L;
	
	private List<MBigcaseclasstag> tags;

	public List<MBigcaseclasstag> getTags() {
		return tags;
	}

	public void setTags(List<MBigcaseclasstag> tags) {
		this.tags = tags;
	}
	
	

}
