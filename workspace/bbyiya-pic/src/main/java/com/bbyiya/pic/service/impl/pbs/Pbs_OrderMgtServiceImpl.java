package com.bbyiya.pic.service.impl.pbs;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.common.enums.SendMsgEnums;
import com.bbyiya.common.vo.SmsParam;
import com.bbyiya.common.vo.wechatmsg.ShippingParam;
import com.bbyiya.dao.OOrderaddressMapper;
import com.bbyiya.dao.OOrderproductdetailsMapper;
import com.bbyiya.dao.OOrderproductphotosMapper;
import com.bbyiya.dao.OOrderproductsMapper;
import com.bbyiya.dao.OPayorderMapper;
import com.bbyiya.dao.OProducerordercountMapper;
import com.bbyiya.dao.OUserorderextMapper;
import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.PMyproductactivitycodeMapper;
import com.bbyiya.dao.PMyproductdetailsMapper;
import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.PMyproducttempMapper;
import com.bbyiya.dao.PMyproducttempapplyMapper;
import com.bbyiya.dao.TiActivityworksMapper;
import com.bbyiya.dao.TiMyworkcustomersMapper;
import com.bbyiya.dao.TiMyworksMapper;
import com.bbyiya.dao.TiProductstylesMapper;
import com.bbyiya.dao.TiPromotersMapper;
import com.bbyiya.dao.UBranchesMapper;
import com.bbyiya.dao.UBranchtransaccountsMapper;
import com.bbyiya.dao.UBranchtransamountlogMapper;
import com.bbyiya.dao.UOtherloginMapper;
import com.bbyiya.enums.AccountLogType;
import com.bbyiya.enums.AmountType;
import com.bbyiya.enums.MyProductTempType;
import com.bbyiya.enums.OrderStatusEnum;
import com.bbyiya.enums.OrderTypeEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.calendar.AddressTypeEnum;
import com.bbyiya.model.OOrderaddress;
import com.bbyiya.model.OOrderproductdetails;
import com.bbyiya.model.OOrderproductphotos;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.model.OPayorder;
import com.bbyiya.model.OProducerordercount;
import com.bbyiya.model.OUserorderext;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.PMyproductactivitycode;
import com.bbyiya.model.PMyproductdetails;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PMyproducttemp;
import com.bbyiya.model.PMyproducttempapply;
import com.bbyiya.model.TiActivityworks;
import com.bbyiya.model.TiMyworkcustomers;
import com.bbyiya.model.TiMyworks;
import com.bbyiya.model.TiProductstyles;
import com.bbyiya.model.TiPromoters;
import com.bbyiya.model.UBranches;
import com.bbyiya.model.UBranchtransaccounts;
import com.bbyiya.model.UBranchtransamountlog;
import com.bbyiya.model.UOtherlogin;
import com.bbyiya.pic.dao.IPic_OrderMgtDao;
import com.bbyiya.pic.service.IPic_MemberMgtService;
import com.bbyiya.pic.service.pbs.IPbs_OrderMgtService;
import com.bbyiya.pic.utils.FileToZip;
import com.bbyiya.pic.vo.order.PbsUserOrderResultVO;
import com.bbyiya.service.IBaseUserAccountService;
import com.bbyiya.service.IRegionService;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.FileUtils;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.SendSMSByMobile;
import com.bbyiya.utils.WechatMsgUtil;
import com.bbyiya.utils.upload.FileDownloadUtils;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.order.SearchOrderParam;
import com.bbyiya.vo.user.UUserInfoVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("pbs_orderMgtService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Pbs_OrderMgtServiceImpl implements IPbs_OrderMgtService{
	//客户信息处理
	@Resource(name = "pic_memberMgtService")
	private IPic_MemberMgtService memberMgtService;
	/*------------------订单模块--------------------------------------*/
	@Autowired
	private OUserordersMapper userOrdersMapper;
	@Autowired
	private OUserorderextMapper userOrderextMapper;
	@Autowired 
	private IPic_OrderMgtDao orderDao;
	@Autowired
	private OOrderproductsMapper orderProductMapper;
	@Autowired
	private OOrderproductdetailsMapper detailMapper;
	
	@Autowired
	private OOrderproductphotosMapper photoMapper;
	@Autowired
	private OOrderaddressMapper addressMapper;
	@Autowired
	private OPayorderMapper payoderMapper;
	
	@Autowired
	private OProducerordercountMapper producerOrderMapper;
	
	@Resource(name = "regionServiceImpl")
	private IRegionService regionService;
	
	@Autowired
	private PMyproductdetailsMapper mydetailMapper;
	
	/*----------------------代理模块--------------------------*/
	@Autowired
	private UBranchesMapper branchesMapper;
	@Autowired
	private UBranchtransaccountsMapper branchesTransMapper;
	@Autowired
	private UBranchtransamountlogMapper branchesTransLogMapper;
	
	@Autowired
	private TiPromotersMapper promoterMapper;
	@Autowired
	private TiActivityworksMapper tiworkMapper;
	@Autowired
	private TiMyworksMapper timyworkMapper;
	@Autowired
	private TiMyworkcustomersMapper timyworkcusMapper;
	@Resource(name = "baseUserAccountService")
	private IBaseUserAccountService accountService;
	@Autowired
	private UOtherloginMapper otherloginMapper;
	@Autowired
	private PMyproducttempapplyMapper mytempapplyMapper;
	@Autowired
	private PMyproductactivitycodeMapper activitycodeMapper;
	@Autowired
	private PMyproductsMapper myproductsMapper;
	@Autowired
	private TiProductstylesMapper tistyleMapper;
	
	public PageInfo<PbsUserOrderResultVO> find_pbsOrderList(SearchOrderParam param,Integer type,int index,int size){
		if(param==null)
			param=new SearchOrderParam();
		if(param.getEndTimeStr()!=null&&!param.getEndTimeStr().equals("")){
			param.setEndTimeStr(DateUtil.getEndTime(param.getEndTimeStr()));
		}
		PageHelper.startPage(index, size);
		List<PbsUserOrderResultVO> list=null;
		if(type==null)type=0;
		if(type==0){
			list=orderDao.findPbsUserOrders(param);
		}else{
			list=orderDao.findPbsTiUserOrdersByProducerUserId(param);
		}
		PageInfo<PbsUserOrderResultVO> reuslt=new PageInfo<PbsUserOrderResultVO>(list);
		
		if(list!=null&&list.size()>0){
			for (PbsUserOrderResultVO product : reuslt.getList()) {
				OUserorders order=userOrdersMapper.selectByPrimaryKey(product.getUserorderid());
				product.setOrder(order);
				if(order.getPaytime()!=null)
					product.setPayTimeStr(DateUtil.getTimeStr(order.getPaytime(), "yyyy-MM-dd HH:mm:ss"));
				OOrderaddress address= addressMapper.selectByPrimaryKey(order.getOrderaddressid());
				UBranches branch=null;
				TiMyworks mywork=null;
				TiActivityworks work=null;
				int orderType = order.getOrdertype() == null ? 0 : order.getOrdertype();
				order.setOrdertype(orderType);
				//默认到关联账户流水的关键字为订单号
				product.setPostlogrelationid(order.getUserorderid());
				product.setTiNeedPayPost(0);//默认都需付邮费
				//咿呀12的生产商
				if(orderType==Integer.parseInt(OrderTypeEnum.nomal.toString())||orderType==Integer.parseInt(OrderTypeEnum.brachOrder.toString())){
					branch=branchesMapper.selectByPrimaryKey(order.getBranchuserid());
					if(branch!=null){
						product.setBranchesName(branch.getBranchcompanyname());
					}
					if(orderType==Integer.parseInt(OrderTypeEnum.brachOrder.toString())){
						product.setTiNeedPayPost(1);
					}
				}else{
					
					//台历挂历的订单
					TiPromoters promoters=promoterMapper.selectByPrimaryKey(order.getBranchuserid());
					if(promoters!=null){
						product.setBranchesName(promoters.getCompanyname());
					}
					//普通用户订单
					if(orderType==Integer.parseInt(OrderTypeEnum.ti_nomal.toString())){
						product.setTiNeedPayPost(0);;//默认都省邮费
						//如果是邮寄到推广者地址自提，则需要付邮费
						if(order.getIspromoteraddress()!=null&&order.getIspromoteraddress().intValue()==1){
							product.setTiNeedPayPost(1);
						}
					}
					//推广者下单
					else if(orderType==Integer.parseInt(OrderTypeEnum.ti_branchOrder.toString())){
						product.setTiNeedPayPost(1);//默认都需付邮费
						if(product.getCartid()!=null){
							mywork=timyworkMapper.selectByPrimaryKey(product.getCartid());
							//如果是代客制作
							if(mywork!=null&&mywork.getIsinstead()!=null&&mywork.getIsinstead().intValue()==1){
								product.setTiNeedPayPost(1);
							}else{
								work=tiworkMapper.selectByPrimaryKey(product.getCartid());
								//如果是邮寄到客户地址，则不需要再付邮费
								if(work!=null&&work.getAddresstype()!=null&&work.getAddresstype().intValue()==1){
									product.setTiNeedPayPost(0);
									OPayorder postpayroder=payoderMapper.getpostPayorderByworkId(work.getWorkid().toString());
									if(postpayroder!=null){
										product.setPostlogrelationid(postpayroder.getPayid());
									}
								}
							}
							
						}	
					}
					
				}
				
				OProducerordercount producerorder=producerOrderMapper.selectByPrimaryKey(product.getUserorderid());
				if(producerorder!=null){
					product.setPrintIndex(producerorder.getPrintindex());
				}
				//影楼直接下单
				if (orderType == Integer.parseInt(OrderTypeEnum.brachOrder.toString())) {
					product.setBranchesprovince(address.getProvince());
					product.setBranchesrcity(address.getCity());
					product.setBranchesdistrict(address.getDistrict());
					product.setBranchesAddress(address.getStreetdetail());
					product.setBranchesPhone(address.getPhone());
					product.setBranchesUserName(address.getReciver());
				}else if (orderType == Integer.parseInt(OrderTypeEnum.ti_branchOrder.toString())) {
					//如果是代客制作
					if(mywork!=null&&mywork.getIsinstead()!=null&&mywork.getIsinstead().intValue()==1){
						TiMyworkcustomers mycus=timyworkcusMapper.selectByPrimaryKey(mywork.getWorkid());
						if(mycus!=null&&mycus.getAddresstype()!=null&&mycus.getAddresstype().intValue()==Integer.parseInt(AddressTypeEnum.cusaddr.toString())){
							product.setReciver(address.getReciver());
							product.setBuyerPhone(address.getPhone());
							product.setBuyerprovince(address.getProvince());
							product.setBuyercity(address.getCity());
							product.setBuyerdistrict(address.getDistrict());
							product.setBuyerstreetdetail(address.getStreetdetail());
						}else{
							product.setBranchesprovince(address.getProvince());
							product.setBranchesrcity(address.getCity());
							product.setBranchesdistrict(address.getDistrict());
							product.setBranchesAddress(address.getStreetdetail());
							product.setBranchesPhone(address.getPhone());
							product.setBranchesUserName(address.getReciver());
							if(mycus!=null){
								product.setReciver(mycus.getReciever());
								product.setBuyerPhone(mycus.getRecieverphone());
							}
						}
					}else{
						//如果是老客户回顾活动订单-邮寄到客户地址
						if(work!=null&&work.getAddresstype()!=null&&work.getAddresstype().intValue()==1){
							product.setReciver(address.getReciver());
							product.setBuyerPhone(address.getPhone());
							product.setBuyerprovince(address.getProvince());
							product.setBuyercity(address.getCity());
							product.setBuyerdistrict(address.getDistrict());
							product.setBuyerstreetdetail(address.getStreetdetail());
						}else{
							product.setBranchesprovince(address.getProvince());
							product.setBranchesrcity(address.getCity());
							product.setBranchesdistrict(address.getDistrict());
							product.setBranchesAddress(address.getStreetdetail());
							product.setBranchesPhone(address.getPhone());
							product.setBranchesUserName(address.getReciver());
							if(work!=null){
								product.setReciver(work.getReciever());
								product.setBuyerPhone(work.getMobiephone());
							}
						}
					}
				}else if (orderType == Integer.parseInt(OrderTypeEnum.ti_nomal.toString())) {
					if(order.getIspromoteraddress()!=null&&order.getIspromoteraddress().intValue()==1){
						product.setBranchesprovince(address.getProvince());
						product.setBranchesrcity(address.getCity());
						product.setBranchesdistrict(address.getDistrict());
						product.setBranchesAddress(address.getStreetdetail());
						product.setBranchesPhone(address.getPhone());
						product.setBranchesUserName(address.getReciver());
						OUserorderext orderext=userOrderextMapper.selectByPrimaryKey(order.getUserorderid());
						if(orderext!=null){
							product.setReciver(orderext.getContactname());
							product.setBuyerPhone(orderext.getPhone());
						}			
					}else{
						product.setReciver(address.getReciver());
						product.setBuyerPhone(address.getPhone());
						product.setBuyerprovince(address.getProvince());
						product.setBuyercity(address.getCity());
						product.setBuyerdistrict(address.getDistrict());
						product.setBuyerstreetdetail(address.getStreetdetail());
					}
					
				}else{
					//普通用户下单如果是影楼抢单
					if(orderType==Integer.parseInt(OrderTypeEnum.nomal.toString())&&order.getIsbranch()!=null&&order.getIsbranch()==1){
						if(branch!=null){
							product.setBranchesprovince(regionService.getProvinceName(branch.getProvince()));
							product.setBranchesrcity(regionService.getCityName(branch.getCity()));
							product.setBranchesdistrict(regionService.getAresName(branch.getArea()));
							product.setBranchesAddress(branch.getStreetdetail());
						}
					}
					product.setReciver(address.getReciver());
					product.setBuyerPhone(address.getPhone());
					product.setBuyerprovince(address.getProvince());
					product.setBuyercity(address.getCity());
					product.setBuyerdistrict(address.getDistrict());
					product.setBuyerstreetdetail(address.getStreetdetail());
				}
				
				
			}
		}
		return reuslt;
	}
	
	/**
	 * 修改运单号
	 */
	public ReturnModel editLogistics(String orderId,String expressCom,String expressOrder,String expressCode) throws Exception {
		ReturnModel rq = new ReturnModel();
		OUserorders userorders = userOrdersMapper.selectByPrimaryKey(orderId);
		
		//查找运单号相同的订单号
		if(userorders!=null){
			if(userorders.getStatus().intValue()==Integer.parseInt(OrderStatusEnum.noPay.toString())){
				rq.setStatu(ReturnStatus.ParamError);		
				rq.setStatusreson("未支付的订单不能添加物流信息！");
				return rq;
			}
			List<OUserorders> orderList=orderDao.findUserOrderByExpressOrder(userorders.getExpressorder(), userorders.getExpresscom());
			if(orderList!=null&&orderList.size()>0){
				for (OUserorders order : orderList) {
					order.setExpresscom(expressCom);
					order.setExpressorder(expressOrder);
					order.setExpresscode(expressCode);
					order.setDeliverytime(new Date()); 
					//修改订单状态为已发货状态
					if(order.getStatus()!=null&&order.getStatus().intValue()!=Integer.parseInt(OrderStatusEnum.recived.toString())){
						order.setStatus(Integer.parseInt(OrderStatusEnum.send.toString()));
						if(!order.getUserorderid().equalsIgnoreCase(userorders.getUserorderid())){
							deliverSendMsg(order);
						}
					}
					userOrdersMapper.updateByPrimaryKeySelective(order);
				}
			}
			//  修改本张订单 
			if(userorders.getOrdertype()==null) userorders.setOrdertype(0);
			
			//修改订单状态为已发货状态
			if(userorders.getStatus()!=null&&userorders.getStatus().intValue()!=Integer.parseInt(OrderStatusEnum.recived.toString())){
				userorders.setStatus(Integer.parseInt(OrderStatusEnum.send.toString()));
				deliverSendMsg(userorders);
			}		
			userorders.setExpresscom(expressCom);
			userorders.setExpressorder(expressOrder);
			userorders.setExpresscode(expressCode);
			userorders.setDeliverytime(new Date());
			userOrdersMapper.updateByPrimaryKeySelective(userorders);

			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(userorders);
			rq.setStatusreson("修改运单号成功!");
		}else{
			rq.setStatu(ReturnStatus.ParamError);		
			rq.setStatusreson("orderId参数传入有误！");
			return rq;
		}
		
		return rq;
	}  
	
	public ReturnModel addPostage(String orderId,Double postage) throws Exception {
		ReturnModel rq = new ReturnModel();
		OUserorders userorders = userOrdersMapper.selectByPrimaryKey(orderId);
		if(userorders!=null){
			if(userorders.getStatus().intValue()==Integer.parseInt(OrderStatusEnum.noPay.toString())){
				rq.setStatu(ReturnStatus.ParamError);		
				rq.setStatusreson("未支付的订单不能添加运费！");
				return rq;
			}
			
			if(postage==null)postage=0.0;
			//是否需要付邮费
			boolean isNeedPayPost=this.getIsNeedPayPost(userorders);
			
			if(isNeedPayPost){
				//首次填写运单号，则执行自动扣运费款操作
				if(userorders.getPostage()==null||userorders.getPostage().doubleValue()<=0){
					accountService.add_accountsLog(userorders.getBranchuserid(), Integer.parseInt(AccountLogType.use_freight.toString()), postage, userorders.getPayid(), userorders.getUserorderid());
				}else if(userorders.getPostage()!=null&&userorders.getPostage().doubleValue()>0){
					rq.setStatu(ReturnStatus.OrderError);
					rq.setStatusreson("已完成运费自动扣款操作，不能修改！");
					return rq;
				}
				userorders.setPostage(postage);
			}
			
			//修改订单状态为已发货状态
			if(userorders.getStatus()!=null&&userorders.getStatus().intValue()!=Integer.parseInt(OrderStatusEnum.recived.toString())){
				userorders.setStatus(Integer.parseInt(OrderStatusEnum.send.toString()));
				deliverSendMsg(userorders);
			}
			userorders.setDeliverytime(new Date());
			userOrdersMapper.updateByPrimaryKeySelective(userorders);
			
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(userorders);
			rq.setStatusreson("添加运费成功!");
		}else{
			rq.setStatu(ReturnStatus.ParamError);		
			rq.setStatusreson("orderId参数传入有误！");
			return rq;
		}
		
		return rq;
	}  
	
	
	public boolean getIsNeedPayPost(OUserorders userorders){
		//是否需要付邮费
		boolean isNeedPayPost=false;
		int ordertype=(userorders.getOrdertype()==null?0:userorders.getOrdertype());
		//咿呀12的生产商
		if(ordertype==Integer.parseInt(OrderTypeEnum.nomal.toString())){
			isNeedPayPost=false;
		}else if(ordertype==Integer.parseInt(OrderTypeEnum.brachOrder.toString())){
			isNeedPayPost=true;
		}else if(ordertype==Integer.parseInt(OrderTypeEnum.ti_nomal.toString())){
			isNeedPayPost=false;
			//如果是邮寄到推广者地址自提，则需要付邮费
			if(userorders.getIspromoteraddress()!=null&&userorders.getIspromoteraddress().intValue()==1){
				isNeedPayPost=true;
			}
		}else if(ordertype==Integer.parseInt(OrderTypeEnum.ti_branchOrder.toString())){
			isNeedPayPost=true;//默认都需付邮费
			OOrderproducts product=orderProductMapper.getOProductsByOrderId(userorders.getUserorderid());
			
			if(product.getCartid()!=null){
				TiMyworks mywork=timyworkMapper.selectByPrimaryKey(product.getCartid());
				//如果是代客制作,都需要扣影楼邮费
				if(mywork!=null&&mywork.getIsinstead()!=null&&mywork.getIsinstead().intValue()==1){
					isNeedPayPost=true;
				}else{
					TiActivityworks work=tiworkMapper.selectByPrimaryKey(product.getCartid());
					//如果是邮寄到客户地址，则不需要再付邮费
					if(work!=null&&work.getAddresstype()!=null&&work.getAddresstype().intValue()==1){
						isNeedPayPost=false;
					}else{
						isNeedPayPost=true;
					}
				}
				
			}
		}	
		return isNeedPayPost;
	}
	/**
	 * 多个订单是否可以运单合并
	 * 条件： 1：如果有多个订单有不同运单信息，则不能合并
	 * 		 2：必须是同一用户的订单才能进行合单操作
	 * 	     2：如果所有订单都没有运单信息，则需要弹出录入运单的弹出框
	 *       3：如果只有一个订单有运单信息，则按有运单号的订单补录其它订单运单信息
	 *       
	 * @param orderIds
	 * @param postage
	 * @return
	 * @throws Exception
	 */
	public ReturnModel isCanMergeOrderLogistic(String orderIds) throws Exception {
		ReturnModel rq = new ReturnModel();
		if(orderIds==null||orderIds.equals("")){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("订单号不能为空！");
			return rq;
		}
		String orderArr[]=orderIds.split(",");
		HashMap<Long,String> userIdHash=new HashMap<Long,String>();
		HashMap<String,OUserorders> expressHash=new HashMap<String, OUserorders>();
		HashMap<Integer,String> typeHash=new HashMap<Integer,String>();
		HashMap<String,Object> result=new HashMap<String, Object>();

		int ordertypeLast=0;
		if(orderArr!=null&&orderArr.length>0){
			for (String orderid : orderArr) {
				OUserorders order=userOrdersMapper.selectByPrimaryKey(orderid);
				if(order.getStatus().intValue()==Integer.parseInt(OrderStatusEnum.noPay.toString())){
					rq.setStatu(ReturnStatus.ParamError);		
					rq.setStatusreson("存在未支付的订单，不能进行运单合单操作！");
					return rq;
				}
				boolean isNeedPayPost=this.getIsNeedPayPost(order);
				if(!isNeedPayPost){
					rq.setStatu(ReturnStatus.ParamError);		
					rq.setStatusreson("存在不需要付邮费的订单，不能进行运单合单操作！");
					return rq;
				}
				if(order!=null){
					int type=order.getOrdertype()==null?0:order.getOrdertype();
					if(!typeHash.containsKey(type)){
						typeHash.put(type,"ordertype");
						ordertypeLast=type;
					}
				}
			}
			if(typeHash.size()>1){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("不同用户订单不能进行运单合单操作！");
				return rq;
			}
		}
		if(orderArr!=null&&orderArr.length>0){
			for (String orderid : orderArr) {
				OUserorders order=userOrdersMapper.selectByPrimaryKey(orderid);
				if(order!=null){
					//如果是代理商订单合单
					if(ordertypeLast==Integer.parseInt(OrderTypeEnum.brachOrder.toString())||ordertypeLast==Integer.parseInt(OrderTypeEnum.ti_branchOrder.toString())){
						if(!userIdHash.containsKey(order.getBranchuserid())){
							userIdHash.put(order.getBranchuserid(), order.getUserorderid());
						}
						
						String express=(order.getExpresscom()==null?"":order.getExpresscom())+(order.getExpressorder()==null?"":order.getExpressorder())+((order.getPostage()==null||order.getPostage()==0)?"":order.getPostage());
						if(express!=null&&express.length()>0){
							expressHash.put(order.getExpressorder(), order);
						}
					}else{
						//否则就是普通用户的合单
						if(!userIdHash.containsKey(order.getUserid())){
							userIdHash.put(order.getUserid(), order.getUserorderid());
						}
						String express=(order.getExpresscom()==null?"":order.getExpresscom())+(order.getExpressorder()==null?"":order.getExpressorder());
						if(express!=null&&express.length()>0){
							expressHash.put(order.getExpressorder(), order);
						}
					}
					
					
				}
			}
			if(userIdHash.size()>1){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("不同用户的订单不能进行运单合单操作！");
				return rq;
			}
			if(expressHash.size()>0){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("存在含用运单信息的订单，不能合单！");
				return rq;
			}
			
			result.put("orderids", orderIds);
			result.put("ordertype", ordertypeLast);
			rq.setBasemodle(result);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("");
		
		}
		return rq;
	}
	
	/**
	 * 
	 * @param type 1:所有订单没有运单信息，2：有其中一张有运单信息
	 * @param orderIds 需要补填运单信息的运单号
	 * @param expressCom
	 * @param expressOrder
	 * @param postage
	 * @return
	 * @throws Exception
	 */
	public ReturnModel MergeOrderLogistic(int ordertype,String orderIds,String expressCom,String expressOrder,Double postage,String expressCode) throws Exception {
		ReturnModel rq = new ReturnModel();
		if(orderIds==null||orderIds.equals("")){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("订单号不能为空！");
			return rq;
		}
		//再次校验一下
		rq=isCanMergeOrderLogistic(orderIds);
		if(rq.getStatu()!=ReturnStatus.Success){			
			return rq;
		}
		
		String orderArr[]=orderIds.split(",");
		if(orderArr!=null&&orderArr.length>0){
			for(int i=0;i<orderArr.length;i++){
				OUserorders userorders = userOrdersMapper.selectByPrimaryKey(orderArr[i]);
				if(userorders!=null){
					userorders.setExpresscom(expressCom);
					userorders.setExpressorder(expressOrder);
					userorders.setExpresscode(expressCode);
					
					//影楼订单只能第一张单且ordertype=1才会自动扣款
					if(i==0&&ordertype==Integer.parseInt(OrderTypeEnum.brachOrder.toString())){
						ReturnModel rqmodel=addPostage(orderArr[i], postage);
						if(rqmodel.getStatu()!=ReturnStatus.Success){
							return rqmodel;
						}	
					}else if(i==0&&ordertype==Integer.parseInt(OrderTypeEnum.ti_branchOrder.toString())){
						ReturnModel rqmodel=addPostage(orderArr[i], postage);
						if(rqmodel.getStatu()!=ReturnStatus.Success){
							return rqmodel;
						}	
					}else if(i==0&&ordertype==Integer.parseInt(OrderTypeEnum.ti_nomal.toString())){
						ReturnModel rqmodel=addPostage(orderArr[i], postage);
						if(rqmodel.getStatu()!=ReturnStatus.Success){
							return rqmodel;
						}	
					}else{
						userorders.setPostage(0.0);
					}
					
					//修改订单状态为已发货状态
					if(userorders.getStatus()!=null&&userorders.getStatus().intValue()!=Integer.parseInt(OrderStatusEnum.recived.toString())){
						userorders.setStatus(Integer.parseInt(OrderStatusEnum.send.toString()));
						deliverSendMsg(userorders);
					}
					userorders.setDeliverytime(new Date());
					userOrdersMapper.updateByPrimaryKeySelective(userorders);
				}
			}
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("合并运单信息成功！");
		return rq;
		
	}
	public String pbsdownloadImg(List<PbsUserOrderResultVO> orderlist){
		String sep=System.getProperty("file.separator");
		String  basePath = System.getProperty("user.home") +sep + "imagedownloadtemp"+sep+"orderImg";
		FileUtils.isDirExists(basePath);
		Calendar c1 =  Calendar.getInstance();
		Date nowtime=new Date();
		c1.setTime(nowtime); 
		String file_temp=DateUtil.getTimeStr(c1.getTime(), "yyyyMMddHHmm");
		//创建文件夹
		FileUtils.isDirExists(basePath+sep+file_temp);
		for (PbsUserOrderResultVO order : orderlist) {
			FileUtils.isDirExists(basePath+sep+file_temp+sep+order.getBuyeruserid()+"-"+order.getProducttitle()+"-"+order.getPropertystr().replaceAll("/", "-")+"×"+order.getCount()+"("+order.getUserorderid()+")");
			int i=1;			
			List<OOrderproductdetails> detallist=orderDao.findOrderProductDetailsByProductOrderId(order.getOrderproductid());
			for (OOrderproductdetails detail : detallist) {
				if(detail.getImageurl()!=null)
					detail.setImageurl("http://pic.bbyiya.com/"+detail.getImageurl());
				if(detail.getBackimageurl()!=null)
					detail.setBackimageurl("http://pic.bbyiya.com/"+detail.getBackimageurl()); 
				
				String file_dir=basePath+sep+file_temp+sep+order.getBuyeruserid()+"-"+order.getProducttitle()+"-"+order.getPropertystr().replaceAll("/", "-")+"×"+order.getCount()+"("+order.getUserorderid()+")";
				
				String fileFull_name=file_dir+sep+i+".jpg";
				i++;
				String filebackFull_name=file_dir+sep+i+".jpg";
				if(!FileUtils.isFileExists(fileFull_name)){
					try {
						if(!ObjectUtil.isEmpty(detail.getImageurl())){
							FileDownloadUtils.download(detail.getImageurl(),fileFull_name);
							FileDownloadUtils.setDPI(fileFull_name);
						}
						
						if(!ObjectUtil.isEmpty(detail.getBackimageurl())){
							FileDownloadUtils.download(detail.getBackimageurl(),filebackFull_name);
							FileDownloadUtils.setDPI(filebackFull_name);
							
						}
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				i++;
				
			}
			
		}
		
		//压缩成zip文件		
		FileToZip z = new FileToZip();  
		z.zip(basePath+sep+file_temp, basePath+sep+file_temp+".zip"); 	
		File file = new File(basePath+sep+file_temp+".zip");
		z.deleteDirectory(basePath+sep+file_temp);
		return file.getPath();
	}
	
	/**
	 * 下载原始图片
	 */
	public String pbsdownloadOriginalImage(List<PbsUserOrderResultVO> orderlist){
		String sep=System.getProperty("file.separator");
		String  basePath = System.getProperty("user.home") +sep + "imagedownloadtemp"+sep+"orderImg";
		FileUtils.isDirExists(basePath);
		Calendar c1 =  Calendar.getInstance();
		Date nowtime=new Date();
		c1.setTime(nowtime); 
		String file_temp=DateUtil.getTimeStr(c1.getTime(), "yyyyMMddHHmm");
		//创建文件夹
		FileUtils.isDirExists(basePath+sep+file_temp);
		for (PbsUserOrderResultVO order : orderlist) {
			//为了兼容以前的版本，如果没有订单原图，则取作品的原图
			List<OOrderproductphotos> photoList=photoMapper.findOrderProductPhotosByProductOrderId(order.getOrderproductid());
			if(photoList==null||photoList.size()<=0){
				List<PMyproductdetails> detailsList=mydetailMapper.findMyProductdetails(order.getCartid());
				if(detailsList!=null&&detailsList.size()>0){
					photoList=new ArrayList<OOrderproductphotos>();
					for (PMyproductdetails pde : detailsList) {
						OOrderproductphotos photo=new OOrderproductphotos();
						photo.setContent(pde.getContent());
						photo.setCreatetime(new Date());
						photo.setImgurl(pde.getImgurl());
						photo.setOrderproductid(order.getOrderproductid());
						photo.setSenendes(pde.getDescription());
						photo.setSort(pde.getSort());
						photo.setTitle(pde.getTitle());
						photoList.add(photo);
					}
				}
			}

			FileUtils.isDirExists(basePath+sep+file_temp+sep+order.getBuyeruserid()+"-"+order.getProducttitle()+"-"+order.getPropertystr().replaceAll("/", "-")+"×"+order.getCount()+"("+order.getUserorderid()+")");
			int i=1;		
			for (OOrderproductphotos photo : photoList) {
				if(photo.getImgurl()!=null)
					photo.setImgurl("http://pic.bbyiya.com/"+photo.getImgurl());
				String file_dir=basePath+sep+file_temp+sep+order.getBuyeruserid()+"-"+order.getProducttitle()+"-"+order.getPropertystr().replaceAll("/", "-")+"×"+order.getCount()+"("+order.getUserorderid()+")";
				String fileFull_name=file_dir+sep+i+".jpg";
				if(!FileUtils.isFileExists(fileFull_name)){
					try {
						if(!ObjectUtil.isEmpty(photo.getImgurl())){
							FileDownloadUtils.download(photo.getImgurl(),fileFull_name);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				i++;
				
			}
		}
		//压缩成zip文件		
		FileToZip z = new FileToZip();  
		z.zip(basePath+sep+file_temp, basePath+sep+file_temp+".zip"); 	
		File file = new File(basePath+sep+file_temp+".zip");
		z.deleteDirectory(basePath+sep+file_temp);
		return file.getPath();
	}
	
	/**
	 * 发货发送微消息
	 * @param order
	 */
	public void deliverSendMsg(OUserorders userorders){
		int ordertype=(userorders.getOrdertype()==null?0:userorders.getOrdertype());
		ShippingParam param=new ShippingParam();
		param.setOrderId(userorders.getUserorderid());
		param.setTransCompany(userorders.getExpresscom());
		param.setTransOrderId(userorders.getExpressorder());
		param.setRemark(userorders.getRemark());
		OOrderaddress addr=addressMapper.selectByPrimaryKey(userorders.getOrderaddressid());
		if(addr==null) return;
		if(addr!=null){
			SmsParam sendparam=new SmsParam();
			sendparam.setTransName(userorders.getExpresscom());
			sendparam.setTransNum(userorders.getExpressorder());
			SendSMSByMobile.sendSmS(Integer.parseInt(SendMsgEnums.delivery.toString()), addr.getPhone(), sendparam);
			param.setAddress(addr.getStreetdetail());
		}
		UOtherlogin user=null;
		//咿呀12的生产商普通用户下单
		if(ordertype==Integer.parseInt(OrderTypeEnum.nomal.toString())){
			user=otherloginMapper.getWxloginByUserId(userorders.getUserid());
			param.setTotalPrice(userorders.getOrdertotalprice());
			WechatMsgUtil.sendMsg_Shipping(user.getOpenid(), param);
			return;
			
		}else if(ordertype==Integer.parseInt(OrderTypeEnum.brachOrder.toString())){
			//咿呀12的影楼的订单
			OOrderproducts product= orderProductMapper.getOProductsByOrderId(userorders.getUserorderid());
			if(product!=null){
				PMyproducts cart= myproductsMapper.selectByPrimaryKey(product.getCartid());
				if(cart!=null){
					user=otherloginMapper.getWxloginByUserId(cart.getUserid());
					param.setTotalPrice(userorders.getOrdertotalprice());
					WechatMsgUtil.sendMsg_Shipping(user.getOpenid(), param);
					return;
				}
			}
		}else if(ordertype==Integer.parseInt(OrderTypeEnum.ti_nomal.toString())){
			user=otherloginMapper.getWxloginByUserId(userorders.getUserid());
			param.setTotalPrice(userorders.getOrdertotalprice());
			WechatMsgUtil.sendMsg_Shipping(user.getOpenid(), param);
			return;	
		}else if(ordertype==Integer.parseInt(OrderTypeEnum.ti_branchOrder.toString())){
			OOrderproducts product=orderProductMapper.getOProductsByOrderId(userorders.getUserorderid());
			if(product.getCartid()!=null){
				TiMyworks mywork=timyworkMapper.selectByPrimaryKey(product.getCartid());
				TiProductstyles style=tistyleMapper.selectByPrimaryKey(product.getStyleid());
				//如果是活动订单：
				if(mywork!=null&&mywork.getIsinstead()!=null&&mywork.getIsinstead().intValue()==1){
					//代客制作
					user=otherloginMapper.getWxloginByUserId(mywork.getUserid());
					param.setTotalPrice(style.getPromoterprice()*product.getCount()+(userorders.getPostage()==null?0:userorders.getPostage()));
					WechatMsgUtil.sendMsg_Shipping(user.getOpenid(), param);
					return;
				}else if(mywork!=null&&(mywork.getIsinstead()==null||mywork.getIsinstead().intValue()!=1)){
					//老客户回顾
					user=otherloginMapper.getWxloginByUserId(mywork.getUserid());
					param.setTotalPrice(style.getPrice()*product.getCount()+(userorders.getPostage()==null?0:userorders.getPostage()));
					WechatMsgUtil.sendMsg_Shipping(user.getOpenid(), param);
					return;
				}else{
					user=otherloginMapper.getWxloginByUserId(userorders.getUserid());
					param.setTotalPrice(userorders.getOrdertotalprice());
					WechatMsgUtil.sendMsg_Shipping(user.getOpenid(), param);
					return;
				}
				
			}
		}	
	}
	
}
