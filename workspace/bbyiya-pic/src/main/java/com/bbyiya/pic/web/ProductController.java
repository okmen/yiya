package com.bbyiya.pic.web;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.service.pic.IBaseProductService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/product")
public class ProductController extends SSOController {
	@Resource(name="baseProductServiceImpl")
	private IBaseProductService productService;
	/**
	 * P01 场景列表
	 * @param type
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/scenelist")
	public String area(@RequestParam(required = false, defaultValue = "0") int type, String code) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(ConfigUtil.getMaplist("scenelist")); 
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * P02 相册列表
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/productlist")
	public String product(@RequestParam(required = false, defaultValue = "0") long uid) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(productService.findProductList(uid));   
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 * P03 产品详情/相册详情
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/pro")
	public String productlist(@RequestParam(required = false, defaultValue = "0") long pid) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(productService.getProductResult(pid));   
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
}
