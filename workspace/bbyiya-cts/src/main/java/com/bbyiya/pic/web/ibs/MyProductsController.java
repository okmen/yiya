package com.bbyiya.pic.web.ibs;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.InviteStatus;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PMyproductsinvites;
import com.bbyiya.pic.service.ibs.IIbs_MyproductService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
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
	
	/**
	 * 得到影楼作品的邀请二维码
	 * @param cartid
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getProductInviteCode")
	public String getProductInviteCode(String cartid) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=proService.getProductInviteCode(user.getUserId(), ObjectUtil.parseLong(cartid));
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}

}
