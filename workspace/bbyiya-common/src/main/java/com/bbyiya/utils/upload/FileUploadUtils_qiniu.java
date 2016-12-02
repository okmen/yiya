package com.bbyiya.utils.upload;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import net.sf.json.JSONObject;

import com.bbyiya.common.enums.UploadTypeEnum;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.encrypt.MD5Encrypt;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

public class FileUploadUtils_qiniu {
	
	// 设置好账号的ACCESS_KEY和SECRET_KEY
	private static String ACCESS_KEY = ConfigUtil.getSingleValue("qiniu_ACCESS_KEY");
	private static String SECRET_KEY = ConfigUtil.getSingleValue("qiniu_SECRET_KEY");
	// 要上传的空间
	private static String bucketname = "yiya"; // 填写新建的那个存储空间对象的名称
	// 下载图片域名
	// private String IMAGE_DOMAIN="obbepyhga.bkt.clouddn.com";

	// 密钥配置
	static Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
	// 创建上传对象
	static UploadManager uploadManager = new UploadManager();

	// 简单上传，使用默认策略，只需要设置上传的空间名就可以了
	public static String getUpToken() {
		return auth.uploadToken(bucketname);
	}


	/**
	 * 文件上传 并返回url
	 * @param FilePath 本地路径
	 * @param type 文件的类型（相册图片、头像等等）
	 * @return
	 * @throws IOException
	 */
	public static String uploadReturnUrl(String FilePath, UploadTypeEnum type) throws IOException {
		try {
			if(ObjectUtil.isEmpty(FilePath))
				return "";
			String suffix=FilePath.substring(FilePath.lastIndexOf(".")); 
			String fileName = DateUtil.getTimeStr(new Date(), "HHmmsss") + UUID.randomUUID().toString().replace("-", "");
			
			// 上传到七牛后保存的文件名
			String key = DateUtil.getTimeStr(new Date(), "yyyyMM") + "/" + fileName+suffix;
			
//			System.out.println(key);
			// 调用put方法上传
			Response res = uploadManager.put(FilePath, key, getUpToken());
			JSONObject object = JSONObject.fromObject(res.bodyString());
			if (object != null) {
				return ConfigUtil.getSingleValue("qiniu_imagedomain") + String.valueOf(object.get("key"));
			}
		} catch (Exception e) {
			// TODO: handle exception
			return e.toString();
		}
		return "";
	}
}
