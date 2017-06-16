package com.bbyiya.baseUtils;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.UUID;

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
	
	
	/**
	 * 字符数组
	 */
	public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

	public static String[] numbers=new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };

	/**
	 * 生成 随机编号
	 * @param size
	 * @return
	 */
	public static String generateUuid_Char(int size) {
		StringBuffer shortBuffer = new StringBuffer();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		for (int i = 0; i < size; i++) {
			String str = uuid.substring(i * 4, i * 4 + 4);
			int x = Integer.parseInt(str, 16);
			shortBuffer.append(chars[x % 0x3E]);
		}
		return shortBuffer.toString();
	}
	

}
