package com.bbyiya.service.impl.calendar;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.baseUtils.GenUtils;
import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.dao.EErrorsMapper;
import com.bbyiya.dao.OOrderaddressMapper;
import com.bbyiya.dao.OOrderproductphotosMapper;
import com.bbyiya.dao.OOrderproductsMapper;
import com.bbyiya.dao.OPayorderMapper;
import com.bbyiya.dao.OProducerordercountMapper;
import com.bbyiya.dao.OUserorderextMapper;
import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.TiActivitysMapper;
import com.bbyiya.dao.TiActivityworksMapper;
import com.bbyiya.dao.TiDiscountdetailsMapper;
import com.bbyiya.dao.TiDiscountmodelMapper;
import com.bbyiya.dao.TiGroupactivityMapper;
import com.bbyiya.dao.TiGroupactivityworksMapper;
import com.bbyiya.dao.TiMyartsdetailsMapper;
import com.bbyiya.dao.TiMyworkcustomersMapper;
import com.bbyiya.dao.TiMyworksMapper;
import com.bbyiya.dao.TiProductareasMapper;
import com.bbyiya.dao.TiProductsMapper;
import com.bbyiya.dao.TiProductsextMapper;
import com.bbyiya.dao.TiProductstylesMapper;
import com.bbyiya.dao.TiPromoteremployeesMapper;
import com.bbyiya.dao.TiPromotersMapper;
import com.bbyiya.dao.TiUserdiscountsMapper;
import com.bbyiya.dao.UAccountsMapper;
import com.bbyiya.dao.UBranchesMapper;
import com.bbyiya.dao.UBranchusersMapper;
import com.bbyiya.dao.UUseraddressMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.AccountLogType;
import com.bbyiya.enums.OrderStatusEnum;
import com.bbyiya.enums.OrderTypeEnum;
import com.bbyiya.enums.PayOrderTypeEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.calendar.ActivityWorksStatusEnum;
import com.bbyiya.enums.calendar.AddressTypeEnum;
import com.bbyiya.enums.calendar.GroupActWorkStatus;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.model.EErrors;
import com.bbyiya.model.OOrderaddress;
import com.bbyiya.model.OOrderproductphotos;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.model.OPayorder;
import com.bbyiya.model.OProducerordercount;
import com.bbyiya.model.OUserorderext;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PPostmodel;
import com.bbyiya.model.PProducts;
import com.bbyiya.model.PProductstyles;
import com.bbyiya.model.TiActivitys;
import com.bbyiya.model.TiActivityworks;
import com.bbyiya.model.TiDiscountdetails;
import com.bbyiya.model.TiDiscountmodel;
import com.bbyiya.model.TiGroupactivity;
import com.bbyiya.model.TiGroupactivityworks;
import com.bbyiya.model.TiMyartsdetails;
import com.bbyiya.model.TiMyworkcustomers;
import com.bbyiya.model.TiMyworks;
import com.bbyiya.model.TiProductareas;
import com.bbyiya.model.TiProducts;
import com.bbyiya.model.TiProductsext;
import com.bbyiya.model.TiProductstyles;
import com.bbyiya.model.TiPromoteremployees;
import com.bbyiya.model.TiPromoters;
import com.bbyiya.model.TiUserdiscounts;
import com.bbyiya.model.UAccounts;
import com.bbyiya.model.UBranches;
import com.bbyiya.model.UBranchusers;
import com.bbyiya.model.UUseraddress;
import com.bbyiya.model.UUsers;
import com.bbyiya.service.IBaseUserAccountService;
import com.bbyiya.service.IRegionService;
import com.bbyiya.service.calendar.ITi_OrderMgtService;
import com.bbyiya.service.pic.IBasePostMgtService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.address.OrderaddressParam;
import com.bbyiya.vo.calendar.TiActivityOrderSubmitParam;
import com.bbyiya.vo.calendar.TiGroupActivityOrderSubmitParam;
import com.bbyiya.vo.order.UserOrderSubmitParam;
@Service("tiOrderMgtServiceImpl")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Ti_OrderMgtServiceImpl implements ITi_OrderMgtService {
	//------------------------产品---------------------------------------------
	@Autowired
	private TiProductstylesMapper styleMapper;
	@Autowired
	private TiProductsMapper productMapper;
	@Autowired
	private TiProductsextMapper productextMapper;
	//-------------------------用户模块-----------------------------------------------
	@Autowired
	private UUsersMapper usersMapper;
	@Autowired
	private TiPromoteremployeesMapper ePromoteremployeesMapper;
	@Autowired
	private TiPromotersMapper promotersMapper;
	@Autowired
	private UAccountsMapper accountsMapper;// 账户信息
	@Autowired
	private UUseraddressMapper addressMapper;
	@Autowired
	private UBranchusersMapper branchusersMapper;
	@Autowired
	private UBranchesMapper branchesMapper;
	@Resource(name = "regionServiceImpl")
	private IRegionService regionService;

	@Resource(name = "basePostMgtServiceImpl")
	private IBasePostMgtService postMgtService;
	
	@Resource(name = "baseUserAccountService")
	private IBaseUserAccountService accountService;
	//---------------------订单模块------------------------------------------------------
	@Autowired
	private OPayorderMapper payOrderMapper;// 支付单
	@Autowired
	private OUserordersMapper userOrdersMapper;
	@Autowired
	private OOrderproductsMapper oproductMapper;
	@Autowired
	private OOrderproductphotosMapper ophotoMapper;
	@Autowired
	private OOrderaddressMapper orderaddressMapper;
	@Autowired
	private OProducerordercountMapper producerOrderMapper;
	
	@Autowired
	private TiProductareasMapper productareasMapper;
	@Autowired
	private OUserorderextMapper userorderExtMapper;

	//---------------------------作品、活动------------------------------------------------------
	@Autowired
	private TiMyworksMapper myworksMapper;
	@Autowired
	private TiActivitysMapper myactMapper;
	@Autowired
	private TiActivityworksMapper activityworksMapper;
	@Autowired
	private TiMyartsdetailsMapper detailsMapper;
	@Autowired
	private PMyproductsMapper mycartMapper;
	
	@Autowired
	private TiMyworkcustomersMapper workcusMapper;
	@Autowired
	private TiGroupactivityworksMapper groupactworkMapper;
	@Autowired
	private TiGroupactivityMapper groupactMapper;
	//------------------------优惠信息-------------------------------------------
	@Autowired
	private TiUserdiscountsMapper mydiscountMapper;
	@Autowired
	private TiDiscountmodelMapper disModelMapper;
	@Autowired
	private TiDiscountdetailsMapper discountDetailsMapper;
	//--------------------
	@Autowired
	private EErrorsMapper logMapper;
	
	
	public ReturnModel submitOrder(UserOrderSubmitParam param) {
		ReturnModel rq = new ReturnModel();
		try {
			rq = checkOrderParam(param);
			if (!rq.getStatu().equals(ReturnStatus.Success)) {// 未通过参数验证
				return rq;
			}
			int orderType = param.getOrderType() == null ? 0 : param.getOrderType();
			// 订单收货地址
			long orderAddressId = 0;
			if (orderType == Integer.parseInt(OrderTypeEnum.ti_branchOrder.toString())) {// 影楼订单
				orderAddressId = getOrderAddressIdByBUserId(param.getBranchUserId(), orderType);
			} else {// 普通购买
				//通过用户下单，地址寄到影楼
				if(param.getBranchUserId()!=null&&param.getOrderExt()!=null&&!ObjectUtil.isEmpty(param.getOrderExt().getPhone())){
					orderAddressId = getOrderAddressIdByBUserId(param.getBranchUserId(), Integer.parseInt(OrderTypeEnum.ti_branchOrder.toString()));
				}else {
					orderAddressId = addOrderAddressReturnId(param.getAddrId());
				}
			}
			if (orderAddressId > 0) {
				param.setOrderAddressId(orderAddressId);
			}
			return submitOrder_common(param);
		} catch (Exception e) {
			addlog(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	
	
	public ReturnModel submitOrder_ibs(TiActivityOrderSubmitParam param) {
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);
		if(param==null||ObjectUtil.isEmpty(param.getSubmitUserId())||ObjectUtil.isEmpty(param.getWorkId())){
			rq.setStatusreson("参数不能为空！");
			return rq;
		}
		if(param.getCount()<=0){
			rq.setStatusreson("数量不能小于1！");
			return rq;
		}
		try {
			//用户作品
			TiMyworks work= myworksMapper.selectByPrimaryKey(param.getWorkId());
			if(work==null){
				rq.setStatusreson("作品不存在！");
				return rq;
			}
			//用户作品对应的产品款式
			TiProductstyles style=styleMapper.selectByPrimaryKey(work.getStyleid()==null?work.getProductid():work.getStyleid());
			if(style==null){
				rq.setStatusreson("作品信息不全！");
				return rq;
			}
			
			List<TiMyartsdetails> detailsList=detailsMapper.findDetailsByWorkId(param.getWorkId());
			if(detailsList==null||detailsList.size()<style.getImgcount().intValue()){
				rq.setStatusreson("作品图片数量不够！");
				return rq;
			}
			//是否达到下单的状态
			Boolean isCompleteBoolean=false;
			TiActivityworks actWork= activityworksMapper.selectByPrimaryKey(param.getWorkId());
			if(actWork!=null&&actWork.getStatus()!=null){
				if(actWork.getStatus()==Integer.parseInt(ActivityWorksStatusEnum.imagesubmit.toString())||
						actWork.getStatus()==Integer.parseInt(ActivityWorksStatusEnum.completeshare.toString())||
						actWork.getStatus()==Integer.parseInt(ActivityWorksStatusEnum.completeorder.toString())){
					isCompleteBoolean=true;
				}
			}
			if(isCompleteBoolean){
				UUsers promoter= usersMapper.selectByPrimaryKey( param.getSubmitUserId());
				if(promoter!=null&&promoter.getIdentity()!=null&&ValidateUtils.isIdentity(promoter.getIdentity(), UserIdentityEnums.ti_promoter)){
					UAccounts accounts=accountsMapper.selectByPrimaryKey(param.getSubmitUserId());
					double totalprice=style.getPromoterprice().doubleValue()*param.getCount(); 
					if(accounts==null||accounts.getAvailableamount()==null||accounts.getAvailableamount().doubleValue()<totalprice){
						rq.setStatusreson("您的账户余额不足！");
						rq.setStatu(ReturnStatus.SystemError);
						return rq;
					}
					//下单操作------------------
					
					//订单收货地址
					long orderAddressId=0l;
					
					//订单号
					String payId = GenUtils.getOrderNo(param.getSubmitUserId());
					String userOrderId=payId;
					String orderProductId=userOrderId;
					
					
					//得到订单编号
					//用户自己付邮费
					if(actWork.getOrderaddressid()!=null&&actWork.getOrderaddressid().longValue()>0){
						orderAddressId=actWork.getOrderaddressid();
					}else {//寄到B端地址
						orderAddressId= getOrderAddressIdByBUserId(param.getSubmitUserId(), Integer.parseInt(OrderTypeEnum.ti_branchOrder.toString()));
					}
					
					Long producerUserId=getProducerUserId(orderAddressId,work.getProductid(),param.getSubmitUserId());
					Integer orderindex=producerOrderMapper.getMaxOrderIndexByProducerIdAndUserId(producerUserId, param.getSubmitUserId());
					if(orderindex==null){
						orderindex=1;
					}else{
						orderindex=orderindex.intValue()+1;
					}

					String printindex=orderindex.toString();
					//邮寄到客户地址
					if(actWork.getOrderaddressid()!=null&&actWork.getOrderaddressid().longValue()>0){
						printindex=printindex+"A";
					}
					OProducerordercount producerorder=new OProducerordercount();
					producerorder.setProduceruserid(producerUserId);
					producerorder.setUserid(param.getSubmitUserId());
					producerorder.setUserorderid(userOrderId);
					producerorder.setOrderindex(orderindex);
					producerorder.setPrintindex(printindex);
					producerOrderMapper.insert(producerorder);
					
					
					if(detailsList!=null&&detailsList.size()>0){
						for (TiMyartsdetails dd : detailsList) {
							OOrderproductphotos op=new OOrderproductphotos();
							op.setOrderproductid(orderProductId);
							op.setImgurl(dd.getImageurl());
							op.setSort(dd.getSort());
							op.setCreatetime(new Date());
							ophotoMapper.insert(op);
						}
					}
					
					OUserorders userOrder = new OUserorders();
					Date ordertime = new Date();// 订单操作时间
					userOrder.setUserorderid(userOrderId);// 用户订单号
					userOrder.setPayid(payId); // 会写支付单号
					userOrder.setUserid(param.getSubmitUserId());
					userOrder.setBranchuserid(param.getSubmitUserId());// 分销商userId
					userOrder.setRemark(param.getRemark());
					userOrder.setOrdertype(Integer.parseInt(OrderTypeEnum.ti_branchOrder.toString()));// 订单类型
					userOrder.setOrdertime(ordertime);
					userOrder.setPaytime(ordertime); 
					userOrder.setStatus(Integer.parseInt(OrderStatusEnum.waitFoSend.toString()));
					userOrder.setTotalprice(totalprice);
					userOrder.setOrdertotalprice(totalprice); 
					userOrder.setOrderaddressid(orderAddressId); 
					userOrder.setProduceruserid(producerUserId); 
					userOrdersMapper.insert(userOrder);
					//订单产品
					TiProducts product=productMapper.selectByPrimaryKey(style.getProductid());
					OOrderproducts orderProduct = new OOrderproducts();
					orderProduct.setOrderproductid(orderProductId); 
					orderProduct.setUserorderid(userOrderId);
					orderProduct.setBuyeruserid(param.getSubmitUserId()); 
					orderProduct.setProductid(work.getProductid());
					orderProduct.setStyleid(style.getStyleid());
					orderProduct.setPropertystr(style.getDescription());
					orderProduct.setPrice(style.getPromoterprice());
					orderProduct.setCount(param.getCount());
					orderProduct.setProductimg(style.getDefaultimg());
					orderProduct.setProducttitle(product.getTitle());
					orderProduct.setCartid(param.getWorkId());
					oproductMapper.insert(orderProduct);
					//支付单信息
					OPayorder payorder = new OPayorder();
					payorder.setPayid(payId);
					payorder.setUserorderid(userOrderId);
					payorder.setUserid(param.getSubmitUserId());
					payorder.setStatus(Integer.parseInt(OrderStatusEnum.payed.toString()));
					payorder.setTotalprice(totalprice);
					payorder.setWalletamount(totalprice);
					payorder.setCashamount(0d);
					payorder.setCreatetime(new Date());
					payorder.setOrdertype(Integer.parseInt(PayOrderTypeEnum.ti_gouwu.toString()));
					payOrderMapper.insert(payorder);  
					//账户结算
					accountService.add_accountsLog(param.getSubmitUserId(), Integer.parseInt(AccountLogType.use_payment.toString()), totalprice, payId, "");
					
					//反写活动状态
					if(actWork!=null&&actWork.getStatus()!=null){
						TiActivitys act=myactMapper.selectByPrimaryKey(actWork.getActid());
						if(act!=null){
							//反写已下单人数
							if(actWork.getStatus().intValue()!=Integer.parseInt(ActivityWorksStatusEnum.completeorder.toString())){
								act.setCompletecount(act.getCompletecount()==null?1:act.getCompletecount().intValue()+1);
								myactMapper.updateByPrimaryKey(act);
							}
						}
						actWork.setStatus(Integer.parseInt(ActivityWorksStatusEnum.completeorder.toString()));
						activityworksMapper.updateByPrimaryKey(actWork);
					}
					
					//销量
					TiProductsext productsext = productextMapper.selectByPrimaryKey(work.getProductid());
					if (productsext == null) {
						productsext = new TiProductsext();
						productsext.setProductid(work.getProductid());
						productsext.setSales(param.getCount());
						productsext.setMonthssales(param.getCount());
						productextMapper.insert(productsext);
					} else {
						productsext.setSales((productsext.getSales() == null ? 0 : productsext.getSales().intValue()) + param.getCount());
						productsext.setMonthssales((productsext.getMonthssales() == null ? 0 : productsext.getMonthssales().intValue()) + param.getCount());
						productextMapper.updateByPrimaryKeySelective(productsext);
					}
					
					Map<String, Object> mapResult = new HashMap<String, Object>();
					String orderId = payId;
					mapResult.put("payId", payId);
					mapResult.put("orderId", orderId);
					mapResult.put("productId", product.getProductid());
					mapResult.put("styleId", style.getStyleid());
					mapResult.put("totalPrice", totalprice);
					rq.setBasemodle(mapResult);
					rq.setStatu(ReturnStatus.Success);
					rq.setStatusreson("下单成功"); 
				}
			}else {
				rq.setStatusreson("用户作品未完成（不在可下单的状态）！"); 
			}
		} catch (Exception e) {
			addlog("活动下单：workId="+param.getWorkId()+"error:"+e.getMessage()); 
			throw new RuntimeException(e);
		}
		return rq;
	}
	
	
	/**
	 * 获取生产商userId
	 */
	public long getProducerUserId(Long orderAddressId,Long productId,Long userId){
		OOrderaddress addr = orderaddressMapper.selectByPrimaryKey(orderAddressId);
		if(addr!=null&&addr.getDistrictcode()!=null){
			List<TiProductareas> list = productareasMapper.findProductAreaListByProductIdAndArea(productId, addr.getDistrictcode());
			if (list != null && list.size() > 0) {
				return list.get(0).getProduceruserid();
			}
		}
		if (userId!=null&&userId>0) {
			if(addr!=null){
				userId=addr.getUserid();
			}
			UUseraddress useraddress = addressMapper.get_UUserAddressDefault(userId);
			if (useraddress != null) {
				List<TiProductareas> list = productareasMapper.findProductAreaListByProductIdAndArea(productId, useraddress.getArea());
				if (list != null && list.size() > 0) {
					return list.get(0).getProduceruserid();
				}
			} else {
				TiPromoters promoters = promotersMapper.selectByPrimaryKey(userId);
				if (promoters != null) {
					List<TiProductareas> list = productareasMapper.findProductAreaListByProductIdAndArea(productId, promoters.getArea());
					if (list != null && list.size() > 0) {
						return list.get(0).getProduceruserid();
					}
				}
			}
		}
		long defaultproducer = ObjectUtil.parseLong(ConfigUtil.getSingleValue("defaultproducer"));
		if (defaultproducer <= 0)
			defaultproducer = 65l;
		return defaultproducer;
	}

	private ReturnModel submitOrder_common(UserOrderSubmitParam param) throws Exception {
		ReturnModel rq = new ReturnModel();
		Map<String, Object> mapResult = new HashMap<String, Object>();
		String payId = GenUtils.getOrderNo(param.getUserId());
		String orderId = payId;
		mapResult.put("payId", payId);
		mapResult.put("orderId", orderId);
		OUserorders userOrder = new OUserorders();
		Date ordertime = new Date();// 订单操作时间
		userOrder.setUserorderid(orderId);// 用户订单号
		userOrder.setPayid(payId); // 会写支付单号
		userOrder.setUserid(param.getUserId());
		userOrder.setBranchuserid(param.getBranchUserId());// 分销商userId
		userOrder.setAgentuserid(param.getAgentUserId());
		userOrder.setRemark(param.getRemark());
		userOrder.setOrdertype(param.getOrderType());// 订单类型
		userOrder.setOrdertime(ordertime);
		userOrder.setPaytime(ordertime);
		userOrder.setStatus(Integer.parseInt(OrderStatusEnum.noPay.toString()));
		userOrder.setIsbranch(0);
		userOrder.setPostmodelid(param.getPostModelId());
	
		if (param.getOrderAddressId() != null && param.getOrderAddressId() > 0) {
			userOrder.setOrderaddressid(param.getOrderAddressId());
		} else {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("收货地址有误");
			return rq;
		}
		userOrder.setProduceruserid(getProducerUserId(userOrder.getOrderaddressid(),param.getOrderproducts().getProductid(),param.getUserId())); 
		if (param.getOrderproducts() != null) {
			// 实际需要付款的总价（包括邮费）
			Double orderTotalPrice = 0d;
			// 订单总价
			Double totalPrice = 0d;
			// 订单产品
			OOrderproducts orderProduct = param.getOrderproducts();
			orderProduct.setOrderproductid(orderId); 
			orderProduct.setUserorderid(orderId);
			orderProduct.setBuyeruserid(param.getUserId()); 
			// 获取产品信息
			int orderType = param.getOrderType() == null ? 0 : param.getOrderType();
			//------------------台历、挂历、年历 板块----------------------------------------
			if (orderType == Integer.parseInt(OrderTypeEnum.ti_branchOrder.toString()) || orderType == Integer.parseInt(OrderTypeEnum.ti_nomal.toString())) {
				TiProductstyles style = styleMapper.selectByPrimaryKey(orderProduct.getStyleid());
				if (style != null) {
					orderProduct.setPropertystr(style.getDescription()); 
					totalPrice = style.getPrice() * orderProduct.getCount();
					if (orderType == Integer.parseInt(OrderTypeEnum.ti_branchOrder.toString())) {
						orderProduct.setPrice(style.getPromoterprice());
					} else if (orderType == Integer.parseInt(OrderTypeEnum.ti_nomal.toString())) {
						//普通用户下单
						orderProduct.setPrice(style.getPrice());
						// 优惠、折扣处理----
						TiDiscountmodel discountmodel = getDiscountList(param.getUserId());
						if (discountmodel != null && discountmodel.getDetails() != null) {
							for (TiDiscountdetails discountdetails : discountmodel.getDetails()) {
								if (discountdetails.getProductid().longValue() == style.getProductid().longValue()) {
									if (discountmodel.getType() == 1) {
										orderProduct.setPrice(style.getPrice() * discountdetails.getDiscount());
										TiUserdiscounts mydis= getMydiscount(param.getUserId());
										if(mydis!=null){
											mydis.setStatus(1);
											mydis.setUserorderid(orderId);
											mydiscountMapper.updateByPrimaryKeySelective(mydis);
										}
									}
								}
							}
						}
					}
				}
				orderTotalPrice = orderProduct.getPrice() * orderProduct.getCount();
				if (param.getPostPrice() != null) {
					userOrder.setPostage(param.getPostPrice());
					orderTotalPrice += param.getPostPrice();
				}
			}else if (orderType == Integer.parseInt(OrderTypeEnum.nomal.toString()) || orderType == Integer.parseInt(OrderTypeEnum.brachOrder.toString())) {
				// 12photos 购物订单
				rq.setStatusreson("12photos订单暂不提供下单！");
				return rq;
			}
			userOrder.setOrdertotalprice(orderTotalPrice);
			userOrder.setTotalprice(totalPrice);
			if (orderType == Integer.parseInt(OrderTypeEnum.ti_branchOrder.toString())) {
				addPayOrder(param.getUserId(), payId, payId, orderTotalPrice, orderTotalPrice);
			} else {
				addPayOrder(param.getUserId(), payId, payId, orderTotalPrice, 0d);
			}
			//作品id 通过 原作品表来获取
			PMyproducts mycart=new PMyproducts();
			mycart.setUserid(0l);
			mycart.setCreatetime(new Date());
			mycartMapper.insertReturnId(mycart);
			//获取
			TiMyworks myworks = new TiMyworks();
			myworks.setUserid(param.getUserId());
			myworks.setCreatetime(new Date());
			myworks.setWorkid(mycart.getCartid());
			myworks.setProductid(orderProduct.getProductid());
			myworks.setStyleid(orderProduct.getStyleid()); 
			myworksMapper.insert(myworks);

			// 插入订单
			orderProduct.setCartid(myworks.getWorkid());
			//邮寄到影楼地址（用户收货联系方式 备注信息）
			if(param.getOrderExt()!=null&&!ObjectUtil.isEmpty(param.getOrderExt().getPhone())){
				userOrder.setIspromoteraddress(1); 
				OUserorderext userorderext=param.getOrderExt();
				userorderext.setUserorderid(userOrder.getUserorderid());
				userorderExtMapper.insert(userorderext);
			}
			userOrdersMapper.insert(userOrder);
			
			// 插入订单产品
			oproductMapper.insert(orderProduct);
			// 插入--订单打印号
			OProducerordercount oproducerModel=new OProducerordercount();
			oproducerModel.setUserorderid(orderId);
			oproducerModel.setProduceruserid(userOrder.getProduceruserid());
			Integer indexCount=0;
			//寄到影楼
			if(userOrder.getBranchuserid()!=null&& userOrder.getIspromoteraddress()!=null&&userOrder.getIspromoteraddress().intValue()>0){
				oproducerModel.setUserid(userOrder.getBranchuserid()); 
				indexCount=producerOrderMapper.getMaxOrderIndexByProducerIdAndUserId(userOrder.getProduceruserid(),userOrder.getBranchuserid());
				int orderIndex=indexCount==null?1:(indexCount+1);
				oproducerModel.setOrderindex(orderIndex);
				oproducerModel.setPrintindex(String.valueOf(orderIndex));
			}else {//寄到用户自己
				oproducerModel.setUserid(userOrder.getUserid()); 
				indexCount=producerOrderMapper.getMaxOrderIndexByProducerIdAndUserId(userOrder.getProduceruserid(),userOrder.getUserid());
				int orderIndex=indexCount==null?1:(indexCount+1);
				oproducerModel.setPrintindex(orderIndex+"A");
			}
			producerOrderMapper.insert(oproducerModel);
			
			mapResult.put("totalPrice", orderTotalPrice);
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(mapResult);
		}
		return rq;
	}

	public void addPayOrder(Long userId, String payId, String userOrderId, Double totalPrice, Double walletAmount) {
		OPayorder payorder = new OPayorder();
		payorder.setPayid(payId);
		payorder.setUserorderid(userOrderId);
		payorder.setUserid(userId);
		payorder.setStatus(Integer.parseInt(OrderStatusEnum.noPay.toString()));
		payorder.setTotalprice(totalPrice);
		payorder.setWalletamount(walletAmount);
		payorder.setCashamount(totalPrice.doubleValue() - walletAmount.doubleValue());
		payorder.setCreatetime(new Date());
		payorder.setOrdertype(Integer.parseInt(PayOrderTypeEnum.ti_gouwu.toString()));
		payOrderMapper.insert(payorder);  
	}


	private TiDiscountmodel getDiscountList(Long userId) {
		List<TiUserdiscounts> discounts = mydiscountMapper.findMyDiscounts(userId);
		if (discounts != null && discounts.size() > 0) {
			TiDiscountmodel model = disModelMapper.selectByPrimaryKey(discounts.get(0).getDiscountid());
			if (model != null) {
				// 具体优惠信息
				List<TiDiscountdetails> dislist = discountDetailsMapper.findDiscountlist(discounts.get(0).getDiscountid());
				if (dislist != null && dislist.size() > 0) {
					model.setDetails(dislist);
					return model;
				}
			}
		}
		return null;
	}
	private TiUserdiscounts getMydiscount(Long userId){
		List<TiUserdiscounts> discounts = mydiscountMapper.findMyDiscounts(userId);
		if (discounts != null && discounts.size() > 0) {
			return discounts.get(0);
		}
		return null;
	}

	private long getOrderAddressIdByBUserId(Long userId, int orderType) {
		UUsers users = usersMapper.selectByPrimaryKey(userId);
		if (users != null) {
			if (orderType == Integer.parseInt(OrderTypeEnum.brachOrder.toString())) {
				UBranchusers branchusers = branchusersMapper.selectByPrimaryKey(userId);
				if (branchusers != null && branchusers.getBranchuserid() != null) {
					UBranches branches = branchesMapper.selectByPrimaryKey(branchusers.getBranchuserid());
					if (branches != null) {
						OOrderaddress orderAddress = new OOrderaddress();
						orderAddress.setUserid(branches.getBranchuserid());
						orderAddress.setPhone(branches.getPhone());
						orderAddress.setReciver(branches.getUsername());
						orderAddress.setProvincecode(branches.getProvince());
						orderAddress.setCitycode(branches.getCity());
						orderAddress.setDistrictcode(branches.getArea()); 
						orderAddress.setCity(regionService.getCityName(branches.getCity()));
						orderAddress.setProvince(regionService.getProvinceName(branches.getProvince()));
						orderAddress.setDistrict(regionService.getAresName(branches.getArea()));
						orderAddress.setStreetdetail(branches.getStreetdetail());
						orderAddress.setCreatetime(new Date());
						orderaddressMapper.insertReturnId(orderAddress);
						return orderAddress.getOrderaddressid();
					}
				}
			} else if (orderType == Integer.parseInt(OrderTypeEnum.ti_branchOrder.toString())) {
				TiPromoters promoters = promotersMapper.selectByPrimaryKey(userId);
				if (promoters != null) {
					OOrderaddress orderAddress = new OOrderaddress();
					orderAddress.setUserid(promoters.getPromoteruserid());
					orderAddress.setPhone(promoters.getMobilephone());
					orderAddress.setReciver(promoters.getContacts());
					orderAddress.setProvincecode(promoters.getProvince());
					orderAddress.setCitycode(promoters.getCity());
					orderAddress.setDistrictcode(promoters.getArea()); 
					orderAddress.setCity(regionService.getCityName(promoters.getCity()));
					orderAddress.setProvince(regionService.getProvinceName(promoters.getProvince()));
					orderAddress.setDistrict(regionService.getAresName(promoters.getArea()));
					orderAddress.setStreetdetail(promoters.getStreetdetails());
					orderAddress.setCreatetime(new Date());
					orderaddressMapper.insertReturnId(orderAddress);
					return orderAddress.getOrderaddressid();
				}
			}
		}
		return 0;
	}

	public long addOrderAddressReturnId(Long userAddrId) {
		if (userAddrId != null && userAddrId > 0) {
			UUseraddress addr = addressMapper.get_UUserAddressByKeyId(userAddrId);
			if (addr != null) {
				OOrderaddress orderAddress = new OOrderaddress();
				orderAddress.setUserid(addr.getUserid());
				orderAddress.setPhone(addr.getPhone());
				orderAddress.setProvincecode(addr.getProvince());
				orderAddress.setCitycode(addr.getCity());
				orderAddress.setDistrictcode(addr.getArea()); 
				orderAddress.setReciver(addr.getReciver());
				orderAddress.setCity(regionService.getCityName(addr.getCity()));
				orderAddress.setProvince(regionService.getProvinceName(addr.getProvince()));
				orderAddress.setDistrict(regionService.getAresName(addr.getArea()));
				orderAddress.setStreetdetail(addr.getStreetdetail());
				orderAddress.setCreatetime(new Date());
				orderaddressMapper.insertReturnId(orderAddress);
				return orderAddress.getOrderaddressid();
			}
		}
		return 0;
	}

	private ReturnModel checkOrderParam(UserOrderSubmitParam param) throws Exception {
		ReturnModel rq = new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		if (param == null || param.getUserId() == null || param.getOrderproducts() == null) {
			rq.setStatusreson("参数有误");
			return rq;
		}
		TiProductstyles style = null;
		TiProducts product = null;
		double totalprice = 0d;
		int count = param.getOrderproducts().getCount() == null ? 1 : param.getOrderproducts().getCount();
		if (param.getOrderproducts().getProductid() != null && param.getOrderproducts().getStyleid() != null) {
			style = styleMapper.selectByPrimaryKey(param.getOrderproducts().getStyleid());
			if (style != null) {
				product = productMapper.selectByPrimaryKey(style.getProductid());
				param.getOrderproducts().setPrice(style.getPrice());
			} else {
				rq.setStatusreson("产品不存在");
			}
		} else {
			rq.setStatusreson("产品不存在");
			return rq;
		}
		OOrderproducts oproduct = param.getOrderproducts();
		oproduct.setProducttitle(product.getTitle());
		oproduct.setProductimg(style.getDefaultimg());

		int orderType = param.getOrderType() == null ? 0 : param.getOrderType();
		if (orderType == Integer.parseInt(OrderTypeEnum.ti_branchOrder.toString())) {// 影楼订单
			UUsers users = usersMapper.selectByPrimaryKey(param.getUserId());
			if (users != null) {
				//推广者
				TiPromoters promoter=null;
				// case1: 下单人就是推广者
				if(ValidateUtils.isIdentity(users.getIdentity(), UserIdentityEnums.ti_promoter)){
					promoter = promotersMapper.selectByPrimaryKey(users.getUserid());
				}
				// case2:下单人是推广者 员工
				else {
					TiPromoteremployees employee = ePromoteremployeesMapper.selectByPrimaryKey(users.getUserid());
					if (employee != null && employee.getPromoteruserid() != null) {
						promoter = promotersMapper.selectByPrimaryKey(employee.getPromoteruserid());
					}
				}
//				TiPromoteremployees employee = ePromoteremployeesMapper.selectByPrimaryKey(users.getUserid());
//				if (employee != null && employee.getPromoteruserid() != null) {
//					promoter = promotersMapper.selectByPrimaryKey(employee.getPromoteruserid());
					if (promoter != null) {
						UAccounts accounts = accountsMapper.selectByPrimaryKey(promoter.getPromoteruserid());
						if (accounts != null && accounts.getAvailableamount() != null) {
							totalprice = style.getPromoterprice().doubleValue() * count;
							if (accounts.getAvailableamount().doubleValue() >= totalprice) {
								param.setBranchUserId(promoter.getPromoteruserid());
								param.setAgentUserId(promoter.getAgentuserid());
								param.setPostPrice(0d); 
								rq.setStatu(ReturnStatus.Success);
								rq.setBasemodle(param);
								oproduct.setPrice(style.getPromoterprice());
								rq.setStatu(ReturnStatus.Success); 
								return rq;
							}
						}
						rq.setStatusreson("账户可用余额不足！");
						return rq;
					}else {
						rq.setStatusreson("非法操作！");
						return rq;
					}
//				}
			}
		} else if (orderType == Integer.parseInt(OrderTypeEnum.ti_nomal.toString())) {
			if(param.getBranchUserId()!=null&&param.getOrderExt()!=null&&!ObjectUtil.isEmpty(param.getOrderExt().getPhone())){
				//普通用户下单，寄到影楼
				param.setPostModelId(0);
				param.setPostPrice(0d); 
			}else {
				UUseraddress addr = addressMapper.get_UUserAddressByKeyId(param.getAddrId());// 用户收货地址
				if (addr != null) {
					if (param.getPostModelId() == null || param.getPostModelId() <= 0) {
						rq.setStatusreson("快递方式未填写！");
						return rq;
					} else {
						PPostmodel postmodel = postMgtService.getPostmodel(param.getPostModelId(), addr.getArea());
						if (postmodel != null) {
							param.setPostPrice(postmodel.getAmount());
						} else {
							rq.setStatusreson("快递方式不存在！");
							return rq;
						}
					}
				}
			}
		}
		rq.setStatu(ReturnStatus.Success); 
		return rq;
	}
	

	public void addlog(String msg) {
		EErrors errors = new EErrors();
		errors.setClassname(this.getClass().getName());
		errors.setMsg("下单失败：" + msg);
		errors.setCreatetime(new Date());
		logMapper.insert(errors);
	}
	
	/**
	 * 代客制作下单
	 */
	public ReturnModel submitTiCustomerOrder_ibs(TiActivityOrderSubmitParam param,OrderaddressParam addressParam) {
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);
		if(param==null||ObjectUtil.isEmpty(param.getSubmitUserId())||ObjectUtil.isEmpty(param.getWorkId())){
			rq.setStatusreson("参数不能为空！");
			return rq;
		}
		if(param.getCount()<=0){
			rq.setStatusreson("数量不能小于1！");
			return rq;
		}
		try {
			//用户作品
			TiMyworks work= myworksMapper.selectByPrimaryKey(param.getWorkId());
			if(work==null){
				rq.setStatusreson("作品不存在！");
				return rq;
			}
			//用户作品对应的产品款式
			TiProductstyles style=styleMapper.selectByPrimaryKey(work.getStyleid()==null?work.getProductid():work.getStyleid());
			if(style==null){
				rq.setStatusreson("作品信息不全！");
				return rq;
			}
			
			List<TiMyartsdetails> detailsList=detailsMapper.findDetailsByWorkId(param.getWorkId());
			if(detailsList==null||detailsList.size()<style.getImgcount().intValue()){
				rq.setStatusreson("作品图片数量不够！");
				return rq;
			}
			//是否达到下单的状态
			Boolean isCompleteBoolean=false;
			TiMyworkcustomers workcus=workcusMapper.selectByPrimaryKey(param.getWorkId());
			if(workcus!=null&&workcus.getStatus()!=null){
				if(workcus.getStatus()==Integer.parseInt(ActivityWorksStatusEnum.imagesubmit.toString())||
						workcus.getStatus()==Integer.parseInt(ActivityWorksStatusEnum.completeshare.toString())||
								workcus.getStatus()==Integer.parseInt(ActivityWorksStatusEnum.completeorder.toString())){
					isCompleteBoolean=true;
				}
			}
			if(isCompleteBoolean){
				double totalprice=style.getPromoterprice().doubleValue()*param.getCount(); 
				double needpayprice=totalprice;
				
				//判断如果是红包筹集
				if(workcus.getNeedredpackettotal()!=null&&workcus.getNeedredpackettotal().doubleValue()>0){
					needpayprice=totalprice-(workcus.getRedpacketamount()==null?0:workcus.getRedpacketamount());
				}
				UUsers promoter= usersMapper.selectByPrimaryKey( param.getSubmitUserId());
				if(promoter!=null&&promoter.getIdentity()!=null&&ValidateUtils.isIdentity(promoter.getIdentity(), UserIdentityEnums.ti_promoter)){
					if(needpayprice>0){
						UAccounts accounts=accountsMapper.selectByPrimaryKey(param.getSubmitUserId());
						if(accounts==null||accounts.getAvailableamount()==null||accounts.getAvailableamount().doubleValue()<needpayprice){
							rq.setStatusreson("您的账户余额不足！");
							rq.setStatu(ReturnStatus.SystemError);
							return rq;
						}
					}
					
					//下单操作------------------
					
					//订单收货地址
					long orderAddressId=0l;
					//得到订单编号
					//手动选择地址下单
					if(addressParam!=null&&!ObjectUtil.isEmpty(addressParam.getProvince())){
						OOrderaddress orderAddress = new OOrderaddress();
						orderAddress.setUserid(addressParam.getUserid());
						orderAddress.setPhone(addressParam.getPhone());
						orderAddress.setReciver(addressParam.getReciver());
						orderAddress.setCity(regionService.getCityName(addressParam.getCity()));
						orderAddress.setProvince(regionService.getProvinceName(addressParam.getProvince()));
						orderAddress.setDistrict(regionService.getAresName(addressParam.getDistrict()));
						orderAddress.setStreetdetail(addressParam.getStreetdetail());
						orderAddress.setCreatetime(new Date());
						orderaddressMapper.insertReturnId(orderAddress);
						orderAddressId=orderAddress.getOrderaddressid();
						//客户地址
						if(addressParam.getAddresstype()!=null&&addressParam.getAddresstype().intValue()==Integer.parseInt(AddressTypeEnum.cusaddr.toString())){
							//这种情况是自定义地址
							if(workcus.getAddresstype()==null||workcus.getAddresstype().intValue()==0){
								workcus.setAddresstype(Integer.parseInt(AddressTypeEnum.cusaddr.toString()));
								workcus.setStreetdetails(addressParam.getStreetdetail());
								workcus.setProvince(addressParam.getProvince());
								workcus.setCity(addressParam.getCity());
								workcus.setDistrict(addressParam.getDistrict());
								workcus.setReciever(addressParam.getReciver());
								workcus.setMobilephone(addressParam.getPhone());
								workcusMapper.updateByPrimaryKey(workcus);
							}
						}
					}
					//邮寄到客户地址
					else if(workcus.getAddresstype()!=null&&workcus.getAddresstype().intValue()==Integer.parseInt(AddressTypeEnum.cusaddr.toString())){
						OOrderaddress orderAddress = new OOrderaddress();
						orderAddress.setUserid(workcus.getPromoteruserid());
						orderAddress.setPhone(workcus.getRecieverphone());
						orderAddress.setReciver(workcus.getReciever());
						orderAddress.setCity(regionService.getCityName(workcus.getCity()));
						orderAddress.setProvince(regionService.getProvinceName(workcus.getProvince()));
						orderAddress.setDistrict(regionService.getAresName(workcus.getDistrict()));
						orderAddress.setStreetdetail(workcus.getStreetdetails());
						orderAddress.setCreatetime(new Date());
						orderaddressMapper.insertReturnId(orderAddress);
						orderAddressId=orderAddress.getOrderaddressid();
					}else {//寄到B端地址
						orderAddressId= getOrderAddressIdByBUserId(param.getSubmitUserId(), Integer.parseInt(OrderTypeEnum.ti_branchOrder.toString()));
					}
					
					Long producerUserId=getProducerUserId(orderAddressId,work.getProductid(),param.getSubmitUserId());
					Integer orderindex=producerOrderMapper.getMaxOrderIndexByProducerIdAndUserId(producerUserId, param.getSubmitUserId());
					if(orderindex==null){
						orderindex=1;
					}else{
						orderindex=orderindex.intValue()+1;
					}
					String printindex=orderindex.toString();
					if(addressParam!=null&&!ObjectUtil.isEmpty(addressParam.getProvince())){
						if(addressParam.getAddresstype()!=null&&addressParam.getAddresstype().intValue()==Integer.parseInt(AddressTypeEnum.cusaddr.toString())){
							printindex=printindex+"A";
						}
					}else if(workcus.getAddresstype()!=null&&workcus.getAddresstype().intValue()==Integer.parseInt(AddressTypeEnum.cusaddr.toString())){
						printindex=printindex+"A";
					}
						
					
					//订单号
					String payId = GenUtils.getOrderNo(param.getSubmitUserId());
					String userOrderId=payId;
					String orderProductId=userOrderId;
					
					OProducerordercount producerorder=new OProducerordercount();
					producerorder.setProduceruserid(producerUserId);
					producerorder.setUserid(param.getSubmitUserId());
					producerorder.setUserorderid(userOrderId);
					
					producerorder.setOrderindex(orderindex);
					producerorder.setPrintindex(printindex);
					producerOrderMapper.insert(producerorder);
					
					if(detailsList!=null&&detailsList.size()>0){
						for (TiMyartsdetails dd : detailsList) {
							OOrderproductphotos op=new OOrderproductphotos();
							op.setOrderproductid(orderProductId);
							op.setImgurl(dd.getImageurl());
							op.setSort(dd.getSort());
							op.setCreatetime(new Date());
							ophotoMapper.insert(op);
						}
					}
					
					OUserorders userOrder = new OUserorders();
					Date ordertime = new Date();// 订单操作时间
					userOrder.setUserorderid(userOrderId);// 用户订单号
					userOrder.setPayid(payId); // 会写支付单号
					userOrder.setUserid(param.getSubmitUserId());
					userOrder.setBranchuserid(param.getSubmitUserId());// 分销商userId
					userOrder.setRemark(param.getRemark());
					userOrder.setOrdertype(Integer.parseInt(OrderTypeEnum.ti_branchOrder.toString()));// 订单类型
					userOrder.setOrdertime(ordertime);
					userOrder.setPaytime(ordertime); 
					userOrder.setStatus(Integer.parseInt(OrderStatusEnum.waitFoSend.toString()));
					userOrder.setTotalprice(totalprice);
					userOrder.setOrdertotalprice(totalprice); 
					userOrder.setOrderaddressid(orderAddressId); 
					userOrder.setProduceruserid(producerUserId); 
					userOrdersMapper.insert(userOrder);
					//订单产品
					TiProducts product=productMapper.selectByPrimaryKey(style.getProductid());
					OOrderproducts orderProduct = new OOrderproducts();
					orderProduct.setOrderproductid(orderProductId); 
					orderProduct.setUserorderid(userOrderId);
					orderProduct.setBuyeruserid(param.getSubmitUserId()); 
					orderProduct.setProductid(work.getProductid());
					orderProduct.setStyleid(style.getStyleid());
					orderProduct.setPropertystr(style.getDescription());
					orderProduct.setPrice(style.getPromoterprice());
					orderProduct.setCount(param.getCount());
					orderProduct.setProductimg(style.getDefaultimg());
					orderProduct.setProducttitle(product.getTitle());
					orderProduct.setCartid(param.getWorkId());
					oproductMapper.insert(orderProduct);
					
					
					//支付单信息
					OPayorder payorder = new OPayorder();
					payorder.setPayid(payId);
					payorder.setUserorderid(userOrderId);
					payorder.setUserid(param.getSubmitUserId());
					payorder.setStatus(Integer.parseInt(OrderStatusEnum.payed.toString()));
					payorder.setTotalprice(totalprice);
					payorder.setWalletamount(totalprice);
					payorder.setCashamount(0d);
					payorder.setCreatetime(new Date());
					payorder.setOrdertype(Integer.parseInt(PayOrderTypeEnum.ti_gouwu.toString()));
					payOrderMapper.insert(payorder);  		
					//红包金额入账
					if(workcus.getRedpacketamount()!=null&&workcus.getRedpacketamount().doubleValue()>0){
						accountService.add_accountsLog(param.getSubmitUserId(), Integer.parseInt(AccountLogType.get_ti_redpaket.toString()), workcus.getRedpacketamount(), payId, "");
					}
					
					//账户结算
					accountService.add_accountsLog(param.getSubmitUserId(), Integer.parseInt(AccountLogType.use_payment.toString()), totalprice, payId, "");
					
					//反写活动状态
					if(workcus!=null&&workcus.getStatus()!=null){
						workcus.setStatus(Integer.parseInt(ActivityWorksStatusEnum.completeorder.toString()));
						workcusMapper.updateByPrimaryKey(workcus);
					}
					
					//销量
					TiProductsext productsext = productextMapper.selectByPrimaryKey(work.getProductid());
					if (productsext == null) {
						productsext = new TiProductsext();
						productsext.setProductid(work.getProductid());
						productsext.setSales(param.getCount());
						productsext.setMonthssales(param.getCount());
						productextMapper.insert(productsext);
					} else {
						productsext.setSales((productsext.getSales() == null ? 0 : productsext.getSales().intValue()) + param.getCount());
						productsext.setMonthssales((productsext.getMonthssales() == null ? 0 : productsext.getMonthssales().intValue()) + param.getCount());
						productextMapper.updateByPrimaryKeySelective(productsext);
					}
					
					Map<String, Object> mapResult = new HashMap<String, Object>();
					String orderId = payId;
					mapResult.put("payId", payId);
					mapResult.put("orderId", orderId);
					mapResult.put("productId", product.getProductid());
					mapResult.put("styleId", style.getStyleid());
					mapResult.put("totalPrice", totalprice);
					rq.setBasemodle(mapResult);
					rq.setStatu(ReturnStatus.Success);
					rq.setStatusreson("下单成功"); 
				}
			}else {
				rq.setStatusreson("用户作品未完成（不在可下单的状态）！"); 
			}
		} catch (Exception e) {
			addlog("代客制作下单：workId="+param.getWorkId()+"error:"+e.getMessage()); 
			throw new RuntimeException(e);
		}
		return rq;
	}
	
	
	
	
	/**
	 * 分销下单
	 */
	public ReturnModel submitTiGroupActivityOrder_ibs(TiGroupActivityOrderSubmitParam param) {
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);
		if(param==null||ObjectUtil.isEmpty(param.getSubmitUserId())||ObjectUtil.isEmpty(param.getWorkId())){
			rq.setStatusreson("参数不能为空！");
			return rq;
		}
		if(param.getCount()<=0){
			rq.setStatusreson("数量不能小于1！");
			return rq;
		}
		try {
			TiGroupactivityworks work= groupactworkMapper.selectByPrimaryKey(param.getWorkId());
			if(work==null){
				rq.setStatusreson("作品不存在！");
				return rq;
			}
			TiGroupactivity act=groupactMapper.selectByPrimaryKey(work.getGactid());
			
			//用户作品对应的产品款式
			TiProductstyles style=styleMapper.selectByPrimaryKey(work.getSttyleid()==null?work.getProductid():work.getSttyleid());
			if(style==null){
				rq.setStatusreson("作品信息不全！");
				return rq;
			}
			
			List<TiMyartsdetails> detailsList=detailsMapper.findDetailsByWorkId(param.getWorkId());
			if(detailsList==null||detailsList.size()<style.getImgcount().intValue()){
				rq.setStatusreson("作品图片数量不够！");
				return rq;
			}
			//是否达到下单的状态
			Boolean isCompleteBoolean=false;
			if(work.getStatus().intValue()==Integer.parseInt(GroupActWorkStatus.payed.toString())){
				isCompleteBoolean=true;
				//检查是否完成集赞，如果完成，下单数量+1
				
				if(act!=null&&act.getPraisecount()!=null&&work.getPraisecount()!=null&& act.getPraisecount().intValue()<=work.getPraisecount().intValue()){
					param.setCount(param.getCount()+1);
				}
			}
			if(isCompleteBoolean){
				double totalprice=style.getPromoterprice().doubleValue()*param.getCount(); 
//				double incomeprice=0.0; //收入金额 
//				if(work.getTotalprice()!=null&&work.getTotalprice().doubleValue()>0){
//					incomeprice=work.getTotalprice().doubleValue()-totalprice;
//				}
				UUsers promoter= usersMapper.selectByPrimaryKey( param.getSubmitUserId());
				if(promoter!=null&&promoter.getIdentity()!=null&&ValidateUtils.isIdentity(promoter.getIdentity(), UserIdentityEnums.ti_promoter)){
//					if(incomeprice<0){//收入小于0的时候,检查余额
						UAccounts accounts=accountsMapper.selectByPrimaryKey(param.getSubmitUserId());
						if(accounts==null||accounts.getAvailableamount()==null||accounts.getAvailableamount().doubleValue()<Math.abs(totalprice)){
							rq.setStatusreson("您的账户余额不足！");
							rq.setStatu(ReturnStatus.SystemError);
							return rq;
						}
//					}
				
					//下单操作------------------
					if(work.getAddresstype().intValue()==Integer.parseInt(AddressTypeEnum.promoteraddr.toString())){
						OOrderaddress orderAddress = new OOrderaddress();
						orderAddress.setUserid(act.getPromoteruserid());
						orderAddress.setPhone(act.getMobilephone());
						orderAddress.setReciver(act.getReciver());
						orderAddress.setCity(regionService.getCityName(act.getCity()));
						orderAddress.setProvince(regionService.getProvinceName(act.getProvince()));
						orderAddress.setDistrict(regionService.getAresName(act.getArea()));
						orderAddress.setStreetdetail(act.getStreetdetails());
						orderAddress.setCreatetime(new Date());
						orderaddressMapper.insertReturnId(orderAddress);						
						Long orderAddressId=orderAddress.getOrderaddressid();
						param.setOrderAddressId(orderAddressId);
						work.setAddressid(orderAddressId);
					}
					//订单收货地址
					Long producerUserId=getProducerUserId(work.getAddressid(),work.getProductid(),param.getSubmitUserId());
					Integer orderindex=producerOrderMapper.getMaxOrderIndexByProducerIdAndUserId(producerUserId, param.getSubmitUserId());
					if(orderindex==null){
						orderindex=1;
					}else{
						orderindex=orderindex.intValue()+1;
					}
					String printindex=orderindex.toString();
					//订单号
					String payId = GenUtils.getOrderNo(param.getSubmitUserId());
					String userOrderId=payId;
					String orderProductId=userOrderId;
					work.setUserorderid(payId); 
					OProducerordercount producerorder=new OProducerordercount();
					producerorder.setProduceruserid(producerUserId);
					producerorder.setUserid(param.getSubmitUserId());
					producerorder.setUserorderid(userOrderId);
					
					//邮寄到客户地址
					if(work.getAddresstype()!=null&&work.getAddresstype().intValue()==Integer.parseInt(AddressTypeEnum.cusaddr.toString())){
						printindex=printindex+"A";
					}
					producerorder.setOrderindex(orderindex);
					producerorder.setPrintindex(printindex);
					producerOrderMapper.insert(producerorder);
					
					if(detailsList!=null&&detailsList.size()>0){
						for (TiMyartsdetails dd : detailsList) {
							OOrderproductphotos op=new OOrderproductphotos();
							op.setOrderproductid(orderProductId);
							op.setImgurl(dd.getImageurl());
							op.setSort(dd.getSort());
							op.setCreatetime(new Date());
							ophotoMapper.insert(op);
						}
					}
					
					OUserorders userOrder = new OUserorders();
					Date ordertime = new Date();// 订单操作时间
					userOrder.setUserorderid(userOrderId);// 用户订单号
					userOrder.setPayid(payId); // 会写支付单号
					userOrder.setUserid(param.getSubmitUserId());
					userOrder.setBranchuserid(param.getSubmitUserId());// 分销商userId
					userOrder.setRemark(param.getRemark());
					userOrder.setOrdertype(Integer.parseInt(OrderTypeEnum.ti_branchOrder.toString()));// 订单类型
					userOrder.setOrdertime(ordertime);
					userOrder.setPaytime(ordertime); 
					userOrder.setStatus(Integer.parseInt(OrderStatusEnum.waitFoSend.toString()));
					userOrder.setTotalprice(totalprice);
					userOrder.setOrdertotalprice(totalprice); 
					userOrder.setOrderaddressid(work.getAddressid()); 
					userOrder.setProduceruserid(producerUserId); 
					userOrdersMapper.insert(userOrder);
					//订单产品
					TiProducts product=productMapper.selectByPrimaryKey(style.getProductid());
					OOrderproducts orderProduct = new OOrderproducts();
					orderProduct.setOrderproductid(orderProductId); 
					orderProduct.setUserorderid(userOrderId);
					orderProduct.setBuyeruserid(param.getSubmitUserId()); 
					orderProduct.setProductid(work.getProductid());
					orderProduct.setStyleid(style.getStyleid());
					orderProduct.setPropertystr(style.getDescription());
					orderProduct.setPrice(style.getPromoterprice());
					orderProduct.setCount(param.getCount());
					orderProduct.setProductimg(style.getDefaultimg());
					orderProduct.setProducttitle(product.getTitle());
					orderProduct.setCartid(param.getWorkId());
					oproductMapper.insert(orderProduct);
					
					
					//支付单信息
					OPayorder payorder = new OPayorder();
					payorder.setPayid(payId);
					payorder.setUserorderid(userOrderId);
					payorder.setUserid(param.getSubmitUserId());
					payorder.setStatus(Integer.parseInt(OrderStatusEnum.payed.toString()));
					payorder.setTotalprice(totalprice);
					payorder.setWalletamount(totalprice);
					payorder.setCashamount(0d);
					payorder.setCreatetime(new Date());
					payorder.setOrdertype(Integer.parseInt(PayOrderTypeEnum.ti_gouwu.toString()));
					payOrderMapper.insert(payorder);  		
					//红包金额入账
					accountService.add_accountsLog(param.getSubmitUserId(), Integer.parseInt(AccountLogType.use_payment.toString()),totalprice, payId, "");
					
					//反写活动状态
					work.setStatus(Integer.parseInt(GroupActWorkStatus.completeorder.toString()));
					groupactworkMapper.updateByPrimaryKey(work);
					
					//销量
					TiProductsext productsext = productextMapper.selectByPrimaryKey(work.getProductid());
					if (productsext == null) {
						productsext = new TiProductsext();
						productsext.setProductid(work.getProductid());
						productsext.setSales(param.getCount());
						productsext.setMonthssales(param.getCount());
						productextMapper.insert(productsext);
					} else {
						productsext.setSales((productsext.getSales() == null ? 0 : productsext.getSales().intValue()) + param.getCount());
						productsext.setMonthssales((productsext.getMonthssales() == null ? 0 : productsext.getMonthssales().intValue()) + param.getCount());
						productextMapper.updateByPrimaryKeySelective(productsext);
					}
					
					Map<String, Object> mapResult = new HashMap<String, Object>();
					String orderId = payId;
					mapResult.put("payId", payId);
					mapResult.put("orderId", orderId);
					mapResult.put("productId", product.getProductid());
					mapResult.put("styleId", style.getStyleid());
					mapResult.put("totalPrice", totalprice);
					rq.setBasemodle(mapResult);
					rq.setStatu(ReturnStatus.Success);
					rq.setStatusreson("下单成功"); 
				}
			}else {
				rq.setStatusreson("用户作品未完成（不在可下单的状态）！"); 
			}
		} catch (Exception e) {
			addlog("分销下单：workId="+param.getWorkId()+"error:"+e.getMessage()); 
			throw new RuntimeException(e);
		}
		return rq;
	}
	
}
