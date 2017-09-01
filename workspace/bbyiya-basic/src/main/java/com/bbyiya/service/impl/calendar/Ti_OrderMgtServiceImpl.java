package com.bbyiya.service.impl.calendar;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.baseUtils.GenUtils;
import com.bbyiya.dao.EErrorsMapper;
import com.bbyiya.dao.OOrderaddressMapper;
import com.bbyiya.dao.OOrderproductsMapper;
import com.bbyiya.dao.OPayorderMapper;
import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.TiDiscountdetailsMapper;
import com.bbyiya.dao.TiDiscountmodelMapper;
import com.bbyiya.dao.TiMyworksMapper;
import com.bbyiya.dao.TiProductsMapper;
import com.bbyiya.dao.TiProductstylesMapper;
import com.bbyiya.dao.TiPromoteremployeesMapper;
import com.bbyiya.dao.TiPromotersMapper;
import com.bbyiya.dao.TiUserdiscountsMapper;
import com.bbyiya.dao.UAccountsMapper;
import com.bbyiya.dao.UBranchesMapper;
import com.bbyiya.dao.UBranchusersMapper;
import com.bbyiya.dao.UUseraddressMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.OrderStatusEnum;
import com.bbyiya.enums.OrderTypeEnum;
import com.bbyiya.enums.PayOrderTypeEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.EErrors;
import com.bbyiya.model.OOrderaddress;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.model.OPayorder;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PPostmodel;
import com.bbyiya.model.PProducts;
import com.bbyiya.model.PProductstyles;
import com.bbyiya.model.TiDiscountdetails;
import com.bbyiya.model.TiDiscountmodel;
import com.bbyiya.model.TiMyworks;
import com.bbyiya.model.TiProducts;
import com.bbyiya.model.TiProductstyles;
import com.bbyiya.model.TiPromoteremployees;
import com.bbyiya.model.TiPromoters;
import com.bbyiya.model.TiUserdiscounts;
import com.bbyiya.model.UAccounts;
import com.bbyiya.model.UBranches;
import com.bbyiya.model.UBranchusers;
import com.bbyiya.model.UUseraddress;
import com.bbyiya.model.UUsers;
import com.bbyiya.service.IRegionService;
import com.bbyiya.service.calendar.ITi_OrderMgtService;
import com.bbyiya.service.pic.IBasePostMgtService;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.order.UserOrderSubmitParam;
@Service("tiOrderMgtServiceImpl")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Ti_OrderMgtServiceImpl implements ITi_OrderMgtService {
	@Autowired
	private TiProductstylesMapper styleMapper;
	@Autowired
	private TiProductsMapper productMapper;
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
	@Resource(name = "basePostMgtServiceImpl")
	private IBasePostMgtService postMgtService;
	@Autowired
	private UBranchusersMapper branchusersMapper;
	@Autowired
	private UBranchesMapper branchesMapper;
	@Autowired
	private OOrderaddressMapper orderaddressMapper;
	@Resource(name = "regionServiceImpl")
	private IRegionService regionService;

	@Autowired
	private OPayorderMapper payOrderMapper;// 支付单
	@Autowired
	private OUserordersMapper userOrdersMapper;
	@Autowired
	private OOrderproductsMapper oproductMapper;
	@Autowired
	private TiMyworksMapper myworksMapper;

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
				orderAddressId = getOrderAddressId(param.getAddrId());
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
			if (orderType == Integer.parseInt(OrderTypeEnum.ti_branchOrder.toString()) || orderType == Integer.parseInt(OrderTypeEnum.ti_nomal.toString())) {
				TiProductstyles style = styleMapper.selectByPrimaryKey(orderProduct.getStyleid());
				if (style != null) {
					totalPrice = style.getPrice() * orderProduct.getCount();
					if (orderType == Integer.parseInt(OrderTypeEnum.ti_branchOrder.toString())) {
						orderProduct.setPrice(style.getPromoterprice());
					} else if (orderType == Integer.parseInt(OrderTypeEnum.ti_nomal.toString())) {
						orderProduct.setPrice(style.getPrice());
						// 优惠处理
						TiDiscountmodel discountmodel = getDiscountList(param.getUserId());
						if (discountmodel != null && discountmodel.getDetails() != null) {
							for (TiDiscountdetails discountdetails : discountmodel.getDetails()) {
								if (discountdetails.getStyleid().longValue() == style.getStyleid().longValue()) {
									if (discountmodel.getType() == 1) {
										orderProduct.setPrice(style.getPrice() * discountdetails.getDiscount());
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
			}
			userOrder.setOrdertotalprice(orderTotalPrice);
			userOrder.setTotalprice(totalPrice);
			if (orderType == Integer.parseInt(OrderTypeEnum.ti_branchOrder.toString())) {
				addPayOrder(param.getUserId(), payId, payId, orderTotalPrice, orderTotalPrice);
			} else {
				addPayOrder(param.getUserId(), payId, payId, orderTotalPrice, 0d);
			}
			TiMyworks myworks = new TiMyworks();
			myworks.setUserid(param.getUserId());
			myworks.setCreatetime(new Date());
			myworksMapper.insertReturnId(myworks);
			orderProduct.setCartid(myworks.getWorkid());
			// 插入订单
			userOrdersMapper.insert(userOrder);
			// 插入订单产品
			oproductMapper.insert(orderProduct);
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

	@Autowired
	private TiUserdiscountsMapper mydiscountMapper;
	@Autowired
	private TiDiscountmodelMapper disModelMapper;
	@Autowired
	private TiDiscountdetailsMapper discountDetailsMapper;

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
					orderAddress.setUserid(promoters.getProduceruserid());
					orderAddress.setPhone(promoters.getMobilephone());
					orderAddress.setReciver(promoters.getContacts());
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

	public long getOrderAddressId(Long userAddrId) {
		if (userAddrId != null && userAddrId > 0) {
			UUseraddress addr = addressMapper.get_UUserAddressByKeyId(userAddrId);
			if (addr != null) {
				OOrderaddress orderAddress = new OOrderaddress();
				orderAddress.setUserid(addr.getUserid());
				orderAddress.setPhone(addr.getPhone());
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
				TiPromoteremployees employee = ePromoteremployeesMapper.selectByPrimaryKey(users.getUserid());
				if (employee != null && employee.getPromoteruserid() != null) {
					TiPromoters promoter = promotersMapper.selectByPrimaryKey(employee.getPromoteruserid());
					if (promoter != null) {
						UAccounts accounts = accountsMapper.selectByPrimaryKey(promoter.getPromoteruserid());
						if (accounts != null && accounts.getAvailableamount() != null) {
							totalprice = style.getPromoterprice().doubleValue() * count;
							if (accounts.getAvailableamount().doubleValue() >= totalprice) {
								param.setBranchUserId(promoter.getPromoteruserid());
								param.setAgentUserId(promoter.getAgentuserid());
								rq.setStatu(ReturnStatus.Success);
								rq.setBasemodle(param);
								oproduct.setPrice(style.getPromoterprice());
							}
						}
					}
				}
			}
		} else if (orderType == Integer.parseInt(OrderTypeEnum.ti_nomal.toString())) {
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
		rq.setStatu(ReturnStatus.Success); 
		return rq;
	}

	@Autowired
	private EErrorsMapper logMapper;

	public void addlog(String msg) {
		EErrors errors = new EErrors();
		errors.setClassname(this.getClass().getName());
		errors.setMsg("下单失败：" + msg);
		errors.setCreatetime(new Date());
		logMapper.insert(errors);
	}
}
