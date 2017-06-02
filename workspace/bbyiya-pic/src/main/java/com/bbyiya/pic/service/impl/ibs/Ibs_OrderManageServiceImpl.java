package com.bbyiya.pic.service.impl.ibs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.OOrderproductsMapper;
import com.bbyiya.dao.OPayorderextMapper;
import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.PMyproducttempapplyMapper;
import com.bbyiya.dao.UBranchesMapper;
import com.bbyiya.dao.UBranchusersMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.OOrderaddress;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.model.OPayorderext;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PMyproducttempapply;
import com.bbyiya.model.UBranches;
import com.bbyiya.model.UBranchusers;
import com.bbyiya.model.UUsers;
import com.bbyiya.pic.service.ibs.IIbs_OrderManageService;
import com.bbyiya.pic.vo.order.OrderCountResultVO;
import com.bbyiya.service.IRegionService;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.address.OrderaddressVo;
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
	@Autowired
	private PMyproductsMapper myproductMapper;
	@Autowired
	private PMyproducttempapplyMapper myproductapplyMapper;
	
	@Resource(name = "regionServiceImpl")
	private IRegionService regionService;
	/*------------------------代理模块-------------------------------------*/
	@Autowired
	private UBranchesMapper branchesMapper;
	@Autowired
	private UBranchusersMapper branchusersMapper;
	
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
	 * 影楼内部异业作品下单前得到作品的相关地址
	 */
	public ReturnModel getMyProductAddressList(Long userId,Long cartid){
		ReturnModel rq=new ReturnModel();
		PMyproducts myproduct=myproductMapper.selectByPrimaryKey(cartid);
		if(myproduct!=null){
			List<OrderaddressVo> addressList=new ArrayList<OrderaddressVo>();
			UBranchusers branchusers= branchusersMapper.selectByPrimaryKey(userId);
			if(branchusers!=null&&branchusers.getBranchuserid()!=null){
				UBranches branches=branchesMapper.selectByPrimaryKey(branchusers.getBranchuserid());
				//得到影楼地址
				if (branches != null) {
					OrderaddressVo orderAddress = new OrderaddressVo();
					orderAddress.setUserid(branches.getBranchuserid());
					orderAddress.setPhone(branches.getPhone());
					orderAddress.setReciver(branches.getUsername());
					orderAddress.setCity(branches.getCity());
					orderAddress.setProvince(branches.getProvince());
					orderAddress.setDistrict(branches.getArea());
					orderAddress.setCityName(regionService.getCityName(branches.getCity()));
					orderAddress.setProvinceName(regionService.getProvinceName(branches.getProvince()));
					orderAddress.setDistrictName(regionService.getAresName(branches.getArea()));
					orderAddress.setStreetdetail(branches.getStreetdetail());
					orderAddress.setAddressType(0);
					addressList.add(orderAddress);
				}
			}
			//得到作品用户申请地址
			PMyproducttempapply tempApply=myproductapplyMapper.getMyProducttempApplyByCartId(cartid);
			if(tempApply!=null){
				OrderaddressVo orderAddress2 = new OrderaddressVo();
				orderAddress2.setUserid(tempApply.getUserid());
				orderAddress2.setPhone(tempApply.getMobilephone());
				orderAddress2.setReciver(tempApply.getReceiver());
				orderAddress2.setCity(tempApply.getCity());
				orderAddress2.setProvince(tempApply.getProvince());
				orderAddress2.setDistrict(tempApply.getArea());
				orderAddress2.setCityName(regionService.getCityName(tempApply.getCity()));
				orderAddress2.setProvinceName(regionService.getProvinceName(tempApply.getProvince()));
				orderAddress2.setDistrictName(regionService.getAresName(tempApply.getArea()));
				orderAddress2.setStreetdetail(tempApply.getAdress());
				orderAddress2.setAddressType(1);
				addressList.add(orderAddress2);
			}
			
			rq.setBasemodle(addressList);
			rq.setStatu(ReturnStatus.Success);
		}
		
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
