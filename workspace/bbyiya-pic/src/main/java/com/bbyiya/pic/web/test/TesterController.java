package com.bbyiya.pic.web.test;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.web.base.SSOController;


@Controller
@RequestMapping(value = "/test")
public class TesterController  extends SSOController{

	@ResponseBody
	@RequestMapping(value = "/send")
	public String download(String url,int count,int pageSize) throws Exception {
		ReturnModel rq = new ReturnModel();
		Date t1=new Date();
		if(count>0){
			ThreadCreateUtil th1=new ThreadCreateUtil(url, pageSize, count);
			int pageCount=count/pageSize;
			for(int i=0;i<pageCount;i++){
				new Thread(th1, "a" + i).start(); 
			}
		}
		Date t2=new Date();
		long timeS= t2.getTime()-t1.getTime();
		float seconds=(float)timeS/1000 ;
		rq.setBasemodle(seconds); 
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}
	@RequestMapping(value = "/export")
	public String downloadorder() throws Exception {
		return "exportOrder";
	}
	
}

