package com.bbyiya.pic.web;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.picture.PicUtil;
import com.bbyiya.utils.upload.FileUploadUtils_qiniu;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;
//import com.sdicons.json.validator.impl.predicates.Content;

@Controller
@RequestMapping(value = "/upload")
public class UploadController extends SSOController {
	
	@ResponseBody
	@RequestMapping(value = "/getUploadToken")
	public String loginAjax() throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			String token=FileUploadUtils_qiniu.getUpToken();
			rq.setStatu(ReturnStatus.Success);
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("upToken", token);
			rq.setBasemodle(map);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
//	@ResponseBody
//	@RequestMapping(value = "/pic")
//	public String pic(String content,int x,int y) throws Exception {
//		PicUtil tt = new PicUtil();  
//		  
//        BufferedImage d = tt.loadImageLocal("C:\\Users\\Administrator\\Desktop\\temp\\1.JPG");  
////      BufferedImage b = tt.loadImageLocal("E:\\文件(word,excel,pdf,ppt.txt)\\zte-logo.png");  
//         tt.writeImageLocal("C:\\Users\\Administrator\\Desktop\\temp\\11.JPG",tt.modifyImage(d,content,x,y)  
//        //往图片上写文件  
//         );   
//  
//        //tt.writeImageLocal("D:\\cc.jpg", tt.modifyImagetogeter(b, d));  
//        //将多张图片合在一起  
//        System.out.println("success"); 
//		return "";
//	}
	
	
}
