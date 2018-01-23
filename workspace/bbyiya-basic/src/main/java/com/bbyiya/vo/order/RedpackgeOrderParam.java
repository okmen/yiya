package com.bbyiya.vo.order;


public class RedpackgeOrderParam {
	/**
	 * 市
	 */
	private String City;
	/**
	 * 收货地址详情
	 */
	private String Address;
	/**
	 * 广告主公司名称
	 */
	private String AgentBless;
	/**
	 * 反面祝福语
	 */
//	private String CustomerBless;
	/**
	 * 系统定值 iHYXL9q2FRasjCnn3szsJw==
	 */
	private String ClientCodeEn;
	/**
	 * 系统定值 2qbbZUQ1VVsC3+vGwxcMZg==
	 */
	private String AuthCode;
	/**
	 * 代理商地址
	 */
	private String  DeliveAddress;
	
	private String DelivePhone;
	private String DropShopping;
	private String OrderCode;
	private Integer Copys;
	private String Province;
	private String PrintCode;
	private String RePrint;
	private String RePrintOrderCode;
	private String ReceiverName;
	private String ReceiverPhone;
	private String Town;
	private String Type;
	private String Url;
	private Object CustomerBlessList; 
	public String getAddress() {
		return Address;
	}

	public String getAuthCode() {
		return AuthCode;
	}
	public String getCity() {
		return City;
	}
	public String getClientCodeEn() {
		return ClientCodeEn;
	}
	
	public String getDeliveAddress() {
		return DeliveAddress;
	}
	public String getDelivePhone() {
		return DelivePhone;
	}
	public String getDropShopping() {
		return DropShopping;
	}
	public String getOrderCode() {
		return OrderCode;
	}
	public String getProvince() {
		return Province;
	}
	public String getRePrint() {
		return RePrint;
	}
	public String getRePrintOrderCode() {
		return RePrintOrderCode;
	}
	public String getReceiverName() {
		return ReceiverName;
	}
	public String getReceiverPhone() {
		return ReceiverPhone;
	}
	public String getTown() {
		return Town;
	}
	public String getType() {
		return Type;
	}
	public String getUrl() {
		return Url;
	}
	public void setAddress(String address) {
		Address = address;
	}

	public void setAuthCode(String authCode) {
		AuthCode = authCode;
	}
	public void setCity(String city) {
		City = city;
	}
	public void setClientCodeEn(String clientCodeEn) {
		ClientCodeEn = clientCodeEn;
	}

	public void setDeliveAddress(String deliveAddress) {
		DeliveAddress = deliveAddress;
	}
	public void setDelivePhone(String delivePhone) {
		DelivePhone = delivePhone;
	}
	public void setDropShopping(String dropShopping) {
		DropShopping = dropShopping;
	}
	public void setOrderCode(String orderCode) {
		OrderCode = orderCode;
	}
	public void setProvince(String province) {
		Province = province;
	}
	public void setRePrint(String rePrint) {
		RePrint = rePrint;
	}
	public void setRePrintOrderCode(String rePrintOrderCode) {
		RePrintOrderCode = rePrintOrderCode;
	}
	public void setReceiverName(String receiverName) {
		ReceiverName = receiverName;
	}
	public void setReceiverPhone(String receiverPhone) {
		ReceiverPhone = receiverPhone;
	}
	public void setTown(String town) {
		Town = town;
	}
	public void setType(String type) {
		Type = type;
	}
	public void setUrl(String url) {
		Url = url;
	}

	public String getAgentBless() {
		return AgentBless;
	}

	public Object getCustomerBlessList() {
		return CustomerBlessList;
	}

	public void setAgentBless(String agentBless) {
		AgentBless = agentBless;
	}

	public void setCustomerBlessList(Object customerBlessList) {
		CustomerBlessList = customerBlessList;
	}

	public String getPrintCode() {
		return PrintCode;
	}

	public void setPrintCode(String printCode) {
		PrintCode = printCode;
	}

	public Integer getCopys() {
		return Copys;
	}

	public void setCopys(Integer copys) {
		Copys = copys;
	}
	
	
	
	
}
