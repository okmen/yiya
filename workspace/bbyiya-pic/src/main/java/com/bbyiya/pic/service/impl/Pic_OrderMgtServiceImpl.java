package com.bbyiya.pic.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.OOrderaddressMapper;
import com.bbyiya.dao.OOrderproductdetailsMapper;
import com.bbyiya.dao.OOrderproductsMapper;
import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.UBranchesMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.BranchStatusEnum;
import com.bbyiya.model.OOrderaddress;
import com.bbyiya.model.OOrderproductdetails;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.UBranches;
import com.bbyiya.pic.dao.IPic_OrderMgtDao;
import com.bbyiya.pic.service.IPic_OrderMgtService;
import com.bbyiya.pic.vo.order.SearchOrderParam;
import com.bbyiya.pic.vo.order.UserOrderResultVO;
import com.bbyiya.vo.ReturnModel;

@Service("pic_orderMgtService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Pic_OrderMgtServiceImpl implements IPic_OrderMgtService{
	
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
	 * 我的
	 * @param branchUserId
	 * @return
	 */
	public ReturnModel findAgentOrders(Long branchUserId){
		ReturnModel rq=new ReturnModel();
		UBranches branches= branchesMapper.selectByPrimaryKey(branchUserId);
		if(branches!=null&&branches.getStatus().intValue()==Integer.parseInt(BranchStatusEnum.ok.toString())){
			List<OUserorders> userorders= userOrdersMapper.findOrdersByAgentUserId(branches.getAgentuserid());
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(userorders);
		}else {
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("您还不是合作商，权限不足！");
		}
		return rq;
	}
	
	
}
