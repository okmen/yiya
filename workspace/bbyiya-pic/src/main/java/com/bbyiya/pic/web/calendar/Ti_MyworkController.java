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

import com.bbyiya.dao.OOrderproductphotosMapper;
import com.bbyiya.dao.OOrderproductsMapper;
import com.bbyiya.dao.OProducerordercountMapper;
import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.TiActivitysMapper;
import com.bbyiya.dao.TiActivityworksMapper;
import com.bbyiya.dao.TiMyartsdetailsMapper;
import com.bbyiya.dao.TiMyworksMapper;
import com.bbyiya.dao.TiProductsMapper;
import com.bbyiya.dao.TiProductstylesMapper;
import com.bbyiya.dao.TiPromoteradvertimgsMapper;
import com.bbyiya.dao.TiPromoteradvertinfoMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.OrderStatusEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.calendar.ActivityWorksStatusEnum;
import com.bbyiya.model.OOrderproductphotos;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.model.OProducerordercount;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.TiActivitys;
import com.bbyiya.model.TiActivityworks;
import com.bbyiya.model.TiMyartsdetails;
import com.bbyiya.model.TiMyworks;
import com.bbyiya.model.TiProducts;
import com.bbyiya.model.TiProductstyles;
import com.bbyiya.model.TiPromoteradvertinfo;
import com.bbyiya.model.UUsers;
import com.bbyiya.pic.vo.calendar.MyworkDetailsParam;
import com.bbyiya.service.calendar.ITi_MyworksZansService;
import com.bbyiya.service.calendar.ITi_OrderMgtService;
import com.bbyiya.service.calendar.ITi_PromoterAdvertService;
import com.bbyiya.service.calendar.ItiAcitivityMgtService;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ImgDomainUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.calendar.TiActivityOrderSubmitParam;
import com.bbyiya.vo.calendar.TiActivitysVo;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/ti_work")
public class Ti_MyworkController extends SSOController {
	@Autowired
	private TiProductstylesMapper styleMapper;
	@Autowired
	private TiMyworksMapper workMapper;
	@Autowired
	private TiActivityworksMapper activityworkMapper;
	@Autowired
	private TiMyartsdetailsMapper detailMapper;
	@Autowired
	private OOrderproductsMapper oproductOrderMapper;
	@Autowired
	private OUserordersMapper userOrderMapper;
	@Autowired
	private OOrderproductphotosMapper ophotoMapper;

	@Autowired
	private OProducerordercountMapper oproducerOrderCountMapper;

	@Autowired
	private TiActivitysMapper actMapper;
	@Autowired
	private TiProductsMapper productMapper;
	@Autowired
	private UUsersMapper userMapper;
	//商家分享广告Info 
	@Autowired
	private TiPromoteradvertinfoMapper advertInfoMapper;
	@Autowired
	private TiPromoteradvertimgsMapper advertImgMapper;

	
	//订单处理service
	@Resource(name = "tiOrderMgtServiceImpl")
	private ITi_OrderMgtService orderMgtService;
	//分享广告service
	@Resource(name = "ti_PromoterAdvertService")
	private ITi_PromoterAdvertService advertService;
	//点赞处理
	@Resource(name = "ti_myworksZansServiceImpl")
	private  ITi_MyworksZansService zanService;
	/**
	 * 活动
	 */
	@Resource(name = "ti_AcitivityMgtServiceImpl")
	private  ItiAcitivityMgtService actService;
	/**
	 * 参与活动 -图片上传
	 * @param detailJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/editMyWork")
	public String editMyWork(String detailJson,@RequestParam(required = false, defaultValue = "0")int isOk) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			MyworkDetailsParam param=(MyworkDetailsParam)JsonUtil.jsonStrToObject(detailJson, MyworkDetailsParam.class);
			if(param!=null&&param.getWorkId()!=null&&param.getDetails()!=null&&param.getDetails().size()>0){
				TiMyworks myworks= workMapper.selectByPrimaryKey(param.getWorkId());
				if(myworks!=null) {
					if(myworks.getUserid()!=null&&myworks.getUserid().longValue()==user.getUserId().longValue()){
						if(myworks.getCompletetime()!=null){
							rq.setStatu(ReturnStatus.ParamError);
							rq.setStatusreson("您已经提交了作品！");
							return JsonUtil.objectToJsonStr(rq);
						}
						
						TiProductstyles style=styleMapper.selectByPrimaryKey(myworks.getStyleid()==null?myworks.getProductid():myworks.getStyleid());
						if(style!=null&&style.getImgcount().intValue()>=param.getDetails().size()) {
							//清除旧的
							List<TiMyartsdetails> oldlistList= detailMapper.findDetailsByWorkId(param.getWorkId());
							if(oldlistList!=null&&oldlistList.size()>0){
								for (TiMyartsdetails tiMyartsdetails : oldlistList) {
									detailMapper.deleteByPrimaryKey(tiMyartsdetails.getDetailid());
								}
							}
							Map<String, Object> mapResult=new HashMap<String, Object>();
							Date time=new Date();
							for(int i=0;i<param.getDetails().size();i++){
								String url=param.getDetails().get(i).getImageurl();
								if(!ObjectUtil.isEmpty(url)){
									url=ImgDomainUtil.getImageUrl_Full(url);
									TiMyartsdetails detail=new TiMyartsdetails();
									detail.setWorkid(param.getWorkId());
									detail.setImageurl(url);
									detail.setSort(i);
									detail.setCreatetime(time); 
									detailMapper.insert(detail);
								}
							}
							//完成上传操作
							if(isOk>0&&style.getImgcount().intValue()==param.getDetails().size()){
								myworks.setCompletetime(new Date());
								workMapper.updateByPrimaryKeySelective(myworks);
								//活动作品提交
								if(myworks.getActid()!=null&&myworks.getActid().intValue()>0){
									TiActivityworks activityworks= activityworkMapper.selectByPrimaryKey(myworks.getWorkid());
									if(activityworks!=null){
										if(activityworks.getStatus()==null||activityworks.getStatus()==Integer.parseInt(ActivityWorksStatusEnum.apply.toString())){
											TiActivitys actInfo=actMapper.selectByPrimaryKey(myworks.getActid());
											//参与人数
											if(actInfo!=null){
												int applyingCount=actInfo.getApplyingcount()==null?0:actInfo.getApplyingcount().intValue();
												if(actInfo.getApplylimitcount()!=null&&actInfo.getApplylimitcount()>0&&actInfo.getApplylimitcount().intValue()<=applyingCount){
													rq.setStatu(ReturnStatus.ParamError);
													rq.setStatusreson("不好意思，领取名额已经领完！");
													return JsonUtil.objectToJsonStr(rq);
												}
												actInfo.setApplyingcount(applyingCount+1);
												actMapper.updateByPrimaryKeySelective(actInfo);
											}
										}
										activityworks.setStatus(Integer.parseInt(ActivityWorksStatusEnum.imagesubmit.toString()));
										activityworks.setSubmittime(new Date()); 
										activityworkMapper.updateByPrimaryKeySelective(activityworks);
									}
									
								}
								
								//图片上传时间
								OOrderproducts oproduct= oproductOrderMapper.getOProductsByWorkId(param.getWorkId());
								if(oproduct!=null){
									OUserorders userorders= userOrderMapper.selectByPrimaryKey(oproduct.getUserorderid());
									if(userorders!=null&&userorders.getStatus().intValue()==Integer.parseInt(OrderStatusEnum.payed.toString())){
										userorders.setStatus(Integer.parseInt(OrderStatusEnum.waitFoSend.toString()));
										userorders.setUploadtime(new Date()); 
										userOrderMapper.updateByPrimaryKeySelective(userorders);
										List<OOrderproductphotos> photoList= ophotoMapper.findOrderProductPhotosByProductOrderId(oproduct.getOrderproductid());
										if(photoList==null||photoList.size()<=0){
											for(int i=0;i<param.getDetails().size();i++){
												OOrderproductphotos detail=new OOrderproductphotos();
												detail.setOrderproductid(oproduct.getOrderproductid());
												detail.setImgurl(param.getDetails().get(i).getImageurl());
												detail.setSort(i); 
												detail.setCreatetime(time); 
												ophotoMapper.insert(detail);
											}
										}
										//生成打印号
										OProducerordercount oproducerModel=oproducerOrderCountMapper.selectByPrimaryKey(oproduct.getUserorderid());
										if(oproducerModel==null){
											oproducerModel= new OProducerordercount();
											oproducerModel.setUserorderid(oproduct.getUserorderid());
											oproducerModel.setUserid(userorders.getUserid());
											oproducerModel.setProduceruserid(userorders.getProduceruserid());
											Integer indexCount=oproducerOrderCountMapper.getMaxOrderIndexByProducerIdAndUserId(userorders.getProduceruserid(),userorders.getUserid());
											int orderIndex=indexCount==null?1:(indexCount+1);
											oproducerModel.setOrderindex(orderIndex);
											oproducerModel.setPrintindex(orderIndex+"A");
											oproducerOrderCountMapper.insert(oproducerModel);
										}
										mapResult.put("ordered", 1);
										mapResult.put("userOrderId", userorders.getUserorderid());
									}
								}
							}
							rq.setBasemodle(mapResult); 
							rq.setStatu(ReturnStatus.Success);
							rq.setStatusreson("提交成功"); 
						}else {
							rq.setStatu(ReturnStatus.SystemError);
							rq.setStatusreson("图片不能超过"+style.getImgcount()+"张！"); 
						}
					}else {
						rq.setStatu(ReturnStatus.SystemError);
						rq.setStatusreson("非本人操作！"); 
					}
					
				}else {
					rq.setStatu(ReturnStatus.SystemError);
					rq.setStatusreson("作品不存在！"); 
				}
			}
		}else { 
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	

	/**
	 * 门店自提
	 * @param detailJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/submitReciverInfo")
	public String submitReciverInfo(long workId, String reciever,String phone ,@RequestParam(required = false, defaultValue = "-1") int addressType) throws Exception {
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(ObjectUtil.isEmpty(reciever)||!ObjectUtil.isMobile(phone)){
				rq.setStatusreson("姓名不能为空/手机号不正确");
				return JsonUtil.objectToJsonStr(rq);
			}
			TiActivityworks work=activityworkMapper.selectByPrimaryKey(workId);
			if(work!=null&&work.getUserid()!=null&&user.getUserId().longValue()==work.getUserid().longValue()){
				work.setReciever(reciever);
				work.setMobiephone(phone);
//				if(addressType>=0)
				work.setAddresstype(addressType); 
				activityworkMapper.updateByPrimaryKey(work);
				
				//是否可以直接下单
				boolean canOrder=false; 
				TiActivitys activitys=actMapper.selectByPrimaryKey(work.getActid());
				if(activitys!=null&&work.getStatus()!=null&&work.getStatus().intValue()==Integer.parseInt(ActivityWorksStatusEnum.completeshare.toString()))
					canOrder=true;
				else {
					if(activitys!=null&&activitys.getExtcount()==null||activitys.getExtcount().intValue()==0){
						canOrder=true;
					}
				}
				//达到完成条件 自动下单
				if(canOrder){
					TiActivityOrderSubmitParam OrderParam = new TiActivityOrderSubmitParam();
					OrderParam.setCount(1);
					OrderParam.setSubmitUserId(activitys.getProduceruserid());
					OrderParam.setWorkId(workId);
					ReturnModel orderResult = orderMgtService.submitOrder_ibs(OrderParam);
					if (ReturnStatus.Success.equals(orderResult.getStatu())) {
						// 不需要分享，直接下单，更新领取名额
						if(activitys!=null&&activitys.getExtcount()==null||activitys.getExtcount().intValue()==0){
							actService.updateActivitylimitCountByActId(activitys.getActid());
						}
						rq.setStatu(ReturnStatus.Success);
						rq.setStatusreson("下单成功！");
						return JsonUtil.objectToJsonStr(rq);
					}
				}
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("成功！");
			}else { 
				rq.setStatusreson("作品不存在");
				return JsonUtil.objectToJsonStr(rq);
			}
		}else { 
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	@Resource(name = "ti_AcitivityMgtServiceImpl")
	private ItiAcitivityMgtService activityService;
	/**
	 * 作品详情
	 * @param workId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/workInfo")
	public String workInfo(@RequestParam(required = false, defaultValue = "0")long workId)throws Exception {
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			TiMyworks myworks= workMapper.selectByPrimaryKey(workId);
			if(myworks!=null){
				//款式信息
				TiProductstyles style= styleMapper.selectByPrimaryKey(myworks.getStyleid()==null?myworks.getProductid():myworks.getStyleid());
				if(style!=null){
					//产品信息
					TiProducts products=productMapper.selectByPrimaryKey(style.getProductid());
					if(products!=null){
						List<TiMyartsdetails> details= detailMapper.findDetailsByWorkId(workId);
						if(details!=null&&details.size()>0){
							for (TiMyartsdetails dd : details) {
								dd.setImageurl(ImgDomainUtil.getImageUrl_Full(dd.getImageurl()));
							} 
						}
						Map<String, Object> map=new HashMap<String, Object>();
						map.put("details", details);//作品图片
						map.put("imgCount", style.getImgcount()); //需要上传图片数量
						map.put("title", products.getTitle()); 
						map.put("cateId", products.getCateid());
						if(products.getCateid()!=null&&products.getCateid().intValue()==5){
							map.put("shareTitle", "这是我定做的压岁红包，来看看里面有没有你的一份！");//我抢先get到了DIY新年红包技能，快来给我疯狂打Call！
							map.put("shareContent", "DIY新年红包，自己写祝福语，送长辈送孩子绝佳，快帮我领回家！");
							map.put("shareImg","http://document.bbyiya.com/styledefault201801141-2801.jpg");
						}else{
							map.put("shareTitle", "我抢先get到了新年最炫的晒照技能，快来给我疯狂打Call！");
							map.put("shareContent", "DIY自己的个性日历，你也有份哦~");
							map.put("shareImg","http://document.bbyiya.com/shareLogo.jpg");
						}
						
						map.put("nowTime",new Date());
						//作品拥有者昵称
						UUsers workUsers=userMapper.selectByPrimaryKey(myworks.getUserid()==null?0l:myworks.getUserid()); 
						map.put("nickName", workUsers==null?"":(ObjectUtil.isEmpty(workUsers.getNickname())?"":workUsers.getNickname()));
						if(myworks.getActid()!=null&&myworks.getActid().intValue()>0){
							TiActivitys activitys= actMapper.selectByPrimaryKey(myworks.getActid());
							if(activitys!=null&&activitys.getProduceruserid()!=null){
								//广告信息
								map.put("advert", advertService.addViewCountReurnTiPromoteradvertinfo(user,  activitys.getAdvertid()));
								map.put("activity", activitys);//参与的活动信息
								//集赞到期时间
								if(!ObjectUtil.isEmpty(myworks.getCompletetime())&& activitys.getHourseffective()!=null&&activitys.getHourseffective().intValue()>0){
									myworks.setExpireTime(DateUtil.getDate(myworks.getCompletetime().getTime()+activitys.getHourseffective()*60*60*1000, "yyyy-MM-dd HH:mm:ss"));
								}else if(!ObjectUtil.isEmpty(myworks.getCompletetime())){
									myworks.setExpireTime(DateUtil.getDate(myworks.getCompletetime().getTime()+24*7*60*60*1000, "yyyy-MM-dd HH:mm:ss"));									
								}
								//到期时间
								if(!ObjectUtil.isEmpty(myworks.getExpireTime())){
									if(new Date().getTime()>myworks.getExpireTime().getTime()){
//										activityService.updateActivityWorkTofailse(activitys.getProduceruserid(), workId);
									}
								}
							}
							map.put("users", zanService.findZansList(workId));//点赞的用户列表
						}
						map.put("workInfo", myworks);
						rq.setStatu(ReturnStatus.Success);
						rq.setBasemodle(map); 
					}
				}
			}
		}else { 
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 获取活动作品集赞情况
	 * @param workId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/myactInfo")
	public String myactInfo(long workId)throws Exception {
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			TiMyworks myworks= workMapper.selectByPrimaryKey(workId);
			if(myworks!=null&&myworks.getActid()!=null&&myworks.getActid()>0){
				TiActivitysVo actInfo=actMapper.getResultByActId(myworks.getActid());
				if(actInfo!=null){
					TiActivityworks myactInfo=activityworkMapper.selectByPrimaryKey(workId);
					if(myactInfo!=null){
						Map<String, Object> map=new HashMap<String, Object>();
						map.put("needCount", actInfo.getExtcount());
						map.put("extCount", myactInfo.getExtcount());
						map.put("userId", myactInfo.getUserid());
						map.put("actStatus", myactInfo.getStatus()); 
						rq.setBasemodle(map); 
					}
					rq.setStatu(ReturnStatus.Success);
				}
			}
		}else { 
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	
	/**
	 * 获取广告banner信息，新增浏览次数
	 * @param advertid
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/advertBannerInfo")
	public String advertBannerInfo(int advertid)throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user=super.getLoginUser();
		if(user!=null){
			//分享广告详情
			TiPromoteradvertinfo advertInfo=advertService.addViewCountReurnTiPromoteradvertinfo(user, advertid); //advertService.getTiPromoteradvertinfo(advertid);
			if(advertInfo!=null){
				rq.setBasemodle(advertInfo);
			}
			rq.setStatu(ReturnStatus.Success);
		}
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	
	
	
}
