package com.bbyiya.vo.calendar;
import java.util.Date;

import com.bbyiya.model.TiActivitys;
import com.bbyiya.model.TiActivityworks;
import com.bbyiya.model.TiPromoteradvertinfo;
import com.bbyiya.vo.calendar.product.TiProductResult;


public class TiActivitysVo extends TiActivitys{
	private String productName; //产品名称
	private String createTimestr;//创建时间字符串
	private Integer notsubmitcount;//未完成数量，图片未提交完
	private Integer notsharecount;//未完成分享数
	private Integer yaoqingcount; //已邀请数量
	private Long myworkId;
	private TiProductResult product;
	private String codeurl;//二维码地址
	private Integer applyStatus;
	private Date completeTime;
	private String adverttitle;//广告名称
	private TiActivityworks myactInfo;
	private TiPromoteradvertinfo advert;
	private String shareImg;
	private int isPromoteremployees;
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCreateTimestr() {
		return createTimestr;
	}

	public void setCreateTimestr(String createTimestr) {
		this.createTimestr = createTimestr;
	}

	public Integer getNotsubmitcount() {
		return notsubmitcount;
	}

	public Integer getNotsharecount() {
		return notsharecount;
	}

	public Integer getYaoqingcount() {
		return yaoqingcount;
	}

	public void setNotsubmitcount(Integer notsubmitcount) {
		this.notsubmitcount = notsubmitcount;
	}

	public void setNotsharecount(Integer notsharecount) {
		this.notsharecount = notsharecount;
	}

	public void setYaoqingcount(Integer yaoqingcount) {
		this.yaoqingcount = yaoqingcount;
	}

	public TiProductResult getProduct() {
		return product;
	}

	public void setProduct(TiProductResult product) {
		this.product = product;
	}

	public Long getMyworkId() {
		return myworkId;
	}

	public void setMyworkId(Long myworkId) {
		this.myworkId = myworkId;
	}

	public String getCodeurl() {
		return codeurl;
	}

	public void setCodeurl(String codeurl) {
		this.codeurl = codeurl;
	}

	public Integer getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(Integer applyStatus) {
		this.applyStatus = applyStatus;
	}

	
	public Date getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}

	public TiActivityworks getMyactInfo() {
		return myactInfo;
	}

	public void setMyactInfo(TiActivityworks myactInfo) {
		this.myactInfo = myactInfo;
	}

	public String getAdverttitle() {
		return adverttitle;
	}

	public void setAdverttitle(String adverttitle) {
		this.adverttitle = adverttitle;
	}

	public TiPromoteradvertinfo getAdvert() {
		return advert;
	}

	public void setAdvert(TiPromoteradvertinfo advert) {
		this.advert = advert;
	}

	public String getShareImg() {
		return shareImg;
	}

	public void setShareImg(String shareImg) {
		this.shareImg = shareImg;
	}

	public int getIsPromoteremployees() {
		return isPromoteremployees;
	}

	public void setIsPromoteremployees(int isPromoteremployees) {
		this.isPromoteremployees = isPromoteremployees;
	}
	
	
}
