package com.bbyiya.common.vo;

public class ResultMsg {
	/**
	 * 返回结果状态
	 */
	private int status;
	/**
	 * 结果信息
	 */
	private String msg;
	public int getStatus()
	{
		return status;
	}
	public String getMsg()
	{
		return msg;
	}
	public void setStatus(int val)
	{
		this.status=val;
	}
	public void setMsg(String val)
	{
		this.msg=val;
	}
}
