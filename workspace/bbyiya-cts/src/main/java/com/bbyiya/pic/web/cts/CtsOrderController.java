package com.bbyiya.pic.web.cts;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.pic.service.cts.ICts_OrderMgtService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.order.SearchOrderParam;
import com.bbyiya.vo.order.UserOrderResultVO;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "/cts/order")
public class CtsOrderController extends SSOController {
	
	@Resource(name = "cts_OrderMgtService")
	private ICts_OrderMgtService orderService;
	
	
	/**
	 * O01 PBS查询订单列表
	 *  
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getCtsOrderList")
	public String getCtsOrderList(String myproductJson,int index,int size) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();		
		if (user != null) {	
			JSONObject jb = JSONObject.fromObject(myproductJson);
			if(jb.getString("status").equals("")){
				myproductJson=myproductJson.replaceAll("\"status\":\"\"", "\"status\":null");
			}
			if(jb.getString("ordertype").trim().equals("")){
				myproductJson=myproductJson.replaceAll("\"ordertype\":\"\"", "\"ordertype\":null");
			}
			if(jb.getString("agentUserId").equals("")){
				myproductJson=myproductJson.replaceAll("\"agentUserId\":\"\"", "\"agentUserId\":null");
			}
			
			Object object=JsonUtil.jsonStrToObject(myproductJson, SearchOrderParam.class);
			if(object==null){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数传入错误！");
				return JsonUtil.objectToJsonStr(rq);
			}

			SearchOrderParam param = (SearchOrderParam)JSONObject.toBean(jb,SearchOrderParam.class);			
			PageInfo<UserOrderResultVO> result= orderService.find_ctsorderList(param, index, size);
			rq.setBasemodle(result);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("获取列表成功！");
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
}
