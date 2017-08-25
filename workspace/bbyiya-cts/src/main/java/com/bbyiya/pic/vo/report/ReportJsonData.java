package com.bbyiya.pic.vo.report;

import java.util.List;

public class ReportJsonData {
	private static final long serialVersionUID = 1L;
	
	private String name; 
	
	private List<Integer> data;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Integer> getData() {
		return data;
	}
	public void setData(List<Integer> data) {
		this.data = data;
	}
	
	
	
	

}
