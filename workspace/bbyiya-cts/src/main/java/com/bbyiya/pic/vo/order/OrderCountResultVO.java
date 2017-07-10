package com.bbyiya.pic.vo.order;

public class OrderCountResultVO {
	private static final long serialVersionUID = 1L;
	
	private String starttime;//查询开始时间
	private String endtime;//查询结束时间
	private String username;//用户昵称
	private String usercreatetime;//用户注册时间
	private String userorderid;//订单编号
	private String orderpaytime;//订单支付时间
	private String Producttitle;//产品标题+PropertyStr
	private int count;//产品数量 
	private Double totalprice;//订单总金额
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsercreatetime() {
		return usercreatetime;
	}
	public void setUsercreatetime(String usercreatetime) {
		this.usercreatetime = usercreatetime;
	}
	public String getUserorderid() {
		return userorderid;
	}
	public void setUserorderid(String userorderid) {
		this.userorderid = userorderid;
	}
	public String getOrderpaytime() {
		return orderpaytime;
	}
	public void setOrderpaytime(String orderpaytime) {
		this.orderpaytime = orderpaytime;
	}
	public String getProducttitle() {
		return Producttitle;
	}
	public void setProducttitle(String producttitle) {
		Producttitle = producttitle;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Double getTotalprice() {
		return totalprice;
	}
	public void setTotalprice(Double totalprice) {
		this.totalprice = totalprice;
	}
	
	
	
	
	
	

}
