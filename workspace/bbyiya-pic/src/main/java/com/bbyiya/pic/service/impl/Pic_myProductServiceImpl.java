package com.bbyiya.pic.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.common.enums.MsgStatusEnums;
import com.bbyiya.common.enums.SendMsgEnums;
import com.bbyiya.common.vo.ResultMsg;
import com.bbyiya.dao.PMyproductchildinfoMapper;
import com.bbyiya.dao.PMyproductdetailsMapper;
import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.PMyproductsinvitesMapper;
import com.bbyiya.dao.PMyproducttempMapper;
import com.bbyiya.dao.PMyproducttempapplyMapper;
import com.bbyiya.dao.PScenesMapper;
import com.bbyiya.dao.UAgentcustomersMapper;
import com.bbyiya.dao.UBranchesMapper;
import com.bbyiya.dao.UBranchusersMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.CustomerSourceTypeEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.InviteStatus;
import com.bbyiya.enums.pic.InviteType;
import com.bbyiya.enums.pic.MyProductStatusEnum;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.enums.user.UserStatusEnum;
import com.bbyiya.model.PMyproductchildinfo;
import com.bbyiya.model.PMyproductdetails;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PMyproductsinvites;
import com.bbyiya.model.PMyproducttemp;
import com.bbyiya.model.UAgentcustomers;
import com.bbyiya.model.UBranches;
import com.bbyiya.model.UBranchusers;
import com.bbyiya.model.UUsers;
import com.bbyiya.pic.dao.IMyProductsDao;
import com.bbyiya.pic.service.IPic_myProductService;
import com.bbyiya.pic.vo.product.MyProductListVo;
import com.bbyiya.utils.ConfigUtil;
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

	 /*-----------------------我的作品----------------------------------*/
	@Autowired
	private PMyproductsinvitesMapper inviteMapper;
	@Autowired
	private PMyproductsMapper myproductsMapper;
	@Autowired
	private PMyproductdetailsMapper myDetaiMapper;
	@Autowired
	private PMyproductchildinfoMapper childinfoMapper;
	@Autowired
	private PMyproducttempMapper tempMapper;
	@Autowired
	private UAgentcustomersMapper customerMapper;
	
	@Autowired
	private UBranchusersMapper branchuserMapper;
	/*------------------------产品模块-------------------------------------*/
	@Autowired
	private PScenesMapper sceneMapper;
	@Autowired
	private IMyProductsDao myProductsDao;
	@Autowired
	private UUsersMapper usersMapper;
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
//			if(myproducts.getStatus()!=null&&myproducts.getStatus().intValue()==Integer.parseInt(MyProductStatusEnum.ordered.toString())){
//				rq.setStatu(ReturnStatus.SystemError);
//				rq.setStatusreson("已经下单的作品无法进行此操作！");
//				return rq;
//			}
			if(myproducts.getUserid()!=null&&myproducts.getUserid().longValue()==userId){
				List<PMyproductsinvites> list= inviteMapper.findListByCartId(cartId);			
				boolean flag=true;
				if(list!=null&&list.size()>0){
					for (PMyproductsinvites invo : list) {
						if(invo.getStatus()==Integer.parseInt(InviteStatus.agree.toString())){
							flag=false;
							rq.setStatu(ReturnStatus.ParamError);
							rq.setStatusreson("已有人接受邀请协同编辑，不能再邀请了！"); 
							return rq;
						}						
					}
					//如果没有在接受邀请的情况下，清空以前的未接受或已拒绝的邀请
					if(flag){
						for (PMyproductsinvites invo : list) {
							inviteMapper.deleteByPrimaryKey(invo.getInviteid());
						}
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
				//需要重新更新版本号
				String versionString=DateUtil.getTimeStr(new Date(), "yyyyMMddHHMMss"); 
				myproducts.setVersion(versionString);
				myproductsMapper.updateByPrimaryKey(myproducts); 
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
	
	/**
	 * 处理医院扫码页面的接受邀请
	 * @param phone 被邀请人手机号
	 * @param cartId 作品cartid
	 * @param userId 被邀请人用户Id
	 * @author julie at 2017-04-26
	 * @throws Exception
	 */
	public ReturnModel acceptTempScanQrCodeInvite(Long userId,String phone,Long cartId,String vcode,Integer needVerfiCode){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError); 
		if(!ObjectUtil.isMobile(phone)){
			rq.setStatusreson("请输入正确的手机号");
			return rq; 
		}
		//如果需要验证手机短信验证码
		if(needVerfiCode!=null&&needVerfiCode==1){
			ResultMsg msgResult= SendSMSByMobile.validateCode(phone, vcode, SendMsgEnums.register);
			if(msgResult.getStatus()==Integer.parseInt(MsgStatusEnums.ok.toString())) {
				UUsers userPhone=usersMapper.getUUsersByPhone(phone);
				if(userPhone!=null&&userPhone.getUserid().longValue()!=userId.longValue()){
					rq.setStatu(ReturnStatus.SystemError);
					rq.setStatusreson("该手机号已经绑定其他用户！");
					return rq;
				}
				UUsers user= usersMapper.getUUsersByUserID(userId);
				if(user!=null){
					user.setMobilephone(phone);
					user.setMobilebind(1);
					user.setStatus(Integer.parseInt(UserStatusEnum.ok.toString())); 
					user.setPassword(""); //默认密码为空
					usersMapper.updateByPrimaryKey(user);
					//LoginSuccessResult result = baseLoginService.getLoginSuccessResult_Common(user);					
				}else {
					rq.setStatu(ReturnStatus.SystemError);
					rq.setStatusreson("系统错误");
					return rq; 
				}
			}else{
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson(msgResult.getMsg()); 
				return rq; 
			}
		}
		//模板作品ID
		PMyproducts myproducts= myproductsMapper.selectByPrimaryKey(cartId);
		if(myproducts!=null){	
			//查询相应的模板下的作品列表
			List<MyProductListVo> myprolist=myProductsDao.getMyProductResultByTempId(myproducts.getTempid());
			if(myprolist!=null&&myprolist.size()>0){
				for (MyProductListVo mypro : myprolist) {
					//自已不能扫自已的模板作品二维码
					if(mypro.getUserid().longValue()==userId.longValue()){
						rq.setStatu(ReturnStatus.ParamError);					
						rq.setStatusreson("不能接受自已作品的邀请！"); 
						return rq;
					}
					if(mypro.getCartid()!=cartId){
						//这个列表包括模板作品
						List<PMyproductsinvites> list= inviteMapper.findListByCartId(mypro.getCartid());	
						if(list!=null&&list.size()>0){
							for (PMyproductsinvites invo : list) {
								if(invo.getInviteuserid()!=null&&invo.getInviteuserid().longValue()==userId){
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("tempId", myproducts.getTempid());
									map.put("mycartid", invo.getCartid());
									rq.setBasemodle(map);						
									rq.setStatu(ReturnStatus.Success);
									rq.setStatusreson("已经接受邀请协同编辑，请直接跳转到作品页！"); 
									return rq;
								}
							}
						}
					}					
				}
				//如果这个用户以前没有接受邀请，则copy一份作品
				PMyproducts newproducts=new PMyproducts();
				newproducts.setAuthor(myproducts.getAuthor());
				newproducts.setCreatetime(new Date());
				newproducts.setDescription(myproducts.getDescription());
				newproducts.setHeadimg(myproducts.getHeadimg());
				newproducts.setIstemp(0);
				newproducts.setPhone(myproducts.getPhone());
				newproducts.setProductid(myproducts.getProductid());
				newproducts.setStatus(Integer.parseInt(MyProductStatusEnum.ok.toString()));
				newproducts.setUserid(myproducts.getUserid());
				newproducts.setStyleid(myproducts.getStyleid());
				newproducts.setTempid(myproducts.getTempid());
				newproducts.setTitle(myproducts.getTitle());
				myproductsMapper.insertReturnId(newproducts);
				
				PMyproductchildinfo childinfo=childinfoMapper.selectByPrimaryKey(myproducts.getCartid());
				PMyproductchildinfo newchildinfo=new PMyproductchildinfo();				
				if(childinfo!=null){
					newchildinfo.setBirthday(childinfo.getBirthday());
					newchildinfo.setCartid(newproducts.getCartid());
					newchildinfo.setCreatetime(new Date());
					newchildinfo.setNickname(childinfo.getNickname());
					newchildinfo.setRelation(childinfo.getRelation());
					newchildinfo.setUserid(newproducts.getUserid());
					newchildinfo.setIsdue(childinfo.getIsdue());				
				}else{
					newchildinfo.setBirthday(new Date());
					newchildinfo.setCartid(newproducts.getCartid());
					newchildinfo.setCreatetime(new Date());
					newchildinfo.setNickname("");
					newchildinfo.setRelation("");
					newchildinfo.setUserid(newproducts.getUserid());
					newchildinfo.setIsdue(0);
				}
				childinfoMapper.insert(newchildinfo);
				List<PMyproductdetails> details=myDetaiMapper.findMyProductdetails(myproducts.getCartid());
				if(details!=null&&details.size()>0){
					for (PMyproductdetails detail : details) {
						PMyproductdetails newdet=new PMyproductdetails();
						newdet.setCartid(newproducts.getCartid());
						newdet.setContent(detail.getContent());
						newdet.setCreatetime(new Date());
						newdet.setDescription(detail.getDescription());
						newdet.setImgurl(detail.getImgurl());
						newdet.setSceneid(detail.getSceneid());
						newdet.setSort(detail.getSort());
						newdet.setTitle(detail.getTitle());
						newdet.setUserid(newproducts.getUserid());					
						myDetaiMapper.insert(newdet);
					}					
				}
				
				PMyproductsinvites invoMo=new PMyproductsinvites();
				invoMo.setCartid(newproducts.getCartid());
				
				invoMo.setUserid(newproducts.getUserid());//邀请人ID
				invoMo.setInviteuserid(userId);//被邀请人ID
				invoMo.setInvitetype(Integer.parseInt(InviteType.scanQRInvite.toString()));
				invoMo.setStatus(Integer.parseInt(InviteStatus.agree.toString()));
				invoMo.setCreatetime(new Date());
				if(ObjectUtil.isEmpty(phone)){
					UUsers user=usersMapper.selectByPrimaryKey(userId);
					if(user!=null){
						invoMo.setInvitephone(user.getMobilephone());
					}
				}else{
					invoMo.setInvitephone(phone);
				}
				inviteMapper.insert(invoMo);
				newproducts.setInvitestatus(Integer.parseInt(InviteStatus.agree.toString()));
				myproductsMapper.updateByPrimaryKeySelective(newproducts);
				//模板客户获取数加1
				PMyproducttemp temp=tempMapper.selectByPrimaryKey(myproducts.getTempid());
				if(temp!=null){
					int count=temp.getCount()==null?0:temp.getCount();
					count++;
					temp.setCount(count);
					tempMapper.updateByPrimaryKeySelective(temp);
				}
				
				//添加影楼已获取的客户信息
				UBranchusers branchuser=branchuserMapper.selectByPrimaryKey(myproducts.getUserid());
				if(branchuser!=null){
					//添加成为影楼的已获取客户
					UAgentcustomers cus= customerMapper.getCustomersByAgentUserId(branchuser.getAgentuserid(),userId);
					if(cus==null){
						cus=new UAgentcustomers();
						cus.setAgentuserid(branchuser.getAgentuserid());
						cus.setBranchuserid(branchuser.getBranchuserid());
						cus.setUserid(userId);
						cus.setStatus(1);
						cus.setPhone(invoMo.getInvitephone());
						UUsers user=usersMapper.selectByPrimaryKey(userId);
						if(user!=null){
							cus.setName(user.getNickname());
						}
						cus.setCreatetime(new Date());
						cus.setIsmarket(1);
						cus.setSourcetype(Integer.parseInt(CustomerSourceTypeEnum.temp.toString()));
						cus.setExtid(myproducts.getTempid());
						cus.setStaffuserid(myproducts.getUserid());
						customerMapper.insert(cus);
					}else{
						if(ObjectUtil.isEmpty(cus.getIsmarket())||cus.getIsmarket().intValue()==0){
							cus.setIsmarket(1);	
							cus.setExtid(myproducts.getTempid());
							cus.setSourcetype(Integer.parseInt(CustomerSourceTypeEnum.temp.toString()));
							cus.setStaffuserid(myproducts.getUserid());
							customerMapper.updateByPrimaryKey(cus);
						}
					}
				}
				
				
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("tempId", myproducts.getTempid());
				map.put("mycartid", newproducts.getCartid());
				rq.setBasemodle(map);
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("成功接受邀请");

			}	
			
		}else {
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("不存在的作品");
		}
		
		return rq;
	}
	
	public PMyproducts getPMyproducts(Long cartId){
		PMyproducts myproducts= myproductsMapper.selectByPrimaryKey(cartId);
		return myproducts;
	}
	
	

	/**
	 * 处理扫码页面的接受邀请
	 * @param phone 被邀请人手机号
	 * @param cartId 作品cartid
	 * @param userId 被邀请人用户ID
	 * @param vcode  验证码
	 * @param needVerfiCode  是否需要验证手机验证码 0 不需要，1需要
	 * @param version  二维码版本号
	 * @author julie at 2017-04-26
	 * @throws Exception
	 */
	public ReturnModel acceptScanQrCodeInvite(Long userId,String phone,Long cartId,String vcode,Integer needVerfiCode,String version){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError); 
		if(!ObjectUtil.isMobile(phone)){
			rq.setStatusreson("请输入正确的手机号");
			return rq; 
		}
		PMyproducts myproducts= myproductsMapper.selectByPrimaryKey(cartId);
		if(myproducts!=null&&myproducts.getVersion()!=null&&(!myproducts.getVersion().equalsIgnoreCase(version))){
			rq.setStatusreson("此二维码已失效，扫码后不能接受邀请");
			return rq;
		}
		//如果需要验证手机短信验证码
		if(needVerfiCode!=null&&needVerfiCode==1){
			ResultMsg msgResult= SendSMSByMobile.validateCode(phone, vcode, SendMsgEnums.register);
			if(msgResult.getStatus()==Integer.parseInt(MsgStatusEnums.ok.toString())) {
				UUsers userPhone=usersMapper.getUUsersByPhone(phone);
				if(userPhone!=null&&userPhone.getUserid().longValue()!=userId.longValue()){
					rq.setStatu(ReturnStatus.SystemError);
					rq.setStatusreson("该手机号已经绑定其他用户！");
					return rq;
				}
				UUsers user= usersMapper.getUUsersByUserID(userId);
				if(user!=null){
					user.setMobilephone(phone);
					user.setMobilebind(1);
					user.setStatus(Integer.parseInt(UserStatusEnum.ok.toString())); 
					user.setPassword(""); //默认密码为空
					usersMapper.updateByPrimaryKey(user);
					//LoginSuccessResult result = baseLoginService.getLoginSuccessResult_Common(user);					
				}else {
					rq.setStatu(ReturnStatus.SystemError);
					rq.setStatusreson("系统错误");
					return rq; 
				}
			}else{
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson(msgResult.getMsg()); 
				return rq; 
			}
		}
		if(myproducts!=null){
			//邀请人ID要不等于受邀人ID，自已不能接受自已的作用邀请
			if(myproducts.getUserid()!=null&&myproducts.getUserid().longValue()!=userId){
				List<PMyproductsinvites> list= inviteMapper.findListByCartId(cartId);
				boolean flag=true;
				if(list!=null&&list.size()>0){
					for (PMyproductsinvites invo : list) {
						if(invo.getInviteuserid()!=null&&invo.getInviteuserid()==userId){
							flag=false;
							rq.setBasemodle(invo);
							rq.setStatu(ReturnStatus.Success);
							rq.setStatusreson("已经接受邀请协同编辑，请直接跳转到作品页！"); 
							return rq;
						}
						if(invo.getStatus()==Integer.parseInt(InviteStatus.agree.toString())){
							flag=false;
							rq.setStatu(ReturnStatus.ParamError);
							rq.setStatusreson("已有人接受邀请协同编辑，不能再邀请了！"); 
							return rq;
						}
					}
					//如果没有在接受邀请的情况下，清空以前的未接受或已拒绝的邀请
					if(flag){
						for (PMyproductsinvites invo : list) {
							inviteMapper.deleteByPrimaryKey(invo.getInviteid());
						}
					}
				}
				PMyproductsinvites invoMo=new PMyproductsinvites();
				invoMo.setCartid(cartId);
				invoMo.setUserid(myproducts.getUserid());//邀请人ID
				invoMo.setInviteuserid(userId);//被邀请人ID
				invoMo.setInvitetype(Integer.parseInt(InviteType.scanQRInvite.toString()));
				invoMo.setStatus(Integer.parseInt(InviteStatus.agree.toString()));
				invoMo.setCreatetime(new Date());	
				if(ObjectUtil.isEmpty(phone)){
					UUsers user=usersMapper.selectByPrimaryKey(userId);
					if(user!=null){
						invoMo.setInvitephone(user.getMobilephone());
					}
				}else{
					invoMo.setInvitephone(phone);
				}
				inviteMapper.insert(invoMo);
				myproducts.setInvitestatus(Integer.parseInt(InviteStatus.agree.toString()));
				myproductsMapper.updateByPrimaryKeySelective(myproducts); 
				
				
				
				//添加影楼已获取的客户信息
				UBranchusers branchuser=branchuserMapper.selectByPrimaryKey(myproducts.getUserid());
				if(branchuser!=null){
					//添加成为影楼的已获取客户
					UAgentcustomers cus= customerMapper.getCustomersByAgentUserId(branchuser.getAgentuserid(),userId);
					if(cus==null){
						cus=new UAgentcustomers();
						cus.setAgentuserid(branchuser.getAgentuserid());
						cus.setBranchuserid(branchuser.getBranchuserid());
						cus.setUserid(userId);
						cus.setStatus(1);
						cus.setPhone(invoMo.getInvitephone());
						UUsers user=usersMapper.selectByPrimaryKey(userId);
						if(user!=null){
							cus.setName(user.getNickname());
						}
						cus.setCreatetime(new Date());
						cus.setIsmarket(1);
						cus.setSourcetype(Integer.parseInt(CustomerSourceTypeEnum.oneinvite.toString()));
						cus.setExtid(myproducts.getTempid());
						cus.setStaffuserid(myproducts.getUserid());
						customerMapper.insert(cus);
					}else{
						if(ObjectUtil.isEmpty(cus.getIsmarket())||cus.getIsmarket().intValue()==0){
							cus.setIsmarket(1);	
							cus.setExtid(myproducts.getTempid());
							cus.setSourcetype(Integer.parseInt(CustomerSourceTypeEnum.oneinvite.toString()));
							cus.setStaffuserid(myproducts.getUserid());
							customerMapper.updateByPrimaryKey(cus);
						}
					}
				}
				
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("成功接受邀请");			
			}else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("不能接受自已作品的邀请！"); 				
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
	@Autowired
	private PMyproducttempapplyMapper tempApplyMapper;
	/**
	 * 我的 个人信息提示
	 */
	public ReturnModel  myUserInfoExp(Long userId,String mobilePhone){
		ReturnModel rq=new ReturnModel();
		int count= inviteMapper.countInvitingsByPhone(mobilePhone,Integer.parseInt(InviteStatus.inviting.toString())); 
		rq.setStatu(ReturnStatus.Success);
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("myInvitingCount", count);//我的待处理 邀请编辑数量
		map.put("tempCount", tempApplyMapper.countMyProducttempApplyByUserIdNews(userId));
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
			//我自己
			UUsers myUsers=usersMapper.selectByPrimaryKey(myUserId);
			for (MyProductListVo vo : list) {
				// 作品详情（图片集合）
				List<PMyproductdetails> detailslist = myDetaiMapper.findMyProductdetails(vo.getCartid());
				int i = 0;
				if (detailslist != null && detailslist.size() > 0) {
					String headimg=detailslist.get(0).getImgurl();
					if(!ObjectUtil.isEmpty(headimg)&&(headimg.contains("http://")||headimg.contains("https://"))){
						vo.setDefaultImg(headimg);
					}else {
						vo.setDefaultImg("http://pic.bbyiya.com/"+headimg);
					}
					for (PMyproductdetails dd : detailslist) {
						if (!ObjectUtil.isEmpty(dd.getImgurl())) {
//							if(dd.getSort()!=null&&dd.getSort().intValue()==0){
//								if(!ObjectUtil.isEmpty(dd.getImgurl())&&(dd.getImgurl().contains("http://")||dd.getImgurl().contains("https://"))){
//									vo.setDefaultImg(dd.getImgurl());
//								}else {
//									vo.setDefaultImg("http://pic.bbyiya.com/"+dd.getImgurl());//+"?imageView2/2/w/200");
//								}
//							}
							i++;
						}
					}
				}
				if(ObjectUtil.isEmpty(vo.getDefaultImg())){
					vo.setDefaultImg("http://pic.bbyiya.com/484983733454448354.png"); 
				}
				vo.setCount(i);
				 /*---------------------作品本人的头像个昵称--------------------------------*/
				 if (ObjectUtil.isEmpty(myUsers.getUserimg())) {
					vo.setMyHeadImg(ConfigUtil.getSingleValue("default-headimg"));//"http://pic.bbyiya.com/userdefaultimg-2017-0303-01.png"
				 } else { 
					vo.setMyHeadImg(myUsers.getUserimg());
				 }
				 if (!ObjectUtil.isEmpty(myUsers.getNickname())&&!"null".equals(myUsers.getNickname())) {
					vo.setMyNickName(myUsers.getNickname());
				 } else {
					vo.setMyNickName(myUsers.getMobilephone());
				 }
				 /*--------------------------非作品本人的头像昵称----------------------------------------------*/
				 if(vo.getUserid().longValue()==myUserId){//我的作品
					vo.setIsMine(1);
					if(vo.getInvitestatus()!=null&&vo.getInvitestatus()>0){
						List<PMyproductsinvites> invlist= inviteMapper.findListByCartId(vo.getCartid());
						if(invlist!=null&&invlist.size()>0){
							UUsers otherUsers = null;
							PMyproductsinvites in=invlist.get(0);
							if(in.getInviteuserid()!=null&&in.getInviteuserid()>0){
								otherUsers=usersMapper.selectByPrimaryKey(in.getInviteuserid());
							}else if (!ObjectUtil.isEmpty( in.getInvitephone())) {
								otherUsers=usersMapper.getUUsersByPhone(in.getInvitephone());
							}
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
								if(!ObjectUtil.isEmpty(in.getInvitephone())){
									vo.setOtherNickName(in.getInvitephone());
								}
								vo.setOtherHeadImg("http://pic.bbyiya.com/userdefaultimg-2017-0303-01.png");
							}
						}
					}
				 }else{//别人
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
