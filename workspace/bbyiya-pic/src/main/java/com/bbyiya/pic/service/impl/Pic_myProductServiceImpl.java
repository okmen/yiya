package com.bbyiya.pic.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;









import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.PMyproductdetailsMapper;
import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.PMyproductsinvitesMapper;
import com.bbyiya.dao.PScenesMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.InviteStatus;
import com.bbyiya.enums.pic.MyProductStatusEnum;
import com.bbyiya.model.PMyproductdetails;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PMyproductsinvites;
import com.bbyiya.model.UUsers;
import com.bbyiya.pic.service.IPic_myProductService;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.product.MyProductResultVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
@Service("pic_myProductService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Pic_myProductServiceImpl implements IPic_myProductService{

	 /*--------------------我的作品----------------------------------*/
	@Autowired
	private PMyproductsinvitesMapper inviteMapper;
	@Autowired
	private PMyproductsMapper myproductsMapper;
	@Autowired
	private PMyproductdetailsMapper myDetaiMapper;
	/*------------------------产品模块-------------------------------------*/
	@Autowired
	private PScenesMapper sceneMapper;
	
	/**
	 * 协同编辑 邀请 
	 */
	public ReturnModel sendInvite(Long userId, String phone,Long cartId){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError); 
		if(!ObjectUtil.isMobile(phone)){
			rq.setStatusreson("请输入正确的手机号");
			return rq; 
		}
		PMyproducts myproducts= myproductsMapper.selectByPrimaryKey(cartId);
		if(myproducts!=null){
			if(myproducts.getStatus()!=null&&myproducts.getStatus().intValue()==Integer.parseInt(MyProductStatusEnum.ordered.toString())){
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("已经下单的作品无法进行此操作！");
				return rq;
			}
			if(myproducts.getUserid()!=null&&myproducts.getUserid().longValue()==userId){
				List<PMyproductsinvites> list= inviteMapper.findListByCartId(cartId);
				if(list!=null&&list.size()>0){
					for (PMyproductsinvites invo : list) {
						inviteMapper.deleteByPrimaryKey(invo.getInviteid());
					}
				}
				PMyproductsinvites invoMo=new PMyproductsinvites();
				invoMo.setCartid(cartId);
				invoMo.setInvitephone(phone);
				invoMo.setUserid(userId);
				invoMo.setStatus(Integer.parseInt(InviteStatus.inviting.toString()));
				invoMo.setCreatetime(new Date());
				inviteMapper.insert(invoMo);
				myproducts.setInvitestatus(Integer.parseInt(InviteStatus.inviting.toString()));
				myproductsMapper.updateByPrimaryKeySelective(myproducts); 
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("成功发送邀请");
			}else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("不是您本人的作品，不能进行此操作"); 
			}
		}else {
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("不存在的作品");
		}
		return rq;
	}
	
	public ReturnModel processInvite(String phone, Long cartId, int status) {
		ReturnModel rq = new ReturnModel();
		PMyproductsinvites invite = inviteMapper.getInviteByPhoneAndCartId(phone, cartId); //inviteMapper.selectByPrimaryKey(inviteId);
		if (invite != null) {
			if (phone.equals(invite.getInvitephone())) {
				invite.setStatus(status);
				inviteMapper.updateByPrimaryKeySelective(invite);
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("成功！");
				return rq;
			}
		}
		rq.setStatu(ReturnStatus.ParamError);
		rq.setStatusreson("已经过期的邀请！");
		return rq;
	}
	
	/**
	 * 我的 个人信息提示
	 */
	public ReturnModel  myUserInfoExp(Long userId,String mobilePhone){
		ReturnModel rq=new ReturnModel();
		int count= inviteMapper.countInvitingsByPhone(mobilePhone,Integer.parseInt(InviteStatus.inviting.toString())); 
		rq.setStatu(ReturnStatus.Success);
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("myInvitingCount", count);//我的待处理 邀请编辑数量
		rq.setBasemodle(map);
		return rq;
	}
	
	/**
	 * 我的作品列表
	 * @param userId
	 * @param index
	 * @param size
	 * @return
	 */
	public ReturnModel find_mycarts(Long userId,int index,int size){
		ReturnModel rq=new ReturnModel();
		PageHelper.startPage(index, size);
		List<MyProductResultVo> mylist = myproductsMapper.findMyProductslist(userId, null);
		PageInfo<MyProductResultVo> resultPage=new PageInfo<MyProductResultVo>(mylist); 
		if(resultPage.getList()!=null&&resultPage.getList().size()>0){
			resultPage.setList(getMyProductResultVo(resultPage.getList())); 
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(resultPage);
		return rq;
	}
	@Autowired
	private UUsersMapper usersMapper;
	
	public ReturnModel find_mycartsInvited(Long userId){
		ReturnModel rq=new ReturnModel();
		UUsers user = usersMapper.getUUsersByUserID(userId);
		if(user!=null&&!ObjectUtil.isEmpty(user.getMobilephone())){
			List<PMyproductsinvites> inviteList=inviteMapper.findListByPhone(user.getMobilephone());
			List<MyProductResultVo> list=findInvites(inviteList);
			rq.setBasemodle(list);
		}
		rq.setStatu(ReturnStatus.Success);
		return rq;
	}
	
	public List<MyProductResultVo> findInvites(List<PMyproductsinvites>  inviteList){
		if(inviteList!=null&&inviteList.size()>0){
			List<MyProductResultVo> resultList=new ArrayList<MyProductResultVo>();
			for (PMyproductsinvites in : inviteList) {
				MyProductResultVo vo=myproductsMapper.getMyProductResultVo(in.getCartid()); 
				if(vo!=null){
					vo.setIsInvited(1);
					vo.setInvitestatus(in.getStatus()); 
					UUsers users=usersMapper.selectByPrimaryKey(vo.getUserid());
					if(users!=null){
						vo.setUserName(users.getMobilephone());
						if(ObjectUtil.isEmpty(users.getUserimg())){
							vo.setUserImg("http://pic.bbyiya.com/userdefaultimg-2017-0303-01.png");
						}else {
							vo.setUserImg(users.getUserimg()); 
						}
					}
					resultList.add(vo);
				}
			}
			return getMyProductResultVo(resultList); 
		}
		return null;
	}

	private List<MyProductResultVo> getMyProductResultVo(List<MyProductResultVo> mylist){
		if (mylist != null && mylist.size() > 0) {
			for (MyProductResultVo item : mylist) {
				if(!ObjectUtil.isEmpty(item.getUpdatetime())){
					item.setCreatetimestr(DateUtil.getTimeStr(item.getUpdatetime(), "yyyy-MM-dd HH:mm:ss")); 
				}else {
					item.setCreatetimestr(DateUtil.getTimeStr(item.getCreatetime(), "yyyy-MM-dd HH:mm:ss")); 
				}
				if(item.getInvitestatus()!=null&&item.getInvitestatus()>0){//邀请协同编辑
					List<PMyproductsinvites> invites= inviteMapper.findListByCartId(item.getCartid());
					if(invites!=null&&invites.size()>0){
						item.setInviteModel(invites.get(0)); 
					} 
				}
				// 作品详情（图片集合）
				List<PMyproductdetails> detailslist = myDetaiMapper.findMyProductdetails(item.getCartid());
				int i = 0;
				if (detailslist != null && detailslist.size() > 0) {
					for (PMyproductdetails dd : detailslist) {
						if (!ObjectUtil.isEmpty(dd.getImgurl())) {
							if(dd.getSort()!=null&&dd.getSort().intValue()==0){
								item.setHeadImg("http://pic.bbyiya.com/"+dd.getImgurl()+"?imageView2/2/w/200");
							}
							i++;
						}
					}
				}
				if(ObjectUtil.isEmpty(item.getHeadImg())){
					item.setHeadImg("http://pic.bbyiya.com/484983733454448354.png"); 
				}
				item.setCount(i);
				if(item.getStatus()!=null&&item.getStatus().intValue()==Integer.parseInt(MyProductStatusEnum.ordered.toString())){
					item.setIsOrder(1);
					item.setCount(12);
				} 
			}
		}
		return mylist;
	}
}
