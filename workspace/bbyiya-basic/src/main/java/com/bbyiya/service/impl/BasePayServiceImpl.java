package com.bbyiya.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.common.enums.SendMsgEnums;
import com.bbyiya.common.vo.SmsParam;
import com.bbyiya.dao.EErrorsMapper;
import com.bbyiya.dao.OOrderaddressMapper;
import com.bbyiya.dao.OOrderproductdetailsMapper;
import com.bbyiya.dao.OOrderproductsMapper;
import com.bbyiya.dao.OPayorderMapper;
import com.bbyiya.dao.OPayorderextMapper;
import com.bbyiya.dao.OPayorderwalletdetailsMapper;
import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.PProductstyleexpMapper;
import com.bbyiya.dao.PProductstylesMapper;
import com.bbyiya.dao.TiAccountlogMapper;
import com.bbyiya.dao.TiActivitysMapper;
import com.bbyiya.dao.TiActivityworksMapper;
import com.bbyiya.dao.TiGroupactivityMapper;
import com.bbyiya.dao.TiGroupactivityworksMapper;
import com.bbyiya.dao.TiMyworkcustomersMapper;
import com.bbyiya.dao.TiMyworkredpacketlogsMapper;
import com.bbyiya.dao.TiProductsextMapper;
import com.bbyiya.dao.TiProductstylesMapper;
import com.bbyiya.dao.TiPromotersMapper;
import com.bbyiya.dao.TiUserdiscountsMapper;
import com.bbyiya.dao.UAccountsMapper;
import com.bbyiya.dao.UAccountslogsMapper;
import com.bbyiya.dao.UBranchesMapper;
import com.bbyiya.dao.UBranchtransaccountsMapper;
import com.bbyiya.dao.UBranchtransamountlogMapper;
import com.bbyiya.dao.UBranchusersMapper;
import com.bbyiya.dao.UUseraddressMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.AccountLogType;
import com.bbyiya.enums.OrderStatusEnum;
import com.bbyiya.enums.OrderTypeEnum;
import com.bbyiya.enums.PayOrderStatusEnums;
import com.bbyiya.enums.PayOrderTypeEnum;
import com.bbyiya.enums.PayTypeEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.calendar.GroupActWorkStatus;
import com.bbyiya.enums.calendar.RedpacketStatus;
import com.bbyiya.enums.calendar.TiAmountLogType;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.model.EErrors;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.model.OPayorder;
import com.bbyiya.model.OPayorderwalletdetails;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.PProductstyleexp;
import com.bbyiya.model.PProductstyles;
import com.bbyiya.model.TiAccountlog;
import com.bbyiya.model.TiActivitys;
import com.bbyiya.model.TiActivityworks;
import com.bbyiya.model.TiGroupactivity;
import com.bbyiya.model.TiGroupactivityworks;
import com.bbyiya.model.TiMyworkcustomers;
import com.bbyiya.model.TiMyworkredpacketlogs;
import com.bbyiya.model.TiProductsext;
import com.bbyiya.model.TiProductstyles;
import com.bbyiya.model.TiPromoters;
import com.bbyiya.model.TiUserdiscounts;
import com.bbyiya.model.UAccounts;
import com.bbyiya.model.UAccountslogs;
import com.bbyiya.model.UBranches;
import com.bbyiya.model.UBranchusers;
import com.bbyiya.model.UUsers;
import com.bbyiya.service.IBasePayService;
import com.bbyiya.service.IBaseUserAccountService;
import com.bbyiya.service.calendar.ITi_OrderMgtService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.SendSMSByMobile;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.calendar.TiActivityOrderSubmitParam;
import com.bbyiya.vo.calendar.TiAmountProportion;

@Service("basePayServiceImpl")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class BasePayServiceImpl implements IBasePayService{
	
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
	@Autowired
	private OPayorderextMapper oextMapper;
	@Autowired
	private OPayorderwalletdetailsMapper owalletMapper;

	/*------------------------用户信息模块----------------------------------*/
	@Autowired
	private UAccountsMapper accountsMapper;//账户信息
	@Autowired
	private UUseraddressMapper addressMapper;// 用户收货地址
	@Autowired
	private UUsersMapper usersMapper;
	@Autowired
	private UBranchesMapper branchMapper;
	@Autowired
	private UBranchusersMapper branchUserMapper;
	
	/*-----------------------产品模块--------------------------------------------*/
	@Autowired
	private PProductstyleexpMapper styltExpMapper;
	@Autowired
	private PProductstylesMapper styleMapper;
	
	/*-- ---------------------错误日志记录-------------------------------------------*/
	@Autowired
	private EErrorsMapper logMapper;
	
	/*-----------------------------------------*/
	@Autowired
	private UBranchtransaccountsMapper transMapper;
	@Autowired
	private UBranchtransamountlogMapper transLogMapper;
	
	@Resource(name = "baseUserAccountService")
	private IBaseUserAccountService accountService;
	@Autowired
	private UAccountslogsMapper accountslogsMapper;
	
//	@Resource(name="basePostMgtServiceImpl")
//	private IBasePostMgtService postService;
	
	@Autowired
	private TiActivityworksMapper activityworksMapper;
	@Autowired
	private TiProductsextMapper tiProductsextMapper;
	@Autowired
	private TiMyworkredpacketlogsMapper tiredpacketMapper;
	@Autowired
	private TiMyworkcustomersMapper tiredpacketCustomerMapper;

	@Resource(name = "tiOrderMgtServiceImpl")
	private  ITi_OrderMgtService basetiorderService;
	
	

	@Autowired
	private TiGroupactivityworksMapper gworkMapper;
	@Autowired
	private TiGroupactivityMapper gActMapper;
	/**
	 * 订单支付成功 回写
	 */
	public boolean paySuccessProcess(String payId) {
		if (!ObjectUtil.isEmpty(payId)) {
			try {
				OPayorder payOrder = payOrderMapper.selectByPrimaryKey(payId);
				if (payOrder != null && payOrder.getStatus()!=null && payOrder.getStatus().intValue()==Integer.parseInt(OrderStatusEnum.noPay.toString())) {
					payOrder.setPaytime(new Date());
					payOrder.setStatus(Integer.parseInt(OrderStatusEnum.payed.toString()));
					payOrder.setPaytype(Integer.parseInt(PayTypeEnum.weiXin.toString())); 
					//订单类型
					int orderType=payOrder.getOrdertype()==null?0:payOrder.getOrdertype();
					/*-------------------------代理商货款充值-----------------------------------------------------*/
					if(orderType==Integer.parseInt(PayOrderTypeEnum.chongzhi.toString())) {
						//账户、流水 变动
						accountService.add_accountsLog(payOrder.getUserid(), Integer.parseInt(AccountLogType.get_recharge.toString()), payOrder.getTotalprice(), payId, "");
						//更新支付单
						payOrderMapper.updateByPrimaryKeySelective(payOrder);
						//------------------------------------短信通知--------------------
						SmsParam param=new SmsParam();
						param.setAmount(payOrder.getTotalprice());
						UUsers branchUsers= usersMapper.selectByPrimaryKey(payOrder.getUserid());
						UBranches branches=branchMapper.selectByPrimaryKey(payOrder.getUserid());
						if(branchUsers!=null&&!ObjectUtil.isEmpty(branchUsers.getMobilephone())){
							 //短信通知商户
							 SendSMSByMobile.sendSmS(Integer.parseInt(SendMsgEnums.recharge.toString()), branchUsers.getMobilephone(), param);
						}
						if(branches!=null){
							param.setUserName(branches.getBranchcompanyname()); 
						}
						//通知 咿呀相关人员
						String adminPhones= ConfigUtil.getSingleValue("adminPhones");
						if(!ObjectUtil.isEmpty(adminPhones)){
							String[] phones=adminPhones.split(",");
							if(phones!=null&&phones.length>0){
								param.setUserId(payOrder.getUserid()); 
								for (String phone : phones) {
									if(!ObjectUtil.isEmpty(phone)&&ObjectUtil.isMobile(phone)){
										SendSMSByMobile.sendSmS(Integer.parseInt(SendMsgEnums.recharge_adminUser.toString()),phone, param);
									}
								}
							}
						}
						/*****************************************************/
						return true;
					}
					else if (orderType==Integer.parseInt(PayOrderTypeEnum.postage.toString())) {
						/*-----------------------------代理商邮费 充值------------------------------------------*/
						accountService.add_accountsLog(payOrder.getUserid(), Integer.parseInt(AccountLogType.get_recharge.toString()), payOrder.getTotalprice(), payId, "");
						//更新支付单
						payOrderMapper.updateByPrimaryKeySelective(payOrder);
						return true;
					}/*-------------------------------------------------------------------*/
					else if (orderType==Integer.parseInt(PayOrderTypeEnum.redPackets.toString())) {
						//发红包------------------------
						OPayorderwalletdetails walletDetails=owalletMapper.selectByPrimaryKey(payId);
						if(walletDetails!=null){
							walletDetails.setStatus(Integer.parseInt(PayOrderStatusEnums.payed.toString()));
							owalletMapper.updateByPrimaryKeySelective(walletDetails);
							//更新收到红包 用户的账户信息
							accountService.add_accountsLog(walletDetails.getForuserid(), Integer.parseInt(AccountLogType.get_redPackets.toString()), payOrder.getTotalprice(), payId, "");
							//更新支付单
							payOrderMapper.updateByPrimaryKeySelective(payOrder);
							return true;
						}else {
							addlog("payId:"+payId+",方法paySuccessProcess。发红包有误，找不到支付记录！");
							return false;
						}
					}
					/************************------------单独运费支付------------------********************************/

					else if (orderType==Integer.parseInt(PayOrderTypeEnum.ti_postage.toString())) {
						//用户付邮费
						TiActivityworks works= activityworksMapper.selectByPrimaryKey(ObjectUtil.parseLong(payOrder.getUserorderid()));
						if(works!=null&&ObjectUtil.parseLong(payOrder.getExtobject())>0){
							works.setAddresstype(1);
							works.setOrderaddressid(ObjectUtil.parseLong(payOrder.getExtobject()));
//							works.setStatus(Integer.parseInt(ActivityWorksStatusEnum.imagesubmit.toString())); 
							activityworksMapper.updateByPrimaryKeySelective(works);
						} 
						payOrderMapper.updateByPrimaryKeySelective(payOrder);
						//台历交易记录
						add_tiAccountLog(payId,payOrder.getUserid()==null?works.getUserid():payOrder.getUserid(), payOrder.getTotalprice(),TiAmountLogType.in_payment);
						
						//影楼自动下单（活动作品）
						if(works!=null) {
							TiActivitys acts= activitysMapper.selectByPrimaryKey(works.getActid());
							if(acts!=null){
								// 自动下单操作
								TiActivityOrderSubmitParam orderParam = new TiActivityOrderSubmitParam();
								orderParam.setSubmitUserId(acts.getProduceruserid());
								orderParam.setWorkId(works.getWorkid());
								orderParam.setCount(1);
								ReturnModel oReturnModel=basetiorderService.submitOrder_ibs(orderParam);
								if(!oReturnModel.getStatu().equals(ReturnStatus.Success)){
									addlog("付邮费参与活动"+oReturnModel.getStatusreson());
								}
							}else {
								addlog("找不到活动。payid:"+payId);
							}
						}else {
							addlog("找不到活动2。payid:"+payId);
						}
						return true;
					}
					/**-----台历红包 --付款到平台------------------------------------------------------------------*/
					else if (orderType==Integer.parseInt(PayOrderTypeEnum.ti_redpaket.toString())) {
						//更新支付状态
						payOrderMapper.updateByPrimaryKeySelective(payOrder);
						//红包投递记录信息
						TiMyworkredpacketlogs redpacketLog= tiredpacketMapper.selectByPrimaryKey(payId);
						if(redpacketLog!=null){
							redpacketLog.setStatus(Integer.parseInt(RedpacketStatus.payed.toString()));
							tiredpacketMapper.updateByPrimaryKeySelective(redpacketLog);
							//代客制作红包金额
							TiMyworkcustomers myworkcustomers= tiredpacketCustomerMapper.selectByPrimaryKey(redpacketLog.getWorkid());
							if(myworkcustomers!=null){
								double amountRed=myworkcustomers.getRedpacketamount()==null?0d:myworkcustomers.getRedpacketamount().doubleValue();
								myworkcustomers.setRedpacketamount(amountRed+payOrder.getTotalprice().doubleValue());
								tiredpacketCustomerMapper.updateByPrimaryKeySelective(myworkcustomers);
								//红包金额满足条件，直接下单
								if(myworkcustomers.getNeedredpackettotal()!=null&&myworkcustomers.getNeedredpackettotal().doubleValue()<=myworkcustomers.getRedpacketamount().doubleValue()){
									TiActivityOrderSubmitParam orderParam=new TiActivityOrderSubmitParam();
									orderParam.setSubmitUserId(myworkcustomers.getPromoteruserid());
									orderParam.setWorkId(myworkcustomers.getWorkid());
									orderParam.setCount(1); 
									basetiorderService.submitTiCustomerOrder_ibs(orderParam, null);
								}
								return true;
							}
						
						}else {
							addlog("payId:"+payId+",台历红包找不到红包记录TiMyworkredpacketlogs！");
							return false;
						}
					}
					/**--------------------------------台历、挂历  团购业务支付-------------------------------------------------*/
					else if (orderType==Integer.parseInt(PayOrderTypeEnum.ti_groupAct.toString())) {
						//更新支付状态
						payOrderMapper.updateByPrimaryKeySelective(payOrder);
						//红包投递记录信息
						TiGroupactivityworks gwork= gworkMapper.selectByPrimaryKey(ObjectUtil.parseLong(payOrder.getUserorderid()));
						if(gwork!=null){
							gwork.setPaytime(new Date());
							gwork.setStatus(Integer.parseInt(GroupActWorkStatus.payed.toString()));  
							gworkMapper.updateByPrimaryKeySelective(gwork);
							TiGroupactivity gact= gActMapper.selectByPrimaryKey(gwork.getGactid());
							if(gact!=null){
								double totalPrice=payOrder.getTotalprice();
								if(gwork.getPostage()!=null&&gwork.getPostage().doubleValue()>0){
									totalPrice=totalPrice-gwork.getPostage().doubleValue();
								}
								accountService.add_accountsLog(gact.getPromoteruserid(), Integer.parseInt(AccountLogType.get_ti_redpaket.toString()), totalPrice , payId, "");
							}
						}
						return true;
					}
					else {//购物
						OUserorders userorders = userOrdersMapper.selectByPrimaryKey(payOrder.getUserorderid());
						// 在可支付的状态中---
						if(userorders!=null&&userorders.getStatus().intValue() == Integer.parseInt(OrderStatusEnum.noPay.toString())){
							double walletPayAmount=payOrder.getWalletamount()==null?0d:payOrder.getWalletamount().doubleValue();
							//------------------使用了钱包------------------------------------------
							if(walletPayAmount>0){
								UAccounts accounts=accountsMapper.selectByPrimaryKey(payOrder.getUserid());
								Double freeAmount=accounts==null?0d:(accounts.getFreezecashamount()==null?0d:accounts.getFreezecashamount().doubleValue());
								if(walletPayAmount>freeAmount){//钱包需要支付的金额不够！
									addlog("payId:"+payId+",方法paySuccessProcess。用到了钱包，但是钱包金额有误！");
									return false;
								}
								//插入钱包支付流水
								UAccountslogs log=new UAccountslogs();
								log.setUserid(payOrder.getUserid());
								log.setCreatetime(new Date());
								log.setType(Integer.parseInt(AccountLogType.use_payment.toString()));
								log.setAmount((-1)*Math.abs(payOrder.getWalletamount()));
								log.setOrderid(payId);
								accountslogsMapper.insert(log);
								//更新钱包的冻结金额
								accounts.setFreezecashamount(accounts.getFreezecashamount().doubleValue()-payOrder.getWalletamount().doubleValue());
								accountsMapper.updateByPrimaryKeySelective(accounts);
								
							}else {
								userorders.setPaytype(Integer.parseInt(PayTypeEnum.weiXin.toString()));
							}
							userorders.setPaytime(new Date()); 
							userorders.setStatus(Integer.parseInt(OrderStatusEnum.payed.toString()));
							//支付单状态修改
							payOrder.setPaytime(new Date());
							payOrder.setStatus(Integer.parseInt(OrderStatusEnum.payed.toString()));
							double payAmount=payOrder.getTotalprice()-(payOrder.getWalletamount()==null?0d:payOrder.getWalletamount().doubleValue());
							if(payAmount<=0){//钱包支付
								payOrder.setPaytype(Integer.parseInt(PayTypeEnum.walletPay.toString())); 
								userorders.setPaytype(Integer.parseInt(PayTypeEnum.walletPay.toString())); 
							}else {
								payOrder.setPaytype(Integer.parseInt(PayTypeEnum.weiXin.toString())); 
							}
						
							//--订单状态修改---
							userOrdersMapper.updateByPrimaryKeySelective(userorders);	
							//--修改支付单状态----
							payOrderMapper.updateByPrimaryKeySelective(payOrder);
							
							//---销售分成--------------------------
							//
							if(userorders.getOrdertype()==null||userorders.getOrdertype()==Integer.parseInt(OrderTypeEnum.brachOrder.toString())||userorders.getOrdertype()==Integer.parseInt(OrderTypeEnum.nomal.toString())){
								addCommission(payOrder,userorders.getUserorderid());
							}else if (userorders.getOrdertype()!=null&&(userorders.getOrdertype()==Integer.parseInt(OrderTypeEnum.ti_branchOrder.toString())||userorders.getOrdertype()==Integer.parseInt(OrderTypeEnum.ti_nomal.toString()))) {
								OOrderproducts oproduct= oproductMapper.getOProductsByOrderId(userorders.getUserorderid());
								if(oproduct!=null){
									TiProductsext productsext=tiProductsextMapper.selectByPrimaryKey(oproduct.getProductid());
									if(productsext==null){
										productsext=new TiProductsext();
										productsext.setProductid(oproduct.getProductid());
										productsext.setSales(oproduct.getCount()); 
										productsext.setMonthssales(oproduct.getCount());
										tiProductsextMapper.insert(productsext);
									}else {
										productsext.setSales((productsext.getSales()==null?0:productsext.getSales().intValue())+oproduct.getCount().intValue());
										productsext.setMonthssales((productsext.getMonthssales()==null?0:productsext.getMonthssales().intValue())+oproduct.getCount().intValue());
										tiProductsextMapper.updateByPrimaryKeySelective(productsext);
									}
								}
								//台历交易记录
								add_tiAccountLog(payId,userorders.getUserid(), payOrder.getTotalprice(),TiAmountLogType.in_payment);
							}
							return true;
						} else {//不在支付状态中
							addlog("payId:"+payId+",方法paySuccessProcess。不在可支付的userOrder状态！");
							return false;
						}	
					}/*----------购物完~~~~~~~~~~~~~~~*/
					
				}else {
					if(payOrder!=null){
						if(payOrder.getStatus()!=null&&payOrder.getStatus().intValue()==Integer.parseInt(PayOrderStatusEnums.payed.toString())){
							return true; 
						}else {
							addlog("payId:"+payId+",方法paySuccessProcess。支付单失效！");
							return true;
						}
					}
					addlog("payId:"+payId+",方法paySuccessProcess。OPayorder找不到此订单！");
					return false;
				} 
			} catch (Exception e) {
				addlog("payId:"+payId+",方法paySuccessProcess。"+e.getMessage());
			}
		}
		return false;
	}

	@Autowired
	private TiAccountlogMapper tiAccountlogMapper;

	/**
	 * 插入台历模块交易流水
	 * @param payId
	 * @param userid
	 * @param amount
	 * @param type
	 */
	public void add_tiAccountLog(String payId,long userid, double amount,TiAmountLogType type){
		try {
			TiAccountlog lastLog= tiAccountlogMapper.getLastResult();
			double totalamount=0d ,totalavailbelAmount=0d;
			if(lastLog!=null){
				totalamount=(lastLog.getTotalamount()==null?0:lastLog.getTotalamount().doubleValue());
				totalavailbelAmount=(lastLog.getAvailableamount()==null?0:lastLog.getAvailableamount().doubleValue());
			}
			TiAccountlog log=new TiAccountlog();
			log.setAmount(amount);
			log.setType(Integer.parseInt(type.toString()));
			if(type!=TiAmountLogType.out_dispenseCash){
				log.setTotalamount(totalamount+ Math.abs(amount));
			}
			log.setAvailableamount(totalavailbelAmount+amount);
			log.setPayid(payId);
			log.setCreatetime(new Date());
			log.setUserid(userid);
			tiAccountlogMapper.insert(log);
		} catch (Exception e) {
			addlog("台历记录:orderId"+payId+e.getMessage());
		}
	}
	
	/**
	 * 新增销售分成
	 * @param userOrder
	 */
	private void addCommission(OPayorder payorder,String userOrderId){
		try {
			UUsers buyer=usersMapper.selectByPrimaryKey(payorder.getUserid());
			//非代理商用户
			long branchUserId=0l;
			long upUserIdentity=0l;
			if(buyer!=null&&!ValidateUtils.isIdentity(buyer.getIdentity(), UserIdentityEnums.branch)){
				//如果上级用户是影楼
				if(buyer.getUpuserid()!=null&&buyer.getUpuserid().longValue()>0){
					UUsers upUser=usersMapper.selectByPrimaryKey(buyer.getUpuserid());
					if(upUser!=null){
						//上级用户的身份标识
						upUserIdentity=upUser.getIdentity();
						
						//如果上级用户是影楼---分利
						if(ValidateUtils.isIdentity(upUser.getIdentity(), UserIdentityEnums.branch)){
							branchUserId=buyer.getUpuserid();
						}
						//如果上级用户是  影楼的员工--找到影楼userId  --分利
						else if (ValidateUtils.isIdentity(upUser.getIdentity(), UserIdentityEnums.salesman)) {
							UBranchusers branchuser= branchUserMapper.selectByPrimaryKey(buyer.getUpuserid());
							if(branchuser!=null){
								UUsers upBranchUser=usersMapper.selectByPrimaryKey(branchuser.getBranchuserid());
								if(upBranchUser!=null&&ValidateUtils.isIdentity(upBranchUser.getIdentity(), UserIdentityEnums.branch)){
									branchUserId=branchuser.getBranchuserid();
								}
							}
						}
						//如果上级用户是 流量主
						else if (ValidateUtils.isIdentity(upUser.getIdentity(), UserIdentityEnums.wei)) {
							branchUserId=buyer.getUpuserid();
						}
					}
				}
			}
			
			if(branchUserId>0){
				//销售分成
				OOrderproducts oproduct= oproductMapper.getOProductsByOrderId(userOrderId);
				if(oproduct!=null&&oproduct.getStyleid()!=null){
					PProductstyles style= styleMapper.selectByPrimaryKey(oproduct.getStyleid());
					if(style!=null&&style.getAgentprice()!=null){
						double conPrice=style.getAgentprice().doubleValue()*oproduct.getCount().intValue();
						//统一运费10块
						double postAmount=10d;
						double commission= payorder.getTotalprice().doubleValue()-conPrice-postAmount;
						//如果是流量主，成本增加7元
						if(!ValidateUtils.isIdentity(upUserIdentity, UserIdentityEnums.branch)&&ValidateUtils.isIdentity(upUserIdentity, UserIdentityEnums.wei)){
							commission=commission-7d;
						}
						if(commission>0){
							accountService.add_accountsLog(branchUserId, Integer.parseInt(AccountLogType.get_Commission.toString()), commission, payorder.getPayid(), "");
						}
					}
				}
			}
			
		} catch (Exception e) {
			addlog("payId:"+payorder.getPayid()+",订单分成。"+e.getMessage());
		}
	}
	
	
	
	/**
	 * 订单完成后新增销量
	 */
	public void addProductExt(String userOrderId){
		try {
			OOrderproducts oproduct= oproductMapper.getOProductsByOrderId(userOrderId);
			if(oproduct!=null&&oproduct.getSalesuserid()!=null){
				PProductstyleexp styleExp= styltExpMapper.selectByPrimaryKey(oproduct.getSalesuserid());
				if(styleExp!=null){
					int count=styleExp.getSalecount()==null?0:styleExp.getSalecount();
					styleExp.setSalecount(count+oproduct.getCount()); 
				}else {
					PProductstyles style=styleMapper.selectByPrimaryKey(oproduct.getStyleid());
					if(style!=null){
						styleExp=new PProductstyleexp();
						styleExp.setStyleid(oproduct.getStyleid());
						styleExp.setProductid(style.getProductid());
						styleExp.setSalecount(oproduct.getCount());
						styltExpMapper.insert(styleExp);
					}
				}
			}
		} catch (Exception e) {
			addlog("userOrderId："+userOrderId+",方法addProductExt。"+e); 
		}
	}
	
	/**
	 * 插入错误Log
	 * 
	 * @param msg
	 */
	public void addlog(String msg) {
		try {
			EErrors errors = new EErrors();
			errors.setClassname(this.getClass().getName());
			errors.setMsg(msg);
			errors.setCreatetime(new Date()); 
			logMapper.insert(errors);
		} catch (Exception e) {
			
		}
	}

	@Autowired
	private TiProductstylesMapper tistyleMapper;
	@Autowired
	private TiPromotersMapper promotersMapper;
	@Autowired
	private TiUserdiscountsMapper userdiscountsMapper;
	
	@Autowired
	private TiActivitysMapper activitysMapper;
	/**
	 * 订单金额分配
	 * @param userOrderId
	 */
	public void distributeOrderAmount(String userOrderId){
		try {
			OUserorders userorders = userOrdersMapper.selectByPrimaryKey(userOrderId);
			if (userorders != null && userorders.getOrdertype() != null && (userorders.getOrdertype() == Integer.parseInt(OrderTypeEnum.ti_nomal.toString()) || userorders.getOrdertype() == Integer.parseInt(OrderTypeEnum.ti_branchOrder.toString()))) {
				List<OOrderproducts> oproductlist = oproductMapper.findOProductsByOrderId(userOrderId);
				if (oproductlist != null && oproductlist.size() > 0) {
					TiProductstyles style = tistyleMapper.selectByPrimaryKey(oproductlist.get(0).getStyleid());
					if (style != null) {
						// 全价（商品全价）
						double totalprice = style.getPrice() * oproductlist.get(0).getCount();//货款总金额
						//实际的货款
						double totalprice_hk=0d;
						if(userorders.getOrdertype().intValue()==Integer.parseInt(OrderTypeEnum.ti_branchOrder.toString())){
							totalprice_hk=userorders.getOrdertotalprice();
						}
						//如果收货地址寄到影楼（影楼付邮费）
						else if(userorders.getIspromoteraddress()!=null&&userorders.getIspromoteraddress().intValue()==1){
							totalprice_hk=userorders.getOrdertotalprice().doubleValue();	
						}else {
							totalprice_hk=userorders.getOrdertotalprice().doubleValue()-(userorders.getPostage()==null?0d:userorders.getPostage().doubleValue());																
						}
						//分配的方式原则  1 影楼惊爆价下单  2 折扣价，3 全价
						int type=0;
						Long promoterUid = userorders.getBranchuserid();// 影楼
						Long agentUid = 0l;// 代理商
						Long producerUid = userorders.getProduceruserid();// 生产商
						Long yiyaUid = 1l;// 咿呀平台
						Long hxgUid = 2l;// 幻想馆
						// 如果是影楼自己下单
						if (userorders.getOrdertype().intValue() == Integer.parseInt(OrderTypeEnum.ti_branchOrder.toString())) {
							type=1;
							//用户自付邮费  邮费分配-----------------------------
						    OOrderproducts oproduct=oproductMapper.getOProductsByOrderId(userOrderId);
						    if(oproduct!=null){
						    	double postageB=0d;
						    	TiActivityworks actwork= activityworksMapper.selectByPrimaryKey(oproduct.getCartid()) ;
						    	if(actwork!=null&&actwork.getOrderaddressid()!=null&&actwork.getOrderaddressid().longValue()>0){
						    		OPayorder postPayorder= payOrderMapper.getpostPayorderByworkId(String.valueOf(actwork.getWorkid()));
						    		if(postPayorder!=null){
						    			postageB=postPayorder.getTotalprice();
						    		}
						    	}else {
									TiGroupactivityworks gwork=gworkMapper.selectByPrimaryKey(oproduct.getCartid());
									if(gwork!=null&&gwork.getPostage()!=null&&gwork.getPostage().doubleValue()>0){
										postageB=gwork.getPostage();
									}
								}
						    	//B端购买，用户自付邮费 
						    	if(postageB>0){
						    		//生产商分利
					    			if (producerUid != null && producerUid > 0) {
					    				accountService.add_accountsLog(producerUid, Integer.parseInt(AccountLogType.get_ti_post.toString()), postageB * TiAmountProportion.post_A, userOrderId, "");										
					    			}
					    			//咿呀平台分利
									accountService.add_accountsLog(yiyaUid, Integer.parseInt(AccountLogType.get_ti_commission.toString()), postageB * TiAmountProportion.post_D, userOrderId, "");	 //0.05							
									//幻想馆分利
									accountService.add_accountsLog(hxgUid, Integer.parseInt(AccountLogType.get_ti_commission.toString()), postageB * TiAmountProportion.post_E, userOrderId, "");																													    		
						    	}
						    }/*----------------------------------------------*/
						} else {//普通订单（c端购买）
							//优惠价购买
							if(totalprice_hk<totalprice){
								type=2;
								TiUserdiscounts discount= userdiscountsMapper.getMyDiscountsByUserOrderId(userOrderId);
								if(discount!=null&&ObjectUtil.isEmpty(discount.getPromoteruserid())){ 
									promoterUid=discount.getPromoteruserid();
								}
							}else {//全价购买
								type=3;
								UUsers buyerUsers= usersMapper.selectByPrimaryKey(userorders.getUserid());
								if(buyerUsers!=null&&buyerUsers.getUpuserid()!=null){
									UUsers upUser= usersMapper.selectByPrimaryKey(buyerUsers.getUpuserid());
									if(upUser!=null){
										if(ValidateUtils.isIdentity(upUser.getIdentity(), UserIdentityEnums.ti_promoter)){
											promoterUid=upUser.getUserid();
										}else if (buyerUsers.getSourseuserid()!=null) {
											UUsers sourseUser= usersMapper.selectByPrimaryKey(buyerUsers.getSourseuserid());
											if(sourseUser!=null&&ValidateUtils.isIdentity(sourseUser.getIdentity(), UserIdentityEnums.ti_promoter)){
												promoterUid=buyerUsers.getSourseuserid();
											}
										}
									}
								}
							}
						}
						//通过推广者获取 订单组织者 
						if (promoterUid != null && promoterUid.longValue() > 0) {
							TiPromoters promoters = promotersMapper.selectByPrimaryKey(promoterUid);
							if (promoters != null) {
								agentUid = promoters.getAgentuserid();
							}
						}
						if(promoterUid==null||promoterUid<=0)
							promoterUid=yiyaUid;
						if(agentUid==null||agentUid<=0)
							agentUid=yiyaUid;
						//纯货款分配
						switch (type) {//1 影楼惊爆价下单
						case 1://
							if (producerUid != null && producerUid > 0) {
								//生产商分利
								accountService.add_accountsLog(producerUid, Integer.parseInt(AccountLogType.get_ti_payment.toString()), totalprice_hk * TiAmountProportion.cost_A, userorders.getPayid(), "");
							}
							if(agentUid!=null&&agentUid.longValue()>0){
								//订单组织者分利
								accountService.add_accountsLog(agentUid, Integer.parseInt(AccountLogType.get_ti_commission.toString()), totalprice_hk * TiAmountProportion.cost_B, userorders.getPayid(), "");								
							}
							//咿呀平台分利
							accountService.add_accountsLog(yiyaUid, Integer.parseInt(AccountLogType.get_ti_commission.toString()), totalprice_hk * TiAmountProportion.cost_D, userorders.getPayid(), "");								
							//幻想馆分利
							accountService.add_accountsLog(hxgUid, Integer.parseInt(AccountLogType.get_ti_commission.toString()), totalprice_hk * TiAmountProportion.cost_E, userorders.getPayid(), "");														
							break;
						case 2:// 2 折扣价
							if (producerUid != null && producerUid > 0) {
								//生产商分利
								accountService.add_accountsLog(producerUid, Integer.parseInt(AccountLogType.get_ti_payment.toString()), totalprice_hk * TiAmountProportion.half_A, userorders.getPayid(), "");
							}
							if(agentUid!=null&&agentUid.longValue()>0){
								//订单组织者分利
								accountService.add_accountsLog(agentUid, Integer.parseInt(AccountLogType.get_ti_commission.toString()), totalprice_hk * TiAmountProportion.half_B, userorders.getPayid(), "");								
							}
							if(promoterUid!=null&&promoterUid.longValue()>0){
								//订单组织者分利
								accountService.add_accountsLog(promoterUid, Integer.parseInt(AccountLogType.get_ti_commission.toString()), totalprice_hk * TiAmountProportion.half_C, userorders.getPayid(), "");								
							}
							//咿呀平台分利
							accountService.add_accountsLog(yiyaUid, Integer.parseInt(AccountLogType.get_ti_commission.toString()), totalprice_hk * TiAmountProportion.half_D, userorders.getPayid(), "");								
							//幻想馆分利
							accountService.add_accountsLog(hxgUid, Integer.parseInt(AccountLogType.get_ti_commission.toString()), totalprice_hk * TiAmountProportion.half_E, userorders.getPayid(), "");														
							break;
						case 3://3 全价
							if (producerUid != null && producerUid > 0) {
								//生产商分利
								accountService.add_accountsLog(producerUid, Integer.parseInt(AccountLogType.get_ti_payment.toString()), totalprice_hk * TiAmountProportion.full_A, userorders.getPayid(), "");
							}
							
							if(agentUid!=null&&agentUid.longValue()>0){
								//订单组织者分利
								accountService.add_accountsLog(agentUid, Integer.parseInt(AccountLogType.get_ti_commission.toString()), totalprice_hk * TiAmountProportion.full_B, userorders.getPayid(), "");								
							}
							if(promoterUid!=null&&promoterUid.longValue()>0){
								//订单组织者分利
								accountService.add_accountsLog(promoterUid, Integer.parseInt(AccountLogType.get_ti_commission.toString()), totalprice_hk * TiAmountProportion.full_C, userorders.getPayid(), "");								
							}
							//咿呀平台分利
							accountService.add_accountsLog(yiyaUid, Integer.parseInt(AccountLogType.get_ti_commission.toString()), totalprice_hk * TiAmountProportion.full_D, userorders.getPayid(), "");								
							//幻想馆分利
							accountService.add_accountsLog(hxgUid, Integer.parseInt(AccountLogType.get_ti_commission.toString()), totalprice_hk * TiAmountProportion.full_E, userorders.getPayid(), "");														
							break;
						default:
							break;
						}
						//运费分配
						if(userorders.getPostage()!=null&&userorders.getPostage().doubleValue()>0){
							//生产商分利（影楼下单，寄到影楼地址，运费不分成，全部给到生产商）
							if(userorders.getOrdertype().intValue()==Integer.parseInt(OrderTypeEnum.ti_branchOrder.toString())){
								accountService.add_accountsLog(producerUid, Integer.parseInt(AccountLogType.get_ti_post.toString()), userorders.getPostage().doubleValue() , userorders.getPayid(), "");							
							}
							//如果寄到影楼（）
							else if(userorders.getIspromoteraddress()!=null&&userorders.getIspromoteraddress().intValue()==1){
								accountService.add_accountsLog(producerUid, Integer.parseInt(AccountLogType.get_ti_post.toString()), userorders.getPostage().doubleValue() , userorders.getPayid(), "");							
							}else{
								//用户付邮费
								accountService.add_accountsLog(producerUid, Integer.parseInt(AccountLogType.get_ti_post.toString()), userorders.getPostage().doubleValue() * TiAmountProportion.post_A, userorders.getPayid(), "");
								accountService.add_accountsLog(yiyaUid, Integer.parseInt(AccountLogType.get_ti_post.toString()), userorders.getPostage().doubleValue() * TiAmountProportion.post_D, userorders.getPayid(), "");
								accountService.add_accountsLog(hxgUid, Integer.parseInt(AccountLogType.get_ti_post.toString()), userorders.getPostage().doubleValue() * TiAmountProportion.post_E, userorders.getPayid(), "");														
							}	
						}
					}
				}
			}
		} catch (Exception e) {
			addlog("订单分成有误userOrderId："+userOrderId+"msg:"+e);  
		}
	}
}
