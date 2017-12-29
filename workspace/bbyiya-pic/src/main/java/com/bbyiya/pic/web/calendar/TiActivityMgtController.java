package com.bbyiya.pic.web.calendar;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.common.vo.ImageInfo;
import com.bbyiya.dao.OOrderaddressMapper;
import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.TiActivityexchangecodesMapper;
import com.bbyiya.dao.TiActivitysMapper;
import com.bbyiya.dao.TiActivitysinglesMapper;
import com.bbyiya.dao.TiActivityworksMapper;
import com.bbyiya.dao.TiDiscountmodelMapper;
import com.bbyiya.dao.TiMyworksMapper;
import com.bbyiya.dao.TiProductsMapper;
import com.bbyiya.dao.TiPromoteradvertinfoMapper;
import com.bbyiya.dao.TiPromoteremployeesMapper;
import com.bbyiya.dao.TiPromotersMapper;
import com.bbyiya.dao.TiUserdiscountsMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.calendar.ActivityWorksStatusEnum;
import com.bbyiya.enums.calendar.TiActivityTypeEnum;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.model.OOrderaddress;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.TiActivityexchangecodes;
import com.bbyiya.model.TiActivitys;
import com.bbyiya.model.TiActivitysingles;
import com.bbyiya.model.TiActivityworks;
import com.bbyiya.model.TiDiscountmodel;
import com.bbyiya.model.TiMyworks;
import com.bbyiya.model.TiPromoteremployees;
import com.bbyiya.model.TiPromoters;
import com.bbyiya.model.TiUserdiscounts;
import com.bbyiya.service.IRegionService;
import com.bbyiya.service.calendar.ITi_OrderMgtService;
import com.bbyiya.service.calendar.ITi_PromoterAdvertService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.calendar.TiActivitysVo;
import com.bbyiya.vo.calendar.product.TiProductResult;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.vo.user.UUserAddressResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/ti_act")
public class TiActivityMgtController extends SSOController {

	@Autowired
	private TiPromotersMapper promoterMapper;
	@Autowired
	private TiActivityworksMapper activityworksMapper;
	@Autowired
	private TiActivitysMapper actMapper;
	@Autowired
	private TiMyworksMapper myworkMapper;
	@Autowired
	private PMyproductsMapper myproductMapper;
	@Autowired
	private TiProductsMapper productMapper;
	
	@Autowired
	private TiPromoteradvertinfoMapper advertInfoMapper;
	//-------------------------------------------------------
	@Autowired
	private TiUserdiscountsMapper userDisMapper;
	@Autowired
	private TiDiscountmodelMapper dismodelMapper;
	//订单收货地址
	@Autowired
	private OOrderaddressMapper oaddressMapper;
	
	@Autowired
	private TiActivitysinglesMapper singMapper;
	@Autowired
	private TiPromoteremployeesMapper employeeMapper;
	//兑换码mapper
	@Autowired
	private TiActivityexchangecodesMapper exchangeMapper;
	
	@Resource(name = "regionServiceImpl")
	private IRegionService regionService;

	//分享广告service
	@Resource(name = "ti_PromoterAdvertService")
	private ITi_PromoterAdvertService advertService;

	@Resource(name = "tiOrderMgtServiceImpl")
	private ITi_OrderMgtService orderMgtService;
	
	/**
	 * 活动详情
	 * @param actId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/actInfo")
	public String actInfo(int actId)throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			TiActivitysVo actInfo=actMapper.getResultByActId(actId);
			if(actInfo!=null){
				boolean showAdvert=false;
				//参与活动情况
				TiActivityworks myActWork= activityworksMapper.getActWorkListByActIdAndUserId(actId, user.getUserId());
				if(myActWork!=null){
					actInfo.setMyactInfo(myActWork); 
					actInfo.setMyworkId(myActWork.getWorkid()); 
					actInfo.setApplyStatus(myActWork.getStatus()); 
					//作品的完成时间
					TiMyworks workMyworks= myworkMapper.selectByPrimaryKey(actInfo.getMyworkId());
					if(workMyworks!=null&&!ObjectUtil.isEmpty(workMyworks.getCompletetime())){
						actInfo.setCompleteTime(workMyworks.getCompletetime()); 
					}
					//作品未提交 显示活动首页，显示广告
					if(myActWork.getStatus()!=null&&!(myActWork.getStatus().intValue()==Integer.parseInt(ActivityWorksStatusEnum.imagesubmit.toString())||myActWork.getStatus().intValue()==Integer.parseInt(ActivityWorksStatusEnum.completeshare.toString())||myActWork.getStatus().intValue()==Integer.parseInt(ActivityWorksStatusEnum.completeorder.toString()))){
						showAdvert = true;
					}
				}else{
					//还未参与活动，显示广告
					showAdvert=true;
				}
				if(showAdvert){
					actInfo.setAdvert(advertService.addViewCountReurnTiPromoteradvertinfo(user, actInfo.getAdvertid()));					
				}
				//一对一活动
				if(actInfo.getActtype()!=null&&actInfo.getActtype().intValue()==Integer.parseInt(TiActivityTypeEnum.toOne.toString())){
					actInfo.setYaoqingcount(singMapper.getYaoqingCountByActId(actId));
				}
				//产品信息
				TiProductResult productResult= productMapper.getResultByProductId(actInfo.getProductid());
				productResult.setDescriptionImglist((List<ImageInfo>)JsonUtil.jsonToList(productResult.getDescriptionimgjson())) ;
				actInfo.setProduct(productResult); 
				rq.setBasemodle(actInfo); 
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		} 
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 款式选择（活动作品）
	 * @param workId
	 * @param styleId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/chooseStyle")
	public String chooseStyle(long workId,long styleId)throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			TiMyworks myworks=myworkMapper.selectByPrimaryKey(workId);
			if(myworks!=null&&myworks.getActid()!=null){
				myworks.setStyleid(styleId);
				myworkMapper.updateByPrimaryKeySelective(myworks);
				rq.setStatu(ReturnStatus.Success);
			}else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("非活动作品！"); 
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
		
	/**
	 * 参与活动
	 * @param actId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/inActivity")
	public String inActivity(int actId,@RequestParam(required=false,defaultValue="0")long eUid, @RequestParam(required=false,defaultValue="0")long versionId,String exCode)throws Exception {
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			Map<String, Object> map=new HashMap<String, Object>();
			// 1活动信息
			TiActivitys actInfo=actMapper.selectByPrimaryKey(actId);
			if(actInfo!=null){
				/**
				 * 限制参与人数
				 */
				if(actInfo.getFreecount()!=null&&actInfo.getFreecount().intValue()>0){
					if(actInfo.getApplycount()!=null&&actInfo.getApplycount().intValue()>=actInfo.getFreecount().intValue()){
						rq.setStatusreson("不好意思，您来晚了，活动参与人数已经满了！");
						return JsonUtil.objectToJsonStr(rq);
					} 
				}
				/**
				 * 限制领取人数
				 */
//				if(actInfo.getApplylimitcount()!=null&&actInfo.getApplylimitcount().intValue()>0){
//					if(actInfo.getApplyingcount()!=null&&actInfo.getApplyingcount().intValue()>=actInfo.getApplylimitcount().intValue()){
//						rq.setStatusreson("不好意思，您来晚了，活动参与人数已经满了！");
//						return JsonUtil.objectToJsonStr(rq);
//					} 
//				}
				//活动类型
				int actType=actInfo.getActtype()==null?0:actInfo.getActtype().intValue();
				if(actType==Integer.parseInt(TiActivityTypeEnum.toOne.toString())) {/**-------一对一活动-------------------------*/
					if(versionId<=0){
						rq.setStatusreson("链接无效01");
						return JsonUtil.objectToJsonStr(rq);
					}
					TiActivitysingles single= singMapper.selectByPrimaryKey(versionId);
					TiPromoteremployees emp= employeeMapper.selectByPrimaryKey(eUid);
					if(single==null||emp==null||emp.getPromoteruserid().longValue()!=actInfo.getProduceruserid().longValue()||single.getStatus().intValue()==1){
						rq.setStatusreson("链接已失效002");
						return JsonUtil.objectToJsonStr(rq);
					}
					if(eUid==user.getUserId()){
						rq.setStatusreson("自己不能参与！");
						return JsonUtil.objectToJsonStr(rq);
					}
					single.setUserid(user.getUserId());
					single.setGetstime(new Date());
					single.setStatus(1); 
					singMapper.updateByPrimaryKeySelective(single);
				}else if (actType==Integer.parseInt(TiActivityTypeEnum.exchangeCode.toString())) {
					//兑换码活动
					rq.setStatu(ReturnStatus.ParamError);
					if(!ObjectUtil.isEmpty(exCode)){
						//查询兑换码信息
						TiActivityexchangecodes exmode= exchangeMapper.selectByPrimaryKey(exCode);

						if(exmode!=null){
							//兑换码有效
							if((exmode.getUserid()!=null&&exmode.getUserid().longValue()==user.getUserId().longValue())
									||exmode.getStatus()==null
									||exmode.getStatus().intValue()==0){
								//兑换码还未使用
								if(ObjectUtil.isEmpty(exmode.getUserid())){
									exmode.setUsedtime(new Date());
									exmode.setUserid(user.getUserId());
									exmode.setStatus(1);
									exchangeMapper.updateByPrimaryKeySelective(exmode); 
								}
							}else{
								rq.setStatusreson("兑换码已被使用！");
								return JsonUtil.objectToJsonStr(rq);
							}
						}else{
							rq.setStatusreson("兑换码输入错误！（注意I与l的的区别哦~）");
							return JsonUtil.objectToJsonStr(rq);
						}
						
					}else {
						rq.setStatu(ReturnStatus.ParamError);
						rq.setStatusreson("兑换码不存在");
						return JsonUtil.objectToJsonStr(rq);
					}
				}
				//我参与的活动作品
				TiActivityworks activityworks= activityworksMapper.getActWorkListByActIdAndUserId(actId, user.getUserId());
				if(activityworks!=null){
					TiMyworks mywork= myworkMapper.selectByPrimaryKey(activityworks.getWorkid());
					if(mywork!=null){
						map.put("workId", mywork.getWorkid());
						map.put("productId", mywork.getProductid());
					}
				}else {//参加活动
					// 2 生成 作品id(cartId=workId)
					PMyproducts cart=new PMyproducts();
					cart.setCreatetime(new Date());
					cart.setUserid(0l);
					myproductMapper.insertReturnId(cart);
					
					// 3 新增我的作品
					TiMyworks myworks=new TiMyworks();
					myworks.setWorkid(cart.getCartid());
					myworks.setUserid(user.getUserId());
					myworks.setActid(actId);
					myworks.setCreatetime(new Date());
					myworks.setProductid(actInfo.getProductid());
					myworks.setStyleid(actInfo.getStyleid()==null?actInfo.getProductid():actInfo.getStyleid());
					myworkMapper.insert(myworks);
					// 4 新增活动作品信息
					activityworks=new TiActivityworks();
					activityworks.setWorkid(myworks.getWorkid());
					activityworks.setUserid(user.getUserId());
					activityworks.setCreatetime(new Date());
					activityworks.setActid(actId);
					activityworks.setStatus(Integer.parseInt(ActivityWorksStatusEnum.apply.toString()));
					activityworks.setExtcount(0);
					activityworksMapper.insert(activityworks);
					
					// 5更新参与人数
					actInfo.setApplycount((actInfo.getApplycount()==null?0:actInfo.getApplycount().intValue())+1);
					//参与中的人数
//					int applyingcount=actInfo.getApplyingcount()==null?(actInfo.getApplycount()==null?0:actInfo.getApplycount().intValue()):actInfo.getApplyingcount().intValue();
//					actInfo.setApplyingcount(applyingcount+1); 
					actMapper.updateByPrimaryKeySelective(actInfo);
					
					
					map.put("workId", myworks.getWorkid());
					map.put("productId", actInfo.getProductid());
				}
				rq.setBasemodle(map); 
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		} 
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 * 一对一活动详情
	 * @param actId
	 * @param eUid
	 * @param versionId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/singleActInfo")
	public String singleActivityInfo(int actId,@RequestParam(required=false,defaultValue="0")long eUid, @RequestParam(required=false,defaultValue="0")long versionId)throws Exception {
		ReturnModel rq=new ReturnModel();
		Map<String, Object> map=new HashMap<String, Object>();
		rq.setStatu(ReturnStatus.ParamError);
		rq.setStatusreson("无效的链接！");
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			// 1活动信息
			TiActivitys actInfo = actMapper.selectByPrimaryKey(actId);
			if (actInfo != null) {
				//一对一活动
				if(actInfo.getActtype()!=null&&actInfo.getActtype().intValue()==1) {
					if(versionId<=0){
						map.put("vstatus", 2);//活动码不可用
						rq.setStatu(ReturnStatus.Success);
						rq.setBasemodle(map);
						return JsonUtil.objectToJsonStr(rq);
					}
					TiActivitysingles single= singMapper.selectByPrimaryKey(versionId);
					TiPromoteremployees emp= employeeMapper.selectByPrimaryKey(eUid);
					if(single==null||emp==null||emp.getPromoteruserid().longValue()!=actInfo.getProduceruserid().longValue()||single.getStatus().intValue()==1){
						rq.setStatusreson("链接已失效"); 
						map.put("vstatus", 2);//活动码已使用
						rq.setStatu(ReturnStatus.Success);
						rq.setBasemodle(map);
						return JsonUtil.objectToJsonStr(rq);
					}
					if(eUid==user.getUserId()){
						map.put("vstatus", 3);//自己无法使用
						rq.setStatu(ReturnStatus.Success);
						rq.setBasemodle(map);
						rq.setStatusreson("自己不能参与！");
						return JsonUtil.objectToJsonStr(rq);
					}
				}else {
					return JsonUtil.objectToJsonStr(rq);
				}
			}else {
				return JsonUtil.objectToJsonStr(rq);
			}
			rq.setStatu(ReturnStatus.Success);
			map.put("vstatus", 1);//自己无法使用 
			rq.setBasemodle(map);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 * 领取优惠券
	 * @param workId
	 * @param styleId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getDiscounts")
	public String getDiscounts(@RequestParam(required = false, defaultValue = "0")int actId,@RequestParam(required = false, defaultValue = "0")long workId)throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=getDiscountsCommon(actId, workId, user.getUserId());
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		} 
		return JsonUtil.objectToJsonStr(rq);
	}

	
	/**
	 * 领取优惠券（两处地方，活动结束时领取，二级用户通过朋友分享领取）
	 * @param actId
	 * @param workId
	 * @param userId
	 * @return
	 */
	public ReturnModel getDiscountsCommon(int actId,long workId, long userId){
		ReturnModel rq=new ReturnModel();
		TiActivityworks activityworks=null;
		TiMyworks myworks=null;
		TiActivitys actInfo=null;
		if(workId>0){
			myworks= myworkMapper.selectByPrimaryKey(workId);
			if(myworks!=null&&myworks.getActid()!=null){
				actId=myworks.getActid();
				activityworks=activityworksMapper.selectByPrimaryKey(workId);
			}
		}
		if(actId>0){
			actInfo=actMapper.selectByPrimaryKey(actId);
		}
		if(actInfo==null){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("活动不存在！");
			return rq;
		}
		if(workId>0){
			List<TiUserdiscounts> mydislist=userDisMapper.findMyDiscountsByWorkId(userId, workId);
			if(mydislist!=null&&mydislist.size()>0){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("您已经领取过优惠券！");
				return rq;
			}
		}else if (actId>0) {
			List<TiUserdiscounts> mydislist=userDisMapper.findMyDiscountsByActId(userId, actId);
			if(mydislist!=null&&mydislist.size()>0){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("您已经领取过优惠券！");
				return rq;
			}
		}
		//获取优惠券
		List<TiDiscountmodel> modlist= dismodelMapper.findDiscountList();
		if(modlist!=null&&modlist.size()>0){
			for(int i=0;i<3;i++){
				TiUserdiscounts model=new TiUserdiscounts();
				model.setActid(actId);
				model.setWorkid(workId);
				model.setPromoteruserid(actInfo.getProduceruserid()); 
				model.setCreatetime(new Date());
				model.setDiscountid(modlist.get(0).getDiscountid());
				model.setStatus(0);
				model.setUserid(userId);
				userDisMapper.insert(model);
			}
			if(workId>0&&activityworks!=null){
				activityworks.setExtcount((activityworks.getExtcount()==null?0:activityworks.getExtcount().intValue())+1); 
				
				//如果活动目标达到，直接下单
				int extcount=actInfo.getExtcount()==null?0:actInfo.getExtcount();
				if(activityworks.getExtcount().intValue()>=extcount&&activityworks.getStatus().intValue()!=Integer.parseInt(ActivityWorksStatusEnum.completeorder.toString())){
					//更新参与活动状态
					activityworks.setStatus(Integer.parseInt(ActivityWorksStatusEnum.completeshare.toString())); 
					activityworksMapper.updateByPrimaryKeySelective(activityworks);

				}else {
					activityworksMapper.updateByPrimaryKeySelective(activityworks);
				}
			}
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("恭喜获得3张5折优惠券（下单时自动使用）");
		}
		return rq;
	}
	/**
	 * 获取版本号
	 * @param actId
	 * @param workId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/genVersionNo")
	public String genVersionNo(@RequestParam(required = false, defaultValue = "0")int actId)throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.ti_employees)){
				TiPromoteremployees employee= employeeMapper.selectByPrimaryKey(user.getUserId());
				TiActivitys activitys= actMapper.selectByPrimaryKey(actId);
				if(activitys!=null&&employee!=null&&employee.getPromoteruserid().longValue()==activitys.getProduceruserid().longValue()){
					TiActivitysingles versionMod=new TiActivitysingles();
					versionMod.setActid(actId);
					versionMod.setPromoteruserid(user.getUserId());
					versionMod.setStatus(0);
					versionMod.setCreatetime(new Date());
					singMapper.insertReturnId(versionMod);
					rq.setStatu(ReturnStatus.Success);
					rq.setBasemodle(versionMod.getActsingleid()); 
				}else {
					rq.setStatu(ReturnStatus.Success);
				}
			}else {
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("不是员工身份");
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		} 
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 工作台-获取活动列表
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/findActslist")
	public String findActslist()throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.ti_employees)){
				TiPromoteremployees employee= employeeMapper.selectByPrimaryKey(user.getUserId());
				if(employee!=null){
					List<TiActivitysVo> actlist= actMapper.findActivityList(employee.getPromoteruserid(), user.getUserId());
					if(actlist!=null&&actlist.size()>0){
						for (TiActivitysVo aa : actlist) {
							if(aa.getActtype()!=null&&aa.getActtype().intValue()==1){
								aa.setYaoqingcount(singMapper.getYaoqingCountByActId(aa.getActid())); 
							}
						}
					}
					rq.setBasemodle(actlist);
					rq.setStatu(ReturnStatus.Success);
				} 
			}else {
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("不是员工身份");
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		} 
		return JsonUtil.objectToJsonStr(rq);
	}

	
	@ResponseBody
	@RequestMapping(value = "/getPromoterAddress")
	public String getPromoterAddress(int actId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if (user != null) {
			TiActivitys activitys = actMapper.selectByPrimaryKey(actId);
			if (activitys != null) {
				TiPromoters promoters = promoterMapper.selectByPrimaryKey(activitys.getProduceruserid());
				if (promoters != null) {
					Map<String, Object> map=new HashMap<String, Object>();
					UUserAddressResult userAddressResult = new UUserAddressResult();
					userAddressResult.setProvinceName(regionService.getProvinceName(promoters.getProvince()));
					userAddressResult.setCityName(regionService.getCityName(promoters.getCity()));
					userAddressResult.setAreaName(regionService.getAresName(promoters.getArea()));
					userAddressResult.setStreetdetail(promoters.getStreetdetails());
					rq.setStatu(ReturnStatus.Success);
					map.put("address", userAddressResult);
					map.put("company", promoters.getCompanyname());
					rq.setBasemodle(map); 
				}
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	/**
	 * 活动分享页 --查看详情（用户的收货地址）
	 * @param workId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getActAddress")
	public String getActAddress(long  workId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if (user != null) {
			TiActivityworks activityworks=activityworksMapper.selectByPrimaryKey(workId);
			if(activityworks!=null&&activityworks.getUserid().longValue()==user.getUserId().longValue()){
				Map<String, Object> map=new HashMap<String, Object>();
				if(activityworks.getOrderaddressid()!=null){
					OOrderaddress address= oaddressMapper.selectByPrimaryKey(activityworks.getOrderaddressid());
					if(address!=null){
						UUserAddressResult userAddressResult = new UUserAddressResult();
						userAddressResult.setProvinceName(address.getProvince());
						userAddressResult.setCityName(address.getCity());
						userAddressResult.setAreaName(address.getDistrict());
						userAddressResult.setStreetdetail(address.getStreetdetail());
						map.put("address", userAddressResult);
						map.put("addressType", 1);
					}
				}else {
					TiActivitys activitys=actMapper.selectByPrimaryKey(activityworks.getActid());
					if(activitys!=null){
						TiPromoters promoters = promoterMapper.selectByPrimaryKey(activitys.getProduceruserid());
						if (promoters != null) {
							UUserAddressResult userAddressResult = new UUserAddressResult();
							userAddressResult.setProvinceName(regionService.getProvinceName(promoters.getProvince()));
							userAddressResult.setCityName(regionService.getCityName(promoters.getCity()));
							userAddressResult.setAreaName(regionService.getAresName(promoters.getArea()));
							userAddressResult.setStreetdetail(promoters.getStreetdetails());
							rq.setStatu(ReturnStatus.Success);
							map.put("address", userAddressResult);
							map.put("addressType", 2);
						}
					}
				}
				rq.setStatu(ReturnStatus.Success);
				rq.setBasemodle(map); 
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
}
