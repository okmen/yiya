package com.bbyiya.model;

import java.util.Date;
import java.util.List;

public class PMyproducttemp {
    private Integer tempid;

    private Long branchuserid;

    private String title;

    private Integer status;
    /**
     * 已通过用户数
     */
    private Integer count;

    private String remark;

    private Long cartid;

    private Date createtime;
    
    private Integer needverifer;
    /**
     * 已报名数
     */
    private Integer applycount;

    private String discription;
    
    private String tempcodeurl;

    private String tempcodesm;
    
    private Integer type;

    private Long styleid;

    private Integer maxapplycount;

    private Integer completecount;

    private Integer maxcompletecount;

    private Integer blesscount;

    private Integer isautoorder;

    private Integer orderhours;
    
    private Integer isbranchaddress;

    private Date begintime;

    private Date endtime;
    
    private Double amountlimit;
    

    /*************************Vo****************************/
    
    //二维码地址
  	private String codeurl; //异业扫码二维码
  	/**
  	 * 待审核数量
  	 */
  	private Integer needCheckCount; 
  	private String createtimestr;
  	private String productName;//产品名称
  	private Long productid;
  	private List<PMyproducttempext> tempextlist;

    public Integer getNeedverifer() {
		return needverifer;
	}

	public void setNeedverifer(Integer needverifer) {
		this.needverifer = needverifer;
	}

	public Integer getApplycount() {
		return applycount;
	}

	public void setApplycount(Integer applycount) {
		this.applycount = applycount;
	}

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	public Integer getTempid() {
        return tempid;
    }

    public void setTempid(Integer tempid) {
        this.tempid = tempid;
    }

    public Long getBranchuserid() {
        return branchuserid;
    }

    public void setBranchuserid(Long branchuserid) {
        this.branchuserid = branchuserid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Long getCartid() {
        return cartid;
    }

    public void setCartid(Long cartid) {
        this.cartid = cartid;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
    public String getTempcodeurl() {
        return tempcodeurl;
    }

    public void setTempcodeurl(String tempcodeurl) {
        this.tempcodeurl = tempcodeurl == null ? null : tempcodeurl.trim();
    }

    public String getTempcodesm() {
        return tempcodesm;
    }

    public void setTempcodesm(String tempcodesm) {
        this.tempcodesm = tempcodesm == null ? null : tempcodesm.trim();
    }
	public String getCodeurl() {
		return codeurl;
	}

	public void setCodeurl(String codeurl) {
		this.codeurl = codeurl;
	}

	public Integer getNeedCheckCount() {
		return needCheckCount;
	}

	public void setNeedCheckCount(Integer needCheckCount) {
		this.needCheckCount = needCheckCount;
	}

	public String getCreatetimestr() {
		return createtimestr;
	}

	public void setCreatetimestr(String createtimestr) {
		this.createtimestr = createtimestr;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getStyleid() {
		return styleid;
	}

	public void setStyleid(Long styleid) {
		this.styleid = styleid;
	}

	public Integer getMaxapplycount() {
		return maxapplycount;
	}

	public void setMaxapplycount(Integer maxapplycount) {
		this.maxapplycount = maxapplycount;
	}

	public Integer getCompletecount() {
		return completecount;
	}

	public void setCompletecount(Integer completecount) {
		this.completecount = completecount;
	}

	public Integer getMaxcompletecount() {
		return maxcompletecount;
	}

	public void setMaxcompletecount(Integer maxcompletecount) {
		this.maxcompletecount = maxcompletecount;
	}

	public Integer getBlesscount() {
		return blesscount;
	}

	public void setBlesscount(Integer blesscount) {
		this.blesscount = blesscount;
	}

	public Integer getIsautoorder() {
		return isautoorder;
	}

	public void setIsautoorder(Integer isautoorder) {
		this.isautoorder = isautoorder;
	}

	public Integer getOrderhours() {
		return orderhours;
	}

	public void setOrderhours(Integer orderhours) {
		this.orderhours = orderhours;
	}

	public Integer getIsbranchaddress() {
		return isbranchaddress;
	}

	public void setIsbranchaddress(Integer isbranchaddress) {
		this.isbranchaddress = isbranchaddress;
	}

	public Date getBegintime() {
		return begintime;
	}

	public void setBegintime(Date begintime) {
		this.begintime = begintime;
	}

	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Long getProductid() {
		return productid;
	}

	public void setProductid(Long productid) {
		this.productid = productid;
	}

	public List<PMyproducttempext> getTempextlist() {
		return tempextlist;
	}

	public void setTempextlist(List<PMyproducttempext> tempextlist) {
		this.tempextlist = tempextlist;
	}

	public Double getAmountlimit() {
		return amountlimit;
	}

	public void setAmountlimit(Double amountlimit) {
		this.amountlimit = amountlimit;
	}

	
    
}