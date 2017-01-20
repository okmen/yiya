package com.bbyiya.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.RegionMapper;
import com.bbyiya.model.RAreas;
import com.bbyiya.model.RCity;
import com.bbyiya.model.RProvince;
import com.bbyiya.service.IRegionService;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.vo.RegionVo;

@Service("regionServiceImpl")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class RegionServiceImpl implements IRegionService {
	@Autowired
	private RegionMapper regionMapper;

	/**
	 * gets the name of region by code
	 * 
	 * @param code
	 *            // region's code
	 * @return
	 */
	public String getName(Integer code) {
		if (code != null && code > 0) {
			List<RegionVo> list = findRegionAll();
			for (RegionVo vo : list) {
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
		@SuppressWarnings("unchecked")
		List<RegionVo> resultList = (List<RegionVo>) RedisUtil.getObject(keyString);
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
		if (resultList != null && resultList.size() > 0) {
			RedisUtil.setObject(keyString, resultList, 3600);
		}
		return resultList;
	}
}
