package com.bbyiya.pic.service.impl.calendar;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.OOrderaddressMapper;
import com.bbyiya.dao.OOrderproductdetailsMapper;
import com.bbyiya.dao.OOrderproductphotosMapper;
import com.bbyiya.dao.OOrderproductsMapper;
import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.TiActivityworksMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.MyProductTempType;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.InviteStatus;
import com.bbyiya.model.OOrderaddress;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PMyproducttemp;
import com.bbyiya.model.TiActivityworks;
import com.bbyiya.model.UBranches;
import com.bbyiya.model.UUsers;
import com.bbyiya.pic.service.calendar.IIbs_TiOrderMgtService;
import com.bbyiya.pic.vo.calendar.TiOrderVo;
import com.bbyiya.service.pic.IBaseUserAddressService;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.PageInfoUtil;
import com.bbyiya.vo.ReturnModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("ibs_TiOrderMgtService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Ibs_TiOrderMgtServiceImpl implements IIbs_TiOrderMgtService{
	@Autowired
	private TiActivityworksMapper actworkMapper;
	@Autowired
	private OUserordersMapper orderMapper;
	@Autowired
	private OOrderproductsMapper orderProductMapper;
	
	/*-------------------用户信息------------------------------------------------*/
	@Autowired
	private UUsersMapper usersMapper;
	@Autowired
	private OOrderaddressMapper addressMapper;
	@Resource(name = "baseUserAddressServiceImpl")
	private IBaseUserAddressService baseAddressService;

	public ReturnModel findTiMyOrderlist(Long branchUserId,Integer ordertype,Integer status,String keywords,int index,int size){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.Success);
		PageHelper.startPage(index, size);
		//订单列表
		List<OUserorders> userorders= orderMapper.findOrdersByBranchUserId(branchUserId,ordertype,status,keywords);
		PageInfo<OUserorders> resultPage=new PageInfo<OUserorders>(userorders); 
		if(resultPage!=null&&resultPage.getList()!=null&&resultPage.getList().size()>0){
			List<Long> ids = new ArrayList<Long>();
			for (OUserorders oo : resultPage.getList()) {
				ids.add(oo.getOrderaddressid());
			}
			//订单的收货地址
			List<OOrderaddress> addressList = addressMapper.findListByIds(ids);
			List<TiOrderVo> resultlist = new ArrayList<TiOrderVo>();
			for (OUserorders order : userorders) {
				TiOrderVo vo = new TiOrderVo();
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
				UUsers user=usersMapper.selectByPrimaryKey(order.getUserid());
				if(user!=null){
					vo.setUsernickname(user.getNickname());
				}
				OOrderproducts product= orderProductMapper.getOProductsByOrderId(order.getUserorderid());
				if(product!=null){
					vo.setProducttitle(product.getProducttitle());
					vo.setPropertystr(product.getProducttitle());
					vo.setPrice(product.getPrice());
					vo.setCartid(product.getCartid());
					TiActivityworks cart=actworkMapper.selectByPrimaryKey(product.getCartid());
					if(cart!=null){
						if(!ObjectUtil.isEmpty(cart.getActid())){
							vo.setActid(cart.getActid());
						}
					}
				}
				resultlist.add(vo);
			}	
			PageInfoUtil<TiOrderVo> resultPagelist=new PageInfoUtil<TiOrderVo>(resultPage, resultlist);
			rq.setBasemodle(resultPagelist);
		}	
		rq.setStatusreson("ok");
		return rq;
	}

	
}
