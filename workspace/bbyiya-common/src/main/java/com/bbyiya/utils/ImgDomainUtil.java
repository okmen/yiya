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
	
	/**
	 * 获取全路径图片地址
	 * @param imageUrl
	 * @return
	 */
	public static String getImageUrl_Full(String imageUrl){
		if(ObjectUtil.isEmpty(imageUrl))
			return imageUrl;
		if(imageUrl.contains("http://")||imageUrl.contains("https://")){
			return imageUrl;
		}
		int index = new Random().nextInt(imgdomainDefault.size());
		String imgdomain=imgdomainDefault.get(index).get("domain");
		return imgdomain+"/"+imageUrl;
	}
	
	/**
	 * 获取原图
	 * @param imageUrl
	 * @return
	 */
	public static String getImageUrl_Sourse(String imageUrl){
		if(ObjectUtil.isEmpty(imageUrl))
			return imageUrl;
		if(imageUrl.contains("http://")||imageUrl.contains("https://")){
			for (Map<String, String> map : imgdomainDefault) {
				if(imageUrl.contains(map.get("domain"))){
					imageUrl= imageUrl.replace(map.get("domain"), map.get("source"));
					return imageUrl;
				}
			}
			return imageUrl;
		}
		int index = new Random().nextInt(imgdomainDefault.size());
		String imgdomain=imgdomainDefault.get(index).get("domain");
		return imgdomain+"/"+imageUrl;
	}
	
}
