package com.bbyiya.pic.vo.order;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.model.OUserorders;

public class PbsUserOrderResultVO extends OOrderproducts{
	private static final long serialVersionUID = 1L;
	private OUserorders order;	
	private String payTimeStr;
	private String branchesName;
	private String branchesUserName;
	private String branchesPhone;
	private String branchesprovince;
	private String branchesrcity;
	private String branchesdistrict;
	private String branchesAddress;
	
	private String reciver;
    private String buyerPhone;
    private String buyerprovince;
    private String buyercity;
    private String buyerdistrict;
    private String buyerstreetdetail;

	public OUserorders getOrder() {
		return order;
	}

	public void setOrder(OUserorders order) {
		this.order = order;
	}

	public String getBuyerPhone() {
		return buyerPhone;
	}

	public void setBuyerPhone(String buyerPhone) {
		this.buyerPhone = buyerPhone;
	}

	public String getBranchesName() {
		return branchesName;
	}

	public void setBranchesName(String branchesName) {
		this.branchesName = branchesName;
	}

	public String getBranchesPhone() {
		return branchesPhone;
	}

	public void setBranchesPhone(String branchesPhone) {
		this.branchesPhone = branchesPhone;
	}

	public String getBranchesAddress() {
		return branchesAddress;
	}

	public void setBranchesAddress(String branchesAddress) {
		this.branchesAddress = branchesAddress;
	}

	public String getReciver() {
		return reciver;
	}

	public void setReciver(String reciver) {
		this.reciver = reciver;
	}

	public String getBuyerprovince() {
		return buyerprovince;
	}

	public void setBuyerprovince(String buyerprovince) {
		this.buyerprovince = buyerprovince;
	}

	public String getBuyercity() {
		return buyercity;
	}

	public void setBuyercity(String buyercity) {
		this.buyercity = buyercity;
	}

	public String getBuyerdistrict() {
		return buyerdistrict;
	}

	public void setBuyerdistrict(String buyerdistrict) {
		this.buyerdistrict = buyerdistrict;
	}

	public String getBuyerstreetdetail() {
		return buyerstreetdetail;
	}

	public void setBuyerstreetdetail(String buyerstreetdetail) {
		this.buyerstreetdetail = buyerstreetdetail;
	}

	public String getPayTimeStr() {
		return payTimeStr;
	}

	public void setPayTimeStr(String payTimeStr) {
		this.payTimeStr = payTimeStr;
	}

	public String getBranchesUserName() {
		return branchesUserName;
	}

	public void setBranchesUserName(String branchesUserName) {
		this.branchesUserName = branchesUserName;
	}

	public String getBranchesprovince() {
		return branchesprovince;
	}

	public void setBranchesprovince(String branchesprovince) {
		this.branchesprovince = branchesprovince;
	}

	public String getBranchesrcity() {
		return branchesrcity;
	}

	public void setBranchesrcity(String branchesrcity) {
		this.branchesrcity = branchesrcity;
	}

	public String getBranchesdistrict() {
		return branchesdistrict;
	}

	public void setBranchesdistrict(String branchesdistrict) {
		this.branchesdistrict = branchesdistrict;
	}
	
	
	

}
