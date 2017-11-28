package com.bbyiya.pic.vo.calendar;

import java.util.List;

import com.bbyiya.model.TiMyartsdetails;

public class WorkForCustomerParam {
	private String workId;
	private Long productId;
	private Long styleId;
	private String babynickname;
	private Integer needShareCount;
	private Double needRedpacketTotal;
	private List<TiMyartsdetails> details;
	
	
	public Double getNeedRedpacketTotal() {
		return needRedpacketTotal;
	}
	public void setNeedRedpacketTotal(Double needRedpacketTotal) {
		this.needRedpacketTotal = needRedpacketTotal;
	}
	public Long getProductId() {
		return productId;
	}
	public Long getStyleId() {
		return styleId;
	}
	public Integer getNeedShareCount() {
		return needShareCount;
	}
	
	public List<TiMyartsdetails> getDetails() {
		return details;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public void setStyleId(Long styleId) {
		this.styleId = styleId;
	}
	public void setNeedShareCount(Integer needShareCount) {
		this.needShareCount = needShareCount;
	}
	public void setDetails(List<TiMyartsdetails> details) {
		this.details = details;
	}
	public String getWorkId() {
		return workId;
	}
	public void setWorkId(String workId) {
		this.workId = workId;
	}
	public String getBabynickname() {
		return babynickname;
	}
	public void setBabynickname(String babynickname) {
		this.babynickname = babynickname;
	}
	
	
	
}
