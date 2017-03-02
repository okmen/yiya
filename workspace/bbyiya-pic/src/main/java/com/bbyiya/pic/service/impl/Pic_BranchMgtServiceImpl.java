package com.bbyiya.pic.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.UBranchareapriceMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.UBranchareaprice;
import com.bbyiya.pic.service.IPic_BranchMgtService;
import com.bbyiya.service.IRegionService;
import com.bbyiya.vo.ReturnModel;

@Service("pic_BranchMgtService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Pic_BranchMgtServiceImpl implements IPic_BranchMgtService{
	
	@Autowired
	private UBranchareapriceMapper branchAreaMapper;
	@Resource(name = "regionServiceImpl")
	private IRegionService regionService;
	
	public ReturnModel getBranchAreaPrice(Integer province,Integer city,Integer district){
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.Success);
		Map<String, Object> areaMap=new HashMap<String, Object>();
		Double priceTemp=1000d;
		if(province!=null){
			areaMap.put("province", province);
			areaMap.put("provinceName", regionService.getName(province));
			if(priceTemp<=1001d){
				UBranchareaprice mo1= branchAreaMapper.selectByPrimaryKey(province);
				if(mo1!=null){
					priceTemp=mo1.getPrice();
				}
			}
		}
		if(city!=null&&city>0){
			areaMap.put("city", city);
			areaMap.put("cityName", regionService.getName(city));
			if(priceTemp<=1001d){
				UBranchareaprice mo2= branchAreaMapper.selectByPrimaryKey(city);
				if(mo2!=null){
					priceTemp=mo2.getPrice();
				}	
			}
			
		}
		if(district!=null&&district>0){
			areaMap.put("district", city);
			areaMap.put("districtName", regionService.getName(district));
			if(priceTemp<=1001d){
				UBranchareaprice mo3= branchAreaMapper.selectByPrimaryKey(district);
				if(mo3!=null){
					priceTemp=mo3.getPrice();
				}
			}
		}
		areaMap.put("price", priceTemp);
		rqModel.setBasemodle(areaMap); 
		return rqModel;
	}
}
