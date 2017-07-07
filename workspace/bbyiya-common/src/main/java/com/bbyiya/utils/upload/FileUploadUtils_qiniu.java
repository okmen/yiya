package com.bbyiya.utils.upload;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import net.sf.json.JSONObject;

import com.bbyiya.common.enums.UploadTypeEnum;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ObjectUtil;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

public class FileUploadUtils_qiniu {

	// 设置好账号的ACCESS_KEY和SECRET_KEY
	private static String ACCESS_KEY = ConfigUtil.getSingleValue("qiniu_ACCESS_KEY");
	private static String SECRET_KEY = ConfigUtil.getSingleValue("qiniu_SECRET_KEY");
	
	// 默认的七牛存储空间
	private static String bucketname = "yiya"; // 填写新建的那个存储空间对象的名称
	// 密钥配置
	static Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
	// 创建上传对象
	static UploadManager uploadManager = new UploadManager();

	// 简单上传，使用默认策略，只需要设置上传的空间名就可以了
	public static String getUpToken() {
		return auth.uploadToken(bucketname);
	}
	
	public static Map<String, String> getUpTokenNew(){
		Map<String, String> result=new HashMap<String, String>();
		List<Map<String, String>> mapList=ConfigUtil.getMaplist("uploadcdns");
		if(mapList!=null&&mapList.size()>0){
			int index = new Random().nextInt(mapList.size());
			String bucketName=mapList.get(index).get("bucketname");
			String token=auth.uploadToken(bucketName);
			result.put("upToken", token);
			result.put("domain", mapList.get(index).get("domain"));
			return result;
		}
		return null;
	}
	
	/**
	 * 获取上传图片接口，替换
	 * @param key
	 * @return
	 */
	public static Map<String, String> getUpTokenNew(String key){
		Map<String, String> result=new HashMap<String, String>();
		List<Map<String, String>> mapList=ConfigUtil.getMaplist("uploadcdns");
		if(mapList!=null&&mapList.size()>0){
			int index = new Random().nextInt(mapList.size());
			String bucketName=mapList.get(index).get("bucketname");
			String token=auth.uploadToken(bucketName,key);
			result.put("upToken", token);
			result.put("domain", mapList.get(index).get("domain"));
			return result;
		}
		return null;
	}

	/**
	 * 文件上传 并返回url
	 * 
	 * @param FilePath 本地路径
	 * @param type  文件的类型（相册图片、头像等等）
	 * @return
	 * @throws IOException
	 */
	public static String uploadReturnUrl(String FilePath, UploadTypeEnum type) throws IOException {
		try {
			if (ObjectUtil.isEmpty(FilePath))
				return "";
			String suffix = FilePath.substring(FilePath.lastIndexOf("."));
			String fileName = DateUtil.getTimeStr(new Date(), "HHmmsss") + UUID.randomUUID().toString().replace("-", "");

			// 上传到七牛后保存的文件名
			String key = DateUtil.getTimeStr(new Date(), "yyyyMM") + "/" + fileName + suffix;

			// System.out.println(key);
			// 调用put方法上传
			Response res = uploadManager.put(FilePath, key, getUpToken());
			JSONObject object = JSONObject.fromObject(res.bodyString());
			if (object != null) {
				return ConfigUtil.getSingleValue("qiniu_imagedomain_cdn") + String.valueOf(object.get("key"));
			}
		} catch (Exception e) {
			// TODO: handle exception
			return e.toString();
		}
		return "";
	}
}
