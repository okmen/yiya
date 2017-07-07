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
import com.bbyiya.utils.RedisUtil;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

public class FileUploadUtils_qiniu {

	// 设置好账号的ACCESS_KEY和SECRET_KEY
	private static String ACCESS_KEY = ConfigUtil.getSingleValue("qiniu_ACCESS_KEY");
	private static String SECRET_KEY = ConfigUtil.getSingleValue("qiniu_SECRET_KEY");
	
	// 默认的七牛存储空间
	private static String BUCKETNAME_DEFULT = "yiya";
	//七牛 密钥配置
	static Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
	// 创建上传对象
	static UploadManager uploadManager = new UploadManager();



	/**
	 * 获取上传uploadToken （默认 存储空间）
	 * @return
	 */
	public static String getUpToken() {
		return getUpTokenCommon(BUCKETNAME_DEFULT,null);		
	}
	
	/**
	 * 获取上传uploadToken 随机分配存储空间 
	 * @return 
	 * 并返回cdn域名
	 */
	public static Map<String, String> getUpTokenNew(){
		Map<String, String> result=new HashMap<String, String>();
		List<Map<String, String>> mapList=ConfigUtil.getMaplist("uploadcdns");
		if(mapList!=null&&mapList.size()>0){
			int index = new Random().nextInt(mapList.size());
			String token=getUpTokenCommon(mapList.get(index).get("bucketname"),null);
			result.put("upToken", token);
			result.put("domain", mapList.get(index).get("domain"));
			return result;
		}
		return null;
	}
	
	
	/**
	 * 获取uploadToken 缓存半小时
	 * @param bucketNameStr
	 * @param key
	 * @return
	 */
	public static String getUpTokenCommon(String bucketNameStr,String key) {
		if(ObjectUtil.isEmpty(key)){
			String keyString=ConfigUtil.getSingleValue("currentRedisKey-Base")+"_uploadtoken_"+bucketNameStr;
			String uploadToken=RedisUtil.getString(keyString);
			if(ObjectUtil.isEmpty(uploadToken)){
				uploadToken=auth.uploadToken(bucketNameStr,null, 3600, null,true);
				RedisUtil.setString(keyString, uploadToken, 1800); 
			}
			return uploadToken;
		}else {
			return auth.uploadToken(bucketNameStr,key, 3600, null,true);
		}
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
