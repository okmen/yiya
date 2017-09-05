package com.bbyiya.pic.web.calendar;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.baseUtils.GenUtils;
import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.dao.OPayorderMapper;
import com.bbyiya.dao.PPostmodelMapper;
import com.bbyiya.dao.PPostmodelareasMapper;
import com.bbyiya.dao.TiActivityworksMapper;
import com.bbyiya.dao.TiMyworksMapper;
import com.bbyiya.dao.TiProductsMapper;
import com.bbyiya.dao.TiProductstylesMapper;
import com.bbyiya.dao.UUseraddressMapper;
import com.bbyiya.enums.OrderStatusEnum;
import com.bbyiya.enums.PayOrderTypeEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.model.OPayorder;
import com.bbyiya.model.PPostmodel;
import com.bbyiya.model.PPostmodelareas;
import com.bbyiya.model.TiActivityworks;
import com.bbyiya.model.TiMyworks;
import com.bbyiya.model.TiProducts;
import com.bbyiya.model.TiProductstyles;
import com.bbyiya.model.UUseraddress;
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
	@Autowired
	private OPayorderMapper payMapper;
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
	public String findpostlist(long addressId, @RequestParam(required = false, defaultValue = "0") long productId,@RequestParam(required = false, defaultValue = "0") long workId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			//TODO b端用户运费判断
			if(productId<=0){
				if(workId>0){
					TiMyworks myworks= workMapper.selectByPrimaryKey(workId);
					if(myworks!=null){
						productId=myworks.getProductid();
					}
				}
			}
			rq = postMgtService.find_postlist_ti(addressId, productId);
			
		}
		rq.setStatu(ReturnStatus.Success);
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

	@Autowired
	private TiMyworksMapper workMapper;
	@Autowired
	private TiProductsMapper tiProductsMapper;
	@Autowired
	private PPostmodelMapper postmodelMapper;
	@Autowired
	private UUseraddressMapper addressMapper;
	@Autowired
	private PPostmodelareasMapper postmodelareasMapper;
	@Autowired
	private TiActivityworksMapper actworkMapper;
	
	/**
	 * 邮费订单
	 * @param addressId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/orderForPost")
	public String orderForPost(long addressId,long workId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			TiMyworks myworks= workMapper.selectByPrimaryKey(workId);
			TiActivityworks actWork=actworkMapper.selectByPrimaryKey(workId);
			if(myworks!=null&&actWork!=null){
				TiProducts products= tiProductsMapper.selectByPrimaryKey(myworks.getProductid());
				if(products!=null){
					PPostmodel post = postmodelMapper.selectByPrimaryKey(products.getPostmodelid());
					if (post != null) {
						UUseraddress addr= addressMapper.get_UUserAddressByKeyId(addressId);
						if(addr!=null){
							PPostmodelareas areamod = postmodelareasMapper.getPostAreaModel(post.getPostmodelid(), addr.getArea());
							if (areamod != null) {
								post.setAmount(areamod.getAmount());
							}
						}
						if(post.getAmount()!=null&&post.getAmount().doubleValue()>0){
							long orderAddressId=orderMgtService.addOrderAddressReturnId(addressId);
							OPayorder payorder=new OPayorder();
							payorder.setPayid(GenUtils.getOrderNo(user.getUserId()));
							payorder.setCreatetime(new Date());
							payorder.setOrdertype(Integer.parseInt(PayOrderTypeEnum.ti_postage.toString()));
							payorder.setTotalprice(post.getAmount());
							payorder.setUserorderid(String.valueOf(workId));//
							payorder.setExtobject(String.valueOf(orderAddressId));  
							payorder.setStatus(Integer.parseInt(OrderStatusEnum.noPay.toString()));
							payMapper.insert(payorder);
							Map<String, Object> resultMap=new HashMap<String, Object>();
							resultMap.put("payId", payorder.getPayid());
							rq.setStatu(ReturnStatus.Success);
							rq.setBasemodle(resultMap); 
						}
					}
				}
			}
			
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
}
