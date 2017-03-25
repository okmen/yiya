package com.bbyiya.utils;

public class ImgDomainUtil {
	
	private static final String IMG_DOMAIN="http://pic.bbyiyya.com/";
	
	public static String getImageUrl(String imageUrl){
		if(ObjectUtil.isEmpty(imageUrl))
			return imageUrl;
		if(imageUrl.contains("http://")||imageUrl.contains("https://"))
			return imageUrl;
		return IMG_DOMAIN+imageUrl;
	}
	
//	public static String getImageUrl(String imageUrl,int size){
//		if(ObjectUtil.isEmpty(imageUrl))
//			return imageUrl;
//		if(imageUrl.contains("http://")||imageUrl.contains("https://"))
//			return imageUrl;
//		return IMG_DOMAIN+imageUrl;
//	}
}
