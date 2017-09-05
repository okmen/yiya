package com.bbyiya.pic.service.impl.cts;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.OOrderaddressMapper;
import com.bbyiya.dao.OOrderproductsMapper;
import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.PProductsMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.OOrderaddress;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.pic.dao.ICts_OrderMgtDao;
import com.bbyiya.pic.service.cts.ICts_OrderMgtService;
import com.bbyiya.service.pic.IBaseUserAddressService;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.order.SearchOrderParam;
import com.bbyiya.vo.order.UserOrderResultVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


@Service("cts_OrderMgtService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Cts_OrderMgtServiceImpl implements ICts_OrderMgtService{
	
	
	/*-------------------用户信息------------------------------------------------*/
	
	
	@Autowired
	private UUsersMapper usersMapper;	
	@Autowired
	private ICts_OrderMgtDao orderDao;
	@Autowired
	private PProductsMapper productsMapper;
	@Autowired
	private OOrderaddressMapper addressMapper;
	@Autowired
	private OOrderproductsMapper orderProductMapper;
	
	@Resource(name = "baseUserAddressServiceImpl")
	private IBaseUserAddressService baseAddressService;
	

	public PageInfo<UserOrderResultVO> find_ctsorderList(SearchOrderParam param,int index,int size){
		
		if(param==null)
			param=new SearchOrderParam();
		if(param.getEndTimeStr()!=null&&!param.getEndTimeStr().equals("")){
			param.setEndTimeStr(DateUtil.getEndTime(param.getEndTimeStr()));
		}
		PageHelper.startPage(index, size);
		List<UserOrderResultVO> list=orderDao.find_CtsUserOrders(param);
		PageInfo<UserOrderResultVO> result=new PageInfo<UserOrderResultVO>(list);
		if(result!=null&&result.getList()!=null&&result.getList().size()>0){
			for (UserOrderResultVO order : result.getList()) {
				if(order.getPaytime()!=null)
					order.setPayTimeStr(DateUtil.getTimeStr(order.getPaytime(), "yyyy-MM-dd HH:mm:ss"));
				OOrderaddress address= addressMapper.selectByPrimaryKey(order.getOrderaddressid());
				if(address!=null){
					order.setAddress(address);
				} 
				List<OOrderproducts> proList= orderProductMapper.findOProductsByOrderId(order.getUserorderid());
				if(proList!=null&&proList.size()>0){
					order.setOrderproduct(proList.get(0));
				}
			}
		}
		return result;
	}
	
}
