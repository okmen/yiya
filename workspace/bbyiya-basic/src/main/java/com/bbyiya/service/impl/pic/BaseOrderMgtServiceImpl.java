package com.bbyiya.service.impl.pic;

import java.text.DecimalFormat;
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
import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.dao.EErrorsMapper;
import com.bbyiya.dao.OOrderaddressMapper;
import com.bbyiya.dao.OOrderproductdetailsMapper;
import com.bbyiya.dao.OOrderproductphotosMapper;
import com.bbyiya.dao.OOrderproductsMapper;
//import com.bbyiya.dao.OPayforuserorderMapper;
import com.bbyiya.dao.OPayorderMapper;
import com.bbyiya.dao.OPayorderwalletdetailsMapper;
import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.PMyproductdetailsMapper;
import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.PMyproductsinvitesMapper;
import com.bbyiya.dao.PMyproducttempMapper;
import com.bbyiya.dao.PMyproducttempapplyMapper;
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
import com.bbyiya.enums.AccountLogType;
import com.bbyiya.enums.CustomerSourceTypeEnum;
import com.bbyiya.enums.OrderStatusEnum;
import com.bbyiya.enums.OrderTypeEnum;
import com.bbyiya.enums.PayOrderTypeEnum;
import com.bbyiya.enums.PayTypeEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.AgentStatusEnum;
import com.bbyiya.enums.pic.BranchStatusEnum;
import com.bbyiya.enums.pic.MyProductStatusEnum;
import com.bbyiya.enums.pic.MyProducttempApplyStatusEnum;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.model.DMyproductdiscountmodel;
import com.bbyiya.model.EErrors;
import com.bbyiya.model.OOrderaddress;
import com.bbyiya.model.OOrderproductdetails;
import com.bbyiya.model.OOrderproductphotos;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.model.OPayorder;
import com.bbyiya.model.OPayorderwalletdetails;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.PMyproductdetails;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PMyproductsinvites;
import com.bbyiya.model.PMyproducttemp;
import com.bbyiya.model.PMyproducttempapply;
import com.bbyiya.model.PPostmodel;
import com.bbyiya.model.PProducts;
import com.bbyiya.model.PProductstyles;
import com.bbyiya.model.RAreaplans;
import com.bbyiya.model.UAccounts;
import com.bbyiya.model.UAgentcustomers;
import com.bbyiya.model.UAgents;
import com.bbyiya.model.UBranches;
import com.bbyiya.model.UBranchusers;
import com.bbyiya.model.UUseraddress;
import com.bbyiya.model.UUsers;
import com.bbyiya.service.IBasePayService;
import com.bbyiya.service.IBaseUserAccountService;
import com.bbyiya.service.IRegionService;
import com.bbyiya.service.pic.IBaseDiscountService;
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
import com.bbyiya.vo.user.LoginSuccessResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("baseOrderMgtServiceImpl")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class BaseOrderMgtServiceImpl implements IBaseOrderMgtService {

	@Resource(name = "regionServiceImpl")
	private IRegionService regionService;
	
	@Resource(name = "basePostMgtServiceImpl")
	private IBasePostMgtService postMgtService;
	
	@Resource(name = "basePayServiceImpl")
	private IBasePayService basePayService;
	
	@Resource(name = "baseUserAccountService")
	private IBaseUserAccountService accountService;
	
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
	@Autowired
	private PMyproductsinvitesMapper myinviteMapper;//我的作品
	@Autowired
	private PMyproductdetailsMapper mydetailMapper;

	// --------------------订单模块注解--------------------------------------
	@Autowired
	private OOrderaddressMapper orderaddressMapper;// 订单收货地址
	@Autowired
	private OOrderproductsMapper oproductMapper;// 订单产品
	@Autowired
	private OOrderproductphotosMapper ophotoMapper;// 订单产品
	
	@Autowired
	private OUserordersMapper userOrdersMapper;// 订单
	@Autowired
	private OPayorderMapper payOrderMapper;// 支付单
	@Autowired
	private OOrderproductdetailsMapper odetailMapper;// 产品图片集合
	
	@Autowired
	private OPayorderwalletdetailsMapper walletDetailMapper;
	

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
	
	@Autowired
	private PMyproducttempapplyMapper tempApplyMapper;
	@Autowired
	private PMyproducttempMapper tempMapper;
	
	/*---错误日志记录------------------------------------------*/
	@Autowired
	private EErrorsMapper logMapper;
	
	@Autowired
	private UBranchtransaccountsMapper transMapper;
	@Resource(name = "baseDiscountServiceImpl")
	private IBaseDiscountService discountService;
	@Resource(name = "basePayServiceImpl")
	private IBasePayService orderMgtService;

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
		userOrder.setPaytime(ordertime);
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
			
			
			//从作品中取数插入o_orderproductphotos 获取此刻作品原图及文字信息
			List<PMyproductdetails> myproductdetailsList=mydetailMapper.findMyProductdetails(param.getCartId());
			if(myproductdetailsList!=null){
				String base_code = param.getUserId() + "-" + param.getCartId();
				int i = 1;
				for (PMyproductdetails detail : myproductdetailsList) {
					OOrderproductphotos photos=new OOrderproductphotos();
					photos.setContent(detail.getContent());
					photos.setCreatetime(detail.getCreatetime());
					photos.setImgurl(detail.getImgurl());
					photos.setOrderproductid(orderId);
					photos.setPrintno(base_code + "-" + String.format("%02d", i));
					photos.setSenendes(detail.getDescription());
					photos.setSort(detail.getSort());
					photos.setTitle(detail.getTitle());
					ophotoMapper.insert(photos);
					i++;
				}
			}
			
			
			if (orderType == Integer.parseInt(OrderTypeEnum.brachOrder.toString())) {
				//邮费 b端下单不录入邮费
				userOrder.setPostage(0d); 
				UAccounts accounts= accountsMapper.selectByPrimaryKey(param.getBranchUserId());
				//double payprice=totalPrice/3;
				double payprice=styles.getAgentprice() * orderProduct.getCount();
				DecimalFormat    df   = new DecimalFormat("######0.00"); 
				payprice=Double.parseDouble(df.format(payprice));
				double walletAmount=0;//需扣减红包金额，默认为0
				double cashAmount=payprice;//需支付的金额金额 默认为需支付总金额
				
				//Add at 2017-07-21 by julie判断是否是红包众筹活动的订单，如果是则需要扣减红包金额
				ReturnModel redresult=redPacketsOrderOpt(param.getCartId(),payprice,orderId);
				if(redresult.getStatu()==ReturnStatus.Success){
					HashMap<String, Double> map=(HashMap<String, Double>) redresult.getBasemodle();
					walletAmount=map.get("walletAmount");
					cashAmount=map.get("cashAmount");
				}else{
					rq.setStatu(ReturnStatus.CashError);
					rq.setStatusreson("冻结金额余额不足！");
					return rq;
				}
				
				if(accounts!=null&&accounts.getAvailableamount()!=null&&accounts.getAvailableamount().doubleValue()>=cashAmount) {
					// 影楼订单，直接预存款支付 ， 插入支付记录
					if(payOrder_logAdd(param.getBranchUserId(),orderId,orderId,payprice,walletAmount,cashAmount)){
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
				
				//是否优惠购买
				if(userOrder.getUserid()!=null&&param.getCartId()!=null){
					List<DMyproductdiscountmodel> dislit=discountService.findMycartDiscount(userOrder.getUserid(), param.getCartId());
					if(dislit!=null&&dislit.size()>0){
						for (DMyproductdiscountmodel dis : dislit) {
							if(styles.getStyleid().longValue()==dis.getStyleid().longValue()){
								//使用优惠
								if(discountService.useMycartDiscount(userOrder.getUserid(), param.getCartId())){
									orderTotalPrice=orderTotalPrice-dis.getAmount();
									userOrder.setOrdertotalprice(orderTotalPrice); 
								}
							}
						}
					}
				}
				Double walletAmount=0.0;
				//add at 2017-07-21 by julie得到可减免的红包金额转移到冻结账户
				walletAmount=accountService.transferCashAccountsToFreeze(userOrder.getUserid(),orderTotalPrice);
				// 插入支付订单记录
				addPayOrder(param.getUserId(), payId, payId, orderTotalPrice,walletAmount);
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
							cus.setIsmarket(0);
							cus.setSourcetype(Integer.parseInt(CustomerSourceTypeEnum.order.toString()));
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
				//-----------------------异业合作---------------------------------------------------------
				if(Integer.parseInt(OrderTypeEnum.brachOrder.toString())==orderType && mycart.getTempid()!=null){
					PMyproducttempapply apply= tempApplyMapper.getMyProducttempApplyByCartId(param.getCartId());
					//if(apply!=null&&apply.getStatus()!=null&&apply.getStatus().intValue()==Integer.parseInt(MyProducttempApplyStatusEnum.complete.toString())){
						apply.setStatus(Integer.parseInt(MyProducttempApplyStatusEnum.pass.toString()));
						tempApplyMapper.updateByPrimaryKeySelective(apply);
					//}
				}//-----------------------------------------------------------------------------------
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
	
	public ReturnModel submitOrder_IBS(UserOrderSubmitParam param) {
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
				OOrderaddress orderAddress = new OOrderaddress();
				orderAddress.setUserid(param.getAddressparam().getUserid());
				orderAddress.setPhone(param.getAddressparam().getPhone());
				orderAddress.setReciver(param.getAddressparam().getReciver());				
				orderAddress.setCity(regionService.getCityName(param.getAddressparam().getCity()));
				orderAddress.setProvince(regionService.getProvinceName(param.getAddressparam().getProvince()));
				orderAddress.setDistrict(regionService.getAresName(param.getAddressparam().getDistrict()));
			    orderAddress.setStreetdetail(param.getAddressparam().getStreetdetail());
				orderAddress.setCreatetime(new Date());
				orderaddressMapper.insertReturnId(orderAddress);
				orderAddressId=orderAddress.getOrderaddressid();
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
				//List<OOrderproductdetails> details = odetailMapper.findOrderProductDetailsByProductOrderId(oproduct.getOrderproductid());
				List<OOrderproductphotos> photos=ophotoMapper.findOrderProductPhotosByProductOrderId(oproduct.getOrderproductid());
				if (photos != null && photos.size() > 0) {
					/*------------------------影楼内部订单------------------------------------------------*/
					if ( param.getOrderType() == Integer.parseInt(OrderTypeEnum.brachOrder.toString())) {
						// 影楼金额是否足够
						UBranchusers branchusers = branchusersMapper.selectByPrimaryKey(userId);
						if (branchusers != null && branchusers.getBranchuserid() != null) {
							UBranches branches = branchesMapper.selectByPrimaryKey(branchusers.getBranchuserid());
							if (branches != null && branches.getStatus() != null && branches.getStatus().intValue() == Integer.parseInt(BranchStatusEnum.ok.toString())) {
								param.setBranchUserId(branches.getBranchuserid());
								param.setAgentUserId(branches.getAgentuserid());
								//double totalprice=(style.getPrice()* param.getCount())/3;
								double totalprice = style.getAgentprice() * param.getCount();
								
								//*************add by julie at 2017-07-24****************************/
								Double walletAmount=0.0;
								Double cashAmount=totalprice;//再需支付的现金金额
								
								PMyproducts mycart= myproductMapper.selectByPrimaryKey(oproduct.getCartid());
								if(mycart!=null&&mycart.getTempid()!=null){					
									PMyproducttemp temp=tempMapper.selectByPrimaryKey(mycart.getTempid());
									PMyproducttempapply tempapply=tempApplyMapper.getMyProducttempApplyByCartId(oproduct.getCartid());
									List<PMyproductsinvites> inviteslist=myinviteMapper.findListByCartId(oproduct.getCartid());
									Long inviteuserid=null;
									if(inviteslist!=null&&inviteslist.size()>0){
										inviteuserid=inviteslist.get(0).getInviteuserid();
									}
									if(inviteuserid!=null&&inviteuserid.longValue()>0&&tempapply!=null&&tempapply.getStatus().intValue()==Integer.parseInt(MyProducttempApplyStatusEnum.complete.toString())&&temp!=null&&temp.getAmountlimit()!=null&&temp.getAmountlimit()>0){
										walletAmount=temp.getAmountlimit();
										cashAmount=totalprice-walletAmount;
									}
								}
								//*************End****************************/
								UAccounts accounts = accountsMapper.selectByPrimaryKey(branches.getBranchuserid());
								if(accounts==null||accounts.getAvailableamount() == null|| accounts.getAvailableamount() <cashAmount){
									rq.setStatu(ReturnStatus.CashError);
									rq.setStatusreson("影楼账户余额不足，无法下单！请通知管理员进行充值，以免影响您的业务！");
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
							double payprice=style.getAgentprice() * param.getCount();//totalPrice/3;
							DecimalFormat    df   = new DecimalFormat("######0.00"); 
							payprice=Double.parseDouble(df.format(payprice));
							double walletAmount=0L;//需扣减红包金额，默认为0
							double cashAmount=payprice;//需支付的金额金额 默认为需支付总金额
							
							//Add at 2017-07-21 by julie 判断是否是红包众筹活动的订单，如果是则需要扣减红包金额
							ReturnModel redresult=redPacketsOrderOpt(oproduct.getCartid(),payprice,orderId);
							if(redresult.getStatu()==ReturnStatus.Success){
								HashMap<String, Double> map=(HashMap<String, Double>) redresult.getBasemodle();
								walletAmount=map.get("walletAmount");
								cashAmount=map.get("cashAmount");
							}else{
								rq.setStatu(ReturnStatus.CashError);
								rq.setStatusreson("冻结金额余额不足！");
								return rq;
							}

							if (accounts != null && accounts.getAvailableamount() != null && accounts.getAvailableamount().doubleValue() >= cashAmount) {
								// 影楼订单，直接预存款支付 ， 插入支付记录
								if (payOrder_logAdd(param.getBranchUserId(), orderId, orderId, cashAmount,walletAmount,cashAmount)) {
									
									userOrder_Repeat.setStatus(Integer.parseInt(OrderStatusEnum.payed.toString()));
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
							
							//add at 2017-07-21 by julie 得到可支付的红包金额转移到冻结金额
							Double walletAmount=accountService.transferCashAccountsToFreeze(userId,orderTotalPrice);
							
							addPayOrder(userId, payId, payId, orderTotalPrice,walletAmount); // 插入支付订单记录
						}
						
						for (OOrderproductphotos ph : photos) {
							OOrderproductphotos phpt=new OOrderproductphotos();
							phpt.setContent(ph.getContent());
							phpt.setCreatetime(new Date());
							phpt.setImgurl(ph.getImgurl());
							phpt.setOrderproductid(orderProduct.getOrderproductid());
							phpt.setPrintno(ph.getPrintno());
							phpt.setSenendes(ph.getSenendes());
							phpt.setSort(ph.getSort());
							phpt.setTitle(ph.getTitle());
							ophotoMapper.insert(phpt);
							
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
		List<PMyproductdetails> detailslist=mydetailMapper.findMyProductdetails(param.getCartId());
		if(detailslist==null){
			rq.setStatusreson("作品还没制作完成，不能下单");
			return rq;
		}else{
			int i=0;
			for (PMyproductdetails dd : detailslist) {
				if (!ObjectUtil.isEmpty(dd.getImgurl())) {
					i++;
				}
			}
			if(i<12){
				rq.setStatusreson("作品还没制作完成，请完成后再下单");
				return rq;
			}
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
							double totalprice=(style.getAgentprice()*count);
							//double totalprice=(style.getPrice()*count)/3;
							//*************add by julie at 2017-07-24****************************/
							Double walletAmount=0.0;
							Double cashAmount=totalprice;//再需支付的现金金额
							if(mycart!=null&&mycart.getTempid()!=null){					
								PMyproducttemp temp=tempMapper.selectByPrimaryKey(mycart.getTempid());
								PMyproducttempapply tempapply=tempApplyMapper.getMyProducttempApplyByCartId(param.getCartId());
								List<PMyproductsinvites> inviteslist=myinviteMapper.findListByCartId(param.getCartId());
								Long inviteuserid=null;
								if(inviteslist!=null&&inviteslist.size()>0){
									inviteuserid=inviteslist.get(0).getInviteuserid();
								}
								if(inviteuserid!=null&&inviteuserid.longValue()>0&&tempapply!=null&&tempapply.getStatus().intValue()==Integer.parseInt(MyProducttempApplyStatusEnum.complete.toString())&&temp!=null&&temp.getAmountlimit()!=null&&temp.getAmountlimit()>0){
									walletAmount=temp.getAmountlimit();
									cashAmount=totalprice-walletAmount;
								}
							}
							//*************End****************************/
							
							UAccounts accounts= accountsMapper.selectByPrimaryKey(branches.getBranchuserid());
							if(accounts!=null&&accounts.getAvailableamount()!=null&&accounts.getAvailableamount().doubleValue()>=cashAmount){
								 rq.setStatu(ReturnStatus.Success);
								 param.setBranchUserId(branches.getBranchuserid());
								 param.setAgentUserId(branches.getAgentuserid()); 
								 rq.setBasemodle(param); 
								 return rq;
							}else {
								rq.setStatu(ReturnStatus.CashError); 
								rq.setStatusreson("影楼账户余额不足，无法下单！请通知管理员进行充值，以免影响您的业务！");
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
							 if(aress.contains(String.valueOf(addr.getProvince()))){
								 rq.setStatusreson(regionService.getProvinceName(addr.getProvince())+"暂时不支持配送！"); 
								 return rq;
							 }
						 }else if ("2".equals(map.get("type"))) {
							 if(map.get("codes").contains(String.valueOf(addr.getCity()))){
								 rq.setStatusreson(regionService.getCityName(addr.getCity())+"暂时不支持配送！"); 
								 return rq;
							 }
						 }else if ("3".equals(map.get("type"))) {
							 if(map.get("codes").contains(String.valueOf(addr.getArea()))){
								 rq.setStatusreson(regionService.getAresName(addr.getArea())+"暂时不支持配送！"); 
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
			if(addr.getArea()!=null){
				RAreaplans areaplans= areaplansMapper.selectByPrimaryKey(addr.getArea());
				if(areaplans!=null&&areaplans.getAgentuserid()!=null&&areaplans.getAgentuserid()>0){
					UAgents agent= agentsMapper.selectByPrimaryKey(areaplans.getAgentuserid());
					if(agent!=null&&agent.getStatus()!=null&&agent.getStatus()==Integer.parseInt(AgentStatusEnum.ok.toString())){
						param.setAgentUserId(agent.getAgentuserid());
					}
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
				addPayOrder(userId, payId, payId, totalPrice_pay,0.0); // 插入支付订单记录
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
						OPayorder payNew = getPayOrderNew(order,userorder.getOrdertotalprice());
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
	public OPayorder getPayOrderNew(OPayorder payorder,Double orderTotalPrice){
		if(payorder!=null){
			int ordertype=payorder.getOrdertype()==null?Integer.parseInt(PayOrderTypeEnum.gouwu.toString()):payorder.getOrdertype();
			if(ordertype==Integer.parseInt(PayOrderTypeEnum.gouwu.toString())){
				boolean isNew=false;
				if(payorder.getPrepaytime()!=null) {
					 Date prepayTime=payorder.getPrepaytime();
					 Calendar prepayCalendar = Calendar.getInstance();
					 prepayCalendar.setTime(prepayTime);
					 prepayCalendar.add(Calendar.MINUTE, 100);
					 Date preNew=prepayCalendar.getTime();
					 Date nowtime=new Date();
					 if(preNew.getTime()<nowtime.getTime()){
						 isNew=true;
					 }
				}
				if((orderTotalPrice==null||payorder.getTotalprice()==null||orderTotalPrice.doubleValue()!=payorder.getTotalprice().doubleValue())){//预付单过期
					isNew =true;
				}
				if(isNew){
					OPayorder payorderNew = new OPayorder();
					payorderNew.setPayid(GenUtils.getOrderNo(payorder.getUserid()));
					payorderNew.setUserorderid(payorder.getUserorderid());
					payorderNew.setUserid(payorder.getUserid());
					payorderNew.setPaytype(payorder.getPaytype());
					payorderNew.setTotalprice(orderTotalPrice);
					payorderNew.setWalletamount(payorder.getWalletamount()); 
					payorderNew.setCashamount(orderTotalPrice-(payorder.getWalletamount()==null?0d:payorder.getWalletamount().doubleValue()));
					payorderNew.setStatus(payorder.getStatus()); 
					payorderNew.setOrdertype(payorder.getOrdertype());
					payorderNew.setCreatetime(new Date());
					payOrderMapper.insert(payorderNew);
					return payorderNew;
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
			if(!ObjectUtil.isEmpty(orderInfo.getPayid())){
				OPayorder payorder=payOrderMapper.selectByPrimaryKey(orderInfo.getPayid());
				if(payorder!=null){
					//Ordertotalprice=totalprice+Postage-disAmount
					model.setCashAmount(payorder.getCashamount());
					model.setWalletAmount(payorder.getWalletamount());
					model.setDisAmount(orderInfo.getTotalprice()+(orderInfo.getPostage()==null?0d:orderInfo.getPostage())-orderInfo.getOrdertotalprice());				
					model.setPayType(payorder.getPaytype()); 
				}
			}
			
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
			if(!ObjectUtil.isEmpty(orderInfo.getExpresscode())){
				model.setExpressCode(orderInfo.getExpresscode());
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
	
	public ReturnModel getPayOrderInfo(Long userId, String userOrderId) {
		ReturnModel rq=new ReturnModel();
		OUserorders userorders=userOrdersMapper.selectByPrimaryKey(userOrderId);
		if(userorders!=null){
			OPayorder orderInfo=payOrderMapper.selectByPrimaryKey(userorders.getPayid());
			if(userorders.getStatus()!=null&&userorders.getStatus().intValue()==Integer.parseInt(OrderStatusEnum.noPay.toString())&&orderInfo!=null){
				OPayorder payNew=getPayOrderNew(orderInfo, userorders.getOrdertotalprice());
				if(payNew!=null){
					userorders.setPayid(payNew.getPayid()); 
					userOrdersMapper.updateByPrimaryKeySelective(userorders);
					orderInfo.setTotalprice(payNew.getTotalprice()); 
				}
				if(orderInfo.getStatus()!=null&&orderInfo.getStatus().intValue()==Integer.parseInt(OrderStatusEnum.noPay.toString())){
					Map<String, Object> map=new HashMap<String, Object>();
					double payPrice=orderInfo.getTotalprice().doubleValue()-(orderInfo.getWalletamount()==null?0d:orderInfo.getWalletamount().doubleValue());				
					if(payPrice<=0d){
						//钱包全额支付
						if(orderMgtService.paySuccessProcess(userorders.getPayid())){
							map.put("payed", 1); 
						}else {
							rq.setStatu(ReturnStatus.SystemError);
							rq.setStatusreson("支付失败");
							return rq;
						} 
					}else {
						map.put("payed", 0); 
					}
					map.put("payId", userorders.getPayid()); 
					map.put("totalPrice", orderInfo.getTotalprice()-(orderInfo.getWalletamount()==null?0d:orderInfo.getWalletamount().doubleValue()));
					
					rq.setStatu(ReturnStatus.Success);
					rq.setBasemodle(map);
				}else {
					rq.setStatu(ReturnStatus.SystemError);
					rq.setStatusreson("订单不在可支付的状态！");
				}
			}else { 
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("不存在此订单");
			}
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
	public void addPayOrder(Long userId, String payId, String userOrderId, Double totalPrice,Double walletAmount) {
		OPayorder payorder = new OPayorder();
		payorder.setPayid(payId);
		payorder.setUserorderid(userOrderId);
		payorder.setUserid(userId);
		payorder.setStatus(Integer.parseInt(OrderStatusEnum.noPay.toString()));
		payorder.setTotalprice(totalPrice);
		payorder.setWalletamount(walletAmount);
		payorder.setCashamount(totalPrice.doubleValue()-walletAmount.doubleValue());
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
	
	public ReturnModel addPayOrderForRedPackges(LoginSuccessResult user,Long cartId, String payId, String userOrderId, Integer payOrderType , Double totalPrice) {
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.SystemError);
		PMyproducts mycart=myproductMapper.selectByPrimaryKey(cartId);
		if(mycart!=null) {
			//作品拥有者
			UUsers myUsers=usersMapper.selectByPrimaryKey(mycart.getUserid());
			//红包付给谁
			Long userFor=0l;
			//作品是否是B端用户
			if(myUsers!=null&&!(ValidateUtils.isIdentity(myUsers.getIdentity(), UserIdentityEnums.salesman)||ValidateUtils.isIdentity(myUsers.getIdentity(), UserIdentityEnums.branch))){
				//如果不是B端用户 红包直接付给作品拥有者
				userFor=mycart.getUserid();
			}else {//如果作品属于B端，付给协同编辑者
				List<PMyproductsinvites> invitelist=myinviteMapper.findListByCartId(cartId);
				if(invitelist!=null&&invitelist.size()>0){
					if(invitelist.get(0).getInviteuserid()!=null){
						userFor=invitelist.get(0).getInviteuserid();
					}
				}
			}
			if(userFor>0){
				//生成支付记录
				OPayorder payorder = new OPayorder();
				payorder.setPayid(payId);
				payorder.setUserorderid(userOrderId);
				payorder.setUserid(user.getUserId());
				payorder.setStatus(Integer.parseInt(OrderStatusEnum.noPay.toString()));
				payorder.setOrdertype(payOrderType); 
				payorder.setTotalprice(totalPrice);
				payorder.setCreatetime(new Date());
				payOrderMapper.insert(payorder);	
				//插入红包记录
				OPayorderwalletdetails detail=new OPayorderwalletdetails();
				detail.setAmount(totalPrice);
				detail.setCartid(cartId);
				detail.setUserid(user.getUserId());
				detail.setHeadimg(user.getHeadImg());
				detail.setNickname(user.getNickName()); 
				detail.setForuserid(userFor);
				detail.setPayid(payId); 
				detail.setPaytime(new Date()); 
				detail.setStatus(Integer.parseInt(OrderStatusEnum.noPay.toString()));
				walletDetailMapper.insert(detail); 
				rqModel.setStatu(ReturnStatus.Success);
				rqModel.setBasemodle(payId); 
			}else {
				rqModel.setStatusreson("红包暂时发不了~~");
				addlog("红包暂时发不了~~");
				return rqModel;
			}
		}else {
			addlog("作品不存在，红包暂时发不了~~");
			rqModel.setStatusreson("红包暂时发不了~~");
			return rqModel;
		}
		return rqModel;
	}
	
	/**
	 * 判断是否是红包众筹活动的订单，如果是则需要扣减红包冻结金额
	 * @param cartId 作品ID
	 * @param payprice 需支付的订单总金额
	 * @param orderId  订单号
	 * @return
	 * @throws Exception
	 */
	public ReturnModel  redPacketsOrderOpt(Long cartId,Double payprice,String orderId) throws Exception{	
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.Success);
		Double walletAmount=0.0;
		Double cashAmount=payprice;
		PMyproducts mycart=myproductMapper.selectByPrimaryKey(cartId) ;
		if(mycart!=null&&mycart.getTempid()!=null){					
			PMyproducttemp temp=tempMapper.selectByPrimaryKey(mycart.getTempid());
			PMyproducttempapply tempapply=tempApplyMapper.getMyProducttempApplyByCartId(cartId);
			List<PMyproductsinvites> inviteslist=myinviteMapper.findListByCartId(cartId);
			Long inviteuserid=null;
			if(inviteslist!=null&&inviteslist.size()>0){
				inviteuserid=inviteslist.get(0).getInviteuserid();
			}
			if(inviteuserid!=null&&inviteuserid.longValue()>0&&tempapply!=null&&tempapply.getStatus().intValue()==Integer.parseInt(MyProducttempApplyStatusEnum.complete.toString())&&temp!=null&&temp.getAmountlimit()!=null&&temp.getAmountlimit()>0){
				walletAmount=temp.getAmountlimit();
				cashAmount=payprice-walletAmount;
				
				UAccounts accounts=accountsMapper.selectByPrimaryKey(inviteuserid);
				if(accounts!=null&&accounts.getFreezecashamount()!=null&&accounts.getFreezecashamount().doubleValue()>=walletAmount){
					//扣减用户红包金额,扣减用户的冻结金额
					accountService.add_FreezeCashAccountsLog(inviteuserid, Integer.parseInt(AccountLogType.use_payment.toString()), walletAmount, orderId, null);
				}else{
					rq.setStatu(ReturnStatus.SystemError);
					rq.setStatusreson("冻结金额余额不足！");
					return rq;
				}
			}
		}
		HashMap<String, Double> map=new HashMap<String, Double>();
		map.put("walletAmount", walletAmount);
		map.put("cashAmount", cashAmount);
		rq.setBasemodle(map);
		return rq;
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
	public boolean payOrder_logAdd(Long userId, String payId, String userOrderId, Double payprice,double walletamount,double cashamount) throws Exception{
		UAccounts accounts=accountsMapper.selectByPrimaryKey(userId);

		if(accounts!=null&&accounts.getAvailableamount()!=null&&accounts.getAvailableamount().doubleValue()>=payprice){
						
			OPayorder payorder = new OPayorder();
			payorder.setPayid(payId);
			payorder.setUserorderid(userOrderId);
			payorder.setUserid(userId);
			payorder.setStatus(Integer.parseInt(OrderStatusEnum.payed.toString()));
			payorder.setPaytype(Integer.parseInt(PayTypeEnum.yiyaCash.toString()));
			payorder.setPaytime(new Date()); 
			
			payorder.setTotalprice(payprice);
			payorder.setCashamount(cashamount);
			payorder.setWalletamount(walletamount);
			payorder.setCreatetime(new Date());
			payOrderMapper.insert(payorder);
			
			//消费扣减余额
			accountService.add_accountsLog(userId, Integer.parseInt(AccountLogType.use_payment.toString()), cashamount, payId, userOrderId);
			
			//订单完成后新增销量
			basePayService.addProductExt(userOrderId);
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
						orderAddress.setCity(regionService.getCityName(branches.getCity()));
						orderAddress.setProvince(regionService.getProvinceName(branches.getProvince()));
						orderAddress.setDistrict(regionService.getAresName(branches.getArea()));
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
