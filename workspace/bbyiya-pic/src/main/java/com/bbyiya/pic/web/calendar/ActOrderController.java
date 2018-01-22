package com.bbyiya.pic.web.calendar;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.baseUtils.GenUtils;
import com.bbyiya.dao.OPayorderMapper;
import com.bbyiya.dao.TiActivityworksMapper;
import com.bbyiya.dao.TiMyworksMapper;
import com.bbyiya.dao.TiProductsMapper;
import com.bbyiya.enums.OrderStatusEnum;
import com.bbyiya.enums.PayOrderTypeEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.calendar.AddressTypeEnum;
import com.bbyiya.model.OPayorder;
import com.bbyiya.model.TiActivityworks;
import com.bbyiya.model.TiMyworks;
import com.bbyiya.model.TiProducts;
import com.bbyiya.service.calendar.ITi_OrderMgtService;
import com.bbyiya.service.calendar.ItiAcitivityMgtService;
import com.bbyiya.service.pic.IBasePostMgtService;
import com.bbyiya.service.pic.IBaseUserAddressService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.vo.user.UUserAddressResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/act_order")
public class ActOrderController  extends SSOController {
	@Autowired
	private TiProductsMapper tiProductsMapper;
	@Autowired
	private OPayorderMapper payMapper;
	@Autowired
	private TiMyworksMapper workMapper;
	@Autowired
	private TiActivityworksMapper actworkMapper;

	@Resource(name = "tiOrderMgtServiceImpl")
	private ITi_OrderMgtService orderMgtService;
	@Resource(name = "basePostMgtServiceImpl")
	private IBasePostMgtService postMgtService;
	@Resource(name = "baseUserAddressServiceImpl")
	private IBaseUserAddressService addressService;
	@Resource(name = "ti_AcitivityMgtServiceImpl")
	private ItiAcitivityMgtService actService;
	
	/**
	 * 活动作品提前下单 拿回家
	 * @param workId 作品id
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/getHome")
	public String getHome(long workId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			rq=actService.timeToSubmitOrder(user.getUserId(), workId);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 * 多买几本
	 * @param addressId
	 * @param addressType
	 * @param workId
	 * @param count
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/orderMoreCounts")
	public String orderMoreCounts(int addressType,long workId,int count) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			TiMyworks myworks= workMapper.selectByPrimaryKey(workId);
			TiActivityworks actWork=actworkMapper.selectByPrimaryKey(workId);
			rq.setStatu(ReturnStatus.ParamError);
			if(myworks!=null&&actWork!=null){
				//用户自己的收货地址id
				long addressId=0l;
				TiProducts products= tiProductsMapper.selectByPrimaryKey(myworks.getProductid());
				if(products!=null){
					double postage=0d;
					long orderAddressId =0l;
					//已经选过地址
					if(actWork.getAddresstype()!=null&&actWork.getAddresstype().longValue()>=0){
						if(actWork.getOrderaddressid()!=null&&actWork.getOrderaddressid().longValue()>0){
							orderAddressId=actWork.getOrderaddressid();
						}
					}
					//邮寄到自己
					else if(addressType==Integer.parseInt(AddressTypeEnum.cusaddr.toString())){
//						if(addressId<=0){
//							rq.setStatusreson("收货地址有误！");
//							return JsonUtil.objectToJsonStr(rq);
//						}
						UUserAddressResult address=addressService.getUserAddressResult(user.getUserId(), null);
						if(address!=null&&address.getAddrid()!=null){
							addressId=address.getAddrid();
							orderAddressId = orderMgtService.addOrderAddressReturnId(addressId);
							//邮寄到家的运费
							postage=postMgtService.getPostAge_ti(addressId, myworks.getProductid());
						}
					}
//					else if(addressType==Integer.parseInt(AddressTypeEnum.promoteraddr.toString())){
//						actWork.setAddresstype(addressType);
//						actworkMapper.updateByPrimaryKeySelective(actWork);
//					}
//					else{
//						rq.setStatu(ReturnStatus.ParamError);
//						rq.setStatusreson("收货地址有误");
//						return JsonUtil.objectToJsonStr(rq);
//					}
					//支付单总价
					double orderTotalPrice=(products.getPrice().doubleValue()/2)*count+postage;
					//新增 追加购买的总金额
					OPayorder payorder = new OPayorder();
					payorder.setUserid(user.getUserId());
					payorder.setUserorderid(String.valueOf(workId));//作品id (之前的订单号)
					payorder.setPayid(GenUtils.getOrderNo(user.getUserId()));
					payorder.setCreatetime(new Date());
					payorder.setOrdertype(Integer.parseInt(PayOrderTypeEnum.ti_halfPriceBuy.toString()));
					payorder.setTotalprice(orderTotalPrice);
					if(orderAddressId>0){
						payorder.setExtobject(String.valueOf(orderAddressId));
					}
					payorder.setExtobject2(String.valueOf(count));
					payorder.setStatus(Integer.parseInt(OrderStatusEnum.noPay.toString()));
					payMapper.insert(payorder);
					Map<String, Object> resultMap = new HashMap<String, Object>();
					resultMap.put("payId", payorder.getPayid());
					resultMap.put("totalPrice", orderTotalPrice);
					
					rq.setStatu(ReturnStatus.Success);
					rq.setBasemodle(resultMap);
				}
			}
			
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	

	/**
	 * 获取多买几本价格
	 * @param addressId
	 * @param addressType
	 * @param workId
	 * @param count
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getProductPriceForMore")
	public String getOrderPriceForMore(long workId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			//我的作品
			TiMyworks myworks= workMapper.selectByPrimaryKey(workId);
			//我的活动作品
			TiActivityworks actWork=actworkMapper.selectByPrimaryKey(workId);
			rq.setStatu(ReturnStatus.ParamError);
			if(myworks!=null&&actWork!=null){
				TiProducts products= tiProductsMapper.selectByPrimaryKey(myworks.getProductid());
				if(products!=null){
					Map<String, Object> result=new HashMap<String, Object>();
					result.put("oldPrice", products.getPrice());
					result.put("price", products.getPrice()/2);
					rq.setStatu(ReturnStatus.Success);
					rq.setBasemodle(result);
				}
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
		
	}
	
}
