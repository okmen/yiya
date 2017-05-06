package com.bbyiya.pic.web.cts;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.EErrorsMapper;
import com.bbyiya.dto.PProductsDTO;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.PProductstyles;
import com.bbyiya.pic.service.cts.IProductMgtService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.product.ProductSearchParam;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;
@Controller
@RequestMapping(value = "/cts/product")
public class PProductController extends SSOController {
	@Resource(name = "productMgtService")
	private IProductMgtService productService;	
	@Autowired
	private EErrorsMapper logger;
	
	
	
	
	
	/**
	 * C03 根据查询条件获取产品列表
	 * @param searchParam
	 * @param index
	 * @param size
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/findPproductsList")
	public String findPproductsList(@ModelAttribute("product") ProductSearchParam searchParam,@RequestParam(required = false, defaultValue = "1")int index,@RequestParam(required = false, defaultValue = "20")int size) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=productService.findProductListBySearchParam(index, size, searchParam);		
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 * C04 修改产品信息
	 * @param searchParam
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/editPproducts")
	public String editPproducts(String myproductJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			PProductsDTO productDto=(PProductsDTO) JsonUtil.jsonStrToObject(myproductJson, PProductsDTO.class);
			rq=productService.updateProductByProductId(productDto);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 * C05 根据查询条件获取产品款式列表
	 * @param searchParam
	 * @param index
	 * @param size
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/findProductStylesList")
	public String findProductStylesList(@ModelAttribute("product") ProductSearchParam searchParam,@RequestParam(required = false, defaultValue = "1")int index,@RequestParam(required = false, defaultValue = "20")int size) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=productService.findProductStylesBySearchParam(index, size, searchParam);		
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 * C06 修改产品款式信息
	 * @param searchParam
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/editProductStyles")
	public String editProductStyles(String myproductJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			PProductstyles style=(PProductstyles) JsonUtil.jsonStrToObject(myproductJson, PProductstyles.class);		
			rq=productService.addAndupdateProductStyles(user.getUserId(),style);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * C06 新增产品款式信息
	 * @param searchParam
	 * @return
	 * @throws Exception
	 */
	@ResponseBody 
	@RequestMapping(value = "/addProductStyles")
	public String addProductStyles(String myproductJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			PProductstyles style=(PProductstyles) JsonUtil.jsonStrToObject(myproductJson, PProductstyles.class);		
			rq=productService.addAndupdateProductStyles(user.getUserId(),style);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
}
