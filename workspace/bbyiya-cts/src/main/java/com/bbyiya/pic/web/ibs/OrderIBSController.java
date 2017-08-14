package com.bbyiya.pic.web.ibs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.pic.service.ibs.IIbs_OrderMgtService;
import com.bbyiya.pic.vo.order.OrderCountResultVO;
import com.bbyiya.utils.FileUtils;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;
import com.github.pagehelper.PageInfo;
import com.sdicons.json.mapper.MapperException;

@Controller
@RequestMapping(value = "/ibs/order")
public class OrderIBSController extends SSOController {
	@Resource(name = "ibs_OrderMgtService")
	private IIbs_OrderMgtService orderService;

	/**
	 * 来自分店的订单列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/myBranchOrderlist")
	public String myBranchOrderlist(@RequestParam(required = false, defaultValue = "1") int status,
			@RequestParam(required = false, defaultValue = "") String keywords,int index,int size) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			if (ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.branch)) {
				rq = orderService.findMyBranchOrderlist(user.getUserId(), status,keywords,index,size);
			} else {
				rq.setStatu(ReturnStatus.SystemError_1);
				rq.setStatusreson("您还不是代理商，没有权限");
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	
	
}
