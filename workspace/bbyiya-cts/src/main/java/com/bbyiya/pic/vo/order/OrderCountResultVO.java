package com.bbyiya.pic.vo.order;

public class OrderCountResultVO {
	private static final long serialVersionUID = 1L;
	
	private String starttime;//��ѯ��ʼʱ��
	private String endtime;//��ѯ����ʱ��
	private String username;//�û��ǳ�
	private String usercreatetime;//�û�ע��ʱ��
	private String userorderid;//�������
	private String orderpaytime;//����֧��ʱ��
	private String Producttitle;//��Ʒ����+PropertyStr
	private int count;//��Ʒ���� 
	private Double totalprice;//�����ܽ��
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
