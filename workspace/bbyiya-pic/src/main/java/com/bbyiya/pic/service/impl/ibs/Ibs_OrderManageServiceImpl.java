package com.bbyiya.pic.service.impl.ibs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.OPayorderextMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.OPayorderext;
import com.bbyiya.pic.service.ibs.IIbs_OrderManageService;
import com.bbyiya.vo.ReturnModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("ibs_OrderManageService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Ibs_OrderManageServiceImpl implements IIbs_OrderManageService{
	@Autowired
	private OPayorderextMapper payextendMapper;
	
	
	public ReturnModel find_payorderExtByUpUserid(Long userId,Integer status, int index,int size){
		ReturnModel rq=new ReturnModel();
		PageHelper.startPage(index, size);
		List<OPayorderext> list= payextendMapper.findListByUpUserid(userId, status);
		PageInfo<OPayorderext> resultPage=new PageInfo<OPayorderext>(list); 
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(resultPage);
		return rq;
	}
}
