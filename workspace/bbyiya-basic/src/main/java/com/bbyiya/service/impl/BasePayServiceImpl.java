package com.bbyiya.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.bbyiya.dao.UAccountsMapper;
import com.bbyiya.dao.UAccountslogsMapper;
import com.bbyiya.dao.UBranchtransaccountsMapper;
import com.bbyiya.dao.UBranchtransamountlogMapper;
import com.bbyiya.dao.UCashlogsMapper;
import com.bbyiya.dao.UUseraddressMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.AccountLogType;
import com.bbyiya.enums.AmountType;
import com.bbyiya.enums.OrderStatusEnum;
import com.bbyiya.enums.PayOrderStatusEnums;
import com.bbyiya.enums.PayOrderTypeEnum;
import com.bbyiya.enums.PayTypeEnum;
import com.bbyiya.model.EErrors;
import com.bbyiya.model.OOrderproductdetails;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.model.OPayorder;
import com.bbyiya.model.OPayorderext;
import com.bbyiya.model.OPayorderwalletdetails;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.PProductstyleexp;
import com.bbyiya.model.PProductstyles;
import com.bbyiya.model.UAccounts;
import com.bbyiya.model.UAccountslogs;
import com.bbyiya.model.UBranchtransaccounts;
import com.bbyiya.model.UBranchtransamountlog;
import com.bbyiya.model.UCashlogs;
import com.bbyiya.model.UUsers;
import com.bbyiya.service.IBasePayService;
import com.bbyiya.service.IBaseUserAccountService;
import com.bbyiya.utils.ObjectUtil;

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
	private UCashlogsMapper cashlogsMapper;//账户流水
	@Autowired
	private UUseraddressMapper addressMapper;// 用户收货地址
	@Autowired
	private UUsersMapper usersMapper;
	
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
	
	/**
	 * 订单支付成功 回写
	 */
	public boolean paySuccessProcess(String payId) {
		if (!ObjectUtil.isEmpty(payId)) {
			try {
				OPayorder payOrder = payOrderMapper.selectByPrimaryKey(payId);
				if (payOrder != null && payOrder.getStatus()!=null && payOrder.getStatus().intValue()==Integer.parseInt(OrderStatusEnum.noPay.toString())) {
					int orderType=payOrder.getOrdertype()==null?0:payOrder.getOrdertype();
					/*-------------------------代理商货款充值-----------------------------------------------------*/
					if(orderType==Integer.parseInt(PayOrderTypeEnum.chongzhi.toString())) {
						accountService.add_accountsLog(payOrder.getUserid(), Integer.parseInt(AccountLogType.get_recharge.toString()), payOrder.getTotalprice(), payId, "");
					}
					else if (orderType==Integer.parseInt(PayOrderTypeEnum.postage.toString())) {
						/*-----------------------------代理商邮费 充值------------------------------------------*/
						accountService.add_accountsLog(payOrder.getUserid(), Integer.parseInt(AccountLogType.get_recharge.toString()), payOrder.getTotalprice(), payId, "");
					}/*-------------------------------------------------------------------*/
					else if (orderType==Integer.parseInt(PayOrderTypeEnum.redPackets.toString())) {
						//发红包------------------------
						OPayorderwalletdetails walletDetails=owalletMapper.selectByPrimaryKey(payId);
						if(walletDetails!=null){
							walletDetails.setStatus(Integer.parseInt(PayOrderStatusEnums.payed.toString()));
							owalletMapper.updateByPrimaryKeySelective(walletDetails);
							//更新收到红包 用户的账户信息
							accountService.add_accountsLog(walletDetails.getForuserid(), Integer.parseInt(AccountLogType.get_redPackets.toString()), payOrder.getTotalprice(), payId, "");
						}else {
							addlog("payId:"+payId+",方法paySuccessProcess。发红包有误，找不到支付记录！");
							return false;
						}
					}
					/************************------------普通购物------------------********************************/
					else {//购物
						OUserorders userorders = userOrdersMapper.selectByPrimaryKey(payOrder.getUserorderid());
						if (userorders != null) {
							if (userorders.getStatus().intValue() == Integer.parseInt(OrderStatusEnum.noPay.toString())) {
								//订单作品详细
								List<OOrderproductdetails> detailsList= odetailMapper.findOrderProductDetailsByProductOrderId(userorders.getUserorderid());
								
								//------更新订单状态----------------------------------------------
								if(detailsList!=null&&detailsList.size()>0){
									userorders.setStatus(Integer.parseInt(OrderStatusEnum.waitFoSend.toString()));
									userorders.setUploadtime(new Date()); 
								}else {
									userorders.setStatus(Integer.parseInt(OrderStatusEnum.payed.toString()));
								}
								userorders.setPaytype(Integer.parseInt(PayTypeEnum.weiXin.toString()));
								userorders.setPaytime(new Date()); 
							
								
								//是否用钱包支付了---------------------
								if(payOrder.getWalletamount()!=null&&payOrder.getWalletamount().doubleValue()>0){
									UAccounts accounts=accountsMapper.selectByPrimaryKey(payOrder.getUserid());
									Double freeAmount=accounts==null?0d:(accounts.getFreezecashamount()==null?0d:accounts.getFreezecashamount().doubleValue());
									if(payOrder.getWalletamount().doubleValue()>freeAmount){
										addlog("payId:"+payId+",方法paySuccessProcess。用到了钱包，但是钱包金额有误！");
										return false;
									}
									//释放红包冻结金额
									UAccountslogs log=new UAccountslogs();
									log.setUserid(payOrder.getUserid());
									log.setCreatetime(new Date());
									log.setType(Integer.parseInt(AccountLogType.use_payment.toString()));
									log.setOrderid(payId);
									accountslogsMapper.insert(log);
									//更新钱包的冻结金额
									accounts.setFreezecashamount(accounts.getFreezecashamount().doubleValue()-payOrder.getWalletamount().intValue());
									accountsMapper.updateByPrimaryKeySelective(accounts);
								}
								//更新订单的状态---
								userOrdersMapper.updateByPrimaryKeySelective(userorders);
								//分提成
//								addOrderExtend(payOrder);
							}
						}
					}
					//更新支付订单状态-----------------------
					payOrder.setPaytime(new Date());
					payOrder.setStatus(Integer.parseInt(OrderStatusEnum.payed.toString()));
					payOrder.setPaytype(Integer.parseInt(PayTypeEnum.weiXin.toString())); 
					payOrderMapper.updateByPrimaryKeySelective(payOrder);
					return true;
				}else {
					addlog("payId:"+payId+",方法paySuccessProcess。不在可支付的状态！");
				}
			} catch (Exception e) {
				addlog("payId:"+payId+",方法paySuccessProcess。"+e.getMessage());
			}
		}
		return false;
	}

	/**
	 * 上级订单
	 * @param payorder
	 */
	public void addOrderExtend(OPayorder payorder){
		try {
			UUsers user= usersMapper.selectByPrimaryKey(payorder.getUserid());
			if(user!=null&&user.getUpuserid()!=null&&user.getUpuserid()>0){
				OPayorderext ext=new OPayorderext();
				ext.setPayid(payorder.getPayid());
				ext.setUserorderid(payorder.getUserorderid());
				ext.setUserid(payorder.getUserid());
				ext.setUpuserid(user.getUpuserid());
				ext.setTotalprice(payorder.getTotalprice());
				ext.setCreatetime(new Date());
				ext.setStatus(payorder.getStatus());
				oextMapper.insert(ext);
			}
		} catch (Exception e) {
			addlog("userOrderId:"+payorder.getUserorderid()+",方法addOrderExtend。"+e.getMessage());
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

}
