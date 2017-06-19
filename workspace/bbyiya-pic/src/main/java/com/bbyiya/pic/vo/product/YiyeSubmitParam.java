package com.bbyiya.pic.vo.product;

import java.io.Serializable;
import java.util.Date;

public class YiyeSubmitParam implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private long cartId;
	private long subUserId;
	private String version;
	private long addressId;
	private String dateTimeStr;
	private long dateTimeVal;
	private String codenum;
	public long getDateTimeVal() {
		return dateTimeVal;
	}
	public void setDateTimeVal(long dateTimeVal) {
		this.dateTimeVal = dateTimeVal;
	}
	private Date dateTime;
	public long getCartId() {
		return cartId;
	}
	public void setCartId(long cartId) {
		this.cartId = cartId;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public long getAddressId() {
		return addressId;
	}
	public void setAddressId(long addressId) {
		this.addressId = addressId;
	}
	public long getSubUserId() {
		return subUserId;
	}
	public void setSubUserId(long subUserId) {
		this.subUserId = subUserId;
	}
	public String getDateTimeStr() {
		return dateTimeStr;
	}
	public void setDateTimeStr(String dateTimeStr) {
		this.dateTimeStr = dateTimeStr;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getCodenum() {
		return codenum;
	}
	public void setCodenum(String codenum) {
		this.codenum = codenum;
	}
	

	
	
}
