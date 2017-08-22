package com.bbyiya.pic.vo.report;

import java.util.List;

public class ReportLineDataVO {
	private static final long serialVersionUID = 1L;
	private String[] xcontent;//x轴内容
	private List<ReportJsonData> data;//数据
	public String[] getXcontent() {
		return xcontent;
	}
	public void setXcontent(String[] xcontent) {
		this.xcontent = xcontent;
	}
	public List<ReportJsonData> getData() {
		return data;
	}
	public void setData(List<ReportJsonData> data) {
		this.data = data;
	}
	
	

}
