package com.bbyiya.pic.service.impl.calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.bbyiya.dao.TiMyartsdetailsMapper;
import com.bbyiya.dao.TiMyworkcustomersMapper;
import com.bbyiya.dao.TiMyworksMapper;
import com.bbyiya.dao.TiProductsMapper;
import com.bbyiya.dao.TiProductstylesMapper;
import com.bbyiya.dao.TiPromotersMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.MyProductTempType;
import com.bbyiya.enums.OrderTypeEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.calendar.AddressTypeEnum;
import com.bbyiya.enums.pic.InviteStatus;
import com.bbyiya.model.OOrderaddress;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PMyproducttemp;
import com.bbyiya.model.TiActivityworks;
import com.bbyiya.model.TiMyworkcustomers;
import com.bbyiya.model.TiMyworks;
import com.bbyiya.model.TiProductstyles;
import com.bbyiya.model.TiPromoters;
import com.bbyiya.model.UBranches;
import com.bbyiya.model.UUsers;
import com.bbyiya.pic.service.calendar.IIbs_TiOrderMgtService;
import com.bbyiya.pic.vo.calendar.TiOrderVo;
import com.bbyiya.service.IRegionService;
import com.bbyiya.service.calendar.ITi_OrderMgtService;
import com.bbyiya.service.pic.IBaseUserAddressService;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.PageInfoUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.address.OrderaddressVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("ibs_TiOrderMgtService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Ibs_TiOrderMgtServiceImpl implements IIbs_TiOrderMgtService{
	//------------------------订单---------------------------------------------
	@Autowired
	private OUserordersMapper orderMapper;
	@Autowired
	private OOrderproductsMapper orderProductMapper;
	@Autowired
	private OOrderaddressMapper orderaddressMapper;
	//------------------------产品---------------------------------------------
	@Autowired
	private TiProductstylesMapper styleMapper;
	@Autowired
	private TiProductsMapper productMapper;
	//---------------------------作品、活动------------------------------------------------------
	@Autowired
	private TiMyworksMapper myworksMapper;
	@Autowired
	private TiActivityworksMapper actworkMapper;
	@Autowired
	private TiMyworkcustomersMapper workcusMapper;
	
	
	/*-------------------用户信息------------------------------------------------*/
	@Autowired
	private TiPromotersMapper promotersMapper;
	@Autowired
	private UUsersMapper usersMapper;
	@Autowired
	private OOrderaddressMapper addressMapper;
	
	@Resource(name = "regionServiceImpl")
	private IRegionService regionService;

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
				if(order.getPaytime()!=null){
					vo.setPaytime(DateUtil.getTimeStr(order.getPaytime(), "yyyy-MM-dd HH:mm:ss"));
				}else{
					vo.setPaytime(DateUtil.getTimeStr(order.getOrdertime(), "yyyy-MM-dd HH:mm:ss"));
					
				}
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
					vo.setPropertystr(product.getPropertystr());
					vo.setPrice(product.getPrice());
					vo.setCartid(product.getCartid());
					TiMyworks mywork=myworksMapper.selectByPrimaryKey(product.getCartid());
					//代客制作
					if(mywork!=null&&mywork.getIsinstead()!=null&&mywork.getIsinstead().intValue()==1){
						TiMyworkcustomers myworkcus=workcusMapper.selectByPrimaryKey(mywork.getWorkid());
						if(myworkcus!=null){
							vo.setActkhmc(myworkcus.getCustomername());
							vo.setActkhphone(myworkcus.getMobilephone());
						}
					}else{
						//老客户回顾
						TiActivityworks cart=actworkMapper.selectByPrimaryKey(product.getCartid());
						if(cart!=null){
							if(!ObjectUtil.isEmpty(cart.getActid())){
								vo.setActid(cart.getActid());
							}
							if(cart.getAddresstype()!=null&&cart.getAddresstype().intValue()==1){
								OOrderaddress orderaddress=orderaddressMapper.selectByPrimaryKey(cart.getOrderaddressid());
								if(orderaddress!=null){
									vo.setActkhmc(orderaddress.getReciver());
									vo.setActkhphone(orderaddress.getPhone());
								}
							}else{
								vo.setActkhmc(cart.getReciever());
								vo.setActkhphone(cart.getMobiephone());
							}
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

	public ReturnModel getIbsTiSubmitAddressList(Long submitUserId,Long workId) {
		ReturnModel rq = new ReturnModel();
		//用户作品
		TiMyworks work= myworksMapper.selectByPrimaryKey(workId);
		if(work==null){
			rq.setStatusreson("作品不存在！");
			return rq;
		}
		//用户作品对应的产品款式
		TiProductstyles style=styleMapper.selectByPrimaryKey(work.getStyleid()==null?work.getProductid():work.getStyleid());
		if(style==null){
			rq.setStatusreson("作品信息不全！");
			return rq;
		}
		TiActivityworks actWork= actworkMapper.selectByPrimaryKey(workId);
		OrderaddressVo orderAddress = new OrderaddressVo();		
		//用户自己付邮费
		if(actWork.getOrderaddressid()!=null&&actWork.getOrderaddressid().longValue()>0){
			OOrderaddress oaddr=orderaddressMapper.selectByPrimaryKey(actWork.getOrderaddressid());
			if(oaddr!=null){
				orderAddress.setUserid(oaddr.getUserid());
				orderAddress.setPhone(oaddr.getPhone());
				orderAddress.setReciver(oaddr.getReciver());
				orderAddress.setCityName(oaddr.getCity());
				orderAddress.setProvinceName(oaddr.getProvince());
				orderAddress.setDistrictName(oaddr.getDistrict());
				orderAddress.setStreetdetail(oaddr.getStreetdetail());
				orderAddress.setAddressType(1);
			}
		}else {
			//寄到B端地址
			TiPromoters promoters = promotersMapper.selectByPrimaryKey(submitUserId);
			if (promoters != null) {
				orderAddress.setUserid(promoters.getPromoteruserid());
				orderAddress.setPhone(promoters.getMobilephone());
				orderAddress.setReciver(promoters.getContacts());
				orderAddress.setCity(promoters.getCity());
				orderAddress.setCityName(regionService.getCityName(promoters.getCity()));
				orderAddress.setProvince(promoters.getProvince());
				orderAddress.setProvinceName(regionService.getProvinceName(promoters.getProvince()));
				orderAddress.setDistrict(promoters.getArea());
				orderAddress.setDistrictName(regionService.getAresName(promoters.getArea()));
				orderAddress.setStreetdetail(promoters.getStreetdetails());
				orderAddress.setAddressType(0);
			}				
		}
		HashMap<String, Object> map=new HashMap<String, Object>();
		map.put("style", style);
		map.put("address", orderAddress);
		rq.setBasemodle(map);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("成功");
		return rq;
	}
	
	public ReturnModel getTiCustomerSubmitAddressList(Long submitUserId,Long workId) {
		ReturnModel rq = new ReturnModel();
		List<OrderaddressVo> addressList=new ArrayList<OrderaddressVo>();	
		//用户作品
		TiMyworks work= myworksMapper.selectByPrimaryKey(workId);
		if(work==null){
			rq.setStatusreson("作品不存在！");
			return rq;
		}
		//用户作品对应的产品款式
		TiProductstyles style=styleMapper.selectByPrimaryKey(work.getStyleid()==null?work.getProductid():work.getStyleid());
		if(style==null){
			rq.setStatusreson("作品信息不全！");
			return rq;
		}
		TiMyworkcustomers workcus=workcusMapper.selectByPrimaryKey(workId);
		//如果选择了用户地址
		if(workcus.getAddresstype()!=null&&workcus.getAddresstype().intValue()==Integer.parseInt(AddressTypeEnum.cusaddr.toString())){
			if(!ObjectUtil.isEmpty(workcus.getStreetdetails())){
				OrderaddressVo orderAddress = new OrderaddressVo();	
				orderAddress.setPhone(workcus.getRecieverphone());
				orderAddress.setReciver(workcus.getReciever());
				orderAddress.setCity(workcus.getCity());
				orderAddress.setCityName(regionService.getCityName(workcus.getCity()));
				orderAddress.setProvince(workcus.getProvince());
				orderAddress.setProvinceName(regionService.getProvinceName(workcus.getProvince()));
				orderAddress.setDistrict(workcus.getDistrict());
				orderAddress.setDistrictName(regionService.getAresName(workcus.getDistrict()));
				orderAddress.setStreetdetail(workcus.getStreetdetails());
				orderAddress.setAddressType(1);
				addressList.add(orderAddress);
			}
		}
		//B端影楼地址
		TiPromoters promoters = promotersMapper.selectByPrimaryKey(submitUserId);
		if (promoters != null) {
			OrderaddressVo orderAddress = new OrderaddressVo();
			orderAddress.setUserid(promoters.getPromoteruserid());
			orderAddress.setPhone(promoters.getMobilephone());
			orderAddress.setReciver(promoters.getContacts());
			orderAddress.setCity(promoters.getCity());
			orderAddress.setCityName(regionService.getCityName(promoters.getCity()));
			orderAddress.setProvince(promoters.getProvince());
			orderAddress.setProvinceName(regionService.getProvinceName(promoters.getProvince()));
			orderAddress.setDistrict(promoters.getArea());
			orderAddress.setDistrictName(regionService.getAresName(promoters.getArea()));
			orderAddress.setStreetdetail(promoters.getStreetdetails());
			orderAddress.setAddressType(0);
			addressList.add(orderAddress);
		}				
		HashMap<String, Object> map=new HashMap<String, Object>();
		map.put("style", style);
		map.put("address", addressList);
		rq.setBasemodle(map);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("成功");
		return rq;
	}
}
