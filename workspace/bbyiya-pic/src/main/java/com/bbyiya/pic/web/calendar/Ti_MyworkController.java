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

import com.bbyiya.dao.EErrorsMapper;
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
import com.bbyiya.dao.TiPromoteradvertcoustomerMapper;
import com.bbyiya.dao.TiPromoteradvertimgsMapper;
import com.bbyiya.dao.TiPromoteradvertinfoMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.OrderStatusEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.calendar.ActivityWorksStatusEnum;
import com.bbyiya.model.EErrors;
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
import com.bbyiya.model.TiPromoteradvertcoustomer;
import com.bbyiya.model.TiPromoteradvertimgs;
import com.bbyiya.model.TiPromoteradvertinfo;
import com.bbyiya.model.UUsers;
import com.bbyiya.pic.vo.calendar.MyworkDetailsParam;
import com.bbyiya.service.IRegionService;
import com.bbyiya.service.calendar.ITi_OrderMgtService;
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
	private TiProductsMapper productMapper;
	@Autowired
	private TiPromoteradvertinfoMapper advertMapper;
	@Autowired
	private UUsersMapper userMapper;
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
								//活动提交
								if(myworks.getActid()!=null&&myworks.getActid().intValue()>0){
									TiActivityworks activityworks= activityworkMapper.selectByPrimaryKey(myworks.getWorkid());
									if(activityworks!=null){
										activityworks.setStatus(Integer.parseInt(ActivityWorksStatusEnum.imagesubmit.toString()));
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
	@Resource(name = "tiOrderMgtServiceImpl")
	private ITi_OrderMgtService orderMgtService;
	
	/**
	 * 门店自提
	 * @param detailJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/submitReciverInfo")
	public String submitReciverInfo(long workId, String reciever,String phone) throws Exception {
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
				activityworkMapper.updateByPrimaryKey(work);
				
				//达到完成条件 自动下单
				if(work.getStatus()!=null&&work.getStatus().intValue()==Integer.parseInt(ActivityWorksStatusEnum.completeshare.toString())){
					TiActivitys activitys=actMapper.selectByPrimaryKey(work.getActid());
					if(activitys!=null&&activitys.getProduceruserid()!=null){
						TiActivityOrderSubmitParam OrderParam=new TiActivityOrderSubmitParam();
						OrderParam.setCount(1);
						OrderParam.setSubmitUserId(activitys.getProduceruserid());
						OrderParam.setWorkId(workId);
						ReturnModel orderResult= orderMgtService.submitOrder_ibs(OrderParam);
						if(ReturnStatus.Success.equals(orderResult.getStatu())){
							rq.setStatu(ReturnStatus.Success);
							rq.setStatusreson("下单成功！");
							return JsonUtil.objectToJsonStr(rq);
						}
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
				TiProductstyles style= styleMapper.selectByPrimaryKey(myworks.getStyleid()==null?myworks.getProductid():myworks.getStyleid());
				if(style!=null){
					TiProducts products=productMapper.selectByPrimaryKey(style.getProductid());
					if(products!=null){
						List<TiMyartsdetails> details= detailMapper.findDetailsByWorkId(workId);
						if(details!=null&&details.size()>0){
							for (TiMyartsdetails dd : details) {
								dd.setImageurl(ImgDomainUtil.getImageUrl_Full(dd.getImageurl()));
							} 
						}
						Map<String, Object> map=new HashMap<String, Object>();
						map.put("details", details);
						map.put("imgCount", style.getImgcount()); 
						map.put("title", products.getTitle()); 
						map.put("cateId", products.getCateid());
						map.put("workInfo", myworks);
						if(myworks.getActid()!=null&&myworks.getActid().intValue()>0){
							TiActivitys activitys= actMapper.selectByPrimaryKey(myworks.getActid());
							if(activitys!=null&&activitys.getProduceruserid()!=null){
								TiPromoteradvertinfo advertMod= advertMapper.getModelByPromoterUserId(activitys.getProduceruserid());
								if(advertMod!=null){
									map.put("advert", advertMod);
								}
							}
							List<UUsers> userList= userMapper.findUsersByWorkId(workId);
							map.put("users", userList);
						}
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
	@Autowired
	private TiActivityworksMapper activityworksMapper;
	@Autowired
	private TiActivitysMapper actMapper;
	
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
	
	@Autowired
	private TiPromoteradvertimgsMapper advertImgMapper;
	@Autowired
	private TiPromoteradvertcoustomerMapper advertCustomerMapper;
	@Resource(name = "regionServiceImpl")
	private IRegionService regionService;
	/**
	 * 获取广告信息详细
	 * @param advertid
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/advertinfo")
	public String advertinfo(int advertid)throws Exception {
		ReturnModel rq=new ReturnModel();
		TiPromoteradvertinfo advertInfo= advertMapper.selectByPrimaryKey(advertid);
		if(advertInfo!=null){
			List<TiPromoteradvertimgs> advertImgs= advertImgMapper.findImgsByAdvertId(advertid);
			if(advertImgs!=null&&advertImgs.size()>0){
				advertInfo.setImglist(advertImgs);
			}
			rq.setBasemodle(advertInfo);
		}
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}
	
	@ResponseBody
	@RequestMapping(value = "/addAdvertcustomer")
	public String addAdvertcustomer(String name,String phone,int advertId, int province,int city,int district,String streetDetail)throws Exception {
		ReturnModel rq=new ReturnModel();
		if(advertId<=0){
			rq.setStatusreson("参数有误");
			return JsonUtil.objectToJsonStr(rq);
		}
		if(ObjectUtil.isEmpty(name)){
			rq.setStatusreson("姓名不能为空！");
			return JsonUtil.objectToJsonStr(rq);
		}
		if(!ObjectUtil.isMobile(phone)){
			rq.setStatusreson("手机号不正确！");
			return JsonUtil.objectToJsonStr(rq);
		}
//		TiPromoteradvertcoustomer cus= advertCustomerMapper.getCustomerByPhone(advertId, phone);
//		if(cus!=null){
//			rq.setStatusreson("此手机号已经提交！");
//			rq.setStatu(ReturnStatus.ParamError);
//			return JsonUtil.objectToJsonStr(rq);
//		}
		TiPromoteradvertcoustomer customer=new TiPromoteradvertcoustomer();
		customer.setAdvertid(advertId);
		customer.setName(name);
		customer.setMobilephone(phone);
		customer.setProvince(province);
		customer.setCity(city);
		customer.setDistrict(district);
		customer.setStreetdetail(streetDetail);
		customer.setCreatetime(new Date()); 
		customer.setAddress(regionService.getProvinceName(province)+regionService.getCityName(city)+regionService.getAresName(district)+streetDetail);
		advertCustomerMapper.insert(customer); 
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	@Autowired
	private EErrorsMapper logMapper;
	public void addlog(String msg) {
		EErrors errors = new EErrors();
		errors.setClassname(this.getClass().getName());
		errors.setMsg("下单失败：" + msg);
		errors.setCreatetime(new Date());
		logMapper.insert(errors);
	}
}
