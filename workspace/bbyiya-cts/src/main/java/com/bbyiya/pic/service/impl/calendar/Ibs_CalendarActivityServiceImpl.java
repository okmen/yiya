package com.bbyiya.pic.service.impl.calendar;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.baseUtils.GenUtils;
import com.bbyiya.dao.OOrderaddressMapper;
import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.TiActivityexchangecodesMapper;
import com.bbyiya.dao.TiActivityoffMapper;
import com.bbyiya.dao.TiActivitysMapper;
import com.bbyiya.dao.TiActivitysinglesMapper;
import com.bbyiya.dao.TiActivityworksMapper;
import com.bbyiya.dao.TiMyartsdetailsMapper;
import com.bbyiya.dao.TiMyworkcustomersMapper;
import com.bbyiya.dao.TiMyworksMapper;
import com.bbyiya.dao.TiProductsMapper;
import com.bbyiya.dao.TiProductstylesMapper;
import com.bbyiya.dao.TiPromoteradvertinfoMapper;
import com.bbyiya.dao.TiPromoteremployeesMapper;
import com.bbyiya.dao.UUseraddressMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.OrderTypeEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.calendar.ActivityWorksStatusEnum;
import com.bbyiya.enums.calendar.TiActivityTypeEnum;
import com.bbyiya.enums.pic.ActivityCodeStatusEnum;
import com.bbyiya.model.OOrderaddress;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.PMyproductactivitycode;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PMyproducttempusers;
import com.bbyiya.model.TiActivityexchangecodes;
import com.bbyiya.model.TiActivityoff;
import com.bbyiya.model.TiActivitys;
import com.bbyiya.model.TiMyartsdetails;
import com.bbyiya.model.TiMyworkcustomers;
import com.bbyiya.model.TiMyworks;
import com.bbyiya.model.TiProducts;
import com.bbyiya.model.TiProductstyles;
import com.bbyiya.model.TiPromoteradvertinfo;
import com.bbyiya.model.TiPromoteremployees;
import com.bbyiya.model.UUsers;
import com.bbyiya.pic.service.calendar.IIbs_CalendarActivityService;
import com.bbyiya.pic.vo.calendar.CalendarActivityAddParam;
import com.bbyiya.pic.vo.calendar.WorkForCustomerParam;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.FileUtils;
import com.bbyiya.utils.ImgDomainUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.PageInfoUtil;
import com.bbyiya.utils.QRCodeUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.address.OrderaddressVo;
import com.bbyiya.vo.calendar.TiActivitysVo;
import com.bbyiya.vo.calendar.TiActivitysWorkVo;
import com.bbyiya.vo.calendar.TiEmployeeActOffVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


@Service("ibs_CalendarActivityService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Ibs_CalendarActivityServiceImpl implements IIbs_CalendarActivityService{
	
	@Autowired
	private TiActivityexchangecodesMapper exchangecodeMapper;
	@Autowired
	private TiActivitysMapper activityMapper;
	@Autowired
	private TiProductsMapper tiproductMapper;
	@Autowired
	private TiActivityworksMapper actworkMapper;
	@Autowired
	private TiActivitysinglesMapper actworksingleMapper;
	@Autowired
	private TiPromoteremployeesMapper promoteremployeeMapper;	
	@Autowired
	private TiActivityoffMapper actoffMapper;
	@Autowired
	private OUserordersMapper orderMapper;
	@Autowired
	private OOrderaddressMapper orderaddressMapper;
	
	@Autowired
	private TiMyworksMapper timyworkMapper;
	@Autowired
	private TiMyworkcustomersMapper workcusMapper;
	@Autowired
	private TiProductstylesMapper tistyleMapper;
	@Autowired
	private TiMyartsdetailsMapper detailMapper;
	@Autowired
	private PMyproductsMapper myproductMapper;
	@Autowired
	private TiPromoteradvertinfoMapper advertinfoMapper;
	
	/*-------------------用户信息------------------------------------------------*/
	@Autowired
	private UUsersMapper usersMapper;
	/**
	 * 添加日历活动
	 * 
	 * */
	public ReturnModel addCalendarActivity(Long userid,CalendarActivityAddParam param){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);	
		TiActivitys ti=new TiActivitys();
		ti.setTitle(param.getTitle());
		ti.setActtype(param.getActtype());
		ti.setCreatetime(new Date());
		ti.setDescription(param.getDescription());
		ti.setExtcount(param.getExtCount());//目标分享人数
		ti.setFreecount(param.getFreecount());//目标参与总数量
		ti.setProductid(param.getProductid());
		ti.setAdvertid(param.getActivityid()); 
		ti.setProduceruserid(userid);//推广者Id
		ti.setStatus(1);//默认就是已开启的活动	
		ti.setAutoaddress(param.getAutoaddress()==null?0:param.getAutoaddress());

		//得到影楼默认分享广告
		TiPromoteradvertinfo advertinfo=advertinfoMapper.getAdvertByPromoterUserId(userid);
		if(advertinfo!=null){
			ti.setAdvertid(advertinfo.getAdvertid());
		}
		activityMapper.insertReturnId(ti);
		
		//如果是选择兑换码则要生成相应数量的兑换码
		if(param.getActtype()!=null&&param.getActtype().intValue()==Integer.parseInt(TiActivityTypeEnum.exchangeCode.toString())){
			//生成活动码
			for(int i=0;i<param.getFreecount();i++){
				String idString= GenUtils.generateUuid_Char8();
				TiActivityexchangecodes code=exchangecodeMapper.selectByPrimaryKey(idString);
				while(code!=null){
					idString= GenUtils.generateUuid_Char8();
					code=exchangecodeMapper.selectByPrimaryKey(idString);
					if(code==null){
						break;
					}
				}
				TiActivityexchangecodes	codelast=new TiActivityexchangecodes();
				codelast.setActid(ti.getActid());
				codelast.setCodenum(idString);
				codelast.setCreatetime(new Date());
				codelast.setStatus(Integer.parseInt(ActivityCodeStatusEnum.notuse.toString()));
				exchangecodeMapper.insert(codelast);
			}
		}
		HashMap<String, Object> map=new HashMap<String, Object>();
		map.put("actid", ti.getActid());
		rq.setBasemodle(map);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("添加日历活动成功！");
		return rq;
	}
	
	/**
	 * 修改日历活动
	 * 
	 * */
	public ReturnModel editCalendarActivity(CalendarActivityAddParam param){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);	
		TiActivitys ti=activityMapper.selectByPrimaryKey(param.getActivityid());
		if(ti!=null){
			
			Integer freecount=(param.getFreecount()==null)?0:param.getFreecount();
			//得到总报名人数
			int applycount=(ti.getApplycount()==null?0:ti.getApplycount());
			
			//如果兑换码活动，报名数量只能调高
			if(ti.getActtype()!=null&&ti.getActtype().intValue()==Integer.parseInt(TiActivityTypeEnum.exchangeCode.toString())){
				if(freecount<ti.getFreecount()){
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("邀请总数只能做调高操作！");
					return rq;
				}
				//新增调高的code
				int count=freecount.intValue()-ti.getFreecount().intValue();
				if(count>0){
					//生成活动码
					for(int i=0;i<count;i++){
						String idString= GenUtils.generateUuid_Char8();
						TiActivityexchangecodes code=exchangecodeMapper.selectByPrimaryKey(idString);
						while(code!=null){
							idString= GenUtils.generateUuid_Char8();
							code=exchangecodeMapper.selectByPrimaryKey(idString);
							if(code==null){
								break;
							}
						}
						TiActivityexchangecodes	codelast=new TiActivityexchangecodes();
						codelast.setActid(ti.getActid());
						codelast.setCodenum(idString);
						codelast.setCreatetime(new Date());
						codelast.setStatus(Integer.parseInt(ActivityCodeStatusEnum.notuse.toString()));
						exchangecodeMapper.insert(codelast);
					}
				}
				
			}
			
			ti.setTitle(param.getTitle());
			ti.setDescription(param.getDescription());
			ti.setFreecount(param.getFreecount());//目标参与总数量
			ti.setAutoaddress(param.getAutoaddress()==null?0:param.getAutoaddress());
			if(!ObjectUtil.isEmpty(param.getActivityid())){
				ti.setAdvertid(param.getAdvertid()); 
			}
			if(freecount.intValue()!=0&&freecount.intValue()<applycount){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("邀请总数限制不得小于总报名人数！");
				return rq;
			}			
			activityMapper.updateByPrimaryKeyWithBLOBs(ti);
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("修改日历活动成功！");
		return rq;
	}
	
	
	
	/**
	 * 保存合成活动图片
	 * @param param
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public ReturnModel savecomposeActImg(Integer actid,String actimg){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);	
		TiActivitysVo ti=activityMapper.getResultByActId(actid);
		if(ti!=null){
			ti.setActimg(actimg);
			activityMapper.updateByPrimaryKey(ti);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("保存合成图片成功！");
		}else{
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("活动不存在！");
		}
		
		return rq;
	}/**
	 * 合成活动图片
	 * @param param
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public ReturnModel composeActImg(Long userid,Integer actid) throws UnsupportedEncodingException{
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);	
		TiActivitysVo ti=activityMapper.getResultByActId(actid);
		if(ti!=null){
			String redirct_url="feedbackAct?actId="+actid;	
			String urlstr= ConfigUtil.getSingleValue("shareulr-base")+"uid="+URLEncoder.encode(userid.toString(),"utf-8")+"&redirct_url="+URLEncoder.encode(redirct_url,"utf-8");
			String url="https://mpic.bbyiya.com/common/generateQRcode?urlstr="+URLEncoder.encode(urlstr,"utf-8");
			ti.setCodeurl(url);
			rq.setBasemodle(ti);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("成功！");
		}else{
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("活动不存在！");
		}
		
		return rq;
	}
	
	
	
	/**
	 * 活动列表
	 * @param index
	 * @param size
	 * @param keywords
	 * @param type
	 * @return
	 */
	public ReturnModel findCalendarActivityList(int index,int size,Long userid,Integer status,String keywords,Integer type){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);
		
		PageHelper.startPage(index, size);
		List<TiActivitysVo> activitylist=activityMapper.findCalenderActivityList(userid,status,keywords,type);
		PageInfo<TiActivitysVo> pageresult=new PageInfo<TiActivitysVo>(activitylist);
		for (TiActivitysVo ti : pageresult.getList()) {
			ti.setCreateTimestr(DateUtil.getTimeStr(ti.getCreatetime(), "yyyy-MM-dd"));
			TiProducts product=tiproductMapper.selectByPrimaryKey(ti.getProductid());
			ti.setProductName(product.getTitle());
			//得到未完成数量
			Integer notsubmitcount=actworkMapper.getCountByActStatus(ti.getActid(), Integer.parseInt(ActivityWorksStatusEnum.apply.toString()));
			ti.setNotsubmitcount(notsubmitcount==null?0:notsubmitcount);
			//得到图片已提交未分享数量
			Integer notsharecount=actworkMapper.getCountByActStatus(ti.getActid(), Integer.parseInt(ActivityWorksStatusEnum.imagesubmit.toString()));
			ti.setNotsharecount(notsharecount==null?0:notsharecount);
			ti.setNotsubmitcount(ti.getNotsubmitcount()+ti.getNotsharecount());
			//得到已邀请数量
			if(ti.getActtype()==Integer.parseInt(TiActivityTypeEnum.toAll.toString())){
				ti.setYaoqingcount(ti.getFreecount());
			}else{
				Integer yaoqingcount=actworksingleMapper.getYaoqingCountByActId(ti.getActid());
				ti.setYaoqingcount(yaoqingcount==null?0:yaoqingcount);
			}
			if(ti.getAdvertid()!=null){
				TiPromoteradvertinfo advertinfo=advertinfoMapper.selectByPrimaryKey(ti.getAdvertid());
				if(advertinfo!=null){
					ti.setAdverttitle(advertinfo.getDescription());
				}
			}else{
				ti.setAdverttitle("无广告");
			}
		}
		rq.setBasemodle(pageresult);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("获取列表成功！");
		return rq;
	}
	
	/**
	 * 活动制作进度列表
	 */
	public ReturnModel getActWorkListByActId(int index,int size,Integer actid,Integer status,String keywords){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);
		
		
		TiActivitys act=activityMapper.selectByPrimaryKey(actid);
		PageHelper.startPage(index, size);
		List<TiActivitysWorkVo> activitylist=actworkMapper.findActWorkListByActId(actid, status, keywords);
		PageInfo<TiActivitysWorkVo> pageresult=new PageInfo<TiActivitysWorkVo>(activitylist);
		if(pageresult!=null&&pageresult.getList()!=null){
			for (TiActivitysWorkVo ti : pageresult.getList()) {
				if(act!=null){
					ti.setTargetextCount(act.getExtcount());
				}
				if(ti.getAddresstype()!=null&&ti.getAddresstype().intValue()==1){
					OOrderaddress orderaddress=orderaddressMapper.selectByPrimaryKey(ti.getOrderaddressid());
					if(orderaddress!=null){
						ti.setReciever(orderaddress.getReciver());
						ti.setMobiephone(orderaddress.getPhone());
					}
					
				}
				ti.setCreateTimestr(DateUtil.getTimeStr(ti.getCreatetime(), "yyyy-MM-dd"));
				UUsers user=usersMapper.selectByPrimaryKey(ti.getUserid());
				if(user!=null){
					ti.setWeiNickName(user.getNickname());
				}		
				// 得到作品订单集合
				List<OUserorders> orderList = orderMapper.findOrderListByCartId(ti.getWorkid(),Integer.parseInt(OrderTypeEnum.ti_branchOrder.toString()));
				List<String> orderNoList = new ArrayList<String>();
				for (OUserorders order : orderList) {
					orderNoList.add(order.getUserorderid());
				}
				ti.setOrdernolist(orderNoList);
				
			}
		}
		rq.setBasemodle(pageresult);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("获取列表成功！");
		return rq;
	}
	
	/**
	 * 修改活动备注
	 */
	public ReturnModel editActivityRemark(Integer actid,String remark){
		ReturnModel rq=new ReturnModel();
		TiActivitys ti=activityMapper.selectByPrimaryKey(actid);
		if(ti!=null){
			ti.setRemark(remark);
			activityMapper.updateByPrimaryKey(ti);
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("设置成功！");
		return rq;
	}
	
	
	/**
	 * 影楼员工负责模板信息列表
	 * @return
	 */
	public ReturnModel findActivityoffList(int index,int size,Long promoterUserId,Integer actid){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.Success);
		PageHelper.startPage(index, size);	
		List<TiPromoteremployees> list= promoteremployeeMapper.findEmployeelistByPromoterUserId(promoterUserId);		
		PageInfo<TiPromoteremployees> reuslt=new PageInfo<TiPromoteremployees>(list); 
		if(reuslt!=null&&reuslt.getList()!=null&&reuslt.getList().size()>0){	
			List<TiEmployeeActOffVo> usertemplist=new ArrayList<TiEmployeeActOffVo>();
			for (TiPromoteremployees buser : list) {
				TiEmployeeActOffVo usertemp=new TiEmployeeActOffVo();
				usertemp.setActid(actid);
				usertemp.setName(buser.getName());
				usertemp.setPromoteruserid(promoterUserId);
				usertemp.setStatus(1);//默认全开启权限
				usertemp.setUserid(buser.getUserid());
				UUsers user=usersMapper.selectByPrimaryKey(buser.getUserid());
				if(user!=null&&user.getMobilephone()!=null){
					usertemp.setPhone(user.getMobilephone());
				}
				TiActivityoff actoff=actoffMapper.selectByPromoterUserIdAndActId(buser.getUserid(), actid);
				if(actoff!=null){
					usertemp.setStatus(0);
				}
				usertemplist.add(usertemp);
			}
			//根据一个PageInfo初始化另一个page
			PageInfoUtil<TiEmployeeActOffVo> reusltPage=new PageInfoUtil<TiEmployeeActOffVo>(reuslt, usertemplist);
			rq.setBasemodle(reusltPage);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("获取列表成功！");
		}
		return rq;
	}
	
	
	/**
	 * 设置员工活动负责权限
	 * @return
	 */
	public ReturnModel setUserActPromotePermission(Long userId,Integer actid,Integer status){
		ReturnModel rq=new ReturnModel();
		if(actid==null){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("参数错误：actid为空！");
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
		TiActivityoff actoff=actoffMapper.selectByPromoterUserIdAndActId(userId, actid);
		if(status==1){
			if(actoff!=null){
				actoffMapper.deleteByPrimaryKey(actoff.getId());
			}
		}else{
			if(actoff==null){
				actoff=new TiActivityoff();
				actoff.setActid(actid);
				actoff.setPromoteruserid(userId);
				actoffMapper.insert(actoff);
			}
		}		
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("设置成功！");
		return rq;
	}
	
	/**
	 * 删除预览垃圾数据
	 * @param userid
	 */
	public void deleteWorkForCustomer(Long userid){
		List<TiMyworks> worklist=timyworkMapper.selectDirtyDataByUserId(userid);
		if(worklist!=null&&worklist.size()>0){
			for (TiMyworks tiMyworks : worklist) {
				timyworkMapper.deleteByPrimaryKey(tiMyworks.getWorkid());
			}
		}
	}
	/**
	 * 代客制作预览
	 */
	public ReturnModel reviewWorkForCustomer(Long userid,WorkForCustomerParam workparam){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);	
		TiMyworks mywork=null;
		if(!ObjectUtil.isEmpty(workparam.getWorkId())){
			mywork=timyworkMapper.selectByPrimaryKey(ObjectUtil.parseLong(workparam.getWorkId()));
		}
		if(mywork==null){
			// 2 生成作品id(cartId=workId)
			PMyproducts cart=new PMyproducts();
			cart.setCreatetime(new Date());
			cart.setUserid(0l);
			myproductMapper.insertReturnId(cart);
			mywork=new TiMyworks();
			mywork.setWorkid(cart.getCartid());
			mywork.setCreatetime(new Date());
			mywork.setIsinstead(1);
			mywork.setProductid(workparam.getProductId());
			mywork.setStyleid(workparam.getStyleId());
			mywork.setUserid(userid);
			timyworkMapper.insert(mywork);
			myproductMapper.deleteByPrimaryKey(cart.getCartid());
		}else{
			mywork.setIsinstead(1);
			mywork.setProductid(workparam.getProductId());
			mywork.setStyleid(workparam.getStyleId());
			mywork.setUserid(userid);
			timyworkMapper.updateByPrimaryKey(mywork);
		}
		
		HashMap<String, Object> map=new HashMap<String, Object>();
		map.put("workId", mywork.getWorkid());
		map.put("styleId", mywork.getStyleid());
		String redirct_url="assistantpreview?workId="+mywork.getWorkid()+"&styleId="+workparam.getStyleId();	

		try {
			String urlstr = ConfigUtil.getSingleValue("shareulr-base")+"uid="+URLEncoder.encode(userid.toString(),"utf-8")+"&redirct_url="+URLEncoder.encode(redirct_url,"utf-8");
			String codeurl="https://mpic.bbyiya.com/common/generateQRcode?urlstr="+URLEncoder.encode(urlstr,"utf-8");
			map.put("codeurl", codeurl);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Date time=new Date();
		//先删除后插入
		List<TiMyartsdetails> detailslist=detailMapper.findDetailsByWorkId(mywork.getWorkid());
		if(detailslist!=null&&detailslist.size()>0){
			for (TiMyartsdetails dd : detailslist) {
				detailMapper.deleteByPrimaryKey(dd.getDetailid());
			}
		}
		for(int i=0;i<workparam.getDetails().size();i++){
			String url=workparam.getDetails().get(i).getImageurl();
			//if(!ObjectUtil.isEmpty(url)){
				url=ImgDomainUtil.getImageUrl_Full(url);
				TiMyartsdetails detail=new TiMyartsdetails();
				detail.setWorkid(mywork.getWorkid());
				detail.setImageurl(url);
				detail.setSort(i);
				detail.setCreatetime(time); 
				detailMapper.insert(detail);
			//}
		}
		
		rq.setBasemodle(map);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("添加预览成功！");
		return rq;
		
	}
		
	/**
	 * 添加客户代客制作
	 * 
	 * */
	public ReturnModel addWorkForCustomer(Long userid,WorkForCustomerParam workparam,OrderaddressVo addressparam){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);	
		
		TiProductstyles style=tistyleMapper.selectByPrimaryKey(workparam.getStyleId());
		if(style!=null&&workparam.getDetails().size()<style.getImgcount().intValue()) {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("请上传完"+style.getImgcount()+"张图片,才能提交!");
			return rq;
		}
		TiMyworks mywork=null;
		if(!ObjectUtil.isEmpty(workparam.getWorkId())){
			mywork=timyworkMapper.selectByPrimaryKey(ObjectUtil.parseLong(workparam.getWorkId()));
		}
		if(mywork==null){
			// 2 生成 作品id(cartId=workId)
			PMyproducts cart=new PMyproducts();
			cart.setCreatetime(new Date());
			cart.setUserid(0l);
			myproductMapper.insertReturnId(cart);
			mywork=new TiMyworks();
			mywork.setWorkid(cart.getCartid());
			mywork.setCreatetime(new Date());
			mywork.setIsinstead(1);
			mywork.setProductid(workparam.getProductId());
			mywork.setStyleid(workparam.getStyleId());
			mywork.setUserid(userid);
			timyworkMapper.insert(mywork);
			myproductMapper.deleteByPrimaryKey(cart.getCartid());
		}else{
			mywork.setIsinstead(1);
			mywork.setProductid(workparam.getProductId());
			mywork.setStyleid(workparam.getStyleId());
			mywork.setUserid(userid);
			timyworkMapper.updateByPrimaryKey(mywork);
		}
		
		
		HashMap<String, Object> map=new HashMap<String, Object>();
		map.put("workId", mywork.getWorkid());
		map.put("productId", mywork.getProductid());
		
		TiMyworkcustomers workcus=new TiMyworkcustomers();
		workcus.setAddresstype(addressparam.getAddressType());
		workcus.setCity(addressparam.getCity());
		workcus.setDistrict(addressparam.getDistrict());
		workcus.setProvince(addressparam.getProvince());
		workcus.setCreatetime(new Date());
		workcus.setCustomername(addressparam.getReciver());
		workcus.setBabynickname(workparam.getBabynickname());
		workcus.setMobilephone(addressparam.getPhone());
		workcus.setNeedredpackettotal(workparam.getNeedRedpacketTotal());
		workcus.setNeedsharecount(workparam.getNeedShareCount());
		workcus.setPromoteruserid(userid);
		workcus.setReciever(addressparam.getReciver());
		workcus.setRecieverphone(addressparam.getPhone());
		workcus.setStreetdetails(addressparam.getStreetdetail());
		workcus.setStatus(Integer.parseInt(ActivityWorksStatusEnum.imagesubmit.toString()));
		workcus.setWorkid(mywork.getWorkid());
		workcusMapper.insert(workcus);
		Date time=new Date();
		//先删除后插入
		List<TiMyartsdetails> detailslist=detailMapper.findDetailsByWorkId(mywork.getWorkid());
		if(detailslist!=null&&detailslist.size()>0){
			for (TiMyartsdetails dd : detailslist) {
				detailMapper.deleteByPrimaryKey(dd.getDetailid());
			}
		}
		for(int i=0;i<workparam.getDetails().size();i++){
			String url=workparam.getDetails().get(i).getImageurl();
			if(!ObjectUtil.isEmpty(url)){
				url=ImgDomainUtil.getImageUrl_Full(url);
				TiMyartsdetails detail=new TiMyartsdetails();
				detail.setWorkid(workcus.getWorkid());
				detail.setImageurl(url);
				detail.setSort(i);
				detail.setCreatetime(time); 
				detailMapper.insert(detail);
			}
		}
		
		rq.setBasemodle(map);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("添加成功！");
		return rq;
	}
	/**
	 * 代客制作列表
	 * @throws UnsupportedEncodingException 
	 */
	public ReturnModel workForCustomerList(Long userid,int index,int size,String keywords) throws UnsupportedEncodingException{
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);
		PageHelper.startPage(index, size);
		List<TiMyworkcustomers> cuslist=workcusMapper.selectListByPromoterUserId(userid,keywords);
		PageInfo<TiMyworkcustomers> pageresult=new PageInfo<TiMyworkcustomers>(cuslist);
		
		if(pageresult!=null&& pageresult.getList()!=null&&pageresult.getList().size()>0){
			for (TiMyworkcustomers cus :  pageresult.getList()) {
				cus.setCreatetimestr(DateUtil.getTimeStr(cus.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
				//条件： 0分享,1收集红包
				if(cus.getNeedsharecount()!=null&&cus.getNeedsharecount().intValue()>0){
					cus.setCondition(0);
				}else{
					cus.setCondition(1);
				}
				String redirct_url="assistant?workId="+cus.getWorkid();	
				String urlstr= ConfigUtil.getSingleValue("shareulr-base")+"uid="+URLEncoder.encode(userid.toString(),"utf-8")+"&redirct_url="+URLEncoder.encode(redirct_url,"utf-8");
				String url="https://mpic.bbyiya.com/common/generateQRcode?urlstr="+URLEncoder.encode(urlstr,"utf-8");
				cus.setCodeUrl(url);
				
				// 得到作品订单集合
				List<OUserorders> orderList = orderMapper.findOrderListByCartId(cus.getWorkid(),Integer.parseInt(OrderTypeEnum.ti_branchOrder.toString()));
				List<String> orderNoList = new ArrayList<String>();
				for (OUserorders order : orderList) {
					orderNoList.add(order.getUserorderid());
				}
				cus.setOrdernolist(orderNoList);
				
			}
		}
		//清除预览垃圾数据
		this.deleteWorkForCustomer(userid);
		rq.setBasemodle(pageresult);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("添加成功！");
		return rq;
	}
		

	/**
	 * 保存模板二维码图片
	 * @return
	 * @throws Exception 
	 */
	public ReturnModel saveRQcode(String url) throws Exception{
		ReturnModel rq=new ReturnModel();
		// 获取用户的当前工作主目录 
		String sep=System.getProperty("file.separator");
		String currentWorkDir = System.getProperty("user.home") +sep+ "imagedownloadtemp"+sep;
		FileUtils.isDirExists(currentWorkDir);
		String filename = "RQcode.jpg";
		File file = new File(currentWorkDir + filename);
		BufferedImage bufImg = QRCodeUtil.createImage(url, "", true);
		// 生成二维码QRCode图片
		ImageIO.write(bufImg, "jpg", file);
		rq.setBasemodle(file.getPath());
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("获取图片成功！");
		return rq;
	}
	

	public List<TiActivityexchangecodes> findTiActivityExchangeCodeList(Integer actid){
		ReturnModel rq=new ReturnModel();
		List<TiActivityexchangecodes> list=exchangecodeMapper.findTiActivityExchangeCodeList(actid);
		rq.setBasemodle(list);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("获取图片成功！");
		return list;		
		
	}
}
