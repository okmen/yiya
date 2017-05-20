package com.bbyiya.model;

import java.util.Date;

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
    
    //二维码地址
  	private String codeurl;
  	/**
  	 * 待审核数量
  	 */
  	private Integer needCheckCount; 
  	private String createtimestr;

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
    
    
}