package com.bbyiya.pic.service.impl;

import java.util.ArrayList;
import java.util.Date;
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
import com.bbyiya.vo.ReturnModel;

@Service("pic_orderMgtService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Pic_OrderMgtServiceImpl implements IPic_OrderMgtService{
	//�ͻ���Ϣ����
	@Resource(name = "pic_memberMgtService")
	private IPic_MemberMgtService memberMgtService;
	/*------------------����ģ��--------------------------------------*/
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
	/*----------------------����ģ��--------------------------*/
	@Autowired
	private UBranchesMapper branchesMapper;
	/**
	 * ��ȡ�����б�
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
	 * ��ȡ������Ķ�����IBS�ã�
	 * @param branchUserId
	 * @return
	 */
	public ReturnModel findAgentOrders(Long branchUserId){
		ReturnModel rq=new ReturnModel();
		UBranches branches= branchesMapper.selectByPrimaryKey(branchUserId);
		if(branches!=null&&branches.getStatus().intValue()==Integer.parseInt(BranchStatusEnum.ok.toString())){
//		if(true){
			List<OUserorders> userorders= userOrdersMapper.findOrdersByAgentUserId(branches.getAgentuserid());
			if(userorders!=null&&userorders.size()>0){
				List<Long> ids=new ArrayList<Long>();
				for (OUserorders oo : userorders) {
					ids.add(oo.getOrderaddressid());
				}
				List<OOrderaddress> addressList= addressMapper.findListByIds(ids);
				List<OrderVo> resultlist=new ArrayList<OrderVo>();
				for (OUserorders order : userorders) {
					 OrderVo vo=new OrderVo();
					 vo.setUserorderid(order.getUserorderid());
					 vo.setStatus(order.getStatus());
					 vo.setUserid(order.getUserid());
					 vo.setBranchuserid(order.getBranchuserid());
					 for (OOrderaddress addr : addressList) {
						if(addr.getOrderaddressid().longValue()==order.getOrderaddressid().longValue()){
							vo.setAddress(addr);
						}
					}
					 resultlist.add(vo);
				}
				rq.setBasemodle(resultlist); 
			}
			rq.setStatu(ReturnStatus.Success);
		}else {
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("�������Ǻ����̣�Ȩ�޲��㣡");
		}
		return rq;
	}
	@Autowired
	private PMyproductsMapper myproductsMapper;
	public ReturnModel findMyOrderlist(Long branchUserId,Integer status){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.Success);
		//�����б�
		List<OUserorders> userorders= userOrdersMapper.findOrdersByBranchUserId(branchUserId,status);
		if(userorders!=null&&userorders.size()>0){
			List<Long> ids = new ArrayList<Long>();
			for (OUserorders oo : userorders) {
				ids.add(oo.getOrderaddressid());
			}
			//�������ջ���ַ
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
	 * ��Ҫ����ͻ�
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
				if(order.getIsbranch()!=null&&order.getIsbranch()==1){//������
					rq.setStatusreson("������˼���˿ͻ��Ѿ��������ˣ�");
					return rq; 
				}
				//�ҵ�Ӱ¥��Ϣ���ҵ��ҵĴ�����
				UBranches branches=branchesMapper.selectByPrimaryKey(branchUserId);
				if(branches!=null&&branches.getStatus()!=null&&branches.getStatus().intValue()==Integer.parseInt(BranchStatusEnum.ok.toString())){
					 //���Ǵ���Ӱ¥���߱����ͻ���Ȩ��
					if(order.getAgentuserid().longValue()==branches.getAgentuserid().longValue()){
						 //���ö�����Ϣ�Ѿ�������
						 order.setIsbranch(1);
						 order.setBranchuserid(branchUserId);
						 userOrdersMapper.updateByPrimaryKeySelective(order);
						 
						 OOrderaddress address=addressMapper.selectByPrimaryKey(order.getOrderaddressid());
						 if(address!=null){
							 //�ͻ���Ϣ
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
							 rq.setStatusreson("�˿ͳɹ�������");
							 return rq;
						 }else {
							rq.setStatusreson("�Ҳ����û��ջ���Ϣ��");
							return rq;
						 }
					}else {
						rq.setStatusreson("�˶����������Ĵ���Χ��");
						return rq;
					}
				}else {
					rq.setStatusreson("�������Ǻ����̣�Ȩ�޲��㣡");
					return rq;
				}
			}else {
				rq.setStatusreson("�Ǵ������򶩵����ݲ��ܷ��䣡");
				return rq;
			}
			
		}else {
			rq.setStatusreson("����������");
		}
		return rq;
	}
	
	/**
	 * ��ȡ��������
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
			
			vo.setAddress(addressMapper.selectByPrimaryKey(order.getAgentuserid()));
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
			rq.setStatusreson("����Ķ���");
		}
		return rq;
	}
}
