package com.bbyiya.pic.service.impl.cts;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.OPayorderextMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.OPayorderext;
import com.bbyiya.pic.service.cts.ICts_OrderManageService;
import com.bbyiya.pic.service.ibs.IIbs_OrderManageService;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("cts_OrderManageService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Cts_OrderManageServiceImpl implements ICts_OrderManageService{
	@Autowired
	private OPayorderextMapper payextendMapper;
	
	/**
	 * 获取影楼推荐人发展的用户订单列表
	 */
	public ReturnModel find_payorderExtByBranchUpUserid(Long branchuserId,String startTimeStr,String endTimeStr,Integer status, int index,int size){
		ReturnModel rq=new ReturnModel();
		
		if(endTimeStr!=null&&!endTimeStr.equals("")){
			endTimeStr=DateUtil.getEndTime(endTimeStr);			
		}
		PageHelper.startPage(index, size);
		List<OPayorderext> list= payextendMapper.find_payorderExtByBranchUpUserid(branchuserId, status, startTimeStr, endTimeStr);
		PageInfo<OPayorderext> resultPage=new PageInfo<OPayorderext>(list); 
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(resultPage);
		return rq;
	}
	
	/**
	 * 获取微商推荐人发展的用户订单列表
	 */
	public ReturnModel find_payorderExtByWeiUserUpUserid(Long weiuserId,String startTimeStr,String endTimeStr,Integer status, int index,int size){
		ReturnModel rq=new ReturnModel();
		if(endTimeStr!=null&&!endTimeStr.equals("")){
			endTimeStr=DateUtil.getEndTime(endTimeStr);			
		}
		PageHelper.startPage(index, size);
		List<OPayorderext> list= payextendMapper.find_payorderExtByWeiUserUpUserid(weiuserId, status, startTimeStr, endTimeStr);
		PageInfo<OPayorderext> resultPage=new PageInfo<OPayorderext>(list); 
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(resultPage);
		return rq;
	}
}
