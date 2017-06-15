package com.bbyiya.pic.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.OOrderaddressMapper;
import com.bbyiya.dao.OOrderproductdetailsMapper;
import com.bbyiya.dao.OOrderproductphotosMapper;
import com.bbyiya.dao.OOrderproductsMapper;
import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.PMyproductchildinfoMapper;
import com.bbyiya.dao.PMyproductdetailsMapper;
import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.UBranchesMapper;
import com.bbyiya.enums.CustomerSourceTypeEnum;
import com.bbyiya.enums.OrderStatusEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.BranchStatusEnum;
import com.bbyiya.model.OOrderaddress;
import com.bbyiya.model.OOrderproductdetails;
import com.bbyiya.model.OOrderproductphotos;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.PMyproductchildinfo;
import com.bbyiya.model.PMyproductdetails;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.UAgentcustomers;
import com.bbyiya.model.UBranches;
import com.bbyiya.pic.dao.IPic_OrderMgtDao;
import com.bbyiya.pic.service.IPic_MemberMgtService;
import com.bbyiya.pic.service.IPic_OrderMgtService;
import com.bbyiya.pic.vo.order.SearchOrderParam;
import com.bbyiya.pic.vo.order.UserOrderResultVO;
import com.bbyiya.pic.vo.order.ibs.OrderPhotoVo;
import com.bbyiya.pic.vo.order.ibs.OrderProductVo;
import com.bbyiya.pic.vo.order.ibs.OrderVo;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.FileUtils;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.PageInfoUtil;
import com.bbyiya.utils.upload.FileDownloadUtils;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.agent.UBranchUserTempVo;
import com.bbyiya.vo.order.UserBuyerOrderResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sdicons.json.validator.impl.predicates.Str;

@Service("pic_orderMgtService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Pic_OrderMgtServiceImpl implements IPic_OrderMgtService{
	//客户信息处理
	@Resource(name = "pic_memberMgtService")
	private IPic_MemberMgtService memberMgtService;
	/*----------------------订单模块--------------------------------------*/
	@Autowired
	private OUserordersMapper userOrdersMapper;
	@Autowired
	private IPic_OrderMgtDao orderDao;
	@Autowired
	private OOrderproductsMapper orderProductMapper;
	@Autowired
	private OOrderproductdetailsMapper detailMapper;
	@Autowired
	private OOrderproductphotosMapper ophotosMapper;
	@Autowired
	private OOrderaddressMapper addressMapper;
	@Autowired
	private PMyproductdetailsMapper mydetailMapper;
	/*----------------------代理模块--------------------------*/
	@Autowired
	private UBranchesMapper branchesMapper;
	@Autowired
	private PMyproductchildinfoMapper pmyChildMapper;
	/**
	 * 获取订单列表
	 * @param userId
	 * @return
	 */
	public ReturnModel find_orderList(Long userId){
		ReturnModel rq=new ReturnModel();
		return rq;
	}
	
	public ReturnModel find_orderList(SearchOrderParam param){
		ReturnModel rq=new ReturnModel();
		if(param==null)
			param=new SearchOrderParam();
		List<UserOrderResultVO> list=orderDao.findUserOrders(param);
		if(list!=null&&list.size()>0){
			for (UserOrderResultVO order : list) {
				OOrderaddress address= addressMapper.selectByPrimaryKey(order.getOrderaddressid());
				if(address!=null){
					order.setAddress(address);
				} 
				List<OOrderproducts> proList= orderProductMapper.findOProductsByOrderId(order.getUserorderid());
				if(proList!=null&&proList.size()>0){
					for (OOrderproducts pro : proList) {
						List<OOrderproductdetails> detailslist= orderDao.findOrderProductDetailsByProductOrderId(pro.getOrderproductid());
						if(detailslist!=null&&detailslist.size()>0){
							for (OOrderproductdetails dd : detailslist) {
								dd.setImageurl("http://pic.bbyiya.com/"+dd.getImageurl()); 
							}
						}
						order.setImglist(detailslist); 
					}
				}
			}
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(list); 
		return rq;
	}
	
	/**
	 * 获取待分配的订单（IBS用）
	 * @param branchUserId
	 * @return
	 */
	public ReturnModel findAgentOrders(Long branchUserId){
		ReturnModel rq = new ReturnModel();
		UBranches branches = branchesMapper.selectByPrimaryKey(branchUserId);
		if (branches != null && branches.getStatus().intValue() == Integer.parseInt(BranchStatusEnum.ok.toString())) {
			// if(true){
			List<OUserorders> userorders = userOrdersMapper.findOrdersByAgentUserId(branches.getAgentuserid());
			if (userorders != null && userorders.size() > 0) {
				List<Long> ids = new ArrayList<Long>();
				for (OUserorders oo : userorders) {
					ids.add(oo.getOrderaddressid());
				}
				List<OOrderaddress> addressList = addressMapper.findListByIds(ids);
				List<OrderVo> resultlist = new ArrayList<OrderVo>();
				for (OUserorders order : userorders) {
					OrderVo vo = new OrderVo();
					vo.setUserorderid(order.getUserorderid());
					vo.setStatus(order.getStatus());
					vo.setUserid(order.getUserid());
					vo.setBranchuserid(order.getBranchuserid());
					for (OOrderaddress addr : addressList) {
						if (addr.getOrderaddressid().longValue() == order.getOrderaddressid().longValue()) {
							vo.setAddress(addr);
						}
					}
					resultlist.add(vo);
				}
				rq.setBasemodle(resultlist);
			}
			rq.setStatu(ReturnStatus.Success);
		} else {
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("您还不是合作商，权限不足！");
		}
		return rq;
	}
	@Autowired
	private PMyproductsMapper myproductsMapper;
	public ReturnModel findMyOrderlist(Long branchUserId,Integer status,String keywords,int index,int size){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.Success);
		PageHelper.startPage(index, size);
		//订单列表
		List<OUserorders> userorders= userOrdersMapper.findOrdersByBranchUserId(branchUserId,status,keywords);
		PageInfo<OUserorders> resultPage=new PageInfo<OUserorders>(userorders); 
		if(resultPage!=null&&resultPage.getList()!=null&&resultPage.getList().size()>0){
			List<Long> ids = new ArrayList<Long>();
			for (OUserorders oo : resultPage.getList()) {
				ids.add(oo.getOrderaddressid());
			}
			//订单的收货地址
			List<OOrderaddress> addressList = addressMapper.findListByIds(ids);
			List<OrderVo> resultlist = new ArrayList<OrderVo>();
			for (OUserorders order : userorders) {
				OrderVo vo = new OrderVo();
				vo.setUserorderid(order.getUserorderid());
				vo.setStatus(order.getStatus());
				vo.setUserid(order.getUserid());
				vo.setBranchuserid(order.getBranchuserid());
				vo.setPaytime(DateUtil.getTimeStr(order.getPaytime(), "yyyy-MM-dd HH:mm:ss"));
				for (OOrderaddress addr : addressList) {
					if (addr.getOrderaddressid().longValue() == order.getOrderaddressid().longValue()) {
						vo.setAddress(addr);
					}
				}
				OOrderproducts product= orderProductMapper.getOProductsByOrderId(order.getUserorderid());
				if(product!=null){
					OrderProductVo oproduct=new OrderProductVo();
					oproduct.setProducttitle(product.getProducttitle());
					oproduct.setPropertystr(product.getPropertystr());
					oproduct.setPrice(product.getPrice());
					oproduct.setCartid(product.getCartid());
					PMyproducts cart= myproductsMapper.selectByPrimaryKey(product.getCartid());
					if(cart!=null){
						oproduct.setCartAuthor(cart.getAuthor());
						oproduct.setCartTitle(cart.getTitle());
					}
					vo.setOrderProduct(oproduct);
				}
				resultlist.add(vo);
			}	
			PageInfoUtil<OrderVo> resultPagelist=new PageInfoUtil<OrderVo>(resultPage, resultlist);
			rq.setBasemodle(resultPagelist);
		}
		rq.setStatusreson("ok");
		return rq;
	}
	
	/**
	 * 我要这个客户
	 * @param branchUserId
	 * @param userOrderId
	 * @return
	 */
	public ReturnModel addCustomer(Long branchUserId, String userOrderId){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);
		OUserorders order=userOrdersMapper.selectByPrimaryKey(userOrderId);
		if(order!=null){
			if(order.getAgentuserid()!=null&&order.getAgentuserid()>0){
				if(order.getIsbranch()!=null&&order.getIsbranch()==1){//可以抢
					rq.setStatusreson("不好意思，此客户已经被分配了！");
					return rq; 
				}
				//我的影楼信息，找到我的代理商
				UBranches branches=branchesMapper.selectByPrimaryKey(branchUserId);
				if(branches!=null&&branches.getStatus()!=null&&branches.getStatus().intValue()==Integer.parseInt(BranchStatusEnum.ok.toString())){
					 //我是代理影楼，具备抢客户的权利
					if(order.getAgentuserid().longValue()==branches.getAgentuserid().longValue()){
						 //设置订单信息已经被分配
						 order.setIsbranch(1);
						 order.setBranchuserid(branchUserId);
						 userOrdersMapper.updateByPrimaryKeySelective(order);
						 
						 OOrderaddress address=addressMapper.selectByPrimaryKey(order.getOrderaddressid());
						 if(address!=null){
							 //客户信息
							 UAgentcustomers customer=new UAgentcustomers();
							 customer.setAgentuserid(branches.getAgentuserid());
							 customer.setBranchuserid(branchUserId);
							 customer.setCreatetime(new Date());
							 customer.setUserid(order.getUserid());
							 customer.setPhone(address.getPhone());
							 customer.setName(address.getReciver());
							 customer.setRemark(address.getStreetdetail());
							 customer.setAddress(address.getStreetdetail());
							 customer.setIsmarket(0);
							 customer.setSourcetype(Integer.parseInt(CustomerSourceTypeEnum.other.toString()));
							 memberMgtService.addCustomer(branchUserId, customer);
							 rq.setStatu(ReturnStatus.Success);
							 rq.setStatusreson("顾客成功锁定！");
							 return rq;
						 }else {
							rq.setStatusreson("找不到用户收货信息！");
							return rq;
						 }
					}else {
						rq.setStatusreson("此订单不在您的代理范围！");
						return rq;
					}
				}else {
					rq.setStatusreson("您还不是合作商，权限不足！");
					return rq;
				}
			}else {
				rq.setStatusreson("非代理区域订单，暂不能分配！");
				return rq;
			}
			
		}else {
			rq.setStatusreson("订单不存在");
		}
		return rq;
	}
	
	/**
	 * 获取订单详情
	 * @param userOrderId
	 * @return
	 */
	public ReturnModel getOrderDetail(String userOrderId) {
		ReturnModel rq=new ReturnModel();
		OUserorders order = userOrdersMapper.selectByPrimaryKey(userOrderId);
		if (order != null) {
			OrderVo vo = new OrderVo();
			vo.setUserorderid(order.getUserorderid());
			vo.setStatus(order.getStatus());
			vo.setUserid(order.getUserid());
			vo.setBranchuserid(order.getBranchuserid());
			if(!ObjectUtil.isEmpty(order.getPaytime())){
				vo.setPaytime(DateUtil.getTimeStr(order.getPaytime(), "yyyy-MM-dd HH:mm:ss") );
			}
			vo.setExpresscom(order.getExpresscom());
			vo.setExpressorder(order.getExpressorder());
			vo.setPostage(order.getPostage());
			vo.setAddress(addressMapper.selectByPrimaryKey(order.getOrderaddressid()));
			OOrderproducts product=orderProductMapper.getOProductsByOrderId(userOrderId);
			if(product!=null){
				OrderProductVo oproduct=new OrderProductVo();
				oproduct.setProducttitle(product.getProducttitle());
				oproduct.setPropertystr(product.getPropertystr());
				oproduct.setPrice(product.getPrice());
				oproduct.setCartid(product.getCartid());
				PMyproducts cart= myproductsMapper.selectByPrimaryKey(product.getCartid());
				if(cart!=null){
					oproduct.setCartAuthor(cart.getAuthor());
					oproduct.setCartTitle(cart.getTitle());
				}
				vo.setOrderProduct(oproduct);
			} 
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(vo); 
			rq.setStatusreson("ok");
		}else {
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("不存的订单");
		}
		return rq;
	}
	
	/**
	 * 获取订单作品图片
	 * @param userOrderId
	 * @return
	 */
	public ReturnModel getOrderPhotos(String userOrderId) {
		ReturnModel rq=new ReturnModel();
		OOrderproducts product=orderProductMapper.getOProductsByOrderId(userOrderId);
		if (product != null) {
			OrderPhotoVo vo = new OrderPhotoVo();
			PMyproducts cart= myproductsMapper.selectByPrimaryKey(product.getCartid());
			if(cart!=null){
				vo.setCartauthor(cart.getAuthor());
				vo.setCarttitle(cart.getTitle());
				vo.setCartid(product.getCartid());
			}
			List<OOrderproductphotos> photoList=ophotosMapper.findOrderProductPhotosByProductOrderId(product.getOrderproductid());
			if(photoList==null||photoList.size()<=0){
				List<PMyproductdetails> detailsList=mydetailMapper.findMyProductdetails(product.getCartid());
				if(detailsList!=null&&detailsList.size()>0){
					photoList=new ArrayList<OOrderproductphotos>();
					for (PMyproductdetails pde : detailsList) {
						OOrderproductphotos photo=new OOrderproductphotos();
						photo.setContent(pde.getContent());
						photo.setCreatetime(new Date());
						photo.setImgurl(pde.getImgurl());
						photo.setOrderproductid(product.getOrderproductid());
						photo.setSenendes(pde.getDescription());
						photo.setSort(pde.getSort());
						photo.setTitle(pde.getTitle());
						photoList.add(photo);
					}
				}
			}
			
			if(photoList!=null&&photoList.size()>0){
				vo.setPhotos(photoList);
			}
			
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(vo); 
			rq.setStatusreson("ok");
		}else {
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("不存的订单");
		}
		return rq;
	}
	
	/**
	 * 取消订单
	 * @param userId
	 * @return
	 */
	public ReturnModel cancelOrder(String orderId) {
		ReturnModel rq = new ReturnModel();
		OUserorders userorders = userOrdersMapper.selectByPrimaryKey(orderId);
		//如果不是未支付的订单不能取消订单
		if(userorders!=null&&userorders.getStatus()!=Integer.parseInt(OrderStatusEnum.noPay.toString())){
			rq.setStatusreson("已支付的订单不能取消订单！");
			rq.setStatu(ReturnStatus.OrderError);
			return rq;
		}
		if(userorders!=null){
			List<OOrderproducts> productList=orderProductMapper.findOProductsByOrderId(userorders.getUserorderid());
			if(productList!=null&&productList.size()>0){
				for (OOrderproducts pro : productList) {
					List<OOrderproductdetails> detailslist= orderDao.findOrderProductDetailsByProductOrderId(pro.getOrderproductid());
					if(detailslist!=null&&detailslist.size()>0){
						for (OOrderproductdetails detail : detailslist) {
							detailMapper.deleteByPrimaryKey(detail.getOproductdetailid());
						}
					}
					orderProductMapper.deleteByPrimaryKey(pro.getOrderproductid());
				}
			}
			if(userorders.getOrderaddressid()!=null){
				OOrderaddress address=addressMapper.selectByPrimaryKey(userorders.getOrderaddressid());
				if(address!=null)
					addressMapper.deleteByPrimaryKey(address.getOrderaddressid());
			}	
			userOrdersMapper.deleteByPrimaryKey(userorders.getUserorderid());
		}
		rq.setBasemodle(null);
		rq.setStatusreson("取消订单成功！");
		rq.setStatu(ReturnStatus.Success);
		return rq;
	}
	/**
	 * 得到订单产品的作品详情，用于重复下单的效果浏览
	 * @param orderProductId
	 * @return
	 */
	public List<OOrderproductdetails> getOrderProductdetails(String orderProductId) {
		List<OOrderproductdetails> detailslist= orderDao.findOrderProductDetailsByProductOrderId(orderProductId);
		return detailslist;
	}
	
	public ReturnModel getOrderProductdetailsByUserOrderId(String userOrderId){
		ReturnModel rq=new ReturnModel();
		OOrderproducts orderproducts= orderProductMapper.getOProductsByOrderId(userOrderId);
		if(orderproducts!=null){
			long temp=orderproducts.getStyleid()%2;
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("type", temp);
			List<Map<String, String>> coversList=ConfigUtil.getMaplist("styleCovers");
			if(coversList!=null&&coversList.size()>0){
				for (Map<String, String> cover : coversList) {
					if(ObjectUtil.parseLong(cover.get("styleId"))==orderproducts.getStyleid().longValue()){
						map.put("headImg",cover.get("url"));
					}
				}
			}
			
			map.put("details", getOrderProductdetails(orderproducts.getOrderproductid()));
			rq.setBasemodle(map); 
			rq.setStatu(ReturnStatus.Success);
		}
		return rq;
	}
	
//	public List<OOrderproductphotos> findOrderProductPhotoListByUserOrderId(String userOrderId){
//		OUserorders userorders = userOrdersMapper.selectByPrimaryKey(userOrderId);
//		OOrderproducts orderproducts = orderProductMapper.getOProductsByOrderId(userOrderId);
//		if (userorders != null && orderproducts != null) {
//			List<OOrderproductphotos> photosList = ophotosMapper.findOrderProductPhotosByProductOrderId(orderproducts.getOrderproductid());
//			if (photosList != null && photosList.size() > 0) {
//				return photosList;
//			} else if (orderproducts.getCartid() != null) {
//				List<PMyproductdetails> details = mydetailMapper.findMyProductdetails(orderproducts.getCartid());
//				if (details != null && details.size() > 0) {
//					photosList = new ArrayList<OOrderproductphotos>();
//					Date nowtime = new Date();
//					String base_code = userorders.getUserid() + "-" + orderproducts.getCartid();
//					int i = 1;
//					for (PMyproductdetails dd : details) {
//						OOrderproductphotos ph = new OOrderproductphotos();
//						ph.setOrderproductid(orderproducts.getOrderproductid());
//						ph.setImgurl(dd.getImgurl());
//						ph.setContent(dd.getContent());
//						ph.setSenendes(dd.getDescription());
//						ph.setTitle(dd.getTitle());
//						ph.setSort(dd.getSort());
//						ph.setCreatetime(nowtime);
//						ph.setPrintno(base_code + "-" + String.format("%02d", i));
//						i++;
//						photosList.add(ph);
//						ophotosMapper.insert(ph);
//					}
//					return photosList;
//				}
//			}
//		}
//		return null;
//	}
	
	public ReturnModel findOrderProductPhotosByUserOrderId(String userOrderId){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);
		OUserorders userorders=userOrdersMapper.selectByPrimaryKey(userOrderId);
		OOrderproducts orderproducts= orderProductMapper.getOProductsByOrderId(userOrderId);
		if(userorders!=null&&orderproducts!=null){
			List<OOrderproductphotos> photosList= ophotosMapper.findOrderProductPhotosByProductOrderId(orderproducts.getOrderproductid());
			if(photosList!=null&&photosList.size()>0){
				rq.setBasemodle(photosList);
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("成功");
			}else if(orderproducts.getCartid()!=null){
				List<PMyproductdetails> details= mydetailMapper.findMyProductdetails(orderproducts.getCartid());
				if(details!=null&&details.size()>0){
					photosList=new ArrayList<OOrderproductphotos>();
					Date nowtime=new Date();
					String base_code = userorders.getUserid() + "-" + orderproducts.getCartid();
					int i=1;
					for (PMyproductdetails dd : details) {
						OOrderproductphotos ph=new OOrderproductphotos();
						ph.setOrderproductid(orderproducts.getOrderproductid());
						ph.setImgurl(dd.getImgurl());
						ph.setContent(dd.getContent());
						ph.setSenendes(dd.getDescription());
						ph.setTitle(dd.getTitle());
						ph.setSort(dd.getSort());
						ph.setCreatetime(nowtime);
						ph.setPrintno(base_code + "-" + String.format("%02d",i)); 
						i++;
						photosList.add(ph);
						ophotosMapper.insert(ph);
					}
					rq.setBasemodle(photosList);
					rq.setStatu(ReturnStatus.Success);
				}else {
					rq.setStatusreson("找不到作品详情图片");
				} 
			} else {
				rq.setStatusreson("找不到作品编号cartid");
			}
		}
		return rq;
	}
	
	
	public ReturnModel getOrderProductInfoByUserOrderId(String userOrderId){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);
		OUserorders userorders=userOrdersMapper.selectByPrimaryKey(userOrderId);
		OOrderproducts orderproducts= orderProductMapper.getOProductsByOrderId(userOrderId);
		if(userorders!=null&&orderproducts!=null){
			Map<String, Object> map=new HashMap<String, Object>();
			//订单作品宝宝信息
			PMyproductchildinfo child= pmyChildMapper.selectByPrimaryKey(orderproducts.getCartid());
			if(child!=null){
				child.setBirthdayStr(DateUtil.getTimeStr(child.getBirthday(), "yyyy-MM-dd"));  
				map.put("child", child);
			}
			//订单图片信息
			List<OOrderproductphotos> photosList= ophotosMapper.findOrderProductPhotosByProductOrderId(orderproducts.getOrderproductid());
			if(photosList!=null&&photosList.size()>0){
				map.put("photos", photosList);
				rq.setStatu(ReturnStatus.Success);
				rq.setBasemodle(map); 
				return rq;
			}else if(orderproducts.getCartid()!=null) {
				List<PMyproductdetails> details= mydetailMapper.findMyProductdetails(orderproducts.getCartid());
				if(details!=null&&details.size()>0){
					photosList=new ArrayList<OOrderproductphotos>();
					Date nowtime=new Date();
					String base_code = userorders.getUserid() + "-" + orderproducts.getCartid();
					int i=1;
					for (PMyproductdetails dd : details) {
						OOrderproductphotos ph=new OOrderproductphotos();
						ph.setOrderproductid(orderproducts.getOrderproductid());
						ph.setImgurl(dd.getImgurl());
						ph.setContent(dd.getContent());
						ph.setSenendes(dd.getDescription());
						ph.setTitle(dd.getTitle());
						ph.setSort(dd.getSort());
						ph.setCreatetime(nowtime);
						ph.setPrintno(base_code + "-" + String.format("%02d",i)); 
						i++;
						photosList.add(ph);
						ophotosMapper.insert(ph);
					}
					map.put("photos", photosList);
					rq.setBasemodle(map);
					rq.setStatu(ReturnStatus.Success);
				}else {
					rq.setStatusreson("找不到作品详情图片");
				} 
			}else {
				rq.setStatusreson("找不到作品编号cartid");
			}
		}
		return rq;
	}
	
	
	public void downloadImg(List<UserOrderResultVO> orderlist,String basePath){
		try {
			FileUtils.isDirExists(basePath);
		} catch (Exception e) {
			basePath="D:\\orderImgs\\";
			FileUtils.isDirExists(basePath);
		}

		for (UserOrderResultVO order : orderlist) {
			Calendar c1 = new GregorianCalendar();
			c1.setTime(order.getPaytime());
			c1.set(Calendar.HOUR_OF_DAY, 18);
			c1.set(Calendar.MINUTE, 0);
			c1.set(Calendar.SECOND, 0);
			Calendar c2 = new GregorianCalendar();
			c2.setTime(order.getPaytime());
			if(c2.getTime().getTime()>c1.getTime().getTime()){
				c2.set(Calendar.DAY_OF_MONTH, 1);
			}
			String file_temp=DateUtil.getTimeStr(c2.getTime(), "MMdd");
			
			//创建文件夹
			FileUtils.isDirExists(basePath+"\\"+file_temp);
			FileUtils.isDirExists(basePath+"\\"+file_temp+"\\"+order.getUserorderid());;
			int i=1;
			for (OOrderproductdetails detail : order.getImglist()) {
				String file_dir=basePath+"\\"+file_temp+"\\"+order.getUserorderid();
				String fileFull_name=file_dir+"\\"+i+".jpg";
				if(!FileUtils.isFileExists(fileFull_name)){
					try {
						FileDownloadUtils.download(detail.getImageurl(),fileFull_name);
						FileDownloadUtils.setDPI(fileFull_name);
//						System.out.println(fileFull_name); 
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				i++;
			}
			
		}
	}
}
