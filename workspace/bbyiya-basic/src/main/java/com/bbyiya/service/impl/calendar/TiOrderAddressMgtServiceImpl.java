package com.bbyiya.service.impl.calendar;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.OOrderaddressMapper;
import com.bbyiya.dao.TiProductareasMapper;
import com.bbyiya.dao.TiPromotersMapper;
import com.bbyiya.dao.UUseraddressMapper;
import com.bbyiya.model.OOrderaddress;
import com.bbyiya.model.TiProductareas;
import com.bbyiya.model.TiPromoters;
import com.bbyiya.model.UUseraddress;
import com.bbyiya.service.IRegionService;
import com.bbyiya.service.calendar.IOrderAddressMgtService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.address.OrderaddressParam;

@Service("ti_OrderAddressMgtServiceImpl")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class TiOrderAddressMgtServiceImpl implements IOrderAddressMgtService{
	@Autowired
	private UUseraddressMapper addressMapper;
	@Autowired
	private OOrderaddressMapper orderaddressMapper;
	@Autowired
	private TiPromotersMapper promotersMapper;
	
	@Autowired
	private TiProductareasMapper productareasMapper;
	
	@Resource(name = "regionServiceImpl")
	private IRegionService regionService;
	
	public long addOrderAddressReturnOrderAddressId(Long userAddrId) {
		if (userAddrId != null && userAddrId > 0) {
			OOrderaddress orderAddress = addOrderAddressReturnOOrderaddress(userAddrId);
			if(orderAddress!=null)
				return orderAddress.getOrderaddressid();
		}
		return 0;
	}
	
	public OOrderaddress addOrderAddressReturnOOrderaddressModel(Long userAddressId) {
		if (userAddressId != null && userAddressId > 0) {
			return addOrderAddressReturnOOrderaddress(userAddressId);
		}
		return null;
	}
	
	public OOrderaddress addOrderAddressReturnOOrderaddressModel_promoterAddress(Long promoterUserId) {
		TiPromoters promoters = promotersMapper.selectByPrimaryKey(promoterUserId);
		if (promoters != null) {
			OOrderaddress orderAddress = new OOrderaddress();
			orderAddress.setUserid(promoters.getPromoteruserid());
			orderAddress.setPhone(promoters.getMobilephone());
			orderAddress.setReciver(promoters.getContacts());
			orderAddress.setProvincecode(promoters.getProvince());
			orderAddress.setCitycode(promoters.getCity());
			orderAddress.setDistrictcode(promoters.getArea()); 
			orderAddress.setCity(regionService.getCityName(promoters.getCity()));
			orderAddress.setProvince(regionService.getProvinceName(promoters.getProvince()));
			orderAddress.setDistrict(regionService.getAresName(promoters.getArea()));
			orderAddress.setStreetdetail(promoters.getStreetdetails());
			orderAddress.setCreatetime(new Date());
			orderaddressMapper.insertReturnId(orderAddress);
			return orderAddress;
		}
		return null;
	}
	
	public OOrderaddress addOrderAddress(OrderaddressParam addressParam) {
		if(addressParam!=null&&!ObjectUtil.isEmpty(addressParam.getProvince())&&!ObjectUtil.isEmpty(addressParam.getCity())&&!ObjectUtil.isEmpty(addressParam.getDistrict())){
			OOrderaddress orderAddress = new OOrderaddress();
			orderAddress.setUserid(addressParam.getUserid());
			orderAddress.setPhone(addressParam.getPhone());
			orderAddress.setReciver(addressParam.getReciver());
			orderAddress.setProvincecode(addressParam.getProvince());
			orderAddress.setCitycode(addressParam.getCity());
			orderAddress.setDistrictcode(addressParam.getDistrict()); 
			orderAddress.setCity(regionService.getCityName(addressParam.getCity()));
			orderAddress.setProvince(regionService.getProvinceName(addressParam.getProvince()));
			orderAddress.setDistrict(regionService.getAresName(addressParam.getDistrict()));
			orderAddress.setStreetdetail(addressParam.getStreetdetail());
			orderAddress.setCreatetime(new Date());
			orderaddressMapper.insertReturnId(orderAddress);
			return orderAddress;
		}
		return null;
	}
	
	
	
	
	public long getProducerUserId(Integer districtcode,Integer cityCode,Integer provinceCode, Long productId){
		if(districtcode!=null&&productId!=null){
			List<TiProductareas> list =null;
			if(districtcode!=null)
				list=productareasMapper.findProductAreaListByProductIdAndArea(productId, districtcode);
			if (list==null||list.size()<=0){
				if(cityCode!=null)
					list=productareasMapper.findProductAreaListByCityCode(productId, cityCode);
				if(list==null||list.size()<=0){
					if(provinceCode!=null)
						list=productareasMapper.findProductAreaListByProvinceCode(productId, provinceCode);
				}
			}
			if (list != null && list.size() > 0) {
				return list.get(0).getProduceruserid();
			}
		}
		return ObjectUtil.parseLong(ConfigUtil.getSingleValue("defaultproducer")) ;
	}
	
	public long getProducerUserIdByOrderAddressId(Long orderAddressId, Long productId){
		if(orderAddressId!=null&&productId!=null){
			OOrderaddress addr = orderaddressMapper.selectByPrimaryKey(orderAddressId);
			if(addr!=null&&addr.getDistrictcode()!=null){
				List<TiProductareas> list = productareasMapper.findProductAreaListByProductIdAndArea(productId, addr.getDistrictcode());
				if (list==null||list.size()<=0){
					list=productareasMapper.findProductAreaListByCityCode(productId, addr.getCitycode());
					if(list==null||list.size()<=0){
						list=productareasMapper.findProductAreaListByProvinceCode(productId, addr.getProvincecode());
					}
				}
				if (list != null && list.size() > 0) {
					return list.get(0).getProduceruserid();
				}
			}
		}
		return ObjectUtil.parseLong(ConfigUtil.getSingleValue("defaultproducer")) ;
	}
	
	private  OOrderaddress addOrderAddressReturnOOrderaddress(Long userAddrId) {
		if (userAddrId != null && userAddrId > 0) {
			UUseraddress addr = addressMapper.get_UUserAddressByKeyId(userAddrId);
			if (addr != null) {
				OOrderaddress orderAddress = new OOrderaddress();
				orderAddress.setUserid(addr.getUserid());
				orderAddress.setPhone(addr.getPhone());
				orderAddress.setProvincecode(addr.getProvince());
				orderAddress.setCitycode(addr.getCity());
				orderAddress.setDistrictcode(addr.getArea()); 
				orderAddress.setReciver(addr.getReciver());
				orderAddress.setCity(regionService.getCityName(addr.getCity()));
				orderAddress.setProvince(regionService.getProvinceName(addr.getProvince()));
				orderAddress.setDistrict(regionService.getAresName(addr.getArea()));
				orderAddress.setStreetdetail(addr.getStreetdetail());
				orderAddress.setCreatetime(new Date());
				orderaddressMapper.insertReturnId(orderAddress);
				return orderAddress;
			}
		}
		return null;
	}
	
	
}
