package com.bbyiya.pic.web.cts;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.EErrorsMapper;
import com.bbyiya.dao.UBranchinfotempMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.pic.service.IPic_BranchMgtService;
import com.bbyiya.pic.service.IPic_ProductService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;
@Controller
@RequestMapping(value = "/ibs/branch/myproduct")
public class MyProductBranchController extends SSOController {
	@Resource(name = "pic_BranchMgtService")
	private IPic_BranchMgtService branchService;
	@Resource(name = "pic_productService")
	private IPic_ProductService proService;
	@Autowired
	private UBranchinfotempMapper tempMapper;
	
	@Autowired
	private EErrorsMapper logger;
	 
	@ResponseBody 
	@RequestMapping(value = "/findMyProductsForBranch")
	public String findMyProductsForBranch(@RequestParam(required = false, defaultValue = "1")int index,@RequestParam(required = false, defaultValue = "20")int size) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=proService.findMyProductsForBranch(user.getUserId(),null,null,index,size);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("µÇÂ¼¹ýÆÚ");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
}
