package com.bbyiya.pic.web.cts;

import java.util.Calendar;
import java.util.Date;
//import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.OOrderproductdetails;
import com.bbyiya.pic.service.IPic_OrderMgtService;
import com.bbyiya.pic.vo.order.SearchOrderParam;
import com.bbyiya.pic.vo.order.UserOrderResultVO;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.FileUtils;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.upload.FileDownloadUtils;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/cts/order")
public class OrderController  extends SSOController {
	@Resource(name = "pic_orderMgtService")
	private IPic_OrderMgtService orderService;
	
	/**
	 * O06订单列表查询
	 * @param myproductJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/findOrderlist")
	public String findOrderlist(String myproductJson,String isDownload,String fileDir,int daycount) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			Calendar c1 = new GregorianCalendar();
			Date nowtime=new Date();
			c1.setTime(nowtime);
			c1.set(Calendar.HOUR_OF_DAY, 14);
			c1.set(Calendar.MINUTE, 0);
			c1.set(Calendar.SECOND, 0);
			Calendar c2 = new GregorianCalendar();
			c2.setTime(nowtime);
			c2.set(Calendar.HOUR_OF_DAY, 14);
			c2.set(Calendar.MINUTE, 0);
			c2.set(Calendar.SECOND, 0);
			c2.set(Calendar.DATE,daycount); 
			
			SearchOrderParam param= (SearchOrderParam)JsonUtil.jsonStrToObject(myproductJson, SearchOrderParam.class);
			param.setStartTime(c2.getTime());
			param.setEndTime(c1.getTime()); 
			param.setStartTimeStr(DateUtil.getTimeStr(param.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
			param.setEndTimeStr(DateUtil.getTimeStr(param.getEndTime(), "yyyy-MM-dd HH:mm:ss")); 
			System.out.println(param.getStartTimeStr() );
			System.out.println(param.getEndTimeStr() );
			rq=orderService.find_orderList(param);
			if(ObjectUtil.parseInt(isDownload)>0){
				if(ObjectUtil.isEmpty(fileDir)){
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("请输入要保存到本地的文件路径");
					return JsonUtil.objectToJsonStr(rq);
				}
				if(rq.getStatu().equals(ReturnStatus.Success)){
					downloadImg((List<UserOrderResultVO>)rq.getBasemodle(),fileDir);
				}
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
//	@ResponseBody
//	@RequestMapping(value = "/aa")
//	public String findOrderlist(String fileDir) throws Exception {
//		ReturnModel rq = new ReturnModel();
//		LoginSuccessResult user = super.getLoginUser();
//		if (user != null) {
//			FileDownloadUtils.setDPI(fileDir);
//		} else {
//			rq.setStatu(ReturnStatus.LoginError);
//			rq.setStatusreson("登录过期");
//		}
//		return JsonUtil.objectToJsonStr(rq);
//	}

	//TODO
	public void downloadImg(List<UserOrderResultVO> orderlist,String basePath){
		try {
			FileUtils.isDirExists(basePath);
		} catch (Exception e) {
			basePath="D:\\orderImgs\\";
			FileUtils.isDirExists(basePath);
		}

		for (UserOrderResultVO order : orderlist) {
			Calendar c1 = new GregorianCalendar();
			c1.setTime(order.getPaytime());
			c1.set(Calendar.HOUR_OF_DAY, 18);
			c1.set(Calendar.MINUTE, 0);
			c1.set(Calendar.SECOND, 0);
			Calendar c2 = new GregorianCalendar();
			c2.setTime(order.getPaytime());
			if(c2.getTime().getTime()>c1.getTime().getTime()){
				c2.set(Calendar.DAY_OF_MONTH, 1);
			}
			String file_temp=DateUtil.getTimeStr(c2.getTime(), "MMdd");
			
			//创建文件夹
			FileUtils.isDirExists(basePath+"\\"+file_temp);
			FileUtils.isDirExists(basePath+"\\"+file_temp+"\\"+order.getUserorderid());;
			int i=1;
			for (OOrderproductdetails detail : order.getImglist()) {
				String file_dir=basePath+"\\"+file_temp+"\\"+order.getUserorderid();
				String fileFull_name=file_dir+"\\"+i+".jpg";
				if(!FileUtils.isFileExists(fileFull_name)){
					try {
						FileDownloadUtils.download(detail.getImageurl(),fileFull_name);
						FileDownloadUtils.setDPI(fileFull_name);
//						System.out.println(fileFull_name); 
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				i++;
			}
			
		}
	}

	


	
}
