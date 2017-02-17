package com.bbyiya.pic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.UBranchareapriceMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.UBranchareaprice;
import com.bbyiya.pic.service.IPic_BranchMgtService;
import com.bbyiya.vo.ReturnModel;

@Service("pic_BranchMgtService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Pic_BranchMgtServiceImpl implements IPic_BranchMgtService{
	
	@Autowired
	private UBranchareapriceMapper branchAreaMapper;
	
	public ReturnModel getBranchAreaPrice(Integer province,Integer city,Integer district){
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.Success);
		if(province!=null){
			UBranchareaprice mo1= branchAreaMapper.selectByPrimaryKey(province);
			if(mo1!=null){
				rqModel.setBasemodle(mo1.getPrice());
				return rqModel;
			}
			UBranchareaprice mo2= branchAreaMapper.selectByPrimaryKey(city);
			if(mo2!=null){
				rqModel.setBasemodle(mo2.getPrice());
				return rqModel;
			}
			UBranchareaprice mo3= branchAreaMapper.selectByPrimaryKey(district);
			if(mo3!=null){
				rqModel.setBasemodle(mo3.getPrice());
				return rqModel;
			}
			
		}
		rqModel.setBasemodle(1000); 
		return rqModel;
	}
}
