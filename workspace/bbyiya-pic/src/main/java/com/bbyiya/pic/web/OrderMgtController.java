package com.bbyiya.pic.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.EErrorsMapper;
import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.MyProductStatusEnum;
import com.bbyiya.model.EErrors;
import com.bbyiya.model.OOrderproductdetails;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.pic.service.IPic_OrderMgtService;
import com.bbyiya.pic.service.pbs.IPbs_OrderMgtService;
import com.bbyiya.pic.vo.order.OrderPhotoParam;
import com.bbyiya.pic.vo.order.SaveOrderPhotoParam;
import com.bbyiya.pic.vo.order.SubmitOrderProductParam;
import com.bbyiya.service.pic.IBaseOrderMgtService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/order")
public class OrderMgtController extends SSOController {

	@Resource(name = "baseOrderMgtServiceImpl")
	private IBaseOrderMgtService orderMgtService;
	
	@Resource(name = "pic_orderMgtService")
	private IPic_OrderMgtService orderService;
	@Autowired
	private PMyproductsMapper myMapper;
	@Autowired
	private EErrorsMapper errorMapper;
	/**
	 * O01 提交订单（购买）
	 * 
	 * @param addrId
	 * @param productJsonStr
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/submitOrder")
	public String submitOrder(@RequestParam(required = false, defaultValue = "0") long addrId, String remark, String productJsonStr) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			if (!ObjectUtil.isEmpty(productJsonStr)) {
				SubmitOrderProductParam param = (SubmitOrderProductParam) JsonUtil.jsonStrToObject(productJsonStr, SubmitOrderProductParam.class);
				if (param != null) {
					OOrderproducts product = new OOrderproducts();
					product.setProductid(param.getProductId());
					product.setStyleid(param.getStyleId());
					product.setCount(param.getCount());
					product.setSalesuserid(0l);
					rq = orderMgtService.submitOrder_singleProduct(user.getUserId(), addrId, remark, product);
					if (param.getCartId() != null && param.getCartId() > 0) {
						PMyproducts myPro = myMapper.selectByPrimaryKey(param.getCartId());
						if (myPro != null) {
							if (rq.getStatu().equals(ReturnStatus.Success)) {
								Map<String, Object> map = (Map<String, Object>) rq.getBasemodle();
								if (map != null) {
									String orderId = String.valueOf(map.get("orderId"));
									myPro.setOrderno(orderId);
									myPro.setStatus(Integer.parseInt(MyProductStatusEnum.ordered.toString())); 
									myMapper.updateByPrimaryKey(myPro);
								}
							} 
						}
					}
				}
			}
			rq.setStatu(ReturnStatus.Success);

		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * O02 保存订单相片
	 * @param orderImagesJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/saveProductOrderDetail")
	public String saveProductOrderDetail(String orderImagesJson) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			if (!ObjectUtil.isEmpty(orderImagesJson)) {
				SaveOrderPhotoParam param = (SaveOrderPhotoParam) JsonUtil.jsonStrToObject(orderImagesJson, SaveOrderPhotoParam.class);// (productImagelistJson);
				if (param == null || ObjectUtil.isEmpty(param.getOrderId())) {
					rq.setStatu(ReturnStatus.ParamError_1);
					rq.setStatusreson("参数不全");
					return JsonUtil.objectToJsonStr(rq);
				}
				if (param.getImageList() != null && param.getImageList().size() > 0) {
					if (param.getImageList().size() < 12) {
						rq.setStatu(ReturnStatus.ParamError_1);
						rq.setStatusreson("图片少于12张！");
						return JsonUtil.objectToJsonStr(rq);
					}
					List<OOrderproductdetails> images = new ArrayList<OOrderproductdetails>();
					for (OrderPhotoParam pp : param.getImageList()) {
						if (ObjectUtil.isEmpty(pp.getImageUrl()) || ObjectUtil.isEmpty(pp.getPrintNo())||ObjectUtil.isEmpty(pp.getBackImageUrl())) {
							rq.setStatu(ReturnStatus.ParamError_1);
							rq.setStatusreson("图片信息有误，打印号：" + pp.getPrintNo());
							addlog(orderImagesJson);
							return JsonUtil.objectToJsonStr(rq); 
						}
						OOrderproductdetails item = new OOrderproductdetails();
						item.setOrderproductid(param.getOrderId());
						item.setPrintno(pp.getPrintNo());
						String printNo = pp.getPrintNo();
						item.setPosition(ObjectUtil.parseInt(printNo.substring(printNo.lastIndexOf("-") + 1, printNo.length())));
						item.setImageurl(pp.getImageUrl());
						item.setBackimageurl(pp.getBackImageUrl()); 
						images.add(item);
					}
					rq = orderMgtService.saveOrderImages(param.getOrderId(), images);
				}
			} else {
				rq.setStatu(ReturnStatus.ParamError_1);
				rq.setStatusreson("参数不全");
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * O04 去支付（从订单详情/列表出发）
	 * 
	 * @param orderId
	 * @param productImagelistJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getPayOrder")
	public String getPayOrder(String userOrderId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			rq = orderMgtService.getPayOrderByOrderId(userOrderId);
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * O03 我的购买订单
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/buylist")
	public String buylist() throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			rq = orderMgtService.findOrderlist(user.getUserId());
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	/**
	 * O03 我的购买订单 带分页
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/userbuylist")
	public String userbuylist(int index,int size) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			rq = orderMgtService.findUserOrderlist(user.getUserId(),index,size);
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * O02取消订单
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/cancelorder")
	public String cancelorder(String orderId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			rq = orderService.cancelOrder(orderId);
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	

	/**
	 * O05 支付详情
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getOrderInfo")
	public String getOrderInfo(String orderId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			rq = orderMgtService.getOrderInfo(user.getUserId(), orderId);
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}


	
	public void addlog(String msg) {
		EErrors errors = new EErrors();
		errors.setClassname(this.getClass().getName());
		errors.setMsg(msg);
		errors.setCreatetime(new Date()); 
		errorMapper.insert(errors);
	}

}
