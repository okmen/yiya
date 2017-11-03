package com.bbyiya.pic.web.common;


import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.OUserorders;
import com.bbyiya.service.pic.IBasePostMgtService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.logistics.LogisticsQuery;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/post")
public class LogisticsController extends SSOController {
	@Resource(name = "basePostMgtServiceImpl")
	private IBasePostMgtService postservice;
	/**
	 * 
	 * 根据运单号，物流公司编码简称得到物流信息
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getlogistics")
	public String getlogistics(String expressCode,String expressCom) throws Exception {
		ReturnModel rqModel = new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(ObjectUtil.isEmpty(expressCode)||ObjectUtil.isEmpty(expressCom)){
				rqModel.setStatu(ReturnStatus.ParamError);		
				rqModel.setStatusreson("运单号或物流编码不能为空！");
				return JsonUtil.objectToJsonStr(rqModel);
			}
			String resultStr=LogisticsQuery.getLogisticsQueryByNum(expressCode, expressCom);
			JSONObject model = JSONObject.fromObject(resultStr);		
			if (model != null) {
				String message = String.valueOf(model.get("message"));
				if(!ObjectUtil.isEmpty(message)&&message.equalsIgnoreCase("ok")){
					rqModel.setStatu(ReturnStatus.Success);		
					rqModel.setStatusreson("获取物流信息成功！");
					rqModel.setBasemodle(model);
				}else {
					String returnCode = String.valueOf(model.get("returnCode"));
					rqModel.setStatu(ReturnStatus.ParamError);		
					rqModel.setStatusreson(message);
					return JsonUtil.objectToJsonStr(rqModel);
				}
			}else{
				rqModel.setStatu(ReturnStatus.ParamError);		
				rqModel.setStatusreson("查询失败");
				return JsonUtil.objectToJsonStr(rqModel);
			}
		}else{
			rqModel.setStatu(ReturnStatus.LoginError);
			rqModel.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rqModel);
		}	
		return JsonUtil.objectToJsonStr(rqModel);
	}
	
	@Autowired
	private OUserordersMapper userorderMapper;
	/**
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getOrderlogistics")
	public String getOrderlogistics(String orderId) throws Exception {
		ReturnModel rqModel = new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			OUserorders order= userorderMapper.selectByPrimaryKey(orderId);
			if(order!=null&&!ObjectUtil.isEmpty(order.getExpresscode())&&!ObjectUtil.isEmpty(order.getExpresscom())){
				String resultStr=LogisticsQuery.getLogisticsQueryByNum(order.getExpresscode(), order.getExpressorder());
				JSONObject model = JSONObject.fromObject(resultStr);		
				if (model != null) {
					String message = String.valueOf(model.get("message"));
					if(!ObjectUtil.isEmpty(message)&&message.equalsIgnoreCase("ok")){
						rqModel.setStatu(ReturnStatus.Success);		
						rqModel.setStatusreson("获取物流信息成功！");
						Map<String, Object> map=new HashMap<String, Object>();
						map.put("expressName", order.getExpresscom());
						map.put("message", model);
						rqModel.setBasemodle(map);
					}else {
						rqModel.setStatu(ReturnStatus.ParamError);		
						rqModel.setStatusreson(message);
						return JsonUtil.objectToJsonStr(rqModel);
					}
				}else{
					rqModel.setStatu(ReturnStatus.ParamError);		
					rqModel.setStatusreson("查询失败");
					return JsonUtil.objectToJsonStr(rqModel);
				}
				
			}
			
			
		}else{
			rqModel.setStatu(ReturnStatus.LoginError);
			rqModel.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rqModel);
		}	
		return JsonUtil.objectToJsonStr(rqModel);
	}
	
	/**
	 * 配置文件中读取物流公司信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getPostInfo")
	public String getPostInfo() throws Exception {
		ReturnModel rqModel = new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rqModel=postservice.getPostInfo();
		}else{
			rqModel.setStatu(ReturnStatus.LoginError);
			rqModel.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rqModel);
		}	
		return JsonUtil.objectToJsonStr(rqModel);
	}
}
