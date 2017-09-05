package com.bbyiya.pic.service.impl.ibs;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.OBranchordersMapper;
import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.PMyproductchildinfoMapper;
import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.PMyproducttempMapper;
import com.bbyiya.dao.PProductsMapper;
import com.bbyiya.dao.UBranchesMapper;
import com.bbyiya.dao.UBranchusersMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.PMyproductchildinfo;
import com.bbyiya.model.PProducts;
import com.bbyiya.model.UBranches;
import com.bbyiya.pic.service.ibs.IIbs_OrderMgtService;
import com.bbyiya.service.pic.IBaseUserAddressService;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.order.BranchOrderVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("ibs_OrderMgtService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Ibs_OrderMgtServiceImpl implements IIbs_OrderMgtService{
	
	@Autowired
	private OBranchordersMapper branchorderMappger;
	
	@Autowired
	private PMyproducttempMapper tempMapper;
	@Autowired
	private PMyproductsMapper myMapper;
	
	@Autowired
	private PMyproductchildinfoMapper mychildinfoMapper;
	

	/*-------------------用户信息------------------------------------------------*/
	
	@Autowired
	private UBranchesMapper branchesMapper;
	@Autowired
	private UUsersMapper usersMapper;
	@Autowired
	private UBranchusersMapper branchusersMapper;// 影楼信息
	@Autowired
	private OUserordersMapper orderMapper;
	@Autowired
	private PProductsMapper productsMapper;
	
	@Resource(name = "baseUserAddressServiceImpl")
	private IBaseUserAddressService baseAddressService;

	public ReturnModel findMyBranchOrderlist(Long branchUserId,Integer status,String keywords,int index,int size){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.Success);
		PageHelper.startPage(index, size);
		//订单列表
		List<BranchOrderVo> branchorders= branchorderMappger.findBranchOrdersByBranchUserId(branchUserId,status,keywords);
		PageInfo<BranchOrderVo> resultPage=new PageInfo<BranchOrderVo>(branchorders); 
		if(resultPage!=null&&resultPage.getList()!=null&&resultPage.getList().size()>0){
			for (BranchOrderVo order : branchorders) {
				if(order.getBirthday()!=null){
					order.setBabybirthdaystr(DateUtil.getTimeStr(order.getBirthday(), "yyyy-MM-dd"));
				}else{
					PMyproductchildinfo mychildinfo=mychildinfoMapper.selectByPrimaryKey(order.getCartid());
					if(mychildinfo!=null){
						order.setBabybirthdaystr(DateUtil.getTimeStr(mychildinfo.getBirthday(), "yyyy-MM-dd"));
					}
				}
				UBranches branches=branchesMapper.selectByPrimaryKey(order.getFrombranchuserid());
				if(branches!=null){
					order.setBranchname(branches.getBranchcompanyname());
				}
				// 得到制作类型
				PProducts product = productsMapper.selectByPrimaryKey(order.getProductid());
				if (product != null && product.getTitle() != null) {
					order.setProducttitle(product.getTitle());
				}	
			}	
			rq.setBasemodle(resultPage);
		}
		rq.setStatusreson("ok");
		return rq;
	}
	
	
	
}
