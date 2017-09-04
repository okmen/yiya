package com.bbyiya.pic.web.calendar;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.dao.TiProductstylesMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.model.TiProductstyles;
import com.bbyiya.service.calendar.ITi_OrderMgtService;
import com.bbyiya.service.pic.IBasePostMgtService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.order.SubmitOrderProductParam;
import com.bbyiya.vo.order.UserOrderSubmitParam;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/ti_submitOrder")
public class Ti_OrderSubmitController extends SSOController {
	@Autowired
	private TiProductstylesMapper styleMapper;
	@Resource(name = "basePostMgtServiceImpl")
	private IBasePostMgtService postMgtService;
	@Resource(name = "tiOrderMgtServiceImpl")
	private ITi_OrderMgtService orderMgtService;
	/**
	 * 运费情况
	 * @param area
	 * @param addressId
	 * @param styleId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/findposts")
	public String findpostlist(long addressId, long styleId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			//TODO b端用户运费判断
			
			TiProductstyles style = styleMapper.selectByPrimaryKey(styleId);
			if (style != null) {
				rq = postMgtService.find_postlist_ti(addressId, style.getProductid());
			}
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * c端用户下单
	 * @param addrId
	 * @param orderType
	 * @param remark
	 * @param productJsonStr
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/submitOrder")
	public String submitOrder(@RequestParam(required = false, defaultValue = "0") long addrId, String orderType, String remark, String productJsonStr) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			SubmitOrderProductParam productParam = (SubmitOrderProductParam) JsonUtil.jsonStrToObject(productJsonStr, SubmitOrderProductParam.class);
			if (productParam != null) {
				OOrderproducts product = new OOrderproducts();
				product.setProductid(productParam.getProductId());
				product.setStyleid(productParam.getStyleId());
				product.setCount(productParam.getCount());

				// 下单参数
				UserOrderSubmitParam param = new UserOrderSubmitParam();
				param.setUserId(user.getUserId());
				param.setRemark(remark);
				if (addrId > 0) {
					param.setAddrId(addrId);
				}
				if (productParam.getCartId() != null && productParam.getCartId() > 0) {
					param.setCartId(productParam.getCartId());
				}
				int type = ObjectUtil.parseInt(orderType);
				if (type > 0) {
					param.setOrderType(type);
				}
				if (productParam.getPostModelId() != null) {
					param.setPostModelId(productParam.getPostModelId());
				}
				param.setOrderproducts(product);
				rq = orderMgtService.submitOrder(param);
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
