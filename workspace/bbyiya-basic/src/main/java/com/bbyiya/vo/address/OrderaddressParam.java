package com.bbyiya.vo.address;

public class OrderaddressParam {
    
    private Long userid;

    private String reciver;

    private String phone;
    
    private Integer province;

    private Integer city;

    private Integer district;
    
    private String streetdetail;
    
	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getReciver() {
		return reciver;
	}

	public void setReciver(String reciver) {
		this.reciver = reciver;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getProvince() {
		return province;
	}

	public void setProvince(Integer province) {
		this.province = province;
	}

	public Integer getCity() {
		return city;
	}

	public void setCity(Integer city) {
		this.city = city;
	}

	public Integer getDistrict() {
		return district;
	}

	public void setDistrict(Integer district) {
		this.district = district;
	}

	public String getStreetdetail() {
		return streetdetail;
	}

	public void setStreetdetail(String streetdetail) {
		this.streetdetail = streetdetail;
	}
    
}