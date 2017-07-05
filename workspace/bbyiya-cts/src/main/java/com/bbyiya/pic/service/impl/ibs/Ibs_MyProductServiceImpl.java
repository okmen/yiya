package com.bbyiya.pic.service.impl.ibs;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.PMyproductchildinfoMapper;
import com.bbyiya.dao.PMyproductdetailsMapper;
import com.bbyiya.dao.PMyproductextMapper;
import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.PMyproductsinvitesMapper;
import com.bbyiya.dao.PMyproducttempMapper;
import com.bbyiya.dao.PMyproducttempapplyMapper;
import com.bbyiya.dao.PProductsMapper;
import com.bbyiya.dao.PProductstylesMapper;
import com.bbyiya.dao.UBranchusersMapper;
import com.bbyiya.dao.UChildreninfoMapper;
import com.bbyiya.dao.UUseraddressMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.MyProductStatusEnum;
import com.bbyiya.enums.pic.MyProducttempApplyStatusEnum;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.PMyproductchildinfo;
import com.bbyiya.model.PMyproductdetails;
import com.bbyiya.model.PMyproductext;
import com.bbyiya.model.PMyproductsinvites;
import com.bbyiya.model.PMyproducttemp;
import com.bbyiya.model.PMyproducttempapply;
import com.bbyiya.model.PProducts;
import com.bbyiya.model.UBranchusers;
import com.bbyiya.model.UUsers;
import com.bbyiya.pic.service.ibs.IIbs_MyproductService;
import com.bbyiya.service.pic.IBaseUserAddressService;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.product.MyProductResultVo;
import com.bbyiya.vo.user.UUserAddressResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


@Service("ibs_MyproductService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Ibs_MyProductServiceImpl implements IIbs_MyproductService{
	@Autowired
	private PMyproducttempMapper myproducttempMapper;
	@Autowired
	private PMyproductsMapper myMapper;
	@Autowired
	private PMyproductdetailsMapper myDetaiMapper;
	@Autowired
	private PProductsMapper productsMapper;
	@Autowired
	private PProductstylesMapper styleMapper;

	@Autowired
	private PMyproductchildinfoMapper mychildMapper;
	@Autowired
	private PMyproducttempapplyMapper tempapplyMapper;
	@Autowired
	private PMyproducttempMapper tempMapper;
	
	@Autowired
	private PMyproductextMapper myextMapper;
	@Autowired
	private PMyproductsinvitesMapper inviteMapper;
	/*-------------------用户信息------------------------------------------------*/
	@Autowired
	private UUsersMapper usersMapper;
	@Autowired
	private UBranchusersMapper branchusersMapper;// 影楼信息
	@Autowired
	private OUserordersMapper orderMapper;
	@Autowired
	private UChildreninfoMapper childMapper;
	@Autowired
	private UUseraddressMapper uaddressMapper;

	
	@Resource(name = "baseUserAddressServiceImpl")
	private IBaseUserAddressService baseAddressService;

	
	public ReturnModel findMyProductsSourceCustomerOfBranch(Long branchUserId, Integer status, Integer inviteStatus,String keywords, int index, int size) {
		ReturnModel rq = new ReturnModel();
		List<Long> idsList = new ArrayList<Long>();
		List<String> phoneList =null;
		idsList.add(branchUserId);
		// 获取影楼的工作人员列表
		List<UBranchusers> userList = branchusersMapper.findMemberslistByBranchUserId(branchUserId);
		if (userList != null && userList.size() > 0) {
			phoneList=new ArrayList<String>();
			for (UBranchusers uu : userList) {
				idsList.add(uu.getUserid());
				phoneList.add(uu.getPhone());
			}
		}
		PageHelper.startPage(index, size);
		List<MyProductResultVo> mylist = myMapper.findMyProductsSourceCustomerOfBranch(idsList,phoneList, status, inviteStatus,keywords);
		PageInfo<MyProductResultVo> resultPage = new PageInfo<MyProductResultVo>(mylist);
		if (resultPage.getList() != null && resultPage.getList().size() > 0) {
			for (MyProductResultVo vv : resultPage.getList()) {
				
				UUsers user=usersMapper.selectByPrimaryKey(vv.getUserid());
				if(user!=null){
					vv.setUserName(user.getNickname());//邀约人名称
					vv.setPhone(user.getMobilephone());//邀约人手机号
					vv.setUserImg(user.getUserimg());
				}
				
				//客户地址
				UUserAddressResult address=baseAddressService.getUserAddressResult(vv.getUserid(), null);
				if(address!=null){
					vv.setAddress(address.getProvinceName()+address.getCityName()+address.getCityName()+address.getStreetdetail());
				}
			}
			resultPage.setList(getMyProductResultVo(resultPage.getList()));
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(resultPage);
		return rq;
	}
	
	/**
	 * 我的作品model转换
	 * 
	 * @param mylist
	 * @return
	 */
	private List<MyProductResultVo> getMyProductResultVo(List<MyProductResultVo> mylist) {
		if (mylist != null && mylist.size() > 0) {
			for (MyProductResultVo item : mylist) {
				if (!ObjectUtil.isEmpty(item.getUpdatetime())) {
					item.setCreatetimestr(DateUtil.getTimeStr(item.getUpdatetime(), "yyyy-MM-dd HH:mm:ss"));
				} else {
					item.setCreatetimestr(DateUtil.getTimeStr(item.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
				}
				if (item.getInvitestatus() != null && item.getInvitestatus() > 0) {// 邀请协同编辑
					List<PMyproductsinvites> invites = inviteMapper.findListByCartId(item.getCartid());
					if (invites != null && invites.size() > 0) {
						item.setInviteModel(invites.get(0));
						item.setInvitestatus(invites.get(0).getStatus());
						UBranchusers branchuser=branchusersMapper.selectByPrimaryKey(invites.get(0).getInviteuserid());
						if(branchuser!=null){
							item.setInvitedName(branchuser.getName());
							item.getInviteModel().setInvitephone(branchuser.getPhone());
						}
						
						// 得到来源，即模板名称
						if (item.getTempid() != null) {
							PMyproducttemp temp = tempMapper.selectByPrimaryKey(item.getTempid());
							if (temp != null && temp.getTitle() != null) {
								item.setTempTitle(temp.getTitle());
							}
						}
					}
				}
				
				// 得到制作类型
				PProducts product = productsMapper.selectByPrimaryKey(item.getProductid());
				if (product != null && product.getTitle() != null) {
					item.setProductTitle(product.getTitle());
				}
				// 作品详情（图片集合）
				List<PMyproductdetails> detailslist = myDetaiMapper.findMyProductdetails(item.getCartid());
				int i = 0;
				if (detailslist != null && detailslist.size() > 0) {
					for (PMyproductdetails dd : detailslist) {
						if (!ObjectUtil.isEmpty(dd.getImgurl())) {
							if (dd.getSort() != null && dd.getSort().intValue() == 0) {
								item.setHeadImg("http://pic.bbyiya.com/" + dd.getImgurl() + "?imageView2/2/w/200");
							}
							i++;
						}
					}
				}
				if (ObjectUtil.isEmpty(item.getHeadImg())) {
					item.setHeadImg("http://pic.bbyiya.com/484983733454448354.png");
				}
				item.setCount(i);
				if (item.getStatus() != null && item.getStatus().intValue() == Integer.parseInt(MyProductStatusEnum.ordered.toString())) {
					item.setIsOrder(1);
					item.setCount(12);
				}
				item.setIsDue(0);//默认不是预产期
				// 得到宝宝生日
				PMyproductchildinfo childinfo = mychildMapper.selectByPrimaryKey(item.getCartid());
				if (childinfo != null && childinfo.getBirthday() != null) {
					item.setBirthdayStr(DateUtil.getTimeStr(childinfo.getBirthday(), "yyyy-MM-dd HH:mm:ss"));
					item.setIsDue(childinfo.getIsdue()==null?0:childinfo.getIsdue());
				}
				
				
				List<OUserorders> orderList = orderMapper.findOrderListByCartId(item.getCartid());
				List<String> orderNoList = new ArrayList<String>();
				for (OUserorders order : orderList) {
					orderNoList.add(order.getUserorderid());
				}
				if (orderNoList.size() > 0) {
					item.setOrderNoList(orderNoList);
				}
				//得到评论数
				item.setCommentsCount(0);
				PMyproductext myext=myextMapper.selectByPrimaryKey(item.getCartid());
				if(myext!=null){
					item.setCommentsCount(myext.getCommentscount()==null?0:myext.getCommentscount());
				}
				
				//得到活动状态
				PMyproducttempapply apply= tempapplyMapper.getMyProducttempApplyByCartId(item.getCartid());
				if(apply==null){
					apply=tempapplyMapper.getMyProducttempApplyByUserId(item.getTempid(), item.getInviteModel().getInviteuserid());
				}
				if(apply!=null){
					item.setActiveStatus(apply.getStatus());
					item.setAddress(apply.getAdress());
					item.setSort(apply.getSort());
				}else{
					if(item.getCount()<12){
						//制作中
						item.setActiveStatus(Integer.parseInt(MyProducttempApplyStatusEnum.ok.toString()));
					}else{
						//制作完成
						item.setActiveStatus(Integer.parseInt(MyProducttempApplyStatusEnum.complete.toString()));
						
					}
				}

			}
		}
		return mylist;
	}

	
}
