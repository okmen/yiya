package com.bbyiya.pic.web.cts;

import java.util.Calendar;
import java.util.Date;
//import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.pic.service.IPic_OrderMgtService;
import com.bbyiya.pic.service.cts.ICts_OrderManageService;
import com.bbyiya.pic.vo.order.SearchOrderParam;
import com.bbyiya.pic.vo.order.UserOrderResultVO;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;


@Controller
@RequestMapping(value = "/cts/order")
public class OrderController  extends SSOController {
	@Resource(name = "pic_orderMgtService")
	private IPic_OrderMgtService orderService;
	
	@Resource(name = "cts_OrderManageService")
	private ICts_OrderManageService ctsOrderService;
	
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
					orderService.downloadImg((List<UserOrderResultVO>)rq.getBasemodle(),fileDir);
				}
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 获取影楼发展的用户下的订单列表
	 * @param branchuserId  影楼ID
	 * @param userorderid	订单ID
	 * @param status	//订单状态
	 * @param index		
	 * @param size
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/findUserOrderOfBranch")
	public String findUserOrderOfBranch(String branchuserId,String startTimeStr,String endTimeStr,Integer status,int index,int size) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();	
		Long branchuserIdL=null;
		if(!ObjectUtil.isEmpty(branchuserId)){
			try {
				branchuserIdL=ObjectUtil.parseLong(branchuserId);
			} catch (Exception e) {
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数错误，branchuserId必须输入数字！");
				return JsonUtil.objectToJsonStr(rq);
			}
		}
		
		if (user != null) {				
			rq=ctsOrderService.find_payorderExtByBranchUpUserid(branchuserIdL, startTimeStr, endTimeStr, status, index, size);			
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 获取微商发展的用户下的订单列表
	 * @param weiuserId  微商ID
	 * @param userorderid	订单ID
	 * @param status	//订单状态
	 * @param index		
	 * @param size
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/findUserOrderOfWeiUser")
	public String findUserOrderOfWeiUser(String userId,String startTimeStr,String endTimeStr,Integer status,int index,int size) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();	
		Long weiuserIdL=null;
		if(!ObjectUtil.isEmpty(userId)){
			try {
				weiuserIdL=ObjectUtil.parseLong(userId);
			} catch (Exception e) {
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数错误，weiuserId必须输入数字！");
				return JsonUtil.objectToJsonStr(rq);
			}
		} 
		 
		if (user != null) {				
			rq=ctsOrderService.find_payorderExtByWeiUserUpUserid(weiuserIdL, startTimeStr, endTimeStr, status, index, size);			
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	

	
}
