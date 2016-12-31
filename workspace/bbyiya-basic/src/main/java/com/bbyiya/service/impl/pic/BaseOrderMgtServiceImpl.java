package com.bbyiya.service.impl.pic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.baseUtils.GenUtils;
import com.bbyiya.dao.OOrderaddressMapper;
import com.bbyiya.dao.OOrderproductsMapper;
import com.bbyiya.dao.RegionMapper;
import com.bbyiya.dao.UUseraddressMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.OOrderaddress;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.RAreas;
import com.bbyiya.model.RCity;
import com.bbyiya.model.RProvince;
import com.bbyiya.model.UUseraddress;
import com.bbyiya.service.pic.IBaseOrderMgtService;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.vo.RegionVo;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.order.UserOrderParam;

@Service("baseOrderMgtServiceImpl")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class BaseOrderMgtServiceImpl implements IBaseOrderMgtService {
	@Autowired
	private UUseraddressMapper addressMapper;
	@Autowired
	private RegionMapper regionMapper;
	@Autowired
	private OOrderaddressMapper orderaddressMapper;
	
	@Autowired
	private OOrderproductsMapper oproductMapper;

	public ReturnModel submitOrder(Long userId, UserOrderParam param) {
		ReturnModel rq = new ReturnModel();
		// 用户订单号（订单号）
		String userOrderId = GenUtils.getOrderNo(userId);
		// 用户订单
		OUserorders userOrder = new OUserorders();
		userOrder.setUserorderid(userOrderId);
		userOrder.setBranchuserid(param.getBranchUserId());
		userOrder.setUserid(userId);
		userOrder.setRemark(param.getRemark());
		long orderAddrId = getOrderAddressId(param.getAddrId());
		if (orderAddrId > 0) {
			userOrder.setOrderaddressid(param.getAddrId());
		} else {
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("订单收货地址生成有误");
			return rq;
		}
		if (param.getProlist() != null && param.getProlist().size() > 0) {
			Double totalPrice=0d;
			for (OOrderproducts pp : param.getProlist()) {
				pp.setOrderproductid(GenUtils.getOrderNo(userId));
				pp.setUserorderid(userOrderId);
				pp.setBuyeruserid(userId);
				oproductMapper.insert(pp);
				totalPrice+=pp.getPrice()*pp.getCount();
				
			}
			userOrder.setTotalprice(totalPrice); 
		} else {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("产品产生有误");
			return rq;
		}

		return rq;

	}

	/**
	 * 根据用户收货地址 生成订单收货地址 并返回订单收货地址的主键Id
	 * 
	 * @param userAddrId
	 *            user's addressId // not null
	 * @return
	 */
	public long getOrderAddressId(Long userAddrId) {
		if (userAddrId != null && userAddrId > 0) {
			UUseraddress addr = addressMapper.get_UUserAddressByKeyId(userAddrId);
			if (addr != null) {
				OOrderaddress orderAddress = new OOrderaddress();
				orderAddress.setUserid(addr.getUserid());
				orderAddress.setPhone(addr.getPhone());
				orderAddress.setReciver(addr.getReciver());
				orderAddress.setCity(getName(addr.getCity()));
				orderAddress.setProvince(getName(addr.getProvince()));
				orderAddress.setDistrict(getName(addr.getArea()));
				orderAddress.setStreetdetail(addr.getStreetdetail());
				orderAddress.setCreatetime(new Date());
				orderaddressMapper.insertReturnId(orderAddress);
				return orderAddress.getOrderaddressid();
			}
		}
		return 0;
	}

	/**
	 * gets the name of region by code
	 * 
	 * @param code
	 *            // region's code
	 * @return
	 */
	public String getName(Integer code) {
		if (code != null && code > 0) {
			for (RegionVo vo : findRegionAll()) {
				if (vo.getCode().intValue() == code.intValue()) {
					return vo.getCodeName();
				}
			}
		}
		return "";
	}

	/**
	 * All regionlist Gets a list of all regions (provinces, cities and
	 * districts the same level) 获取所有的 区域列表（省、市、区 同级）
	 * 
	 * @return
	 */
	public List<RegionVo> findRegionAll() {
		String keyString = "regionList_all";
		List<RegionVo> resultList = (List<RegionVo>) RedisUtil.getObject(keyString);// new
																					// ArrayList<RegionVo>();
		if (resultList != null && resultList.size() > 0) {
			return resultList;
		} else {
			resultList = new ArrayList<RegionVo>();
		}
		List<RProvince> provincelist = regionMapper.findProvincelistAll();
		if (provincelist != null && provincelist.size() > 0) {
			for (RProvince pp : provincelist) {
				RegionVo vo = new RegionVo();
				vo.setCode(pp.getCode());
				vo.setCodeName(pp.getProvince());
				vo.setStep(1);
				resultList.add(vo);
				List<RCity> citylist = regionMapper.findCitylistBy_ProvinceCode(pp.getCode());
				if (citylist != null && citylist.size() > 0) {
					for (RCity cc : citylist) {
						RegionVo vo_cc = new RegionVo();
						vo_cc.setCode(cc.getCode());
						vo_cc.setCodeName(cc.getCity());
						vo_cc.setStep(2);
						resultList.add(vo_cc);
						List<RAreas> arealist = regionMapper.findArealistBy_CityCode(cc.getCode());
						if (arealist != null && arealist.size() > 0) {
							for (RAreas aa : arealist) {
								RegionVo vo_aa = new RegionVo();
								vo_aa.setCode(aa.getCode());
								vo_aa.setCodeName(aa.getArea());
								vo_aa.setStep(3);
								resultList.add(vo_aa);
							}
						}
					}
				}
			}
		}
		return resultList;
	}

}
