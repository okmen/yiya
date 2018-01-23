package com.bbyiya.vo.order;


import java.util.Date;

import com.bbyiya.model.OUserorders;

public class UserOrderRedpackgeResult extends OUserorders{
	private static final long serialVersionUID = 1L;
	
	private Long productId;//产品Id
	private Long styleId;//款式Id
	private Integer Copys;
	private Long workId;
	private Integer isPushed;//是否已经推送
	private Date pushTime;
	private String khName;//代收时：客户的姓名
	private String khPhone;//客户的联系方式
	private String khName1;
	private String khPhone1;
	
	private String reciverName;//收货联系人
	private String reciverPhone;//收货手机号
	private String province;//收货省
	private String city;//市
	private String district;//区
	private String streetDetail;//街道详情
	public Long getProductId() {
		return productId;
	}
	public Long getStyleId() {
		return styleId;
	}
	public Integer getIsPushed() {
		return isPushed;
	}
	public Date getPushTime() {
		return pushTime;
	}
	public String getKhName() {
		return khName;
	}
	public String getKhPhone() {
		return khPhone;
	}
	public String getReciverName() {
		return reciverName;
	}
	public String getReciverPhone() {
		return reciverPhone;
	}
	public String getProvince() {
		return province;
	}
	public String getCity() {
		return city;
	}
	public String getDistrict() {
		return district;
	}
	
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public void setStyleId(Long styleId) {
		this.styleId = styleId;
	}
	public void setIsPushed(Integer isPushed) {
		this.isPushed = isPushed;
	}
	public void setPushTime(Date pushTime) {
		this.pushTime = pushTime;
	}
	public void setKhName(String khName) {
		this.khName = khName;
	}
	public void setKhPhone(String khPhone) {
		this.khPhone = khPhone;
	}
	public void setReciverName(String reciverName) {
		this.reciverName = reciverName;
	}
	public void setReciverPhone(String reciverPhone) {
		this.reciverPhone = reciverPhone;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getStreetDetail() {
		return streetDetail;
	}
	public void setStreetDetail(String streetDetail) {
		this.streetDetail = streetDetail;
	}
	public String getKhName1() {
		return khName1;
	}
	public String getKhPhone1() {
		return khPhone1;
	}
	public void setKhName1(String khName1) {
		this.khName1 = khName1;
	}
	public void setKhPhone1(String khPhone1) {
		this.khPhone1 = khPhone1;
	}
	public Long getWorkId() {
		return workId;
	}
	public void setWorkId(Long workId) {
		this.workId = workId;
	}
	public Integer getCopys() {
		return Copys;
	}
	public void setCopys(Integer copys) {
		Copys = copys;
	}

	
	
	
}
