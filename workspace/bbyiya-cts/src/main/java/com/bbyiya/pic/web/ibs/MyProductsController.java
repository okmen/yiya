package com.bbyiya.pic.web.ibs;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.pic.service.ibs.IIbs_MyproductService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/ibs/branch")
public class MyProductsController extends SSOController {
	
	@Resource(name = "ibs_MyproductService")
	private IIbs_MyproductService proService;
	
	
	
	/**
	 * IBS客户一对一作品列表
	 * @param index
	 * @param size
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/findMyProductsforCustomer")
	public String findMyProductsForBranch(Integer inviteStatus,@RequestParam(required = false, defaultValue = "")String keywords,
			@RequestParam(required = false, defaultValue = "1")int index,@RequestParam(required = false, defaultValue = "20")int size) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=proService.findMyProductsSourceCustomerOfBranch(user.getUserId(),null,inviteStatus,keywords,index,size);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}

}
