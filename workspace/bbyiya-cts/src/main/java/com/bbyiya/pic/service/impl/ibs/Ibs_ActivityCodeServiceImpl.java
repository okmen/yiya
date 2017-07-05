package com.bbyiya.pic.service.impl.ibs;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.baseUtils.GenUtils;
import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.PMyproductactivitycodeMapper;
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
import com.bbyiya.enums.MyProductTempStatusEnum;
import com.bbyiya.enums.MyProductTempType;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.ActivityCodeStatusEnum;
import com.bbyiya.enums.pic.MyProductStatusEnum;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.PMyproductactivitycode;
import com.bbyiya.model.PMyproductchildinfo;
import com.bbyiya.model.PMyproductdetails;
import com.bbyiya.model.PMyproductext;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PMyproducttemp;
import com.bbyiya.model.PMyproducttempapply;
import com.bbyiya.model.PProducts;
import com.bbyiya.model.PProductstyles;
import com.bbyiya.model.UBranchusers;
import com.bbyiya.model.UUsers;
import com.bbyiya.pic.service.ibs.IIbs_ActivityCodeService;
import com.bbyiya.pic.vo.product.ActivityCodeProductVO;
import com.bbyiya.pic.vo.product.MyProductTempAddParam;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.PageInfoUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.product.MyProductResultVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("ibs_ActivityCodeService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Ibs_ActivityCodeServiceImpl implements IIbs_ActivityCodeService{
	@Autowired
	private PMyproducttempMapper myproducttempMapper;
	@Autowired
	private PMyproductsMapper myMapper;
	@Autowired
	private PMyproductdetailsMapper myDetaiMapper;
	@Autowired
	private PMyproductactivitycodeMapper activitycodeMapper;
	@Autowired
	private PProductsMapper productsMapper;
	@Autowired
	private PProductstylesMapper styleMapper;

	@Autowired
	private PMyproductchildinfoMapper mychildMapper;
	@Autowired
	private PMyproducttempapplyMapper tempapplyMapper;
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

	/**
	 * 添加活动码
	 * */
	public ReturnModel addActivityCode(Long userid,MyProductTempAddParam param){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);	

		PMyproducts myproduct = new PMyproducts();	
		myproduct.setUserid(userid);
		myproduct.setProductid(param.getProductid());
		myproduct.setCreatetime(new Date());
		myproduct.setStatus(Integer.parseInt(MyProductStatusEnum.ok.toString()));
		myproduct.setUpdatetime(new Date());	
		myproduct.setIstemp(1);
		myMapper.insertReturnId(myproduct);
			
		PMyproducttemp temp=new PMyproducttemp();
		temp.setBranchuserid(userid);
		temp.setCount(0);//已通过人数
		temp.setCreatetime(new Date());
		temp.setRemark(param.getRemark());
		temp.setStatus(Integer.parseInt(MyProductTempStatusEnum.enable.toString()));
		temp.setTitle(param.getTitle());
		temp.setCartid(myproduct.getCartid());	
		temp.setNeedverifer(0); //不需要审核   0不需要，1 需要
		temp.setDiscription(param.getDiscription()); //活动需知
		if(!ObjectUtil.isEmpty(param.getCodeurl())){
			temp.setTempcodeurl(param.getCodeurl());
		}if(!ObjectUtil.isEmpty(param.getCodesm())){
			temp.setTempcodesm(param.getCodesm());
		}
		temp.setStyleid(param.getStyleId());
		temp.setIsautoorder(1);//默认都是自动下单0 手工下单，1自动下单
		temp.setOrderhours(0); 
		temp.setMaxapplycount(param.getApplycount()==null?0:param.getApplycount());//报名人数为0时不限制
		temp.setBlesscount(param.getBlesscount()==null?0:param.getBlesscount());//收集祝福数
		if(ObjectUtil.isEmpty(param.getIsbranchaddress())){
			param.setIsbranchaddress(0);
		}
		temp.setIsbranchaddress(param.getIsbranchaddress());
		temp.setType(Integer.parseInt(MyProductTempType.code.toString()));//默认为普通类型
		myproducttempMapper.insertReturnId(temp);
		
		//生成活动码
		for(int i=0;i<param.getApplycount();i++){
			String idString= GenUtils.generateUuid_Char8();
			PMyproductactivitycode code=new PMyproductactivitycode();
			code.setBranchuserid(userid);
			code.setCodeno(idString);
			code.setCreatetime(new Date());
			code.setStatus(Integer.parseInt(ActivityCodeStatusEnum.notuse.toString()));
			code.setTempid(temp.getTempid());
			activitycodeMapper.insert(code);
		}
		
		myproduct.setTempid(temp.getTempid());
		myMapper.updateByPrimaryKey(myproduct);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("myProductTempID", temp.getTempid());
		map.put("myProductID", myproduct.getCartid());
		rq.setBasemodle(map);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("添加活动码成功！");
		return rq;
	}
	
	/**
	 * 活动码详情
	 * */
	public ReturnModel getActivityCodeDetail(Integer tempid){
		ReturnModel rq=new ReturnModel();
		if(ObjectUtil.isEmpty(tempid)){
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("tempid参数为空");
			return rq;
		}
		PMyproducttemp temp=myproducttempMapper.selectByPrimaryKey(tempid);
		if(temp!=null){
			PMyproducts product=myMapper.selectByPrimaryKey(temp.getCartid());
			temp.setProductid(product.getProductid());
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("temp", temp);
		rq.setBasemodle(map);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("成功！");
		return rq;
	}
	
	/**
	 * 根据模板ID得到作品列表
	 * @param branchUserId
	 * @param tempid
	 * @param index
	 * @param size
	 * @return
	 */
	public ReturnModel findMyProductslistForActivityCode(Long branchUserId, Integer tempid,Integer activeStatus,String keywords,int index, int size) {
		ReturnModel rq = new ReturnModel();
		
		PageHelper.startPage(index, size);
		List<PMyproductactivitycode> codelist=activitycodeMapper.findActivitycodelistForTempId(tempid, activeStatus, keywords);
		PageInfo<PMyproductactivitycode> resultPage = new PageInfo<PMyproductactivitycode>(codelist);
		List<ActivityCodeProductVO> codevoList=new ArrayList<ActivityCodeProductVO>();
		
		if (resultPage.getList() != null && resultPage.getList().size() > 0) {	
			for (PMyproductactivitycode code : resultPage.getList()) {
				ActivityCodeProductVO codevo=new ActivityCodeProductVO();
				codevo.setCode(code);
				
				PMyproducttemp temp=myproducttempMapper.selectByPrimaryKey(code.getTempid());
				codevo.setStyleid(temp.getStyleid());
				PMyproducts tempproduct=myMapper.selectByPrimaryKey(temp.getCartid());
				codevo.setProductid(tempproduct.getProductid());
				
				// 获取产品信息
				PProducts products = productsMapper.selectByPrimaryKey(tempproduct.getProductid());
				PProductstyles styles = styleMapper.selectByPrimaryKey(temp.getStyleid());
				String producttitle=products.getTitle();
				if (products != null && styles != null) {
					if(styles.getStyleid()%2==0){
						producttitle=producttitle+"-竖版-"+styles.getPrice();
					}else{
						producttitle=producttitle+"-横版-"+styles.getPrice();
					}
					codevo.setProductTitle(producttitle);
				}
				//得到客户昵称
				if(!ObjectUtil.isEmpty(code.getUserid())){
					UUsers user=usersMapper.selectByPrimaryKey(code.getUserid());
					codevo.setInvitedName(user.getNickname());
				}
				//0未使用，1已使用 
				codevo.setActiveStatus(code.getStatus());
				if(!ObjectUtil.isEmpty(code.getCartid())){
					PMyproducts myproduct=myMapper.selectByPrimaryKey(code.getCartid());
					if(myproduct!=null){
						codevo.setTitle(myproduct.getTitle());
						codevo.setCreatetimestr(DateUtil.getTimeStr(myproduct.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
						if(ObjectUtil.isEmpty(myproduct.getUpdatetime())){
							codevo.setUpdatetimestr(DateUtil.getTimeStr(myproduct.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
						}else{
							codevo.setUpdatetimestr(DateUtil.getTimeStr(myproduct.getUpdatetime(), "yyyy-MM-dd HH:mm:ss"));
						}
						
						// 作品详情（图片集合）
						List<PMyproductdetails> detailslist = myDetaiMapper.findMyProductdetails(code.getCartid());
						int i = 0;
						if (detailslist != null && detailslist.size() > 0) {
							for (PMyproductdetails dd : detailslist) {
								if (!ObjectUtil.isEmpty(dd.getImgurl())) {
									if (dd.getSort() != null && dd.getSort().intValue() == 0) {
										codevo.setHeadImg("http://pic.bbyiya.com/" + dd.getImgurl() + "?imageView2/2/w/200");
									}
									i++;
								}
							}
						}
						if (ObjectUtil.isEmpty(codevo.getHeadImg())) {
							codevo.setHeadImg("http://pic.bbyiya.com/484983733454448354.png");
						}
						codevo.setCount(i);
						codevo.setIsDue(0);//默认不是预产期
						// 得到宝宝生日
						PMyproductchildinfo childinfo = mychildMapper.selectByPrimaryKey(myproduct.getCartid());
						if (childinfo != null && childinfo.getBirthday() != null) {
							codevo.setBirthdayStr(DateUtil.getTimeStr(childinfo.getBirthday(), "yyyy-MM-dd HH:mm:ss"));
							codevo.setIsDue(childinfo.getIsdue()==null?0:childinfo.getIsdue());
						}
						PMyproducttempapply tempapply=tempapplyMapper.getMyProducttempApplyByCartId(myproduct.getCartid());
						if(tempapply!=null){
							codevo.setPhone(tempapply.getMobilephone());
							codevo.setAddress(tempapply.getAdress());
							//1已使用 3制作已完成 4作品审核不通过5下单审核通过
							codevo.setActiveStatus(tempapply.getStatus());
						}
						// 得到作品订单集合
						List<OUserorders> orderList = orderMapper.findOrderListByCartId(myproduct.getCartid());
						List<String> orderNoList = new ArrayList<String>();
						for (OUserorders order : orderList) {
							orderNoList.add(order.getUserorderid());
						}
						if (orderNoList.size() > 0) {
							codevo.setOrderNoList(orderNoList);
						}
						//得到评论数
						codevo.setCommentsCount(0);
						PMyproductext myext=myextMapper.selectByPrimaryKey(myproduct.getCartid());
						if(myext!=null){
							codevo.setCommentsCount(myext.getCommentscount()==null?0:myext.getCommentscount());
						}
					}
				}
				codevoList.add(codevo);
			}
		}
		PageInfoUtil<ActivityCodeProductVO> resultPageList=new PageInfoUtil<ActivityCodeProductVO>(resultPage,codevoList);
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(resultPageList);
		return rq;
	}
	
	/**
	 * 删除模板
	 * @param tempid
	 * @return
	 */
	public ReturnModel deleteActivityCode(String codeno){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);		
		PMyproductactivitycode code=activitycodeMapper.selectByPrimaryKey(codeno);	
		if(code!=null){
			if(code.getStatus()!=null&&code.getStatus().intValue()!=Integer.parseInt(ActivityCodeStatusEnum.notuse.toString())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("不好意思，已使用的活动码不能删除！");
				return rq;
			}
			code.setStatus(Integer.parseInt(ActivityCodeStatusEnum.deleted.toString()));
			activitycodeMapper.updateByPrimaryKey(code);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("删除操作成功");
		}else{
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("活动码不存在");
		}
		return rq;
	}
	
	
	/**
	 *重置活动下的已报名的序号
	 * @return
	 */
	public ReturnModel resetAllTempApplySort(Integer tempid){
		ReturnModel rq=new ReturnModel();
		//获取活动列表
		List<PMyproducttemp> templist=myproducttempMapper.findAllTemp(tempid);
		if(templist!=null&&templist.size()>0){
			for (PMyproducttemp temp : templist) {
				
				List<Long> idsList = new ArrayList<Long>();
				idsList.add(temp.getBranchuserid());
				// 获取影楼的工作人员列表
				List<UBranchusers> userList = branchusersMapper.findMemberslistByBranchUserId(temp.getBranchuserid());
				if (userList != null && userList.size() > 0) {
					for (UBranchusers uu : userList) {
						idsList.add(uu.getUserid());
					}
				}
				List<MyProductResultVo> mylist = myMapper.findMyProductslistForTempId(idsList,temp.getTempid(),null,null);
				
				if(mylist!=null&&mylist.size()>0){
					int count=mylist.size();
					for (MyProductResultVo vo : mylist) {
						PMyproducttempapply apply=tempapplyMapper.getMyProducttempApplyByCartId(vo.getCartid());
						if(apply!=null){
							apply.setSort(count);
							tempapplyMapper.updateByPrimaryKey(apply);
						}
						count=count-1;
					}
				}
			}
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("设置成功！");
		return rq;
	}
	
}
