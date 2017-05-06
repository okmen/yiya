package com.bbyiya.baseUtils;

import java.text.DecimalFormat;
import java.util.Date;

import com.bbyiya.utils.DateUtil;

public class GenUtils {

	
	/**
	 * 生成订单编号
	 * @param userId 用户userid 可为null
	 * @return
	 */
	public static String getOrderNo(Long userId){
		if(userId==null)
			userId=1l;
		long temp=userId%100000;
		DecimalFormat df=new DecimalFormat("0000");
	    String str2=df.format(temp);
		String timeStr=DateUtil.getTimeStr(new Date(), "yyyyMMdd"+str2+"HHmmSSS");
		return timeStr;
	}
}
