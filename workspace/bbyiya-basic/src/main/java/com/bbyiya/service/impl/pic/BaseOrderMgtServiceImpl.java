package com.bbyiya.service.impl.pic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.baseUtils.GenUtils;
import com.bbyiya.dao.OOrderaddressMapper;
import com.bbyiya.dao.OOrderproductsMapper;
import com.bbyiya.dao.OPayforuserorderMapper;
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
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.model.OPayforuserorder;
import com.bbyiya.model.OPayorder;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.PProducts;
import com.bbyiya.model.PProductstyles;
import com.bbyiya.model.RAreas;
import com.bbyiya.model.RCity;
import com.bbyiya.model.RProvince;
import com.bbyiya.model.UUseraddress;
import com.bbyiya.service.pic.IBaseOrderMgtService;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.vo.RegionVo;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.order.UserOrderParam;
import com.bbyiya.vo.product.StyleProperty;

@Service("baseOrderMgtServiceImpl")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class BaseOrderMgtServiceImpl implements IBaseOrderMgtService {
	@Autowired
	private UUseraddressMapper addressMapper;//用户收货地址
	@Autowired
	private RegionMapper regionMapper;//区域
	/*--------------------产品模块注解---------------------------------*/
	@Autowired
	private PProductsMapper productsMapper;
	@Autowired
	private PProductstylesMapper styleMapper;
	@Autowired
	private PProductstylepropertyMapper propertyMapper;
	
	//--------------------订单模块注解--------------------------------------
	@Autowired
	private OOrderaddressMapper orderaddressMapper;//订单收货地址
	@Autowired
	private OOrderproductsMapper oproductMapper;//订单产品
	@Autowired
	private OUserordersMapper userOrdersMapper;//订单
	@Autowired
	private OPayorderMapper payOrderMapper;//支付单
	@Autowired
	private OPayforuserorderMapper payForOrderMapper;//支付单-订单 关系
	

//	/**
//	 * 提交订单
//	 * 
//	 * @param userId
//	 * @param param
//	 * @return
//	 */
//	public ReturnModel submitOrder(Long userId, UserOrderParam param) {
//		ReturnModel rq = new ReturnModel();
//		try {
//			// 用户订单号（订单号）
//			String userOrderId = GenUtils.getOrderNo(userId);
//			// 用户订单
//			Date ordertime = new Date();
//			OUserorders userOrder = new OUserorders();
//			userOrder.setUserorderid(userOrderId);
//			userOrder.setBranchuserid(param.getBranchUserId());
//			userOrder.setUserid(userId);// 买家userId
//			// TODO 涉及到拆单操作 ，目前没有分销商
//			userOrder.setBranchuserid(0l);// 分销商userId
//			userOrder.setRemark(param.getRemark());
//			userOrder.setOrdertime(ordertime);
//			userOrder.setStatus(Integer.parseInt(OrderStatusEnum.noPay.toString()));
//			long orderAddrId = getOrderAddressId(param.getAddrId());
//			if (orderAddrId > 0) {
//				userOrder.setOrderaddressid(param.getAddrId());
//			} else {
//				rq.setStatu(ReturnStatus.SystemError);
//				rq.setStatusreson("订单收货地址生成有误");
//				return rq;
//			}
//			if (param.getProlist() != null && param.getProlist().size() > 0) {
//				Double totalPrice = 0d;
//				for (OOrderproducts pp : param.getProlist()) {
//					// 获取产品信息
//					PProducts products = productsMapper.selectByPrimaryKey(pp.getProductid());
//					PProductstyles styles = styleMapper.selectByPrimaryKey(pp.getStyleid());
//					if (products != null && styles != null) {
//						pp.setOrderproductid(GenUtils.getOrderNo(userId));// 产品订单编号
//						pp.setUserorderid(userOrderId);// 用户订单号
//						pp.setBuyeruserid(userId);
//						oproductMapper.insert(pp);
//						// 价格和
//						totalPrice += styles.getPrice() * pp.getCount();
//					} else {
//						throw new Exception("找不到相应的产品！");
//					}
//				}
//				userOrder.setTotalprice(totalPrice);
//				userOrdersMapper.insert(userOrder);
//			} else {
//				rq.setStatu(ReturnStatus.ParamError);
//				rq.setStatusreson("产品产生有误");
//				return rq;
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			rq.setStatu(ReturnStatus.SystemError);
//			rq.setStatusreson(e.getMessage());
//		}
//		return rq;
//	}

	public ReturnModel submitOrder(Long userId, Long addrId, String remark, List<OOrderproducts> prolist) {
		ReturnModel rq = new ReturnModel();
		try {
			Map<String, UserOrderParam> orderlist = getUserOrderParamList(addrId, remark, prolist);
			if (orderlist != null && orderlist.size() > 0) {
				String payId=GenUtils.getOrderNo(userId);
				Double totalPrice_pay=0d; 
				for (UserOrderParam param : orderlist.values()) {
					// 用户订单号（订单号）
					String userOrderId = GenUtils.getOrderNo(userId);
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
					long orderAddrId = getOrderAddressId(param.getAddrId());
					if (orderAddrId > 0) {
						userOrder.setOrderaddressid(param.getAddrId());
					} else {
						rq.setStatu(ReturnStatus.OrderError);
						rq.setStatusreson("订单收货地址生成有误");
						return rq;
					}
					if (param.getProlist() != null && param.getProlist().size() > 0) {
						Double totalPrice = 0d;
						for (OOrderproducts pp : param.getProlist()) {
							// 获取产品信息
							PProducts products = productsMapper.selectByPrimaryKey(pp.getProductid());
							PProductstyles styles = styleMapper.selectByPrimaryKey(pp.getStyleid());
							if (products != null && styles != null) {
//								productOrderId_result=GenUtils.getOrderNo(userId);
								pp.setOrderproductid(GenUtils.getOrderNo(userId));// 产品订单编号
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
						addPayForUserOrder(payId, userOrderId); //插入支付单-用户订单 关系
						totalPrice_pay+=totalPrice;
					} else {
						rq.setStatu(ReturnStatus.ParamError);
						rq.setStatusreson("产品产生有误");
						return rq;
					}
				}
				addPayOrder(userId, payId, totalPrice_pay); //插入支付订单记录
				rq.setStatu(ReturnStatus.Success);
				Map<String, Object> mapResult=new HashMap<String, Object>();
				mapResult.put("payId", payId);
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
	 * 用户订单，产品订单 共用（一个订单对应一个产品 ）
	 * @param userId
	 * @param addrId
	 * @param remark
	 * @param product
	 * @return
	 */
	public ReturnModel submitOrder_singleProduct(Long userId, Long addrId, String remark, OOrderproducts product) {
		ReturnModel rq = new ReturnModel();
		try {
			List<OOrderproducts> prolist=new ArrayList<OOrderproducts>();
			prolist.add(product);
			Map<String, UserOrderParam> orderlist = getUserOrderParamList(addrId, remark, prolist);
			if (orderlist != null && orderlist.size() > 0) {
				String payId=GenUtils.getOrderNo(userId);
				String orderId="";
				Double totalPrice_pay=0d; 
				for (UserOrderParam param : orderlist.values()) {
					// 用户订单号（订单号）
					String userOrderId = GenUtils.getOrderNo(userId);
					orderId=userOrderId;
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
					long orderAddrId = getOrderAddressId(param.getAddrId());
					if (orderAddrId > 0) {
						userOrder.setOrderaddressid(param.getAddrId());
					} else {
						rq.setStatu(ReturnStatus.OrderError);
						rq.setStatusreson("订单收货地址生成有误");
						return rq;
					}
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
						addPayForUserOrder(payId, userOrderId); //插入支付单-用户订单 关系
						totalPrice_pay+=totalPrice;
					} else {
						rq.setStatu(ReturnStatus.ParamError);
						rq.setStatusreson("产品产生有误");
						return rq;
					}
				}
				addPayOrder(userId, payId, totalPrice_pay); //插入支付订单记录
				rq.setStatu(ReturnStatus.Success);
				Map<String, Object> mapResult=new HashMap<String, Object>();
				mapResult.put("payId", payId);
				mapResult.put("orderId", orderId);
				mapResult.put("productId", product.getProductid());
				mapResult.put("styleId", product.getStyleid());
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
	
	public Map<String, Object> getResultStr(String payId){
		OPayforuserorder payforuserorder= payForOrderMapper.selectByPrimaryKey(payId);
		if(payforuserorder!=null){
			OUserorders userorders= userOrdersMapper.selectByPrimaryKey(payforuserorder.getUserorderid());
			if(userorders!=null){
				 List<OOrderproducts> prolist= oproductMapper.findOProductsByOrderId(userorders.getUserorderid());
				 if(prolist!=null&&prolist.size()>0){
					 
				 }
			}
		}
		return null;
	}
	
	/**
	 * 新增支付单
	 * @param userId
	 * @param payId
	 * @param totalPrice
	 */
	public void addPayOrder(Long userId,String payId,Double totalPrice) {
		OPayorder payorder=new OPayorder();
		payorder.setPayid(payId);
		payorder.setUserid(userId);
		payorder.setStatus(Integer.parseInt(OrderStatusEnum.noPay.toString()));
		payorder.setTotalprice(totalPrice);
		payorder.setCreatetime(new Date()); 
		payOrderMapper.insert(payorder);
	}
	
	/**
	 * 新增支付单-用户订单 关系
	 * @param payId
	 * @param userOrderId
	 */
	public void addPayForUserOrder(String payId,String userOrderId) {
		OPayforuserorder model=new OPayforuserorder();
		model.setPayid(payId);
		model.setUserorderid(userOrderId);
		model.setCreatetime(new Date());
		payForOrderMapper.insert(model); 
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
	 * @param styleId
	 * @return
	 */
	public String getStylePropertyStr(Long styleId) {
		String result = ""; 
		List<StyleProperty> list = propertyMapper.findPropertyByStyleId(styleId);
		if (list != null && list.size() > 0) {
			for (StyleProperty property : list) {
				result += property.getStandardValue() + "|";
			}
			result.substring(0, result.length() - 1);
		}
		return result;
	}

	/**
	 * 根据用户收货地址 生成订单收货地址 并返回订单收货地址的主键Id
	 * 
	 * @param userAddrId user's addressId // not null
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
				orderAddress.setCity(getName(addr.getCity()));
				orderAddress.setProvince(getName(addr.getProvince()));
				orderAddress.setDistrict(getName(addr.getArea()));
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
	public String getName(Integer code) {
		if (code != null && code > 0) {
			for (RegionVo vo : findRegionAll()) {
				if (vo.getCode().intValue() == code.intValue()) {
					return vo.getCodeName();
				}
			}
		}
		return "";
	}

	/**
	 * All regionlist Gets a list of all regions (provinces, cities and
	 * districts the same level) 获取所有的 区域列表（省、市、区 同级）
	 * 
	 * @return
	 */
	public List<RegionVo> findRegionAll() {
		String keyString = "regionList_all";
		List<RegionVo> resultList = (List<RegionVo>) RedisUtil.getObject(keyString);// new  ArrayList<RegionVo>();
		if (resultList != null && resultList.size() > 0) {
			return resultList;
		} else {
			resultList = new ArrayList<RegionVo>();
		}
		List<RProvince> provincelist = regionMapper.findProvincelistAll();
		if (provincelist != null && provincelist.size() > 0) {
			for (RProvince pp : provincelist) {
				RegionVo vo = new RegionVo();
				vo.setCode(pp.getCode());
				vo.setCodeName(pp.getProvince());
				vo.setStep(1);
				resultList.add(vo);
				List<RCity> citylist = regionMapper.findCitylistBy_ProvinceCode(pp.getCode());
				if (citylist != null && citylist.size() > 0) {
					for (RCity cc : citylist) {
						RegionVo vo_cc = new RegionVo();
						vo_cc.setCode(cc.getCode());
						vo_cc.setCodeName(cc.getCity());
						vo_cc.setStep(2);
						resultList.add(vo_cc);
						List<RAreas> arealist = regionMapper.findArealistBy_CityCode(cc.getCode());
						if (arealist != null && arealist.size() > 0) {
							for (RAreas aa : arealist) {
								RegionVo vo_aa = new RegionVo();
								vo_aa.setCode(aa.getCode());
								vo_aa.setCodeName(aa.getArea());
								vo_aa.setStep(3);
								resultList.add(vo_aa);
							}
						}
					}
				}
			}
		}
		return resultList;
	}

}
