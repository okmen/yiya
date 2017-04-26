package com.bbyiya.pic.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bbyiya.common.enums.MsgStatusEnums;
import com.bbyiya.common.enums.SendMsgEnums;
import com.bbyiya.common.vo.ResultMsg;
import com.bbyiya.dao.PMyproductdetailsMapper;
import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.PMyproductsinvitesMapper;
import com.bbyiya.dao.PScenesMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.InviteStatus;
import com.bbyiya.enums.pic.InviteType;
import com.bbyiya.enums.pic.MyProductStatusEnum;
import com.bbyiya.enums.user.UserStatusEnum;
import com.bbyiya.model.PMyproductdetails;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PMyproductsinvites;
import com.bbyiya.model.UUsers;
import com.bbyiya.pic.dao.IMyProductsDao;
import com.bbyiya.pic.service.IPic_myProductService;
import com.bbyiya.pic.vo.product.MyProductListVo;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.SendSMSByMobile;
import com.bbyiya.utils.encrypt.MD5Encrypt;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.product.MyProductResultVo;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
@Service("pic_myProductService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Pic_myProductServiceImpl implements IPic_myProductService{

	 /*--------------------�ҵ���Ʒ----------------------------------*/
	@Autowired
	private PMyproductsinvitesMapper inviteMapper;
	@Autowired
	private PMyproductsMapper myproductsMapper;
	@Autowired
	private PMyproductdetailsMapper myDetaiMapper;
	/*------------------------��Ʒģ��-------------------------------------*/
	@Autowired
	private PScenesMapper sceneMapper;
	@Autowired
	private IMyProductsDao myProductsDao;
	@Autowired
	private UUsersMapper usersMapper;
	/**
	 * Эͬ�༭ ���� 
	 */
	public ReturnModel sendInvite(Long userId, String phone,Long cartId){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError); 
		if(!ObjectUtil.isMobile(phone)){
			rq.setStatusreson("��������ȷ���ֻ���");
			return rq; 
		}
		PMyproducts myproducts= myproductsMapper.selectByPrimaryKey(cartId);
		if(myproducts!=null){
//			if(myproducts.getStatus()!=null&&myproducts.getStatus().intValue()==Integer.parseInt(MyProductStatusEnum.ordered.toString())){
//				rq.setStatu(ReturnStatus.SystemError);
//				rq.setStatusreson("�Ѿ��µ�����Ʒ�޷����д˲�����");
//				return rq;
//			}
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
				invoMo.setInvitetype(Integer.parseInt(InviteType.sendPhoneInvite.toString()));
				invoMo.setStatus(Integer.parseInt(InviteStatus.inviting.toString()));
				invoMo.setCreatetime(new Date());
				inviteMapper.insert(invoMo);
				myproducts.setInvitestatus(Integer.parseInt(InviteStatus.inviting.toString()));
				myproductsMapper.updateByPrimaryKeySelective(myproducts); 
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("�ɹ���������");
			}else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("���������˵���Ʒ�����ܽ��д˲���"); 
			}
		}else {
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("�����ڵ���Ʒ");
		}
		return rq;
	}
	/**
	 * ����ɨ��ҳ��Ľ�������
	 * @param phone ���������ֻ���
	 * @param cartId ��Ʒcartid
	 * @param userId ���������û�ID
	 * @param vcode  ��֤��
	 * @param needVerfiCode  �Ƿ���Ҫ��֤�ֻ���֤�� 0 ����Ҫ��1��Ҫ
	 * @author julie at 2017-04-26
	 * @throws Exception
	 */
	public ReturnModel acceptScanQrCodeInvite(Long userId,String phone,Long cartId,String vcode,Integer needVerfiCode){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError); 
		if(!ObjectUtil.isMobile(phone)){
			rq.setStatusreson("��������ȷ���ֻ���");
			return rq; 
		}
		//�����Ҫ��֤�ֻ�������֤��
		if(needVerfiCode!=null&&needVerfiCode==1){
			ResultMsg msgResult= SendSMSByMobile.validateCode(phone, vcode, SendMsgEnums.register);
			if(msgResult.getStatus()==Integer.parseInt(MsgStatusEnums.ok.toString())) {
				UUsers userPhone=usersMapper.getUUsersByPhone(phone);
				if(userPhone!=null&&userPhone.getUserid().longValue()!=userId){
					rq.setStatu(ReturnStatus.SystemError);
					rq.setStatusreson("���ֻ����Ѿ��������û���");
					return rq;
				}
				UUsers user= usersMapper.getUUsersByUserID(userId);
				if(user!=null){
					user.setMobilephone(phone);
					user.setMobilebind(1);
					user.setStatus(Integer.parseInt(UserStatusEnum.ok.toString())); 
					user.setPassword(""); //Ĭ������Ϊ��
					usersMapper.updateByPrimaryKey(user);
					//LoginSuccessResult result = baseLoginService.getLoginSuccessResult_Common(user);					
				}else {
					rq.setStatu(ReturnStatus.SystemError);
					rq.setStatusreson("ϵͳ����");
					return rq; 
				}
			}else{
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson(msgResult.getMsg()); 
				return rq; 
			}
		}
		PMyproducts myproducts= myproductsMapper.selectByPrimaryKey(cartId);
		if(myproducts!=null){
			if(myproducts.getUserid()!=null){
				List<PMyproductsinvites> list= inviteMapper.findListByCartId(cartId);
				if(list!=null&&list.size()>0){
					for (PMyproductsinvites invo : list) {
						inviteMapper.deleteByPrimaryKey(invo.getInviteid());
					}
				}
				PMyproductsinvites invoMo=new PMyproductsinvites();
				invoMo.setCartid(cartId);
				invoMo.setInvitephone(phone);
				invoMo.setUserid(myproducts.getUserid());//������ID
				invoMo.setInviteuserid(userId);//��������ID
				invoMo.setInvitetype(Integer.parseInt(InviteType.scanQRInvite.toString()));
				invoMo.setStatus(Integer.parseInt(InviteStatus.agree.toString()));
				invoMo.setCreatetime(new Date());
				inviteMapper.insert(invoMo);
				myproducts.setInvitestatus(Integer.parseInt(InviteStatus.agree.toString()));
				myproductsMapper.updateByPrimaryKeySelective(myproducts); 
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("�ɹ���������");			
			}else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("���������˵���Ʒ�����ܽ��д˲���"); 				
			}
		}else {
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("�����ڵ���Ʒ");
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
				rq.setStatusreson("�ɹ���");
				return rq;
			}
		}
		rq.setStatu(ReturnStatus.ParamError);
		rq.setStatusreson("�Ѿ����ڵ����룡");
		return rq;
	}
	
	/**
	 * �ҵ� ������Ϣ��ʾ
	 */
	public ReturnModel  myUserInfoExp(Long userId,String mobilePhone){
		ReturnModel rq=new ReturnModel();
		int count= inviteMapper.countInvitingsByPhone(mobilePhone,Integer.parseInt(InviteStatus.inviting.toString())); 
		rq.setStatu(ReturnStatus.Success);
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("myInvitingCount", count);//�ҵĴ����� ����༭����
		rq.setBasemodle(map);
		return rq;
	}
	
	/**
	 * �ҵ���Ʒ�б�
	 * @param userId
	 * @param index
	 * @param size
	 * @return
	 */
	public ReturnModel find_mycarts(Long userId,String phone,int index,int size){
		ReturnModel rq=new ReturnModel();
		PageHelper.startPage(index, size);
//		List<MyProductResultVo> mylist = myproductsMapper.findMyProductslist(userId, null);
//		PageInfo<MyProductResultVo> resultPage=new PageInfo<MyProductResultVo>(mylist); 
//		if(resultPage.getList()!=null&&resultPage.getList().size()>0){
//			resultPage.setList(getMyProductResultVo(resultPage.getList())); 
//		}
//		UUsers users=usersMapper.selectByPrimaryKey(userId);
//		if(users==null||ObjectUtil.isEmpty(users.getMobilephone())){
//			rq.setStatu(ReturnStatus.ParamError);
//			return rq;
//		}
		List<MyProductListVo> mylist=myProductsDao.findMyProductList(userId,phone);  
		PageInfo<MyProductListVo> resultPage=new PageInfo<MyProductListVo>(mylist); 
		if(resultPage!=null&&resultPage.getList()!=null&&resultPage.getList().size()>0){
			resultPage.setList(exchangeMod(userId,resultPage.getList()));
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(resultPage);
		return rq;
	}
	
	public ReturnModel get_mycart(Long userId, Long cartId){
		ReturnModel rq=new ReturnModel();
		MyProductListVo vo=myProductsDao.getMyProductVO(cartId);
		if(vo!=null){
			List<MyProductListVo> list=new ArrayList<MyProductListVo>();
			list.add(vo);
			list=exchangeMod(userId, list);
			rq.setBasemodle(list.get(0));
			
		}
		rq.setStatu(ReturnStatus.Success);
		return rq;
	}


	private List<MyProductListVo> exchangeMod(Long myUserId, List<MyProductListVo> list){
		if(list!=null&&list.size()>0){
			//���Լ�
			UUsers myUsers=usersMapper.selectByPrimaryKey(myUserId);
			for (MyProductListVo vo : list) {
				// ��Ʒ���飨ͼƬ���ϣ�
				List<PMyproductdetails> detailslist = myDetaiMapper.findMyProductdetails(vo.getCartid());
				int i = 0;
				if (detailslist != null && detailslist.size() > 0) {
					for (PMyproductdetails dd : detailslist) {
						if (!ObjectUtil.isEmpty(dd.getImgurl())) {
							if(dd.getSort()!=null&&dd.getSort().intValue()==0){
								vo.setDefaultImg("http://pic.bbyiya.com/"+dd.getImgurl()+"?imageView2/2/w/200");
							}
							i++;
						}
					}
				}
				if(ObjectUtil.isEmpty(vo.getDefaultImg())){
					vo.setDefaultImg("http://pic.bbyiya.com/484983733454448354.png"); 
				}
				vo.setCount(i);
				 /*---------------------��Ʒ���˵�ͷ����ǳ�--------------------------------*/
				 if (ObjectUtil.isEmpty(myUsers.getUserimg())) {
					vo.setMyHeadImg("http://pic.bbyiya.com/userdefaultimg-2017-0303-01.png");
				 } else {
					vo.setMyHeadImg(myUsers.getUserimg());
				 }
				 if (!ObjectUtil.isEmpty(myUsers.getNickname())&&!"null".equals(myUsers.getNickname())) {
					vo.setMyNickName(myUsers.getNickname());
				 } else {
					vo.setMyNickName(myUsers.getMobilephone());
				 }
				 /*--------------------------����Ʒ���˵�ͷ���ǳ�----------------------------------------------*/
				 if(vo.getUserid().longValue()==myUserId){//�ҵ���Ʒ
					vo.setIsMine(1);
					if(vo.getInvitestatus()!=null&&vo.getInvitestatus()>0){
						List<PMyproductsinvites> invlist= inviteMapper.findListByCartId(vo.getCartid());
						if(invlist!=null&&invlist.size()>0){
							UUsers otherUsers = usersMapper.getUUsersByPhone(invlist.get(0).getInvitephone());
							if(otherUsers!=null){
								if (ObjectUtil.isEmpty(otherUsers.getUserimg())) {
									vo.setOtherHeadImg("http://pic.bbyiya.com/userdefaultimg-2017-0303-01.png");
								} else {
									vo.setOtherHeadImg(otherUsers.getUserimg());
								}
								if (!ObjectUtil.isEmpty(otherUsers.getNickname()) && !"null".equals(otherUsers.getNickname())) {
									vo.setOtherNickName(otherUsers.getNickname());
								} else {
									vo.setOtherNickName(otherUsers.getMobilephone());
								}
							}else {
								vo.setOtherNickName(invlist.get(0).getInvitephone());
								vo.setOtherHeadImg("http://pic.bbyiya.com/userdefaultimg-2017-0303-01.png");
							}
						}
					}
				 }else{//����
					UUsers otherUsers = usersMapper.selectByPrimaryKey(vo.getUserid());
					if (ObjectUtil.isEmpty(otherUsers.getUserimg())) {
						vo.setOtherHeadImg("http://pic.bbyiya.com/userdefaultimg-2017-0303-01.png");
					} else {
						vo.setOtherHeadImg(otherUsers.getUserimg());
					}
					if (!ObjectUtil.isEmpty(otherUsers.getNickname()) && !"null".equals(otherUsers.getNickname())) {
						vo.setOtherNickName(otherUsers.getNickname());
					} else {
						vo.setOtherNickName(otherUsers.getMobilephone());
					}
				 }
			}
		}
		return list;
	}
	
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
				if(item.getInvitestatus()!=null&&item.getInvitestatus()>0){//����Эͬ�༭
					List<PMyproductsinvites> invites= inviteMapper.findListByCartId(item.getCartid());
					if(invites!=null&&invites.size()>0){
						item.setInviteModel(invites.get(0)); 
					} 
				}
				// ��Ʒ���飨ͼƬ���ϣ�
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
