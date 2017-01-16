package com.bbyiya.service.impl.pic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.baseUtils.GenUtils;
import com.bbyiya.dao.OOrderaddressMapper;
import com.bbyiya.dao.OOrderproductdetailsMapper;
import com.bbyiya.dao.OOrderproductsMapper;
//import com.bbyiya.dao.OPayforuserorderMapper;
import com.bbyiya.dao.OPayorderMapper;
import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.PProductsMapper;
import com.bbyiya.dao.PProductstylepropertyMapper;
import com.bbyiya.dao.PProductstylesMapper;
import com.bbyiya.dao.RegionMapper;
import com.bbyiya.dao.UUseraddressMapper;
import com.bbyiya.enums.OrderStatusEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.OOrderaddress;
import com.bbyiya.model.OOrderproductdetails;
import com.bbyiya.model.OOrderproducts;
//import com.bbyiya.model.OPayforuserorder;
import com.bbyiya.model.OPayorder;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.PProducts;
import com.bbyiya.model.PProductstyles;
import com.bbyiya.model.RAreas;
import com.bbyiya.model.RCity;
import com.bbyiya.model.RProvince;
import com.bbyiya.model.UUseraddress;
import com.bbyiya.service.IRegionService;
import com.bbyiya.service.pic.IBaseOrderMgtService;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.vo.RegionVo;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.order.UserOrderParam;
import com.bbyiya.vo.order.UserOrderResult;
import com.bbyiya.vo.product.StyleProperty;

@Service("baseOrderMgtServiceImpl")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class BaseOrderMgtServiceImpl implements IBaseOrderMgtService {
	@Autowired
	private UUseraddressMapper addressMapper;// 用户收货地址
	@Autowired
	private RegionMapper regionMapper;// 区域

	@Resource(name = "regionServiceImpl")
	private IRegionService regionService;
	/*--------------------产品模块注解---------------------------------*/
	@Autowired
	private PProductsMapper productsMapper;
	@Autowired
	private PProductstylesMapper styleMapper;
	@Autowired
	private PProductstylepropertyMapper propertyMapper;

	// --------------------订单模块注解--------------------------------------
	@Autowired
	private OOrderaddressMapper orderaddressMapper;// 订单收货地址
	@Autowired
	private OOrderproductsMapper oproductMapper;// 订单产品
	@Autowired
	private OUserordersMapper userOrdersMapper;// 订单
	@Autowired
	private OPayorderMapper payOrderMapper;// 支付单

	@Autowired
	private OOrderproductdetailsMapper odetailMapper;// 产品图片集合

	/**
	 * 用户订单，产品订单 共用（一个订单对应一个产品 ）
	 * 
	 * @param userId
	 * @param addrId
	 * @param remark
	 * @param product
	 * @return
	 */
	public ReturnModel submitOrder_singleProduct(Long userId, Long addrId, String remark, OOrderproducts product) {
		ReturnModel rq = new ReturnModel();
		try {
			long orderAddrId = getOrderAddressId(addrId);
			if (orderAddrId <= 0) {
				rq.setStatu(ReturnStatus.OrderError);
				rq.setStatusreson("订单收货地址生成有误");
				return rq;
			}
			List<OOrderproducts> prolist = new ArrayList<OOrderproducts>();
			prolist.add(product);
			Map<String, UserOrderParam> orderlist = getUserOrderParamList(orderAddrId, remark, prolist);
			if (orderlist != null && orderlist.size() > 0) {
				String payId = GenUtils.getOrderNo(userId);
				String orderId = "";
				Double totalPrice_pay = 0d;
				for (UserOrderParam param : orderlist.values()) {
					// 用户订单号（订单号）
					String userOrderId = payId;// GenUtils.getOrderNo(userId);
					orderId = userOrderId;
					// 用户订单
					Date ordertime = new Date();
					OUserorders userOrder = new OUserorders();
					userOrder.setUserorderid(userOrderId);
					userOrder.setBranchuserid(param.getBranchUserId());
					userOrder.setUserid(userId);// 买家userId
					userOrder.setBranchuserid(param.getBranchUserId());// 分销商userId
					userOrder.setRemark(param.getRemark());
					userOrder.setOrdertime(ordertime);
					userOrder.setStatus(Integer.parseInt(OrderStatusEnum.noPay.toString()));
					userOrder.setOrderaddressid(orderAddrId);
					userOrder.setPayid(payId); // 会写支付单号
					if (param.getProlist() != null && param.getProlist().size() > 0) {
						Double totalPrice = 0d;
						for (OOrderproducts pp : param.getProlist()) {
							// 获取产品信息
							PProducts products = productsMapper.selectByPrimaryKey(pp.getProductid());
							PProductstyles styles = styleMapper.selectByPrimaryKey(pp.getStyleid());
							if (products != null && styles != null) {
								pp.setOrderproductid(userOrderId);// 产品订单编号
								pp.setUserorderid(userOrderId);// 用户订单号
								pp.setBuyeruserid(userId);
								oproductMapper.insert(pp);
								// 价格和
								totalPrice += styles.getPrice() * pp.getCount();
							} else {
								throw new Exception("找不到相应的产品！");
							}
						}
						userOrder.setTotalprice(totalPrice);
						userOrdersMapper.insert(userOrder);

						totalPrice_pay += totalPrice;
					} else {
						rq.setStatu(ReturnStatus.ParamError);
						rq.setStatusreson("产品产生有误");
						return rq;
					}
				}
				addPayOrder(userId, payId, payId, totalPrice_pay); // 插入支付订单记录
				rq.setStatu(ReturnStatus.Success);
				Map<String, Object> mapResult = new HashMap<String, Object>();
				mapResult.put("payId", payId);
				mapResult.put("orderId", orderId);
				mapResult.put("productId", product.getProductid());
				mapResult.put("styleId", product.getStyleid());
				mapResult.put("totalPrice", totalPrice_pay);
				rq.setBasemodle(mapResult);
			} else {
				rq.setStatu(ReturnStatus.OrderError_1);
				rq.setStatusreson("订单参数有误");
				return rq;
			}

		} catch (Exception e) {
			// TODO: handle exception
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson(e.getMessage());
		}
		return rq;
	}

	/**
	 * 去支付
	 * 
	 * @param orderId
	 * @return
	 */
	public ReturnModel getPayOrderByOrderId(String orderId) {
		ReturnModel rq = new ReturnModel();
		OUserorders userorder = userOrdersMapper.selectByPrimaryKey(orderId);
		if (userorder != null) {
			if (userorder.getStatus().intValue() == Integer.parseInt(OrderStatusEnum.noPay.toString())) {
				OPayorder order = payOrderMapper.selectByPrimaryKey(userorder.getPayid());
				if (order != null) {
					if (order.getStatus().intValue() == Integer.parseInt(OrderStatusEnum.noPay.toString())) {
						List<OOrderproducts> proList = oproductMapper.findOProductsByOrderId(orderId);
						if (proList != null && proList.size() > 0) {
							Map<String, Object> mapResult = new HashMap<String, Object>();
							mapResult.put("payId", order.getPayid());
							mapResult.put("orderId", orderId);
							mapResult.put("productId", proList.get(0).getProductid());
							mapResult.put("styleId", proList.get(0).getStyleid());
							mapResult.put("totalPrice", order.getTotalprice());
							rq.setBasemodle(mapResult);
							rq.setStatu(ReturnStatus.Success);
						}
					}
				}
			} else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("订单不在此操作有效的状态！");
				return rq;
			}
		}
		return rq;
	}

	public ReturnModel saveOrderImages(String orderId, List<OOrderproductdetails> imagelist) {
		ReturnModel rq = new ReturnModel();
		OOrderproducts oProduct = oproductMapper.selectByPrimaryKey(orderId);
		if (oProduct != null) {
			Date nowtime = new Date();
			// 用户订单
			OUserorders userorders = userOrdersMapper.selectByPrimaryKey(orderId);
			if (userorders != null) {
				if (userorders.getStatus().intValue() == Integer.parseInt(OrderStatusEnum.payed.toString())) {
					// 插入订单产品 图片集合
					for (OOrderproductdetails de : imagelist) {
						de.setOrderproductid(orderId);
						de.setCreatetime(nowtime);
						odetailMapper.insert(de);
					}
					// 修改订单状态
					userorders.setStatus(Integer.parseInt(OrderStatusEnum.waitFoSend.toString()));
					userOrdersMapper.updateByPrimaryKeySelective(userorders);

					rq.setStatu(ReturnStatus.Success);
					rq.setStatusreson("ok");
				} else {
					rq.setStatu(ReturnStatus.SystemError_1);
					rq.setStatusreson("订单非已支付状态！");
				}
			} else {
				rq.setStatu(ReturnStatus.SystemError_1);
				rq.setStatusreson("找不到相应的订单！");
			}
		} else {
			rq.setStatu(ReturnStatus.SystemError_1);
			rq.setStatusreson("找不到相应的订单产品！");
		}
		return rq;
	}

	public ReturnModel findOrderlist(Long userId) {
		ReturnModel rq = new ReturnModel();
		List<OUserorders> userorders = userOrdersMapper.findOrderByUserId(userId);
		if (userorders != null && userorders.size() > 0) {
			List<UserOrderResult> resultlist = new ArrayList<UserOrderResult>();
			for (OUserorders oo : userorders) {
				UserOrderResult model = new UserOrderResult();
				model.setUserOrderId(oo.getUserorderid());
				model.setTotalprice(oo.getTotalprice());
				model.setStatus(oo.getStatus());
				model.setOrderTimeStr(DateUtil.getTimeStr(oo.getOrdertime(), "yyyy-MM-dd"));
				List<OOrderproducts> proList = oproductMapper.findOProductsByOrderId(oo.getUserorderid());
				model.setProlist(proList);
				resultlist.add(model);
			}
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(resultlist);
		}
		return rq;
	}

	public ReturnModel getOrderInfo(Long userId, String orderId) {
		ReturnModel rq=new ReturnModel();
		OUserorders orderInfo=userOrdersMapper.selectByPrimaryKey(orderId);
		if(orderInfo!=null){
			UserOrderResult model = new UserOrderResult();
			model.setUserOrderId(orderInfo.getUserorderid());
			model.setTotalprice(orderInfo.getTotalprice());
			model.setStatus(orderInfo.getStatus());
			model.setOrderTimeStr(DateUtil.getTimeStr(orderInfo.getOrdertime(), "yyyy-MM-dd"));
			List<OOrderproducts> proList = oproductMapper.findOProductsByOrderId(orderInfo.getUserorderid());
			model.setProlist(proList);
			OOrderaddress addr= orderaddressMapper.selectByPrimaryKey(orderInfo.getOrderaddressid());
			if(addr!=null){
				model.setOrderAddress(addr);
			}
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(model);
		}else { 
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("不存在此订单");
		}
		return rq;
	}

	/**
	 * 新增支付单
	 * 
	 * @param userId
	 * @param payId
	 * @param totalPrice
	 */
	public void addPayOrder(Long userId, String payId, String userOrderId, Double totalPrice) {
		OPayorder payorder = new OPayorder();
		payorder.setPayid(payId);
		payorder.setUserorderid(userOrderId);
		payorder.setUserid(userId);
		payorder.setStatus(Integer.parseInt(OrderStatusEnum.noPay.toString()));
		payorder.setTotalprice(totalPrice);
		payorder.setCreatetime(new Date());
		payOrderMapper.insert(payorder);
	}

	/**
	 * 订单拆分
	 * 
	 * @param addrId
	 * @param remark
	 * @param prolist
	 * @return
	 */
	Map<String, UserOrderParam> getUserOrderParamList(Long addrId, String remark, List<OOrderproducts> prolist) {
		if (prolist != null && prolist.size() > 0) {
			Map<String, UserOrderParam> map = new HashMap<String, UserOrderParam>();
			for (OOrderproducts pp : prolist) {
				// TODO 验证用户是否可用 (目前没有分销商)
				PProducts products = productsMapper.selectByPrimaryKey(pp.getProductid());
				PProductstyles styles = styleMapper.selectByPrimaryKey(pp.getStyleid());
				if (products != null && styles != null) {
					pp.setPrice(styles.getPrice());
					pp.setPropertystr(getStylePropertyStr(pp.getStyleid()));// 款式
					pp.setBranchuserid(products.getUserid());
					pp.setProducttitle(products.getTitle());

					// 根据分销商、业务员 进行拆单
					String keyItem = products.getUserid() + "-" + pp.getSalesuserid();
					if (!map.containsKey(keyItem)) {
						UserOrderParam orderParam = new UserOrderParam();
						orderParam.setBranchUserId(products.getUserid());
						orderParam.setAddrId(addrId);
						orderParam.setRemark(remark);
						List<OOrderproducts> subList = new ArrayList<OOrderproducts>();
						subList.add(pp);
						orderParam.setProlist(subList);
						map.put(keyItem, orderParam);
					} else {
						UserOrderParam orderParam = map.get(keyItem);
						if (orderParam.getProlist() != null && orderParam.getProlist().size() > 0) {
							List<OOrderproducts> subList = orderParam.getProlist();
							subList.add(pp);
							orderParam.setProlist(subList);
							map.put(keyItem, orderParam);
						}
					}
				}
			}
			return map;
		}
		return null;
	}

	/**
	 * 获取产品款式属性（如： 中款|家庭装 ）
	 * 
	 * @param styleId
	 * @return
	 */
	public String getStylePropertyStr(Long styleId) {
		String result = "";
		List<StyleProperty> list = propertyMapper.findPropertyByStyleId(styleId);
		if (list != null && list.size() > 0) {
			for (StyleProperty property : list) {
				result += property.getStandardValue() + "-";
			}
			result = result.substring(0, result.lastIndexOf("-"));
		}
		return result;
	}

	/**
	 * 根据用户收货地址 生成订单收货地址 并返回订单收货地址的主键Id
	 * 
	 * @param userAddrId
	 *            user's addressId // not null
	 * @return
	 */
	public long getOrderAddressId(Long userAddrId) {
		if (userAddrId != null && userAddrId > 0) {
			UUseraddress addr = addressMapper.get_UUserAddressByKeyId(userAddrId);
			if (addr != null) {
				OOrderaddress orderAddress = new OOrderaddress();
				orderAddress.setUserid(addr.getUserid());
				orderAddress.setPhone(addr.getPhone());
				orderAddress.setReciver(addr.getReciver());
				orderAddress.setCity(regionService.getName(addr.getCity()));
				orderAddress.setProvince(regionService.getName(addr.getProvince()));
				orderAddress.setDistrict(regionService.getName(addr.getArea()));
				orderAddress.setStreetdetail(addr.getStreetdetail());
				orderAddress.setCreatetime(new Date());
				orderaddressMapper.insertReturnId(orderAddress);
				return orderAddress.getOrderaddressid();
			}
		}
		return 0;
	}

	/**
	 * gets the name of region by code
	 * 
	 * @param code
	 *            // region's code
	 * @return
	 */
	// public String getName(Integer code) {
	// if (code != null && code > 0) {
	// for (RegionVo vo : findRegionAll()) {
	// if (vo.getCode().intValue() == code.intValue()) {
	// return vo.getCodeName();
	// }
	// }
	// }
	// return "";
	// }

	/**
	 * All regionlist Gets a list of all regions (provinces, cities and
	 * districts the same level) 获取所有的 区域列表（省、市、区 同级）
	 * 
	 * @return
	 */
	// public List<RegionVo> findRegionAll() {
	// String keyString = "regionList_all";
	// @SuppressWarnings("unchecked")
	// List<RegionVo> resultList = (List<RegionVo>)
	// RedisUtil.getObject(keyString);// new
	// // ArrayList<RegionVo>();
	// if (resultList != null && resultList.size() > 0) {
	// return resultList;
	// } else {
	// resultList = new ArrayList<RegionVo>();
	// }
	// List<RProvince> provincelist = regionMapper.findProvincelistAll();
	// if (provincelist != null && provincelist.size() > 0) {
	// for (RProvince pp : provincelist) {
	// RegionVo vo = new RegionVo();
	// vo.setCode(pp.getCode());
	// vo.setCodeName(pp.getProvince());
	// vo.setStep(1);
	// resultList.add(vo);
	// List<RCity> citylist =
	// regionMapper.findCitylistBy_ProvinceCode(pp.getCode());
	// if (citylist != null && citylist.size() > 0) {
	// for (RCity cc : citylist) {
	// RegionVo vo_cc = new RegionVo();
	// vo_cc.setCode(cc.getCode());
	// vo_cc.setCodeName(cc.getCity());
	// vo_cc.setStep(2);
	// resultList.add(vo_cc);
	// List<RAreas> arealist =
	// regionMapper.findArealistBy_CityCode(cc.getCode());
	// if (arealist != null && arealist.size() > 0) {
	// for (RAreas aa : arealist) {
	// RegionVo vo_aa = new RegionVo();
	// vo_aa.setCode(aa.getCode());
	// vo_aa.setCodeName(aa.getArea());
	// vo_aa.setStep(3);
	// resultList.add(vo_aa);
	// }
	// }
	// }
	// }
	// }
	// }
	// if (resultList != null && resultList.size() > 0) {
	// RedisUtil.setObject(keyString, resultList, 3600);
	// }
	// return resultList;
	// }

	// TODO 订单处理状态
	public boolean paySuccessProcess(String payId) {
		if (!ObjectUtil.isEmpty(payId)) {
			try {
				OPayorder payOrder = payOrderMapper.selectByPrimaryKey(payId);
				if (payOrder != null) {
					OUserorders userorders = userOrdersMapper.selectByPrimaryKey(payOrder.getUserorderid());
					if (userorders != null) {
						if (userorders.getStatus().intValue() == Integer.parseInt(OrderStatusEnum.noPay.toString())) {
							userorders.setStatus(Integer.parseInt(OrderStatusEnum.payed.toString()));
							userOrdersMapper.updateByPrimaryKeySelective(userorders);
							payOrder.setStatus(Integer.parseInt(OrderStatusEnum.payed.toString()));
							payOrderMapper.updateByPrimaryKeySelective(payOrder);
							return true;
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return false;
	}

}
