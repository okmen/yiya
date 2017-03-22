package com.bbyiya.pic.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.PMyproductsinvitesMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.InviteStatus;
import com.bbyiya.enums.pic.MyProductStatusEnum;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PMyproductsinvites;
import com.bbyiya.pic.service.IPic_myProductService;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
@Service("pic_myProductService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Pic_myProductServiceImpl implements IPic_myProductService{
	
	@Autowired
	private PMyproductsinvitesMapper inviteMapper;
	@Autowired
	private PMyproductsMapper myproductsMapper;
	
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
	
}
