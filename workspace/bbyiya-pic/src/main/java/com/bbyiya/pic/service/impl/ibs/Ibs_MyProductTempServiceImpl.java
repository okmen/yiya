package com.bbyiya.pic.service.impl.ibs;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.PMyproductchildinfoMapper;
import com.bbyiya.dao.PMyproductdetailsMapper;
import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.PMyproductsinvitesMapper;
import com.bbyiya.dao.PMyproducttempMapper;
import com.bbyiya.dao.PMyproducttempapplyMapper;
import com.bbyiya.dao.PMyproducttempusersMapper;
import com.bbyiya.dao.PMyproducttempverlogsMapper;
import com.bbyiya.dao.PMyproducttempverusersMapper;
import com.bbyiya.dao.UAgentcustomersMapper;
import com.bbyiya.dao.UBranchusersMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.CustomerSourceTypeEnum;
import com.bbyiya.enums.MyProductTempStatusEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.InviteStatus;
import com.bbyiya.enums.pic.InviteType;
import com.bbyiya.enums.pic.MyProductStatusEnum;
import com.bbyiya.enums.pic.MyProducttempApplyStatusEnum;
import com.bbyiya.model.PMyproductchildinfo;
import com.bbyiya.model.PMyproductdetails;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PMyproductsinvites;
import com.bbyiya.model.PMyproducttemp;
import com.bbyiya.model.PMyproducttempapply;
import com.bbyiya.model.PMyproducttempusers;
import com.bbyiya.model.PMyproducttempverlogs;
import com.bbyiya.model.PMyproducttempverusers;
import com.bbyiya.model.UAgentcustomers;
import com.bbyiya.model.UBranchusers;
import com.bbyiya.model.UUsers;
import com.bbyiya.pic.service.ibs.IIbs_MyProductTempService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.FileUtils;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.PageInfoUtil;
import com.bbyiya.utils.QRCodeUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.agent.UBranchUserTempVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("ibs_MyProductTempService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Ibs_MyProductTempServiceImpl implements IIbs_MyProductTempService{
	@Autowired
	private PMyproducttempMapper myproducttempMapper;
	@Autowired
	private PMyproductsMapper myMapper;
	@Autowired
	private PMyproductdetailsMapper myDetaiMapper;
	@Autowired
	private PMyproducttempapplyMapper myproducttempapplyMapper;
	@Autowired
	private PMyproductsinvitesMapper inviteMapper;
	@Autowired
	private PMyproductchildinfoMapper childinfoMapper;
	@Autowired
	private UUsersMapper usersMapper;
	@Autowired
	private UAgentcustomersMapper customerMapper;
	@Autowired
	private UBranchusersMapper branchuserMapper;	
	@Autowired
	private PMyproducttempusersMapper myTempUserMapper;	
	@Autowired
	private PMyproducttempverusersMapper myTempveruserMapper;
	@Autowired
	private PMyproducttempverlogsMapper myTempVeruserlogMapper;
	
	
	/**
	 * 添加模板
	 * */
	public ReturnModel addMyProductTemp(Long userid,String title,String remark,Long productid,int needVerifer,String discription,String codeurl,String codesm){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);	
		
		PMyproducts myproduct = new PMyproducts();	
		myproduct.setUserid(userid);
		myproduct.setProductid(productid);
		myproduct.setCreatetime(new Date());
		myproduct.setStatus(Integer.parseInt(MyProductStatusEnum.ok.toString()));
		myproduct.setUpdatetime(new Date());	
		myproduct.setIstemp(1);
		myMapper.insertReturnId(myproduct);
			
		PMyproducttemp temp=new PMyproducttemp();
		temp.setBranchuserid(userid);
		temp.setCount(0);
		temp.setCreatetime(new Date());
		temp.setRemark(remark);
		temp.setStatus(Integer.parseInt(MyProductTempStatusEnum.enable.toString()));
		temp.setTitle(title);
		temp.setCartid(myproduct.getCartid());	
		temp.setNeedverifer(needVerifer);
		temp.setDiscription(discription);
		if(!ObjectUtil.isEmpty(codeurl)){
			temp.setTempcodeurl(codeurl);
		}if(!ObjectUtil.isEmpty(codeurl)){
			temp.setTempcodesm(codesm);
		}
		myproducttempMapper.insertReturnId(temp);
		
		myproduct.setTempid(temp.getTempid());
		myMapper.updateByPrimaryKey(myproduct);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("myProductTempID", temp.getTempid());
		map.put("myProductID", myproduct.getCartid());
		rq.setBasemodle(map);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("添加模板成功！");
		return rq;
	}
	
	/**
	 * 修改模板
	 * */
	public ReturnModel editMyProductTemp(String title,String remark,Integer tempid,int needVerifer,String discription){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);	
		PMyproducttemp temp=myproducttempMapper.selectByPrimaryKey(tempid);
		if(temp!=null){
			temp.setTitle(title);
			temp.setRemark(remark);
			temp.setNeedverifer(needVerifer);
			temp.setDiscription(discription);
			myproducttempMapper.updateByPrimaryKey(temp);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("修改模板成功！");
		}else{
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("模板不存在！");
		}
		return rq;
	}
	
	/**
	 * 修改二维码信息
	 * */
	public ReturnModel editTempCodeUrl(Integer tempid,String codeurl,String codesm){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);	
		PMyproducttemp temp=myproducttempMapper.selectByPrimaryKey(tempid);
		if(temp!=null){
			temp.setTempcodeurl(codeurl);
			temp.setTempcodesm(codesm);
			myproducttempMapper.updateByPrimaryKey(temp);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("修改模板二维码成功！");
		}else{
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("模板不存在！");
		}
		return rq;
	}
	
	/**
	 * 启用或禁用模板
	 * @param type
	 * @param tempid
	 * @return
	 */
	public ReturnModel editMyProductTempStatus(int type,int tempid){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);
		
		PMyproducttemp temp=myproducttempMapper.selectByPrimaryKey(tempid);	
		if(temp!=null){
			PMyproducts myproduct =myMapper.selectByPrimaryKey(temp.getCartid());
			if(myproduct!=null){
				//启用
				if(type==1){
					temp.setStatus(Integer.parseInt(MyProductTempStatusEnum.enable.toString()));			
					myproduct.setStatus(Integer.parseInt(MyProductStatusEnum.ok.toString()));				
				}else{//禁用
					temp.setStatus(Integer.parseInt(MyProductTempStatusEnum.disabled.toString()));			
					myproduct.setStatus(Integer.parseInt(MyProductStatusEnum.disabled.toString()));
					//将模板的待审核用户的状态全置为审核失败
					List<PMyproducttempapply>  applylist=myproducttempapplyMapper.findMyProducttempApplyList(tempid, Integer.parseInt(MyProducttempApplyStatusEnum.apply.toString()));
					if(applylist!=null&&applylist.size()>0){
						for (PMyproducttempapply apply : applylist) {
							apply.setStatus(Integer.parseInt(MyProducttempApplyStatusEnum.refuse.toString()));
							myproducttempapplyMapper.updateByPrimaryKeySelective(apply);
						}
					}
					
					//禁用员工模板权限
					List<PMyproducttempusers> tempuserList=myTempUserMapper.findTempUserListByTempId(tempid);
					if(tempuserList!=null&&tempuserList.size()>0){
						for (PMyproducttempusers tempuser : tempuserList) {
							tempuser.setStatus(0);
							myTempUserMapper.updateByPrimaryKeySelective(tempuser);
						}
					}
					//禁用员工员工模板审核权限
					List<PMyproducttempverusers> tempveruserList=myTempveruserMapper.findTempVerfiyUserListByTempId(tempid);
					if(tempveruserList!=null&&tempveruserList.size()>0){
						for (PMyproducttempverusers tempveruser : tempveruserList) {
							tempveruser.setStatus(0);
							myTempveruserMapper.updateByPrimaryKeySelective(tempveruser);
						}
					}
				}
			}
			myMapper.updateByPrimaryKey(myproduct);
			myproducttempMapper.updateByPrimaryKey(temp);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("操作成功");
		}else{
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("模板ID不存在");
		}
		return rq;
	}
	
	/**
	 * 删除模板
	 * @param tempid
	 * @return
	 */
	public ReturnModel deleteMyProductTemp(int tempid){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);		
		PMyproducttemp temp=myproducttempMapper.selectByPrimaryKey(tempid);	
		if(temp!=null){
			PMyproducts myproduct =myMapper.selectByPrimaryKey(temp.getCartid());
			temp.setStatus(Integer.parseInt(MyProductTempStatusEnum.del.toString()));			
			myproduct.setStatus(Integer.parseInt(MyProductStatusEnum.deleted.toString()));
			myMapper.updateByPrimaryKey(myproduct);
			myproducttempMapper.updateByPrimaryKey(temp);
			//将模板的待审核用户的状态全置为审核失败
			List<PMyproducttempapply>  applylist=myproducttempapplyMapper.findMyProducttempApplyList(tempid, Integer.parseInt(MyProducttempApplyStatusEnum.apply.toString()));
			if(applylist!=null&&applylist.size()>0){
				for (PMyproducttempapply apply : applylist) {
					apply.setStatus(Integer.parseInt(MyProducttempApplyStatusEnum.refuse.toString()));
					myproducttempapplyMapper.updateByPrimaryKeySelective(apply);
				}
			}
			//删除员工模板权限
			List<PMyproducttempusers> tempuserList=myTempUserMapper.findTempUserListByTempId(tempid);
			if(tempuserList!=null&&tempuserList.size()>0){
				for (PMyproducttempusers tempuser : tempuserList) {
					myTempUserMapper.deleteByPrimaryKey(tempuser.getId());
				}
			}
			//员工员工模板审核权限
			List<PMyproducttempverusers> tempveruserList=myTempveruserMapper.findTempVerfiyUserListByTempId(tempid);
			if(tempveruserList!=null&&tempveruserList.size()>0){
				for (PMyproducttempverusers tempveruser : tempveruserList) {
					myTempveruserMapper.deleteByPrimaryKey(tempveruser.getVerid());
				}
			}
			
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("删除操作成功");
		}else{
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("模板ID不存在");
		}		
		return rq;
	}
	/**
	 * 查询模板列表
	 * @return
	 */
	public ReturnModel findMyProductTempList(int index,int size,Long userid){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);		
		PageHelper.startPage(index, size);	
		List<PMyproducttemp>  templist=myproducttempMapper.findBranchMyProductTempList(userid);
		PageInfo<PMyproducttemp> reuslt=new PageInfo<PMyproducttemp>(templist); 
		if(reuslt!=null&&reuslt.getList()!=null&&reuslt.getList().size()>0){
			for (PMyproducttemp temp : templist) {	
				String redirct_url="apply/form?workId="+temp.getCartid()+"&uid="+userid;			
				String urlstr="";
				String url="";
				try {
					urlstr = ConfigUtil.getSingleValue("shareulr-base")+"redirct_url="+URLEncoder.encode(redirct_url,"utf-8");
					url="https://mpic.bbyiya.com/common/generateQRcode?urlstr="+URLEncoder.encode(urlstr,"utf-8");				
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				temp.setCodeurl(url);
				temp.setCreatetimestr(DateUtil.getTimeStr(temp.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
				//得到待审核数量
				Integer checkCount=myproducttempapplyMapper.getNeedCheckApllyCountByTemp(temp.getTempid(),Integer.parseInt(MyProducttempApplyStatusEnum.apply.toString()));
				temp.setNeedCheckCount(checkCount==null?0:checkCount);
			}
		}
		
		rq.setBasemodle(reuslt);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("获取列表成功！");
		return rq;
	}
	
	
	/**
	 * 获取影楼模板待审核用户列表
	 * @return
	 */
	public ReturnModel getMyProductTempApplyCheckList(int index,int size,Long userid,int tempid){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);	
		
		PageHelper.startPage(index, size);	
		List<PMyproducttempapply>  applylist=myproducttempapplyMapper.findMyProducttempApplyList(tempid, Integer.parseInt(MyProducttempApplyStatusEnum.apply.toString()));
		PageInfo<PMyproducttempapply> reuslt=new PageInfo<PMyproducttempapply>(applylist); 
		if(reuslt!=null&&reuslt.getList()!=null&&reuslt.getList().size()>0){
			for (PMyproducttempapply apply : applylist) {
				apply.setCreatetimestr(DateUtil.getTimeStr(apply.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
				if(!ObjectUtil.isEmpty(apply.getVerfiytime())){
					apply.setVerfiytimestr(DateUtil.getTimeStr(apply.getVerfiytime(), "yyyy-MM-dd HH:mm:ss"));
				}
				if(!ObjectUtil.isEmpty(apply.getBirthday())){
					apply.setBirthdaystr(DateUtil.getTimeStr(apply.getBirthday(), "yyyy-MM-dd HH:mm:ss"));
				}
				if(ObjectUtil.isEmpty(apply.getReceiver())){
					UUsers user=usersMapper.selectByPrimaryKey(apply.getUserid());
					if(user!=null) apply.setUsername(user.getNickname());
				}else{
					apply.setUsername(apply.getReceiver());
				}
			}
		}		
		rq.setBasemodle(reuslt);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("获取列表成功！");
		return rq;
	}
	
	/**
	 * 审核影楼模板申请用户
	 * @param userid
	 * @param tempapplyid
	 * @param status  0 申请中，1通过，2拒绝
	 * @return
	 */
	public ReturnModel audit_TempApplyUser(Long adminUserId,Long tempapplyid,Integer status){
		ReturnModel rq=new ReturnModel();
		PMyproducttempapply apply=myproducttempapplyMapper.selectByPrimaryKey(tempapplyid);
		if(apply!=null){	
			String content="";
			//审核通过操作  新增一份作品ID，插入影楼客户信息
			if(status==Integer.parseInt(MyProducttempApplyStatusEnum.ok.toString())){
				apply.setVerfiytime(new Date());	
				apply.setIsread(0);//消息状态置为未读
				content="审核通过";
				rq=doAcceptOrAutoTempApplyOpt(apply);	
			}else if(status==Integer.parseInt(MyProducttempApplyStatusEnum.refuse.toString())){
				rq.setStatu(ReturnStatus.Success);
				content="审核不通过";
				rq.setStatusreson("拒绝成功");
			}
			apply.setStatus(status);
			myproducttempapplyMapper.updateByPrimaryKey(apply);	
			
			//插入审核日志表记录
			PMyproducttempverlogs verlog=new PMyproducttempverlogs();
			verlog.setCartid(apply.getCartid());
			verlog.setContent(content);
			verlog.setCreatetime(new Date());
			verlog.setCustomeruserid(adminUserId);
			verlog.setOpstatus(status);
			verlog.setTempid(apply.getTempid());
			verlog.setUserid(apply.getUserid());
			myTempVeruserlogMapper.insert(verlog);
			
		}else{
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("找不到申请记录");
		}

		return rq;
	}
	
	/**
	 * 接受邀请或ibs后台审核用户申请操作 (公用方法)
	 * @param apply
	 * @return
	 */
	public ReturnModel doAcceptOrAutoTempApplyOpt(PMyproducttempapply apply){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);	
		//得到模板
		PMyproducttemp temp=myproducttempMapper.selectByPrimaryKey(apply.getTempid());
		if(temp==null){
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("模板不存在！");
			return rq;
		}
		//判断是否已审核通过已生成过客户扫码的作品
		if(apply.getStatus()!=null&&apply.getStatus()==Integer.parseInt(MyProducttempApplyStatusEnum.ok.toString())){
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("审核已通过！");
			return rq;
		}
		
		//得到模板作品
		PMyproducts myproducts= myMapper.selectByPrimaryKey(temp.getCartid());
		if(myproducts!=null){	
			//如果这个用户以前没有申请，则copy一份作品
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
			newproducts.setInvitestatus(Integer.parseInt(InviteStatus.agree.toString()));
			myMapper.insertReturnId(newproducts);
			
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
			invoMo.setInviteuserid(apply.getUserid());//被邀请人ID
			invoMo.setInvitetype(Integer.parseInt(InviteType.scanQRInvite.toString()));
			invoMo.setStatus(Integer.parseInt(InviteStatus.agree.toString()));
			invoMo.setCreatetime(new Date());
			if(ObjectUtil.isEmpty(apply.getMobilephone())){
				UUsers user=usersMapper.selectByPrimaryKey(apply.getUserid());
				if(user!=null){
					invoMo.setInvitephone(user.getMobilephone());
				}
			}else{
				invoMo.setInvitephone(apply.getMobilephone());
			}
			inviteMapper.insert(invoMo);
			
			//模板客户获取数加1
			int count=(temp.getCount()==null?0:temp.getCount());
			count++;
			temp.setCount(count);				
			myproducttempMapper.updateByPrimaryKeySelective(temp);
			
			//员工模板通过人数加1
			PMyproducttempusers tempuser=myTempUserMapper.selectByUserIdAndTempId(apply.getCompanyuserid(), apply.getTempid());
			if(tempuser!=null){
				tempuser.setPasscount((tempuser.getPasscount()==null?0:tempuser.getPasscount())+1);
				myTempUserMapper.updateByPrimaryKeySelective(tempuser);
			}
			
			
			//添加影楼已获取的客户信息
			UBranchusers branchuser=branchuserMapper.selectByPrimaryKey(myproducts.getUserid());
			if(branchuser!=null){
				//添加成为影楼的已获取客户
				UAgentcustomers cus= customerMapper.getCustomersByAgentUserId(branchuser.getAgentuserid(),apply.getUserid());
				if(cus==null){
					cus=new UAgentcustomers();
					cus.setAgentuserid(branchuser.getAgentuserid());
					cus.setBranchuserid(branchuser.getBranchuserid());
					cus.setUserid(apply.getUserid());
					cus.setStatus(1);
					cus.setPhone(invoMo.getInvitephone());
					cus.setAddress(apply.getAdress());
					if(ObjectUtil.isEmpty(apply.getUsername())){
						UUsers user=usersMapper.selectByPrimaryKey(apply.getUserid());
						if(user!=null){
							cus.setName(user.getNickname());
						}
					}else{
						cus.setName(apply.getUsername());
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
			
			//反写申请记录的cartid
			apply.setCartid(newproducts.getCartid());			
			rq.setBasemodle(map);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("审核通过");
		}else{
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("模板作品不存在！");
		}
		return rq;
	}
	/**
	 * 影楼员工负责模板信息列表
	 * @return
	 */
	public ReturnModel find_BranchUserOfTemp(int index,int size,Long branchUserId,Integer tempid){
		ReturnModel rq=new ReturnModel();
		PageHelper.startPage(index, size);	
		List<UBranchusers> list= branchuserMapper.findMemberslistByBranchUserId(branchUserId);		
		PageInfo<UBranchusers> reuslt=new PageInfo<UBranchusers>(list); 
		if(reuslt!=null&&reuslt.getList()!=null&&reuslt.getList().size()>0){	
			List<UBranchUserTempVo> usertemplist=new ArrayList<UBranchUserTempVo>();
			for (UBranchusers buser : list) {
				UBranchUserTempVo usertemp=new UBranchUserTempVo();
				usertemp.setAgentuserid(buser.getAgentuserid());
				usertemp.setBranchuserid(buser.getBranchuserid());
				usertemp.setName(buser.getName());
				usertemp.setUserid(buser.getUserid());
				usertemp.setPhone(buser.getPhone());
				usertemp.setTempid(tempid);
				usertemp.setApplycount(0);
				usertemp.setPasscount(0);
				usertemp.setStatus(0);
				PMyproducttempusers tempuser=myTempUserMapper.selectByUserIdAndTempId(buser.getUserid(), tempid);
				if(tempuser!=null){
					usertemp.setPasscount(tempuser.getPasscount()==null?0:tempuser.getPasscount());
					usertemp.setApplycount(tempuser.getApplycount()==null?0:tempuser.getApplycount());					
					usertemp.setStatus(tempuser.getStatus()==null?0:tempuser.getStatus());
					//如果有负责权限
					if(tempuser.getStatus()!=null&&tempuser.getStatus().intValue()==1){
						PMyproducttemp temp=myproducttempMapper.selectByPrimaryKey(tempid);
						String redirct_url="apply/form?workId="+temp.getCartid()+"&uid="+buser.getBranchuserid()+"&sid="+buser.getUserid();			
						String urlstr="";
						String url="";
						try {
							urlstr = ConfigUtil.getSingleValue("shareulr-base")+"redirct_url="+URLEncoder.encode(redirct_url,"utf-8");
							url="https://mpic.bbyiya.com/common/generateQRcode?urlstr="+URLEncoder.encode(urlstr,"utf-8");				
						} catch (UnsupportedEncodingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						usertemp.setCodeurl(url);
					}
				}
				
				usertemp.setVerfiystatus(0);
				PMyproducttempverusers veruser=myTempveruserMapper.selectByUserIdAndTempId(buser.getUserid(), tempid);
				if(veruser!=null){
					usertemp.setVerfiystatus(veruser.getStatus()==null?0:veruser.getStatus());
				}
				usertemplist.add(usertemp);
			}
			
			
			//根据一个PageInfo初始化另一个page
			PageInfoUtil<UBranchUserTempVo> reusltPage=new PageInfoUtil<UBranchUserTempVo>(reuslt, usertemplist);
			rq.setBasemodle(reusltPage);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("获取列表成功！");
		}
		return rq;
	}
	
	/**
	 * 设置员工模板负责权限
	 * @return
	 */
	public ReturnModel setUserTempPermission(Long userId,Integer tempid,Integer status){
		ReturnModel rq=new ReturnModel();
		if(tempid==null){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("参数错误：tempid为空！");
			return rq;
		}
		if(userId==null){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("参数错误：userId为空！");
			return rq;
		}
		if(status==null){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("参数错误：status为空！");
			return rq;
		}
		PMyproducttempusers tempuser=myTempUserMapper.selectByUserIdAndTempId(userId, tempid);
		if(tempuser==null){
			tempuser=new PMyproducttempusers();
			tempuser.setApplycount(0);
			tempuser.setCreatetime(new Date());
			tempuser.setPasscount(0);
			tempuser.setTempid(tempid);
			tempuser.setUserid(userId);
			tempuser.setStatus(status);
			myTempUserMapper.insert(tempuser);
		}else{
			tempuser.setStatus(status);
			myTempUserMapper.updateByPrimaryKeySelective(tempuser);
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("设置成功！");
		return rq;
	}
	
	/**
	 * 设置员工模板审核负责权限
	 * @return
	 */
	public ReturnModel setUserTempVerfiyPermission(Long userId,Integer tempid,Integer status){
		ReturnModel rq=new ReturnModel();
		if(tempid==null){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("参数错误：tempid为空！");
			return rq;
		}
		if(userId==null){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("参数错误：userId为空！");
			return rq;
		}
		if(status==null){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("参数错误：status为空！");
			return rq;
		}
		PMyproducttempverusers verfiytempuser=myTempveruserMapper.selectByUserIdAndTempId(userId, tempid);
		if(verfiytempuser==null){
			verfiytempuser=new PMyproducttempverusers();
			verfiytempuser.setCreatetime(new Date());
			verfiytempuser.setStatus(status);
			verfiytempuser.setTempid(tempid);
			verfiytempuser.setUserid(userId);		
			myTempveruserMapper.insert(verfiytempuser);
		}else{
			verfiytempuser.setStatus(status);
			myTempveruserMapper.updateByPrimaryKeySelective(verfiytempuser);
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("设置成功！");
		return rq;
	}
	
	/**
	 * 审核模板申请用户作品是否通过
	 * @return
	 */
	public ReturnModel audit_TempApplyProduct(Long userId,Long cartid,Integer status,String reason){
		ReturnModel rq=new ReturnModel();
		if(cartid==null){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("参数错误：cartid为空！");
			return rq;
		}		
		if(status==null){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("参数错误：status为空！");
			return rq;
		}
		if (!ObjectUtil.isEmpty(reason) && !ObjectUtil.validSqlStr(reason)) {
			rq.setStatu(ReturnStatus.ParamError_2);
			rq.setStatusreson("不通过原因存在危险");
			return rq;
		}
		
		PMyproducttempapply tempapply=myproducttempapplyMapper.getMyProducttempApplyByCartId(cartid);
		if(tempapply!=null){
			tempapply.setStatus(status);
			tempapply.setReason(reason);
			myproducttempapplyMapper.updateByPrimaryKeySelective(tempapply);
			if(status==Integer.parseInt(MyProducttempApplyStatusEnum.nopass.toString())){
				PMyproductsinvites invites=inviteMapper.getInviteByPhoneAndCartId(tempapply.getMobilephone(), cartid);
				if(invites!=null){
					invites.setStatus(Integer.parseInt(InviteStatus.agree.toString()));
					inviteMapper.updateByPrimaryKeySelective(invites);
				}
			}
		}
		
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("设置成功！");
		return rq;
	}
	
	/**
	 * 保存模板二维码图片
	 * @return
	 * @throws Exception 
	 */
	public ReturnModel saveProductTempRQcode(String url) throws Exception{
		ReturnModel rq=new ReturnModel();
		// 获取用户的当前工作主目录 
		String sep=System.getProperty("file.separator");
		String currentWorkDir = System.getProperty("user.home") +sep+ "imagedownloadtemp"+sep;
		FileUtils.isDirExists(currentWorkDir);
		String filename = "tempRqcode.jpg";
		File file = new File(currentWorkDir + filename);
		BufferedImage bufImg = QRCodeUtil.createImage(url, "", true);
		// 生成二维码QRCode图片
		ImageIO.write(bufImg, "jpg", file);
		rq.setBasemodle(file.getPath());
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("获图片成功！");
		return rq;
	}
		
}
