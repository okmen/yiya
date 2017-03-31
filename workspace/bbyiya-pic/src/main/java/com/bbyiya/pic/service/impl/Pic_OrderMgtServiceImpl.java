package com.bbyiya.pic.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.OOrderaddressMapper;
import com.bbyiya.dao.OOrderproductdetailsMapper;
import com.bbyiya.dao.OOrderproductsMapper;
import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.UBranchesMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.BranchStatusEnum;
import com.bbyiya.model.OOrderaddress;
import com.bbyiya.model.OOrderproductdetails;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.UAgentcustomers;
import com.bbyiya.model.UBranches;
import com.bbyiya.pic.dao.IPic_OrderMgtDao;
import com.bbyiya.pic.service.IPic_MemberMgtService;
import com.bbyiya.pic.service.IPic_OrderMgtService;
import com.bbyiya.pic.vo.order.SearchOrderParam;
import com.bbyiya.pic.vo.order.UserOrderResultVO;
import com.bbyiya.pic.vo.order.ibs.OrderProductVo;
import com.bbyiya.pic.vo.order.ibs.OrderVo;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.FileUtils;
import com.bbyiya.utils.upload.FileDownloadUtils;
import com.bbyiya.vo.ReturnModel;

@Service("pic_orderMgtService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Pic_OrderMgtServiceImpl implements IPic_OrderMgtService{
	//客户信息处理
	@Resource(name = "pic_memberMgtService")
	private IPic_MemberMgtService memberMgtService;
	/*------------------订单模块--------------------------------------*/
	@Autowired
	private OUserordersMapper userOrdersMapper;
	@Autowired
	private IPic_OrderMgtDao orderDao;
	@Autowired
	private OOrderproductsMapper orderProductMapper;
	@Autowired
	private OOrderproductdetailsMapper detailMapper;
	@Autowired
	private OOrderaddressMapper addressMapper;
	/*----------------------代理模块--------------------------*/
	@Autowired
	private UBranchesMapper branchesMapper;
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
	public ReturnModel findMyOrderlist(Long branchUserId,Integer status){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.Success);
		//订单列表
		List<OUserorders> userorders= userOrdersMapper.findOrdersByBranchUserId(branchUserId,status);
		if(userorders!=null&&userorders.size()>0){
			List<Long> ids = new ArrayList<Long>();
			for (OUserorders oo : userorders) {
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
			rq.setBasemodle(resultlist);
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
			vo.setPaytime(DateUtil.getTimeStr(order.getPaytime(), "yyyy-MM-dd HH:mm:ss") ); 
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
	 * 取消订单
	 * @param userId
	 * @return
	 */
	public ReturnModel cancelOrder(String orderId) {
		ReturnModel rq = new ReturnModel();
		OUserorders userorders = userOrdersMapper.selectByPrimaryKey(orderId);
		if(userorders!=null){
			List<OOrderproducts> productList=orderProductMapper.findOProductsByOrderId(userorders.getUserorderid());
			if(productList!=null&&productList.size()>0){
				for (OOrderproducts pro : productList) {
					List<OOrderproductdetails> detailslist= orderDao.findOrderProductDetailsByProductOrderId(pro.getOrderproductid());
					for (OOrderproductdetails detail : detailslist) {
						detailMapper.deleteByPrimaryKey(detail.getOproductdetailid());
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
