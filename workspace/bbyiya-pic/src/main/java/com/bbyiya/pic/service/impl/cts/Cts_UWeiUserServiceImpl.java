package com.bbyiya.pic.service.impl.cts;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.OPayorderextMapper;
import com.bbyiya.dao.UWeiusersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.OPayorderext;
import com.bbyiya.model.UWeiusers;
import com.bbyiya.pic.service.cts.ICts_UWeiUserManageService;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.UWeiUserSearchParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("cts_UWeiuserService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Cts_UWeiUserServiceImpl implements ICts_UWeiUserManageService{
	@Autowired
	private OPayorderextMapper payextendMapper;
	
	@Autowired
	private UWeiusersMapper weiuserMapper;
	
	/**
	 * 获取影楼推荐人发展的用户订单列表
	 */
	public ReturnModel find_payorderExtByBranchUpUserid(Long branchuserId,String startTimeStr,String endTimeStr,Integer status, int index,int size){
		ReturnModel rq=new ReturnModel();
		PageHelper.startPage(index, size);
		List<OPayorderext> list= payextendMapper.find_payorderExtByBranchUpUserid(branchuserId, status, startTimeStr, endTimeStr);
		PageInfo<OPayorderext> resultPage=new PageInfo<OPayorderext>(list); 
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(resultPage);
		return rq;
	}
	
	public ReturnModel findWeiUserVoList(UWeiUserSearchParam param,int index, int size){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.Success);
		PageHelper.startPage(index, size);
		List<UWeiusers> list=weiuserMapper.findUWeiusersList(param);
		PageInfo<UWeiusers> result=new PageInfo<UWeiusers>(list); 
		rq.setBasemodle(result);
		return rq;
	}
}
