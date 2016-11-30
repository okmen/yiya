package com.bbyiya.utils.qiniu;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import net.sf.json.JSONObject;

import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.DateUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
/**
 * 七牛 文件上传
 * @author Administrator
 *
 */
public class Uploader {
	
	// 设置好账号的ACCESS_KEY和SECRET_KEY
	private static String ACCESS_KEY = ConfigUtil.getSingleValue("qiniu_ACCESS_KEY");
	private static String SECRET_KEY =  ConfigUtil.getSingleValue("qiniu_SECRET_KEY");
	// 要上传的空间
	private static String bucketname = "yiya"; // 填写新建的那个存储空间对象的名称
	//下载图片域名
//	private String IMAGE_DOMAIN="obbepyhga.bkt.clouddn.com";

	// 密钥配置
	static Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
	// 创建上传对象
	static UploadManager uploadManager = new UploadManager();
	
	//简单上传，使用默认策略，只需要设置上传的空间名就可以了  
    public static String getUpToken(){  
        return auth.uploadToken(bucketname);  
    }  
	
	/**
	 * 上传文件到服务器 并返回url
	 * @param FilePath 本地图片路径 "d:\\01.jpg"
	 * @return 图片地址
	 * @throws IOException
	 */
	public static String uploadReturnUrl(String FilePath) throws IOException {
		try {
			String fileName= DateUtil.getTimeStr(new Date(), "HHmmsss")+ UUID.randomUUID().toString().replace("-", "");
			// 上传到七牛后保存的文件名
			String key= DateUtil.getTimeStr(new Date(), "yyyyMM")+"/"+fileName+".jpg" ;
			// 调用put方法上传
			Response res = uploadManager.put(FilePath, key, getUpToken());
			// 打印返回的信息
//			System.out.println(res.bodyString());
			JSONObject object=JSONObject.fromObject(res.bodyString());
			if(object!=null){ 
				return ConfigUtil.getSingleValue("qiniu_imagedomain")+String.valueOf(object.get("key")) ; 
			}
			
		} catch (QiniuException e) {
			Response r = e.response;
			// 请求失败时打印的异常的信息
			System.out.println(r.toString());
			try {
				// 响应的文本信息
				System.out.println(r.bodyString());
			} catch (QiniuException e1) {
				// ignore
			}
		}
		return "";
	}
	
	
	public static String uploadReturnUrl(String FilePath,int type) throws IOException {
		try {
			String fileName= DateUtil.getTimeStr(new Date(), "HHmmsss")+ UUID.randomUUID().toString().replace("-", "");
			// 上传到七牛后保存的文件名
			String key= DateUtil.getTimeStr(new Date(), "yyyyMM")+"/"+fileName+".jpg" ;
			// 调用put方法上传
			Response res = uploadManager.put(FilePath, key, getUpToken());
			JSONObject object=JSONObject.fromObject(res.bodyString());
			if(object!=null){ 
				return ConfigUtil.getSingleValue("qiniu_imagedomain")+String.valueOf(object.get("key")) ; 
			}
			
		} catch (QiniuException e) {
			Response r = e.response;
			// 请求失败时打印的异常的信息
			System.out.println(r.toString());
			try {
				// 响应的文本信息
				System.out.println(r.bodyString());
			} catch (QiniuException e1) {
				// ignore
			}
		}
		return "";
	}
	
	
}
