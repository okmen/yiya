package com.bbyiya.api.web;

import java.io.File;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oreilly.servlet.MultipartRequest;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;



import com.bbyiya.utils.qiniu.Uploader;
import com.bbyiya.vo.ReturnModel;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import sun.misc.BASE64Encoder;

import com.sdicons.json.mapper.MapperException;
@Controller
@RequestMapping(value = "/upload")
public class UploadController {
	
	
	@ResponseBody
	@RequestMapping(value = "/uploadImage", method = { RequestMethod.POST, RequestMethod.GET })
	public String upload(HttpServletRequest request) throws MapperException { 
		ReturnModel rq=new ReturnModel();
		int cateInt = ObjectUtil.parseInt(request.getParameter("cate"));
		int typeInt = ObjectUtil.parseInt(request.getParameter("type"));
		String file = request.getParameter("file");
//		if (cateInt<=0&&( file == null || "".equals(file))) {// 通过文件形式传输
//			ReturnModel  modle = imgReturnBase64(request);
//			if (modle.getStatu().equals(ReturnStatus.Success)) {
//				file = modle.getStatusreson();
//				Map<String, Object> map = (Map<String, Object>) modle.getBasemodle();
//				if (map != null) {
//					cateInt = ObjectUtil.parseInt(String.valueOf(map.get("cate")));
//					typeInt =ObjectUtil.parseInt(String.valueOf(map.get("type")));
//				}
//			} else {
//				rq.setStatu(ReturnStatus.ParamError);
//				rq.setStatusreson(modle.getStatusreson());
//				return JsonUtil.objectToJsonStr(rq);
//			}
//		}
		try {
			Uploader.uploadReturnUrl("C:\\Users\\Administrator\\Desktop\\imgs\\img_11_7_1.jpg");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rq.setBasemodle(file);
		return JsonUtil.objectToJsonStr(rq);//HttpRequestHelper.sendGet("", "cate="+cateInt+"&type="+typeInt);
	}
	
	

	/**
	 * 图片临时保存 获取base64编码后 删除
	 * 
	 * @param request
	 * @return
	 */
	private ReturnModel imgReturnBase64(HttpServletRequest request) {
		ReturnModel resultMsg = new ReturnModel();
		MultipartRequest mr = null;
		// 用来限制用户上传文件大小的
		int maxPostSize = 1 * 100 * 1024 * 1024;
		try {
			String imgBasenew = ConfigUtil.getSingleValue("imgPathTemp");// ResourceBundle.getBundle("commons").getString("imgPathTemp");
			String uploadPath =  DateUtil.getTimeString("yyyyMM") + "/";
			File f = new File(imgBasenew + uploadPath);
			if (!f.isDirectory()) {//文件路劲是否存在
				f.mkdir();
				uploadPath+="Images/";
				String bathString = imgBasenew +uploadPath; 
				File f1 = new File(bathString);
				if (!f1.isDirectory()) {
					f1.mkdir();
				}
			}
			// logger.error("filesPath:"+imgBasenew+uploadPath);
			mr = new MultipartRequest(request, imgBasenew + uploadPath, maxPostSize, "GBK");
			int cate = ObjectUtil.parseInt(mr.getParameter("cate"));
			int type = ObjectUtil.parseInt(mr.getParameter("type"));
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("cate", cate);
			map.put("type", type);
			Enumeration files = mr.getFileNames();
			// logger.error("files");
			String filename = "";
			String filePath = "";
			String pathFull = imgBasenew + uploadPath;
			while (files.hasMoreElements()) {
				filename = (String) files.nextElement();
				File ff = mr.getFile(filename);

				String fileSuffix = ff.getName().substring(ff.getName().lastIndexOf(".") + 1).toUpperCase();
				if (fileSuffix.equals("JPG") || fileSuffix.equals("JPEG") || fileSuffix.equals("GIF") || fileSuffix.equals("PNG") || fileSuffix.equals("BMP")) {
					filePath = mr.getFilesystemName(filename);
					InputStream in = null;
					byte[] data = null;
					// 读取图片字节数组
					try {
						in = new FileInputStream(pathFull + filePath);
						data = new byte[in.available()];
						in.read(data);
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					// 对字节数组Base64编码
					BASE64Encoder encoder = new BASE64Encoder();
					// return ;//返回Base64编码过的字节数组字符串
					resultMsg.setStatu(ReturnStatus.Success);
					resultMsg.setStatusreson(encoder.encode(data));
					resultMsg.setBasemodle(map);
					// 删除临时文件

					if (ff.isFile() && ff.exists()) {
//						 ff.delete();
					}

				} else {
					resultMsg.setStatu(ReturnStatus.SystemError);
					resultMsg.setStatusreson("上传文件格式不正确！");
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			resultMsg.setStatu(ReturnStatus.SystemError);
			resultMsg.setStatusreson(e.getMessage());
			System.out.println(e);
		}
		return resultMsg;
	}
}
