package com.bbyiya.pic.web.calendar;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.enums.OrderTypeEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.pic.service.calendar.IIbs_TiOrderMgtService;
import com.bbyiya.service.calendar.ITi_OrderMgtService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.calendar.TiActivityOrderSubmitParam;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/ibs/ti_order")
public class TiOrderIBSController extends SSOController {
	
	@Resource(name = "ibs_TiOrderMgtService")
	private IIbs_TiOrderMgtService ibstiorderService;
	
	@Resource(name = "tiOrderMgtServiceImpl")
	private  ITi_OrderMgtService basetiorderService;
	
	/**
	 * 本店订单列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/myTiOrderlist")
	public String myTiOrderlist(@RequestParam(required = false, defaultValue = "1") int status,
			@RequestParam(required = false, defaultValue = "") String keywords,int index,int size) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			if (ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.ti_promoter)) {
				rq = ibstiorderService.findTiMyOrderlist(user.getUserId(),Integer.parseInt(OrderTypeEnum.ti_branchOrder.toString()), status,keywords,index,size);
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

	/**
	 * ibs台历下单前的地址信息
	 * @param cartid
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getIbsTiSubmitAddressList")
	public String getIbsTiSubmitAddressList(Long workId) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=ibstiorderService.getIbsTiSubmitAddressList(user.getUserId(), workId);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * ibs台历下单
	 * @param productJsonStr
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/ibsSubmitTiOrder")
	public String ibsSubmitTiOrder(String productJsonStr) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			TiActivityOrderSubmitParam param = (TiActivityOrderSubmitParam) JsonUtil.jsonStrToObject(productJsonStr, TiActivityOrderSubmitParam.class);
			if (param != null) {
				if(ObjectUtil.isEmpty(param.getWorkId())){
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("活动参数有误：workId为空");
					return JsonUtil.objectToJsonStr(rq);
				}
				param.setSubmitUserId(user.getUserId());
				if(ObjectUtil.isEmpty(param.getSubmitUserId())){
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("参数有误：submitUserId为空");
					return JsonUtil.objectToJsonStr(rq);
				}
				rq = basetiorderService.submitOrder_ibs(param);
			} else {
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数有误");
			}

		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
}
