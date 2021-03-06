package com.bbyiya.service.impl.pic;

import java.util.ArrayList;
import java.util.Calendar;
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
import com.bbyiya.dao.OOrderproductdetailsMapper;
import com.bbyiya.dao.OOrderproductsMapper;
//import com.bbyiya.dao.OPayforuserorderMapper;
import com.bbyiya.dao.OPayorderMapper;
import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.PProductsMapper;
import com.bbyiya.dao.PProductstylepropertyMapper;
import com.bbyiya.dao.PProductstylesMapper;
import com.bbyiya.dao.RAreaplansMapper;
import com.bbyiya.dao.RegionMapper;
import com.bbyiya.dao.UAccountsMapper;
import com.bbyiya.dao.UAgentcustomersMapper;
import com.bbyiya.dao.UAgentsMapper;
import com.bbyiya.dao.UBranchesMapper;
import com.bbyiya.dao.UBranchtransaccountsMapper;
import com.bbyiya.dao.UBranchusersMapper;
import com.bbyiya.dao.UCashlogsMapper;
import com.bbyiya.dao.UUseraddressMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.OrderStatusEnum;
import com.bbyiya.enums.OrderTypeEnum;
import com.bbyiya.enums.PayOrderTypeEnum;
import com.bbyiya.enums.PayTypeEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.AgentStatusEnum;
import com.bbyiya.enums.pic.BranchStatusEnum;
import com.bbyiya.enums.pic.MyProductStatusEnum;
import com.bbyiya.model.EErrors;
import com.bbyiya.model.OOrderaddress;
import com.bbyiya.model.OOrderproductdetails;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.model.OPayorder;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PPostmodel;
import com.bbyiya.model.PProducts;
import com.bbyiya.model.PProductstyles;
import com.bbyiya.model.RAreaplans;
import com.bbyiya.model.UAccounts;
import com.bbyiya.model.UAgentcustomers;
import com.bbyiya.model.UAgents;
import com.bbyiya.model.UBranches;
import com.bbyiya.model.UBranchtransaccounts;
import com.bbyiya.model.UBranchusers;
import com.bbyiya.model.UCashlogs;
import com.bbyiya.model.UUseraddress;
import com.bbyiya.model.UUsers;
import com.bbyiya.service.IRegionService;
import com.bbyiya.service.pic.IBaseOrderMgtService;
import com.bbyiya.service.pic.IBasePostMgtService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.order.UserBuyerOrderResult;
import com.bbyiya.vo.order.UserOrderParam;
import com.bbyiya.vo.order.UserOrderResult;
import com.bbyiya.vo.order.UserOrderSubmitParam;
import com.bbyiya.vo.order.UserOrderSubmitRepeatParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("baseOrderMgtServiceImpl")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class BaseOrderMgtServiceImpl implements IBaseOrderMgtService {

	@Resource(name = "regionServiceImpl")
	private IRegionService regionService;
	
	@Resource(name = "basePostMgtServiceImpl")
	private IBasePostMgtService postMgtService;
	@Autowired
	private RegionMapper regionMapper;// 区域

	/*--------------------产品模块注解---------------------------------*/
	@Autowired
	private PProductsMapper productsMapper;
	@Autowired
	private PProductstylesMapper styleMapper;
	@Autowired
	private PProductstylepropertyMapper propertyMapper;
	@Autowired
	private PMyproductsMapper myproductMapper;//我的作品

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
	
	

	/*------------------------用户信息模块----------------------------------*/
	@Autowired
	private UAccountsMapper accountsMapper;//账户信息
	@Autowired
	private UCashlogsMapper cashlogsMapper;//账户流水
	@Autowired
	private UUseraddressMapper addressMapper;// 用户收货地址
	@Autowired
	private UUsersMapper usersMapper;

	
	/*------------------------代理模块-------------------------------------*/
	@Autowired
	private UBranchesMapper branchesMapper;
	@Autowired
	private UBranchusersMapper branchusersMapper;
	@Autowired
	private RAreaplansMapper areaplansMapper;
	@Autowired
	private UAgentsMapper agentsMapper;
	@Autowired
	private UAgentcustomersMapper customerMapper;
	
	/*---错误日志记录------------------------------------------*/
	@Autowired
	private EErrorsMapper logMapper;
	
	@Autowired
	private UBranchtransaccountsMapper transMapper;
	

	/**
	 * 提交订单（param已经被验证）
	 * @param param
	 * @return
	 * @throws Exception
	 */
	private ReturnModel submitOrder_common(UserOrderSubmitParam param)throws Exception{
		ReturnModel rq = new ReturnModel();
		if(param==null||param.getUserId()==null||param.getCartId()==null||param.getOrderproducts()==null){
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("参数有误304");
			return rq;
		}
		int orderType = param.getOrderType() == null ? 0 : param.getOrderType();
		Map<String, Object> mapResult = new HashMap<String, Object>();
		String payId = GenUtils.getOrderNo(param.getUserId());
		String orderId = payId;
		mapResult.put("payId", payId);
		mapResult.put("orderId", orderId);
		
		OUserorders userOrder = new OUserorders();
		Date ordertime = new Date();//订单操作时间
		userOrder.setUserorderid(orderId);//用户订单号
		userOrder.setPayid(payId); // 会写支付单号
		userOrder.setUserid(param.getUserId());
		userOrder.setBranchuserid(param.getBranchUserId());// 分销商userId
		userOrder.setAgentuserid(param.getAgentUserId());
		userOrder.setRemark(param.getRemark());
		userOrder.setOrdertype(param.getOrderType());//订单类型
		userOrder.setOrdertime(ordertime);
		userOrder.setStatus(Integer.parseInt(OrderStatusEnum.noPay.toString()));
		userOrder.setIsbranch(0); 
		userOrder.setPostmodelid(param.getPostModelId()); 
		if(param.getOrderAddressId()!=null&&param.getOrderAddressId()>0){
			userOrder.setOrderaddressid(param.getOrderAddressId());
		}else {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("收货地址有误");
			return rq;
		}
		
		if (param.getOrderproducts() != null) {
			//实际需要付款的总价（包括邮费）
			Double orderTotalPrice = 0d;
			//订单总价
			Double totalPrice = 0d;
			//订单产品
			OOrderproducts orderProduct = param.getOrderproducts();
			// 获取产品信息
			PProducts products = productsMapper.selectByPrimaryKey(orderProduct.getProductid());
			PProductstyles styles = styleMapper.selectByPrimaryKey(orderProduct.getStyleid());
			if (products != null && styles != null) {
				mapResult.put("productId", products.getProductid());
				mapResult.put("styleId", styles.getStyleid());
				orderProduct.setOrderproductid(orderId);// 产品订单编号
				orderProduct.setUserorderid(orderId);// 用户订单号
				orderProduct.setBuyeruserid(param.getUserId());
				orderProduct.setPropertystr(styles.getPropertystr()); 
				orderProduct.setProducttitle(products.getTitle()); 
				orderProduct.setCartid(param.getCartId());//作品Id
				if (!ObjectUtil.isEmpty(styles.getDefaultimg())) {
					orderProduct.setProductimg(styles.getDefaultimg());
				} else {
					orderProduct.setProductimg(products.getDefaultimg());
				}
				// 订单原本实际价格总和
				orderTotalPrice = styles.getPrice() * orderProduct.getCount();
				orderProduct.setPrice(styles.getPrice());
				totalPrice = styles.getPrice() * orderProduct.getCount();

			} else {
				throw new Exception("找不到相应的产品！");
			}
			userOrder.setTotalprice(totalPrice);//订单价格，不包括邮费
			//实际需要付款金额（包括邮费）ps:B端订单邮费后期pbs扣款
			userOrder.setOrdertotalprice(totalPrice);
			
			if (orderType == Integer.parseInt(OrderTypeEnum.brachOrder.toString())) {
				//邮费 b端下单不录入邮费
				userOrder.setPostage(0d); 
				UAccounts accounts= accountsMapper.selectByPrimaryKey(param.getBranchUserId());
				if(accounts!=null&&accounts.getAvailableamount()!=null&&accounts.getAvailableamount()>=totalPrice) {
					// 影楼订单，直接预存款支付 ， 插入支付记录
					if(payOrder_logAdd(param.getBranchUserId(),orderId,orderId,totalPrice)){
						userOrder.setStatus(Integer.parseInt(OrderStatusEnum.payed.toString()));
						userOrder.setPaytime(new Date());
						userOrder.setPaytype(Integer.parseInt(PayTypeEnum.yiyaCash.toString()));  
					}else {
						throw new Exception("下单失败！");
					}
				}else {
					rq.setStatu(ReturnStatus.CashError);
					rq.setStatusreson("企业余额不足！");
					return rq;
				}
			} 
			// 普通购买  算邮费----------------------------------------------
			else{
				if(param.getPostPrice()!=null){
					userOrder.setPostage(param.getPostPrice()); 
					orderTotalPrice+=param.getPostPrice();
					userOrder.setOrdertotalprice(orderTotalPrice);//订单总价 
				}
				 // 插入支付订单记录
				addPayOrder(param.getUserId(), payId, payId, orderTotalPrice);
				// 插入客户记录------------------------------------------------------
				if(userOrder.getAgentuserid()!=null&&userOrder.getAgentuserid()>0){
					UUsers users=usersMapper.selectByPrimaryKey(userOrder.getUserid());
					if(users!=null){
						UAgentcustomers customer= customerMapper.getCustomersByAgentUserId(userOrder.getAgentuserid(), userOrder.getUserid());
						if(customer==null){
							UAgentcustomers cus=new UAgentcustomers();
							cus.setAgentuserid(userOrder.getAgentuserid());
							cus.setUserid(userOrder.getUserid());
							cus.setStatus(1);
							cus.setPhone(users.getMobilephone());
							cus.setName(users.getNickname());
							cus.setCreatetime(new Date());
							customerMapper.insert(cus);
						}
					}
				}
			}
			PMyproducts mycart=myproductMapper.selectByPrimaryKey(param.getCartId()) ;
			if(mycart!=null){
				userOrdersMapper.insert(userOrder);//插入订单
				oproductMapper.insert(orderProduct);//插入订单产品
				mycart.setOrderno(orderId);
				mycart.setStatus(Integer.parseInt(MyProductStatusEnum.ordered.toString()));
				mycart.setUpdatetime(new Date()); 
				myproductMapper.updateByPrimaryKeySelective(mycart);
			}else {
				throw new Exception("作品不存在！");
			}
			rq.setStatu(ReturnStatus.Success);
			mapResult.put("totalPrice", totalPrice);
			rq.setBasemodle(mapResult);
		} else {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("产品产生有误");
			return rq;
		}
		return rq;
	}
	
	
	public ReturnModel submitOrder_new(UserOrderSubmitParam param) {
		ReturnModel rq = new ReturnModel();
		int orderType=param.getOrderType()==null?0:param.getOrderType();
		try {
			//订单参数，用户资格验证
			rq=checkOrderParam(param);
			if(!rq.getStatu().equals(ReturnStatus.Success))//未通过参数验证
			{
				return rq;
			}	
			if(rq.getBasemodle()!=null){
				param=(UserOrderSubmitParam)rq.getBasemodle();
			}
			long orderAddressId =0;
			if(orderType==Integer.parseInt(OrderTypeEnum.brachOrder.toString())){//影楼订单
				orderAddressId = getOrderAddressIdByBranchUserId(param.getUserId(), orderType);
			}else {//普通购买
				orderAddressId  = getOrderAddressId(param.getAddrId());
			}
			if(orderAddressId>0){//
				param.setOrderAddressId(orderAddressId);
			} 
			return submitOrder_common(param);
		} catch (Exception e) {
			addlog(e.getMessage()); 
			throw new RuntimeException(e.getMessage());
		}
	}
	/**
	 * 插入错误Log
	 * 
	 * @param msg
	 */
	public void addlog(String msg) {
		EErrors errors = new EErrors();
		errors.setClassname(this.getClass().getName());
		errors.setMsg(msg);
		errors.setCreatetime(new Date()); 
		logMapper.insert(errors);
	}
	
	/**
	 * 已下单的作品 再次下单
	 * @param userId
	 * @param param
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public ReturnModel submitOrder_repeat(Long userId, UserOrderSubmitRepeatParam param) throws Exception {
		ReturnModel rq = new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		if (param == null || ObjectUtil.isEmpty(param.getUserOrderId())) {
			rq.setStatusreson("参数有误");
			return rq;
		}
		// 01 获取 订单
		OUserorders userorder = userOrdersMapper.selectByPrimaryKey(param.getUserOrderId());
		if (userorder != null) {
			// 02 订单产品
			OOrderproducts oproduct = oproductMapper.getOProductsByOrderId(param.getUserOrderId());
			if (oproduct != null) {
				if(oproduct.getCartid()==null||oproduct.getCartid()<=0){
					rq.setStatusreson("无效的订单信息，无法再次订购！");
					return rq;
				}
				PProducts product = productsMapper.selectByPrimaryKey(oproduct.getProductid());
				PProductstyles style = styleMapper.selectByPrimaryKey(oproduct.getStyleid());
				if (style == null || product == null || style.getStatus() != 1 || product.getStatus() != 1) {
					rq.setStatusreson("相册已经下架，无法下单");
					return rq;
				}
				// 03 订单作品
				List<OOrderproductdetails> details = odetailMapper.findOrderProductDetailsByProductOrderId(oproduct.getOrderproductid());
				if (details != null && details.size() > 0) {
					/*------------------------影楼内部订单------------------------------------------------*/
					if ( param.getOrderType() == Integer.parseInt(OrderTypeEnum.brachOrder.toString())) {
						// 影楼金额是否足够
						UBranchusers branchusers = branchusersMapper.selectByPrimaryKey(userId);
						if (branchusers != null && branchusers.getBranchuserid() != null) {
							UBranches branches = branchesMapper.selectByPrimaryKey(branchusers.getBranchuserid());
							if (branches != null && branches.getStatus() != null && branches.getStatus().intValue() == Integer.parseInt(BranchStatusEnum.ok.toString())) {
								param.setBranchUserId(branches.getBranchuserid());
								param.setAgentUserId(branches.getAgentuserid());
								double totalprice = style.getAgentprice() * param.getCount();
								UAccounts accounts = accountsMapper.selectByPrimaryKey(branches.getBranchuserid());
								if (accounts != null && accounts.getAvailableamount() != null && accounts.getAvailableamount() >= totalprice) {
									// 影楼的运费款 查询
									UBranchtransaccounts transAccount = transMapper.selectByPrimaryKey(branches.getBranchuserid());
									if (transAccount == null || transAccount.getAvailableamount() == null || transAccount.getAvailableamount() <= 0) {
										rq.setStatu(ReturnStatus.CashError);
										rq.setStatusreson("影楼的运费款不足，无法下单！请通知管理员进行充值，以免影响您的业务！ ");
										return rq;
									}
								} else {
									rq.setStatu(ReturnStatus.CashError);
									rq.setStatusreson("影楼预存货款不足，无法下单！请通知管理员进行充值，以免影响您的业务！");
									return rq;
								}
							} else {
								rq.setStatusreson("不是有效的合作伙伴！");
								return rq;
							}
						} else {
							rq.setStatusreson("权限不足");
							return rq;
						}
					}
					/*-----------------------普通用户订单--------------------------------*/
					else {
						if(param.getAddrId()==null||param.getAddrId()<=0){
							rq.setStatusreson("收货地址未填！");
							return rq;
						}
						UUseraddress addr = addressMapper.get_UUserAddressByKeyId(param.getAddrId());// 用户收货地址
						if(addr==null){
							rq.setStatusreson("收货地址不存在");
							return rq;
						}
						/*---------------------通过快递方式查询邮费---------------------------------------------*/
						if (param.getPostModelId()!=null&& param.getPostModelId() <= 0) {
							List<PPostmodel> listpost = postMgtService.find_postlist(addr.getArea());
							if (listpost != null && listpost.size() > 0) {
								param.setPostModelId(listpost.get(0).getPostmodelid());
								param.setPostage(listpost.get(0).getAmount());
							}
						} else {
							PPostmodel postmodel = postMgtService.getPostmodel(param.getPostModelId(), addr.getArea());
							if (postmodel != null) {
								param.setPostage(postmodel.getAmount());
							} else {
								rq.setStatu(ReturnStatus.SystemError);
								rq.setStatusreson("快递方式不存在！");
								return rq;
							}
						}
					}
					// 开始下单操作-------------------------------------------------------------------
					Map<String, Object> mapResult = new HashMap<String, Object>();
					String payId = GenUtils.getOrderNo(userId);
					String orderId = payId;
					mapResult.put("payId", payId);
					mapResult.put("orderId", orderId);

					/*-------------------------------------------------------*/
					OUserorders userOrder_Repeat = new OUserorders();
					Date ordertime = new Date();// 订单操作时间
					userOrder_Repeat.setUserorderid(orderId);// 用户订单号
					userOrder_Repeat.setPayid(payId); // 会写支付单号
					userOrder_Repeat.setUserid(userId);
					userOrder_Repeat.setBranchuserid(userorder.getBranchuserid());// 分销商userId
					userOrder_Repeat.setAgentuserid(userorder.getAgentuserid());
					userOrder_Repeat.setRemark(param.getRemark());
					userOrder_Repeat.setOrdertype(param.getOrderType());// 订单类型
					userOrder_Repeat.setOrdertime(ordertime);
					userOrder_Repeat.setStatus(Integer.parseInt(OrderStatusEnum.noPay.toString()));
					userOrder_Repeat.setIsbranch(0);
					
					long orderAddressId = 0;
					if (param.getOrderType() == Integer.parseInt(OrderTypeEnum.brachOrder.toString())) {// 影楼订单
						orderAddressId = getOrderAddressIdByBranchUserId(userId, param.getOrderType());
					} else {// 普通购买
						orderAddressId = getOrderAddressId(param.getAddrId());
					}
					if (orderAddressId > 0) {
						userOrder_Repeat.setOrderaddressid(orderAddressId);
					} else {
						rq.setStatu(ReturnStatus.ParamError);
						rq.setStatusreson("收货地址有误");
						return rq;
					}

					if (true) {
						// 实际需要付款的总价（包括邮费）
						Double orderTotalPrice = 0d;
						// 订单总价
						Double totalPrice = 0d;
						// 订单产品
						OOrderproducts orderProduct = new OOrderproducts();
						orderProduct.setOrderproductid(orderId);// 产品订单编号
						orderProduct.setUserorderid(orderId);// 用户订单号
						orderProduct.setBuyeruserid(userId);
						orderProduct.setProductid(style.getProductid());
						orderProduct.setStyleid(style.getStyleid()); 
						orderProduct.setPropertystr(style.getPropertystr());
						orderProduct.setProducttitle(product.getTitle());
						orderProduct.setCartid(oproduct.getCartid());// 作品Id
						orderProduct.setProductimg(oproduct.getProductimg());
						orderProduct.setPrice(style.getPrice());
						orderProduct.setCount(param.getCount()); 
						
						// 订单原本实际价格总和
						orderTotalPrice = style.getPrice() * param.getCount();
						totalPrice = style.getPrice() * param.getCount();

						userOrder_Repeat.setTotalprice(totalPrice);// 订单价格，不包括邮费
						// 实际需要付款金额（包括邮费）ps:B端订单邮费后期pbs扣款
						userOrder_Repeat.setOrdertotalprice(totalPrice);

						if (param.getOrderType() == Integer.parseInt(OrderTypeEnum.brachOrder.toString())) {
							orderProduct.setSalesuserid(userId);
							orderProduct.setBranchuserid(param.getBranchUserId()); 
							// 邮费 b端下单不录入邮费
							userOrder_Repeat.setPostage(0d);
							UAccounts accounts = accountsMapper.selectByPrimaryKey(param.getBranchUserId());
							if (accounts != null && accounts.getAvailableamount() != null && accounts.getAvailableamount() >= totalPrice) {
								// 影楼订单，直接预存款支付 ， 插入支付记录
								if (payOrder_logAdd(param.getBranchUserId(), orderId, orderId, totalPrice)) {
									
									userOrder_Repeat.setStatus(Integer.parseInt(OrderStatusEnum.waitFoSend.toString()));
									userOrder_Repeat.setPaytime(new Date());
									userOrder_Repeat.setPaytype(Integer.parseInt(PayTypeEnum.yiyaCash.toString()));
								} else {
									throw new Exception("下单失败！");
								}
							} else {
								rq.setStatu(ReturnStatus.CashError);
								rq.setStatusreson("企业余额不足！");
								return rq;
							}
						} else {// 普通购买 算邮费
							if (param.getPostage() != null) {
								userOrder_Repeat.setPostmodelid(param.getPostModelId());
								userOrder_Repeat.setPostage(param.getPostage());
								orderTotalPrice += param.getPostage();
								userOrder_Repeat.setOrdertotalprice(orderTotalPrice);// 订单总价
							}
							addPayOrder(userId, payId, payId, orderTotalPrice); // 插入支付订单记录
						}
						for (OOrderproductdetails dd : details) {
							OOrderproductdetails det=new OOrderproductdetails();
							det.setOrderproductid(orderProduct.getOrderproductid());
							det.setBackimageurl(dd.getBackimageurl());
							det.setCreatetime(new Date());
							det.setImageurl(dd.getImageurl());
							det.setPosition(dd.getPosition());
							det.setPrintno(dd.getPrintno());
							odetailMapper.insert(det);
						}
						userOrdersMapper.insert(userOrder_Repeat);//插入订单
						oproductMapper.insert(orderProduct);//插入订单产品
						
						rq.setStatu(ReturnStatus.Success);
						// 获取产品信息
						mapResult.put("productId", product.getProductid());
						mapResult.put("styleId", style.getStyleid());
						mapResult.put("isRepeat", 1);//是重新下单
						mapResult.put("totalPrice", totalPrice);
						if(userOrder_Repeat.getStatus()!=null&&userOrder_Repeat.getStatus().intValue()==Integer.parseInt(OrderStatusEnum.waitFoSend.toString())){
							mapResult.put("payed", 1);
						}else {
							mapResult.put("payed", 0);
						}
						rq.setBasemodle(mapResult);
						return rq;
					}
				}else {
					rq.setStatusreson("订单作品缺失！"); 
				}
			}
			rq.setStatusreson("订单信息有误！");
		}else { 
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("重新下单失败（此功能只支持之前已完成下单的作品）"); 
		}
		
		return rq;
	}
	
	/**
	 * 检测订单参数 （订单提交 检验）
	 * @param param
	 * @return
	 * @throws Exception
	 */
	private ReturnModel checkOrderParam(UserOrderSubmitParam param)throws Exception{ 
		ReturnModel rq = new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		if(param==null||param.getUserId()==null||param.getOrderproducts()==null){
			rq.setStatusreson("参数有误");
			return rq;
		}
		PProductstyles style=null;
		if(param.getOrderproducts().getProductid()!=null&&param.getOrderproducts().getStyleid()!=null){
			 style=styleMapper.selectByPrimaryKey(param.getOrderproducts().getStyleid());
			 if(style.getAgentprice()==null)
				 style.setAgentprice(style.getPrice()); 
		}else {
			rq.setStatusreson("产品不存在");
			return rq;
		}
		PMyproducts mycart= myproductMapper.selectByPrimaryKey(param.getCartId());
		if(mycart==null){
			rq.setStatusreson("作品不存在");
			return rq;
		}
		int count= param.getOrderproducts().getCount()==null?1:param.getOrderproducts().getCount();
		int orderType=param.getOrderType()==null?0:param.getOrderType();
		if(orderType==Integer.parseInt(OrderTypeEnum.brachOrder.toString())){//影楼订单
			UUsers users=usersMapper.selectByPrimaryKey(param.getUserId());
			if(users!=null){
				if(orderType==Integer.parseInt(OrderTypeEnum.brachOrder.toString())){//影楼订单
					UBranchusers branchusers= branchusersMapper.selectByPrimaryKey(param.getUserId());
					if(branchusers!=null&&branchusers.getBranchuserid()!=null) {
						UBranches branches=branchesMapper.selectByPrimaryKey(branchusers.getBranchuserid());
						if(branches!=null&&branches.getStatus()!=null&&branches.getStatus().intValue()==Integer.parseInt(BranchStatusEnum.ok.toString())){
							double totalprice=style.getAgentprice()*count;
							UAccounts accounts= accountsMapper.selectByPrimaryKey(branches.getBranchuserid());
							if(accounts!=null&&accounts.getAvailableamount()!=null&&accounts.getAvailableamount()>=totalprice){
								 //影楼的运费款 查询
								 UBranchtransaccounts transAccount= transMapper.selectByPrimaryKey(branches.getBranchuserid());
								 if(transAccount==null||transAccount.getAvailableamount()==null||transAccount.getAvailableamount()<=0){
									 rq.setStatu(ReturnStatus.CashError);
									 rq.setStatusreson("影楼的运费款不足，无法下单！请通知管理员进行充值，以免影响您的业务！ ");
									 return rq;
								 }
								 rq.setStatu(ReturnStatus.Success);
								 param.setBranchUserId(branches.getBranchuserid());
								 param.setAgentUserId(branches.getAgentuserid()); 
								 rq.setBasemodle(param); 
								 return rq;
							}else {
								rq.setStatu(ReturnStatus.CashError); 
								rq.setStatusreson("影楼预存货款不足，无法下单！请通知管理员进行充值，以免影响您的业务！");
								return rq;
							}
						}else {
							rq.setStatusreson("不是有效的合作伙伴！");
							return rq;
						}
					}else {
						rq.setStatusreson("权限不足（如果自己是代理商/影楼管理员，请在ibs管理平台将自己添加为内部账户！）");
						return rq;
					}
				}
			}
		}else {//普通订单
			UUseraddress addr = addressMapper.get_UUserAddressByKeyId(param.getAddrId());//用户收货地址
			if(addr!=null){
				/*-----------------------------过滤不能下单的区域--------------------------------------------------*/
				List<Map<String, String>> exmapList= ConfigUtil.getMaplist("o_excludedareas");
				if(exmapList!=null&&exmapList.size()>0){
					for (Map<String, String> map : exmapList) {
						 if("1".equals(map.get("type"))){
							 String aress=map.get("codes");
							 if(aress.contains(addr.getProvince().toString())){
								 rq.setStatusreson(regionService.getName(addr.getProvince())+"暂时不支持配送！"); 
								 return rq;
							 }
						 }else if ("2".equals(map.get("type"))) {
							 if(map.get("codes").contains(addr.getCity().toString())){
								 rq.setStatusreson(regionService.getName(addr.getCity())+"暂时不支持配送！"); 
								 return rq;
							 }
						 }else if ("3".equals(map.get("type"))) {
							 if(map.get("codes").contains(addr.getArea().toString())){
								 rq.setStatusreson(regionService.getName(addr.getArea())+"暂时不支持配送！"); 
								 return rq;
							 }
						 }
					}
				}
			}else {
				rq.setStatusreson("收货地址不存在！");
				return rq;
			}
			/*---------------------通过快递方式查询邮费---------------------------------------------*/
			if(param.getPostModelId()==null||param.getPostModelId()<=0){
				List<PPostmodel> listpost= postMgtService.find_postlist(addr.getArea());
				if(listpost!=null&&listpost.size()>0){
					param.setPostModelId(listpost.get(0).getPostmodelid());
					param.setPostPrice(listpost.get(0).getAmount());
				}
			}else {
				PPostmodel postmodel= postMgtService.getPostmodel(param.getPostModelId(), addr.getArea());
				if(postmodel!=null){
					param.setPostPrice(postmodel.getAmount());
				}else {
					rq.setStatu(ReturnStatus.SystemError);
					rq.setStatusreson("快递方式不存在！");
					return rq;
				}
			}
			/*-------------------------快递（完）------------------------------------------*/
			
			/*-------------------------区域代理分配---------------------------------------------------*/
			RAreaplans areaplans= areaplansMapper.selectByPrimaryKey(addr.getArea());
			if(areaplans!=null&&areaplans.getAgentuserid()!=null&&areaplans.getAgentuserid()>0){
				UAgents agent= agentsMapper.selectByPrimaryKey(areaplans.getAgentuserid());
				if(agent!=null&&agent.getStatus()!=null&&agent.getStatus()==Integer.parseInt(AgentStatusEnum.ok.toString())){
					param.setAgentUserId(agent.getAgentuserid());
				}
			}
			rq.setStatu(ReturnStatus.Success); 
			rq.setBasemodle(param);
			return rq;
		}
		rq.setStatusreson("参数有误");
		return rq;
	}
	
	
	
	
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
					userOrder.setOrdertype(Integer.parseInt(OrderTypeEnum.nomal.toString())); 
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
								if(!ObjectUtil.isEmpty(styles.getDefaultimg())){
									pp.setProductimg(styles.getDefaultimg()); 
								}else {
									pp.setProductimg(products.getDefaultimg()); 
								}
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
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson(e.getMessage());
		}
		return rq;
	}

	/**
	 * 去支付（通过订单id获取支付信息）
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
						OPayorder payNew = getPayOrderNew(order);
						if(payNew!=null){
							userorder.setPayid(payNew.getPayid()); 
							userOrdersMapper.updateByPrimaryKeySelective(userorder);
						}
						List<OOrderproducts> proList = oproductMapper.findOProductsByOrderId(orderId);
						if (proList != null && proList.size() > 0) {
							Map<String, Object> mapResult = new HashMap<String, Object>();
							mapResult.put("payId", userorder.getPayid());
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

	/**
	 * 检验微信支付是否过期 ，如果微信PrepayId过期，重新生成支付单
	 * 未过期返回null
	 * @param payorder
	 * @return
	 */
	public OPayorder getPayOrderNew(OPayorder payorder){
		if(payorder!=null){
			int ordertype=payorder.getOrdertype()==null?Integer.parseInt(PayOrderTypeEnum.gouwu.toString()):payorder.getOrdertype();
			if(ordertype==Integer.parseInt(PayOrderTypeEnum.gouwu.toString())){
				if(payorder.getPrepaytime()!=null){
					 Date prepayTime=payorder.getPrepaytime();
					 Calendar prepayCalendar = Calendar.getInstance();
					 prepayCalendar.setTime(prepayTime);
					 prepayCalendar.add(Calendar.MINUTE, 100);
					 Date preNew=prepayCalendar.getTime();
					 Date nowtime=new Date();
					 if(preNew.getTime()<nowtime.getTime()){//预付单过期
						 OPayorder payorderNew=new OPayorder();
						 payorderNew.setPayid(GenUtils.getOrderNo(payorder.getUserid())); 
						 payorderNew.setUserorderid(payorder.getUserorderid());
						 payorderNew.setUserid(payorder.getUserid());
						 payorderNew.setPaytype(payorder.getPaytype());
						 payorderNew.setTotalprice(payorder.getTotalprice());
						 payorderNew.setStatus(payorder.getStatus());
						 payorderNew.setOrdertype(payorder.getOrdertype());
						 payorderNew.setCreatetime(new Date());
						 payOrderMapper.insert(payorderNew);
						 return payorderNew;
					 }
					 System.out.println(DateUtil.getTimeStr(preNew, "yyyy-MM-dd HH:mm:ss")); 
				}
			}
		}
		return null;
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
					userorders.setUploadtime(new Date()); 
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
				model.setPayId(oo.getPayid());
				model.setTotalprice(oo.getTotalprice());
				model.setStatus(oo.getStatus());
				model.setBranchUserId(oo.getBranchuserid());
				model.setRemark(oo.getRemark());
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
	
	/**
	 * 获取用户购买订单列表
	 * @param userId
	 * @return
	 */
	public ReturnModel findUserOrderlist(Long userId,int index,int size) {
		ReturnModel rq = new ReturnModel();
		PageHelper.startPage(index, size);
		List<UserBuyerOrderResult> userorderslist = userOrdersMapper.findUserOrderByUserId(userId);
		PageInfo<UserBuyerOrderResult> resultPage=new PageInfo<UserBuyerOrderResult>(userorderslist); 
		if(resultPage!=null&&resultPage.getList()!=null&&resultPage.getList().size()>0){
			for (UserBuyerOrderResult oo : resultPage.getList()) {
				if(oo.getOrdertime()!=null)
					oo.setOrdertimestr(DateUtil.getTimeFormatText(oo.getOrdertime()));
				if(oo.getPaytime()!=null)
					oo.setPaytimestr(DateUtil.getTimeStr(oo.getPaytime(), "yyyy-MM-dd"));
				if(oo.getUploadtime()!=null)
					oo.setUploadtimestr(DateUtil.getTimeStr(oo.getUploadtime(), "yyyy-MM-dd"));
				List<OOrderproducts> proList = oproductMapper.findOProductsByOrderId(oo.getUserorderid());
				if(proList!=null&&proList.size()>0){
					OOrderproducts product=proList.get(0);
					long temp=product.getStyleid()%2;
					oo.setShowType((int)temp); 
					if(product.getProductimg()==null||product.getProductimg().equals("")){
						product.setProductimg("http://pic.bbyiya.com/484983733454448354.png");
					}
					oo.setProduct(proList.get(0));
					PMyproducts myproduct= myproductMapper.selectByPrimaryKey(proList.get(0).getCartid());
					if(myproduct!=null){
						oo.setWorkTitle(myproduct.getTitle()); 
					}
				}
			}
			
		}
		rq.setBasemodle(resultPage);
		rq.setStatu(ReturnStatus.Success);
		return rq;
	}
	
	
	public ReturnModel getOrderInfo(Long userId, String orderId) {
		ReturnModel rq=new ReturnModel();
		OUserorders orderInfo=userOrdersMapper.selectByPrimaryKey(orderId);
		if(orderInfo!=null){
			UserOrderResult model = new UserOrderResult();
			model.setUserOrderId(orderInfo.getUserorderid());
			model.setTotalprice(orderInfo.getTotalprice());
			model.setOrderTotalPrice(orderInfo.getOrdertotalprice());
			model.setPostage(orderInfo.getPostage()==null?0d:orderInfo.getPostage()); 
			model.setStatus(orderInfo.getStatus());			
			model.setOrderTimeStr(DateUtil.getTimeStr(orderInfo.getOrdertime(), "yyyy-MM-dd"));
			if(!ObjectUtil.isEmpty(orderInfo.getPaytime())){ 
				model.setPayTimeStr(DateUtil.getTimeStr(orderInfo.getPaytime(), "yyyy-MM-dd HH:mm:ss")); 
			}
			model.setPayType(orderInfo.getPaytype()); 
			List<OOrderproducts> proList = oproductMapper.findOProductsByOrderId(orderInfo.getUserorderid());
			model.setProlist(proList);
			OOrderaddress addr= orderaddressMapper.selectByPrimaryKey(orderInfo.getOrderaddressid());
			if(addr!=null){
				model.setOrderAddress(addr);
			}
			if(proList!=null&&proList.size()>0){
				model.setCartId(proList.get(0).getCartid()); 
			}
			if(!ObjectUtil.isEmpty(orderInfo.getExpresscom())){
				model.setExpressName(orderInfo.getExpresscom());
			}
			if(!ObjectUtil.isEmpty(orderInfo.getExpressorder())){
				model.setExpressOrderNo(orderInfo.getExpressorder());
			}
//			PMyproducts myproduct= myproductMapper.getMyProductByOrderNo(orderId);
//			if(myproduct!=null){
//				model.setCartId(myproduct.getCartid()); 
//			}
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
	
	public boolean addPayOrder(Long userId, String payId, String userOrderId, Integer payOrderType , Double totalPrice) {
		OPayorder payorder = new OPayorder();
		payorder.setPayid(payId);
		payorder.setUserorderid(userOrderId);
		payorder.setUserid(userId);
		payorder.setStatus(Integer.parseInt(OrderStatusEnum.noPay.toString()));
		payorder.setOrdertype(payOrderType); 
		payorder.setTotalprice(totalPrice);
		payorder.setCreatetime(new Date());
		payOrderMapper.insert(payorder);
		return true;
	}
	
	/**
	 * // 影楼订单，直接预存款支付 ， 插入支付记录
	 * @param userId
	 * @param payId
	 * @param userOrderId
	 * @param totalPrice
	 * @return
	 * @throws Exception
	 */
	public boolean payOrder_logAdd(Long userId, String payId, String userOrderId, Double totalPrice) throws Exception{
		UAccounts accounts=accountsMapper.selectByPrimaryKey(userId);
		if(accounts!=null&&accounts.getAvailableamount()!=null&&accounts.getAvailableamount().doubleValue()>=totalPrice.doubleValue()){
						
			OPayorder payorder = new OPayorder();
			payorder.setPayid(payId);
			payorder.setUserorderid(userOrderId);
			payorder.setUserid(userId);
			payorder.setStatus(Integer.parseInt(OrderStatusEnum.payed.toString()));
			payorder.setPaytype(Integer.parseInt(PayTypeEnum.yiyaCash.toString()));
			payorder.setPaytime(new Date()); 
			payorder.setTotalprice(totalPrice);
			payorder.setCreatetime(new Date());
			payOrderMapper.insert(payorder);
			//消费记录
			UCashlogs cashLog=new UCashlogs();
			cashLog.setAmount(-1*totalPrice);
			cashLog.setPayid(payId);
			cashLog.setUserid(userId);
			cashLog.setUsetype(1);//购物
			cashLog.setCreatetime(new Date());
			cashlogsMapper.insert(cashLog);
			
			accounts.setAvailableamount(accounts.getAvailableamount()-totalPrice);
			accountsMapper.updateByPrimaryKeySelective(accounts); 
			return true;
		}
		return false;
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
					pp.setPropertystr(styles.getPropertystr());// 款式 getStylePropertyStr(pp.getStyleid())
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
	 * 获取订单收货地址
	 * @param userId
	 * @param orderType
	 * @return
	 */
	private long getOrderAddressIdByBranchUserId(Long userId,int orderType) {
		UUsers users=usersMapper.selectByPrimaryKey(userId);
		if(users!=null){
			if(orderType==Integer.parseInt(OrderTypeEnum.brachOrder.toString())){//影楼订单
				UBranchusers branchusers= branchusersMapper.selectByPrimaryKey(userId);
				if(branchusers!=null&&branchusers.getBranchuserid()!=null){
					UBranches branches=branchesMapper.selectByPrimaryKey(branchusers.getBranchuserid());
					if (branches != null) {
						OOrderaddress orderAddress = new OOrderaddress();
						orderAddress.setUserid(branches.getBranchuserid());
						orderAddress.setPhone(branches.getPhone());
						orderAddress.setReciver(branches.getUsername());
						orderAddress.setCity(regionService.getName(branches.getCity()));
						orderAddress.setProvince(regionService.getName(branches.getProvince()));
						orderAddress.setDistrict(regionService.getName(branches.getArea()));
						orderAddress.setStreetdetail(branches.getStreetdetail());
						orderAddress.setCreatetime(new Date());
						orderaddressMapper.insertReturnId(orderAddress);
						return orderAddress.getOrderaddressid();
					}
				}
			}
		}
		return 0;
	}


	
}
