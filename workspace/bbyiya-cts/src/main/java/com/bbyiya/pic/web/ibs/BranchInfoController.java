package com.bbyiya.pic.web.ibs;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.pic.service.ibs.IIbs_BranchMgtService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/ibs/branch")
public class BranchInfoController  extends SSOController{
	@Resource(name = "ibs_BranchMgtService")
	private IIbs_BranchMgtService branchService;
	
	/**
	 * 设置店铺页信息
	 * @param logo
	 * @param promotionstr
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/editBranchShopInfo")
	public String editBranchShopInfo(String logo,String promotionstr) throws Exception {
		ReturnModel rq=new ReturnModel(); 
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=branchService.editBranchShopInfo(user.getUserId(), logo, promotionstr);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
}
