package com.bbyiya.utils.upload;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;







import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.FetchRet;
import com.qiniu.util.Auth;

public class FileUploadUtils_qiniu {

	// 设置好账号的ACCESS_KEY和SECRET_KEY
	private static String ACCESS_KEY = ConfigUtil.getSingleValue("qiniu_ACCESS_KEY");
	private static String SECRET_KEY = ConfigUtil.getSingleValue("qiniu_SECRET_KEY");

	// 默认的七牛存储空间
	public static String BUCKETNAME_DEFULT = "yiya";
	//超过4M的图片存储
	public static String BUCKETNAME_BIGIMG="yiya1";
	// 七牛 密钥配置
	static Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

	/**
	 * 获取上传uploadToken （默认 存储空间）
	 * 
	 * @return
	 */
	public static String getUpToken() {
		return getUpTokenCommon(BUCKETNAME_DEFULT, null);
	}

	/**
	 * 获取上传uploadToken 随机分配存储空间
	 * 
	 * @return 并返回cdn域名
	 */
	public static Map<String, String> getUpTokenNew() {
		Map<String, String> result = new HashMap<String, String>();
		List<Map<String, String>> mapList = ConfigUtil.getMaplist("uploadcdns");
		if (mapList != null && mapList.size() > 0) {
			int index = new Random().nextInt(mapList.size());
			String token = getUpTokenCommon(mapList.get(index).get("bucketname"), null);
			result.put("upToken", token);
			result.put("domain", mapList.get(index).get("domain"));
			return result;
		}
		return null;
	}

	/**
	 * 获取uploadToken 缓存半小时
	 * 
	 * @param bucketNameStr
	 * @param key
	 * @return
	 */
	public static String getUpTokenCommon(String bucketNameStr, String key) {
		if (ObjectUtil.isEmpty(key)) {
			String keyString = ConfigUtil.getSingleValue("currentRedisKey-Base") + "_uploadtoken_" + bucketNameStr;
			String uploadToken = RedisUtil.getString(keyString);
			if (ObjectUtil.isEmpty(uploadToken)) {
				uploadToken = auth.uploadToken(bucketNameStr, null, 3600, null, true);
				RedisUtil.setString(keyString, uploadToken, 1800);
			}
			return uploadToken;
		} else {
			return auth.uploadToken(bucketNameStr, key, 3600, null, true);
		}
	}


	/**
	 * 图片抓取到 七牛
	 * @param remoteUrl 需要抓取的url
	 * @return
	 * @throws QiniuException
	 */
	public static FetchRet fetch(String remoteUrl) throws QiniuException {
		Configuration cfg = new Configuration(Zone.autoZone()); 
		BucketManager bucketManager = new BucketManager(auth,cfg);
		// 图片key
		String key = DateUtil.getTimeStr(new Date(), "yyyyMMdd")+"/"+UUID.randomUUID().toString().replace("-", "") + ".jpg";
		FetchRet fetchRet = bucketManager.fetch(remoteUrl, BUCKETNAME_DEFULT, key);
		return fetchRet; 
	}
	/**
	 * 大图存储
	 * @param remoteUrl
	 * @return
	 * @throws QiniuException
	 */
	public static FetchRet fetchBigIMG(String remoteUrl) throws QiniuException {
		Configuration cfg = new Configuration(Zone.autoZone()); 
		BucketManager bucketManager = new BucketManager(auth,cfg);
		// 图片key
		String key = DateUtil.getTimeStr(new Date(), "yyyyMMdd")+"/"+UUID.randomUUID().toString().replace("-", "") + ".jpg";
		FetchRet fetchRet = bucketManager.fetch(remoteUrl, BUCKETNAME_BIGIMG, key);
		if(fetchRet!=null){
			fetchRet.key="http://pic1.bbyiya.com/"+key;
		}
		return fetchRet; 
	}
	
	/**
	 * 获取文件状态
	 * @param keysList
	 * @return
	 * @throws QiniuException
	 */
	public static List<Map<String, Object>> getBatchStatus(String[] keysList) throws QiniuException {
		Configuration cfg = new Configuration(Zone.autoZone()); 
		BucketManager bucketManager = new BucketManager(auth,cfg);
		try {
			BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
		    batchOperations.addStatOps(BUCKETNAME_DEFULT, keysList);
		    Response response = bucketManager.batch(batchOperations);
		    BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
		    List<Map<String, Object>> result=new ArrayList<Map<String, Object>>();
		    for (int i = 0; i < keysList.length; i++) {
		        BatchStatus status = batchStatusList[i];
		        String key = keysList[i];
		        Map<String, Object> map=new HashMap<String, Object>();
		        map.put("key", key);
		        map.put("status", status.code);
		        if(status.code==200){
		        	 map.put("hash", status.data.hash);
		        	 map.put("mimeType", status.data.mimeType);
		        	 map.put("fsize", status.data.fsize);
		        	 map.put("putTime", status.data.putTime);
		        }else {
		        	 map.put("error", status.data.error);
				}
		        result.add(map);
		    }
		    return result;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;

	}
	
}
