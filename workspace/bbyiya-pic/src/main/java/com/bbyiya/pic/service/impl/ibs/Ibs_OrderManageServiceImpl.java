package com.bbyiya.pic.service.impl.ibs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.OOrderproductsMapper;
import com.bbyiya.dao.OPayorderextMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.model.OPayorderext;
import com.bbyiya.model.UUsers;
import com.bbyiya.pic.service.ibs.IIbs_OrderManageService;
import com.bbyiya.pic.vo.order.OrderCountResultVO;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("ibs_OrderManageService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Ibs_OrderManageServiceImpl implements IIbs_OrderManageService{
	@Autowired
	private OPayorderextMapper payextendMapper;
	@Autowired
	private OOrderproductsMapper orderProductMapper;
	@Autowired
	private UUsersMapper usermapper;
	/**
	 * 根据推荐userId获取推荐的订单列表
	 */
	public ReturnModel find_payorderExtByUpUserid(Long userId,Integer status, String startTime,String endTime,int index,int size){
		ReturnModel rq=new ReturnModel();
		Date startDay=null,endDay=null;
		if(!ObjectUtil.isEmpty(startTime)){
			startDay=DateUtil.getDateByString("yyyy-MM-dd", startTime);
		}
		if(!ObjectUtil.isEmpty(endTime)){
			//获取日期的最后结束时间
			endTime=DateUtil.getEndTime(endTime);
			endDay=DateUtil.getDateByString("yyyy-MM-dd HH:mm:ss", endTime);
		}
		
		PageHelper.startPage(index, size);
		List<OPayorderext> list= payextendMapper.findListByUpUserid(userId, status,startDay,endDay);
		PageInfo<OPayorderext> resultPage=new PageInfo<OPayorderext>(list); 
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(resultPage);
		return rq;
	}
	
	/**
	 * IBS统计excel导出列表
	 * @param userId
	 * @param status
	 * @param startTime
	 * @param endTime
	 * @param index
	 * @param size
	 * @return
	 */
	public List<OrderCountResultVO> find_ibsOrderExportExcelbyUpUserid(Long userId,Integer status, String startTime,String endTime,int index,int size){
		ReturnModel rq=new ReturnModel();
		Date startDay=null,endDay=null;
		if(!ObjectUtil.isEmpty(startTime)){
			startDay=DateUtil.getDateByString("yyyy-MM-dd", startTime);
		}
		if(!ObjectUtil.isEmpty(endTime)){
			//获取日期的最后结束时间
			String endTimefmt=DateUtil.getEndTime(endTime);
			endDay=DateUtil.getDateByString("yyyy-MM-dd HH:mm:ss", endTimefmt);
		}
		
		PageHelper.startPage(index, size);
		List<OPayorderext> list= payextendMapper.findListByUpUserid(userId, status,startDay,endDay);
		PageInfo<OPayorderext> resultPage=new PageInfo<OPayorderext>(list);
		
		List<OrderCountResultVO> resultVoList=new ArrayList<OrderCountResultVO>();
		for (OPayorderext ext : list) {
			OrderCountResultVO resultvo=new OrderCountResultVO();
			OOrderproducts product=orderProductMapper.getOProductsByOrderId(ext.getUserorderid());
			resultvo.setOrderpaytime(DateUtil.getTimeStr(ext.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
			resultvo.setUserorderid(ext.getUserorderid());
			resultvo.setTotalprice(ext.getTotalprice());
			resultvo.setStarttime(startTime);
			resultvo.setEndtime(endTime);
			resultvo.setCount(product.getCount());
			if(product!=null){
				resultvo.setCount(product.getCount());
				resultvo.setProducttitle(product.getProducttitle()+product.getPropertystr());
			}
			UUsers user=usermapper.selectByPrimaryKey(ext.getUserid());
			if(user!=null){
				resultvo.setUsercreatetime(DateUtil.getTimeStr(user.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
				resultvo.setUsername(user.getNickname());
			}
			resultVoList.add(resultvo);
		}
		return resultVoList;
	}
}
