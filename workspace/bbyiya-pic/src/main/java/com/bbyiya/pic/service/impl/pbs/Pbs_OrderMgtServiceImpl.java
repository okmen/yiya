package com.bbyiya.pic.service.impl.pbs;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.OOrderaddressMapper;
import com.bbyiya.dao.OOrderproductdetailsMapper;
import com.bbyiya.dao.OOrderproductsMapper;
import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.UBranchesMapper;
import com.bbyiya.enums.OrderTypeEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.OOrderaddress;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.UBranches;
import com.bbyiya.pic.dao.IPic_OrderMgtDao;
import com.bbyiya.pic.service.IPic_MemberMgtService;
import com.bbyiya.pic.service.pbs.IPbs_OrderMgtService;
import com.bbyiya.pic.vo.order.PbsSearchOrderParam;
import com.bbyiya.pic.vo.order.PbsUserOrderResultVO;
import com.bbyiya.vo.ReturnModel;
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
	
	
	public ReturnModel find_pbsOrderList(PbsSearchOrderParam param){
			
		ReturnModel rq=new ReturnModel();
		if(param==null)
			param=new PbsSearchOrderParam();
		if(param.getSize()==null||param.getSize()==0) param.setSize(10);
		PageHelper.startPage(param.getIndex(), param.getSize());
		List<PbsUserOrderResultVO> list=orderDao.findPbsUserOrders(param);
		PageInfo<PbsUserOrderResultVO> reuslt=new PageInfo<PbsUserOrderResultVO>(list);
		
		if(list!=null&&list.size()>0){
			for (PbsUserOrderResultVO product : reuslt.getList()) {
				OUserorders order=userOrdersMapper.selectByPrimaryKey(product.getUserorderid());
				product.setOrder(order);
				OOrderaddress address= addressMapper.selectByPrimaryKey(order.getOrderaddressid());
				UBranches branch=branchesMapper.selectByPrimaryKey(order.getBranchuserid());
				if(branch!=null){
					product.setBranchesName(branch.getBranchcompanyname());
					product.setBranchesPhone(branch.getPhone());
				}
				int orderType = order.getOrdertype() == null ? 0 : order.getOrdertype();
				//影楼直接下单
				if (orderType == Integer.parseInt(OrderTypeEnum.brachOrder.toString())) {
					product.setBranchesAddress(address.getProvince()+address.getCity()+address.getDistrict()+address.getStreetdetail());
				}else{
					//普通用户下单如果是影楼抢单
					if(order.getIsbranch()!=null&&order.getIsbranch()==1){
						if(branch!=null)
							product.setBranchesAddress(branch.getProvince()+branch.getCity()+branch.getArea()+branch.getStreetdetail());
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
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("获取列表成功！");
		rq.setBasemodle(reuslt); 
		return rq;
	}
	
	
}
