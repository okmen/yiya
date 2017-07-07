package com.bbyiya.utils;

//import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ImgDomainUtil {
	
	/**
	 * 图片默认域名
	 */
	private static List<Map<String, String>> imgdomainDefault=ConfigUtil.getMaplist("imguploadcdnsdefault");
	
	
	public static String getImageUrl_Full(String imageUrl){
		if(ObjectUtil.isEmpty(imageUrl))
			return imageUrl;
		if(imageUrl.contains("http://")||imageUrl.contains("https://")){
//			for (Map<String, String> map : imgdomainDefault) {
//				//随机域名
//				if(imageUrl.contains(map.get("domain"))){
//					int index = new Random().nextInt(imgdomainDefault.size());
//					String imgdomain=imgdomainDefault.get(index).get("domain");
//					return imageUrl.replace(map.get("domain"), imgdomain);
//				}
//			}
			return imageUrl;
		}
		int index = new Random().nextInt(imgdomainDefault.size());
		String imgdomain=imgdomainDefault.get(index).get("domain");
		return imgdomain+"/"+imageUrl;
	}
	
//	public static String getImageUrl(String imageUrl,int size){
//		if(ObjectUtil.isEmpty(imageUrl))
//			return imageUrl;
//		if(imageUrl.contains("http://")||imageUrl.contains("https://"))
//			return imageUrl;
//		return IMG_DOMAIN+imageUrl;
//	}
}
