package com.bbyiya.pic.web.version_one;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.pic.service.IPic_ProductService;
import com.bbyiya.service.pic.IBaseProductService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;


@Controller
@RequestMapping(value = "/productV2")
public class ProductMgtController extends SSOController{
	@Resource(name="baseProductServiceImpl")
	private IBaseProductService productService;
	
	@Resource(name="pic_productService")
	private IPic_ProductService proService;
	

	
//	
//	/**
//	 * 产品详情
//	 * @param pid
//	 * @return
//	 * @throws Exception
//	 */
//	@ResponseBody
//	@RequestMapping(value = "/pro")
//	public String productlist(@RequestParam(required = false, defaultValue = "0") long pid) throws Exception {
//		ReturnModel rq = new ReturnModel();
//		LoginSuccessResult user= super.getLoginUser();
//		if(user!=null){
//			rq.setStatu(ReturnStatus.Success);
//			rq.setBasemodle(productService.getProductResult(pid));   
//		}else {
//			rq.setStatu(ReturnStatus.LoginError);
//			rq.setStatusreson("登录过期");
//		}
//		return JsonUtil.objectToJsonStr(rq);
//	}
	
	@ResponseBody
	@RequestMapping(value = "/sample")
	public String sample(@RequestParam(required = false, defaultValue = "0") long productId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=proService.getProductSamples(productId);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

}
