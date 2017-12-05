package com.bbyiya.pic.web.calendar;

import java.util.ArrayList;
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

import com.bbyiya.baseUtils.GenUtils;
import com.bbyiya.common.vo.ImageInfo;
import com.bbyiya.dao.OOrderaddressMapper;
import com.bbyiya.dao.OPayorderMapper;
import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.TiGroupactivityMapper;
import com.bbyiya.dao.TiGroupactivitypraiseusersMapper;
import com.bbyiya.dao.TiGroupactivityproductsMapper;
import com.bbyiya.dao.TiGroupactivityworksMapper;
import com.bbyiya.dao.TiMyartsdetailsMapper;
import com.bbyiya.dao.TiMyworksMapper;
import com.bbyiya.dao.TiProductsMapper;
import com.bbyiya.dao.TiProductshowproductsMapper;
import com.bbyiya.dao.TiProductshowstylesMapper;
import com.bbyiya.dao.TiProductshowtemplateMapper;
import com.bbyiya.dao.TiProductshowtemplateinfoMapper;
import com.bbyiya.dao.TiProductstylesMapper;
import com.bbyiya.dao.TiPromoteradvertinfoMapper;
import com.bbyiya.dao.TiPromotersMapper;
import com.bbyiya.enums.OrderStatusEnum;
import com.bbyiya.enums.PayOrderTypeEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.calendar.ActivityAddressType;
import com.bbyiya.enums.calendar.AddressTypeEnum;
import com.bbyiya.enums.calendar.GroupActWorkStatus;
import com.bbyiya.enums.calendar.GroupActivityType;
import com.bbyiya.model.OOrderaddress;
import com.bbyiya.model.OPayorder;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.TiGroupactivity;
import com.bbyiya.model.TiGroupactivitypraiseusers;
import com.bbyiya.model.TiGroupactivityproducts;
import com.bbyiya.model.TiGroupactivityworks;
import com.bbyiya.model.TiMyartsdetails;
import com.bbyiya.model.TiMyworks;
import com.bbyiya.model.TiProducts;
import com.bbyiya.model.TiProductshowproducts;
import com.bbyiya.model.TiProductshowstyles;
import com.bbyiya.model.TiProductshowtemplate;
import com.bbyiya.model.TiProductshowtemplateinfo;
import com.bbyiya.model.TiProductstyles;
import com.bbyiya.model.TiPromoteradvertinfo;
import com.bbyiya.service.IRegionService;
import com.bbyiya.service.calendar.ITi_OrderMgtService;
import com.bbyiya.service.pic.IBasePostMgtService;
import com.bbyiya.service.pic.IBaseUserAddressService;
import com.bbyiya.utils.ImgDomainUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.vo.user.UUserAddressResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/groupAct")
public class TiGroupActivityController  extends SSOController {
	@Autowired
	private TiGroupactivityMapper gactMapper;
	@Autowired
	private TiGroupactivityproductsMapper gactProMapper;
	@Autowired
	private TiGroupactivityworksMapper gworkMapper;
	@Autowired
	private PMyproductsMapper myproductMapper;
	@Autowired
	private TiProductsMapper productMapper;
	
	@Autowired
	private TiMyworksMapper workMapper;
	@Autowired
	private TiProductstylesMapper styleMapper;
	@Autowired
	private TiMyartsdetailsMapper detailMapper;
	
	@Autowired
	private TiProductshowtemplateMapper tempMapper;
	@Autowired
	private TiProductshowtemplateinfoMapper tempInfoMapper;
	@Autowired
	private TiProductshowproductsMapper showProductMapper;
	@Autowired
	private TiProductshowstylesMapper showStyleMapper;
	/**
	 * 团购活动-详情
	 * @param gActId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/gActInfo")
	public String actInfo(int groupId)throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			Map<String, Object> result = new HashMap<String, Object>();
			TiGroupactivity actInfo = gactMapper.selectByPrimaryKey(groupId);
			if (actInfo != null) {
				int browsecount = actInfo.getBrowsecount() == null ? 1 : (actInfo.getBrowsecount().intValue() + 1);
				actInfo.setBrowsecount(browsecount);
				gactMapper.updateByPrimaryKeySelective(actInfo);
			}
			result.put("gActInfo", actInfo);
			TiGroupactivityworks gwork= gworkMapper.getTiGroupactivityworksByActIdAndUserId(user.getUserId(), groupId);
			if(gwork!=null&&gwork.getStatus()!=null&&(gwork.getStatus().intValue()==Integer.parseInt(GroupActWorkStatus.payed.toString())||gwork.getStatus().intValue()==Integer.parseInt(GroupActWorkStatus.completeorder.toString()))){
				result.put("gwork", gwork);
				result.put("exists", 1);//已经参与活动
			}else {
				result.put("exists", 0);
				List<TiGroupactivityproducts> gprolist = gactProMapper.findProductsByGActid(groupId);
				if (gprolist != null && gprolist.size() > 0) {
					List<Long> proIds = new ArrayList<Long>();
					for (TiGroupactivityproducts tt : gprolist) {
						proIds.add(tt.getProductid());
					}
					List<TiProducts> proList = productMapper.findProductlistByProductIds(proIds);
					if (proList != null && proList.size() > 0) {
						for (TiGroupactivityproducts pro : gprolist) {
							for (TiProducts pp : proList) {
								if (pro.getProductid().longValue() == pp.getProductid().longValue()) {
									pro.setTitle(pp.getTitle());
									pro.setOriginPrice(pp.getPrice());
									pro.setDefaultimg(pp.getDefaultimg());
								}
							}
						}
					}
					result.put("prolist", gprolist);
				}
				//广告模式
				if(actInfo.getType()!=null&&actInfo.getType().intValue()==Integer.parseInt(GroupActivityType.advert.toString())){
					if(actInfo.getAdvertid()!=null){
						TiPromoteradvertinfo advertMod=advertInfoMapper.selectByPrimaryKey(actInfo.getAdvertid());
						result.put("advert", advertMod);
					}
					if(actInfo.getTempid()!=null){
						// 产品翻页模板 TODO
						// 所有款式图片
						List<TiProductshowtemplateinfo> styleImg = tempInfoMapper.selectByTempId(actInfo.getTempid());
						if (styleImg != null && styleImg.size() > 0) {
							for (TiProductshowtemplateinfo pp : styleImg) {
								if (pp.getImgjson() != null) {
									List<ImageInfo> imList = (List<ImageInfo>) JsonUtil.jsonToList(pp.getImgjson());
									if (imList != null && imList.size() > 0) {
										pp.setImglist(imList);
									}
								}
							}
							//最重要展示的列表
							List<TiProductshowproducts> showResult = new ArrayList<TiProductshowproducts>();
							// 产品列表
							List<TiProductshowproducts> showCateList = showProductMapper.selectAll();
							//款式列表
							List<TiProductshowstyles> showStylelist= showStyleMapper.selectByAll();
							if (showCateList != null && showCateList.size() > 0) {
								for (TiProductshowproducts cate : showCateList) {
									List<TiProductshowtemplateinfo> imgs = new ArrayList<TiProductshowtemplateinfo>();
									for (TiProductshowtemplateinfo ss : styleImg) {
										if (ss.getCateid().intValue() == cate.getCateid().intValue()) {
											if(showStylelist!=null&&showStylelist.size()>0){
												for (TiProductshowstyles sty : showStylelist) {
													if(sty.getShowstyleid().intValue()==ss.getShowstyleid().intValue()){
														ss.setStyleName(sty.getStylename()); 
													}
												}
											}
											imgs.add(ss);
										}
									}
									if (imgs != null && imgs.size() > 0) {
										cate.setStyleList(imgs);
										showResult.add(cate);
									}
								}
							}
							result.put("productTempInfo", showResult);
						}
					}
				}
			}
			rq.setBasemodle(result);
			rq.setStatu(ReturnStatus.Success);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	

	
	/**
	 * 参与 团购
	 * @param gActId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/inAct")
	public String inAct(int groupId,long productId)throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			List<TiGroupactivityworks> gworklist=gworkMapper.findTiGroupactivityworksByActIdAndUserId(user.getUserId(), groupId);			
			if(gworklist!=null&&gworklist.size()>0){
				//如果已经选过的产品，不用再新增
				TiGroupactivityworks result=null;
				for (TiGroupactivityworks gg : gworklist) {
					if(gg.getStatus()!=null&&gg.getStatus().intValue()==Integer.parseInt(GroupActWorkStatus.payed.toString())){
						rq.setBasemodle(gg);
						rq.setStatu(ReturnStatus.Success);
						return JsonUtil.objectToJsonStr(rq); 
					}else if(gg.getProductid()!=null&& gg.getProductid().longValue()==productId){
						result=gg;
					}
				}
				if(result!=null){
					rq.setBasemodle(result);
					rq.setStatu(ReturnStatus.Success);
					return JsonUtil.objectToJsonStr(rq); 
				}
			}
			long workId = getNewWorkId();
			TiGroupactivityworks gwork = new TiGroupactivityworks();
			gwork.setWorkid(workId);
			gwork.setUserid(user.getUserId());
			gwork.setGactid(groupId);
			gwork.setProductid(productId); 
			gwork.setCreatetime(new Date());
			gwork.setStatus(Integer.parseInt(GroupActWorkStatus.apply.toString()));
			gworkMapper.insert(gwork);
			//创建作品
			TiMyworks myworks=new TiMyworks();
			myworks.setWorkid(workId);
			myworks.setProductid(productId);
			myworks.setCreatetime(new Date());
			myworks.setUserid(user.getUserId());
			workMapper.insert(myworks);
			rq.setBasemodle(gwork);
			rq.setStatu(ReturnStatus.Success);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
		}
		return JsonUtil.objectToJsonStr(rq); 
	}
	@Autowired
	private TiGroupactivitypraiseusersMapper gpraiseMapper;

	@Autowired
	private TiPromoteradvertinfoMapper advertInfoMapper;
	/**
	 * 团购作品详情
	 * @param workId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/gworkInfo")
	public String gworkInfo(long workId)throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			TiMyworks myworks= workMapper.selectByPrimaryKey(workId);
			if(myworks!=null){
				TiGroupactivityworks gwork= gworkMapper.selectByPrimaryKey(workId);
				if(gwork!=null){
					TiGroupactivity actGroupactivity= gactMapper.selectByPrimaryKey(gwork.getGactid());
					if(actGroupactivity!=null){
						Map<String, Object> map=new HashMap<String, Object>();
						map.put("gwork", gwork);
						map.put("needPraiseCount", actGroupactivity.getPraisecount());
						map.put("countDownLong", actGroupactivity.getTimespare());
						map.put("titleShare", actGroupactivity.getTitleshare());
						map.put("minTitleShare", actGroupactivity.getTitleminshare());
						map.put("nowTime", new Date());
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
								map.put("details", details);
								map.put("imgCount", style.getImgcount()); 
								map.put("title", products.getTitle()); 
								map.put("cateId", products.getCateid());
								map.put("workInfo", myworks);
								
							}
						}
						if(gwork.getStatus()!=null&&(gwork.getStatus().intValue()==Integer.parseInt(GroupActWorkStatus.payed.toString())||gwork.getStatus().intValue()==Integer.parseInt(GroupActWorkStatus.completeorder.toString()))){
							map.put("praiseUsers", gpraiseMapper.findlistByWorkId(workId));
							if(actGroupactivity.getAdvertid()!=null&&actGroupactivity.getAdvertid().intValue()>0){
								TiPromoteradvertinfo advertMod=advertInfoMapper.selectByPrimaryKey(actGroupactivity.getAdvertid());
								map.put("advert", advertMod);
							}
						}
						rq.setBasemodle(map);
					}
					
					rq.setStatu(ReturnStatus.Success);
				}else {
					rq.setStatu(ReturnStatus.SystemError);
				}
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
		}
		return JsonUtil.objectToJsonStr(rq); 
	}
	
	/**
	 * 作品点赞
	 * @param workId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addGworkPraise")
	public String addGworkPraise(long workId)throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			TiGroupactivitypraiseusers model=gpraiseMapper.existsByWorkIdAndUserId(workId, user.getUserId());
			if(model==null){
				model=new TiGroupactivitypraiseusers();
				model.setUserid(user.getUserId());
				model.setHeadimg(user.getHeadImg());
				model.setWorkid(workId);
				model.setNickname(user.getNickName());
				model.setCreatetime(new Date());
				gpraiseMapper.insert(model);
				TiGroupactivityworks gwork= gworkMapper.selectByPrimaryKey(workId);
				if(gwork!=null&&gwork.getStatus().intValue()==Integer.parseInt(GroupActWorkStatus.payed.toString())){
					gwork.setPraisecount(gwork.getPraisecount()==null?1:(gwork.getPraisecount().intValue()+1));
					gworkMapper.updateByPrimaryKeySelective(gwork);
				}
			}
			rq.setStatu(ReturnStatus.Success); 
		}
		return JsonUtil.objectToJsonStr(rq); 
	}
	
	/**
	 * 选择款式
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
			TiGroupactivityworks gwork= gworkMapper.selectByPrimaryKey(workId);
			TiProductstyles styleInfo=styleMapper.selectByPrimaryKey(styleId);
			if(gwork!=null&&(gwork.getStatus()==null||gwork.getStatus().intValue()!=Integer.parseInt(GroupActWorkStatus.payed.toString()))){
				gwork.setSttyleid(styleId);
				gwork.setProductid(styleInfo.getProductid()); 
				gworkMapper.updateByPrimaryKeySelective(gwork);
				TiMyworks myworks= workMapper.selectByPrimaryKey(workId);
				if(myworks!=null){
					myworks.setStyleid(styleId);
					myworks.setProductid(styleInfo.getProductid()); 
					workMapper.updateByPrimaryKeySelective(myworks);
				}
				rq.setStatu(ReturnStatus.Success);
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	

	@Resource(name = "tiOrderMgtServiceImpl")
	private ITi_OrderMgtService orderMgtService;
	
	/**
	 * 团购收货地址选择
	 * @param workId
	 * @param styleId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/chooseAddress")
	public String chooseAddress(@RequestParam(required = false, defaultValue = "0")long workId,@RequestParam(required = false, defaultValue = "0")long addressId,@RequestParam(required = false, defaultValue = "-1")int addressType, String name,String phone)throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			// 团购作品
			TiGroupactivityworks gwork = gworkMapper.selectByPrimaryKey(workId);
			if (gwork != null) {
				switch (addressType) {
				case 0:// 自提
					if (ObjectUtil.isEmpty(name) || ObjectUtil.isEmpty(phone) || !ObjectUtil.isMobile(phone)) {
						rq.setStatu(ReturnStatus.ParamError);
						rq.setStatusreson("联系方式/手机号不能为空！");
						return JsonUtil.objectToJsonStr(rq);
					}
					gwork.setName(name);
					gwork.setMobilephone(phone);
					gwork.setAddresstype(addressType);
					gworkMapper.updateByPrimaryKeySelective(gwork);
					rq.setStatu(ReturnStatus.Success);
					break;
				case 1:// 直邮到家
					gwork.setAddresstype(addressType);
//					gwork.setAddressid(orderMgtService.addOrderAddressReturnId(addressId)); 
					gworkMapper.updateByPrimaryKeySelective(gwork);
					rq.setStatu(ReturnStatus.Success);
					break;
				default:
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("收货方式有误！");
					break;
				}
			}else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("团购记录不存在！");
			}

		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
//	
//	@Autowired
//	private TiProductstylesMapper styleMapper;
	
	/**
	 * 团购订单提交信息展示信息
	 * @param workId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/gOrderSubmitInfo")
	public String gOrderSubmitInfo(@RequestParam(required = false, defaultValue = "0")long workId)throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			// 团购作品
			TiGroupactivityworks gwork = gworkMapper.selectByPrimaryKey(workId);
			if (gwork != null&&gwork.getSttyleid()!=null) {
				//参与团购活动的产品列表
				List<TiGroupactivityproducts> ActProlist=gactProMapper.findProductsByGActid(gwork.getGactid());
				//产品 实际信息
				TiProducts pro=productMapper.selectByPrimaryKey(gwork.getProductid());
				//产品 款式信息
				TiProductstyles style=styleMapper.selectByPrimaryKey(gwork.getSttyleid());
				if(style!=null){
					Map<String, Object> result=new HashMap<String, Object>();
					result.put("defaultImg", style.getDefaultimg());
					result.put("productId", style.getProductid());
					result.put("styleId", style.getStyleid());
					result.put("title", pro.getTitle());
					result.put("propertystr", style.getDescription());
					result.put("originPrice",style.getPrice());
					if(ActProlist!=null&&ActProlist.size()>0){
						for (TiGroupactivityproducts pp : ActProlist) {
							if(pp.getProductid().longValue()==pro.getProductid().longValue()){
								result.put("price",pp.getPrice());
							}
						}
					}
					rq.setStatu(ReturnStatus.Success);
					rq.setBasemodle(result);
				}
			}else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("团购记录不存在！");
			}

		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	

	@Resource(name = "basePostMgtServiceImpl")
	private IBasePostMgtService postMgtService;

	@Autowired
	private OPayorderMapper payMapper;
	@Autowired
	private OOrderaddressMapper orderaddressMapper;
	/**
	 * 团购提交
	 * @param workId
	 * @param count
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/gSubmitOrder")
	public String gSubmitOrder(@RequestParam(required = false, defaultValue = "0")long workId,@RequestParam(required = false, defaultValue = "1")int count,@RequestParam(required = false, defaultValue = "0")long addressId)throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			// 团购作品
			TiGroupactivityworks gwork = gworkMapper.selectByPrimaryKey(workId);
			if (gwork != null&&gwork.getSttyleid()!=null) {
				if(gwork.getAddresstype()!=null){
					if((gwork.getAddresstype().intValue()==0&&(ObjectUtil.isEmpty(gwork.getName())||ObjectUtil.isEmpty(gwork.getMobilephone())))
					  )
					{
						rq.setStatu(ReturnStatus.ParamError);
						rq.setStatusreson("收货预留信息有误");
						return JsonUtil.objectToJsonStr(rq);
					}
					double totalPrice=0d;
					Double postAge=0d;
					if(addressId>0){
						gwork.setAddresstype(1);
						gwork.setAddressid(orderMgtService.addOrderAddressReturnId(addressId)); 
//						gworkMapper.updateByPrimaryKeySelective(gwork);
						postAge=postMgtService.getPostAge_ti(addressId, gwork.getProductid());
						if(postAge!=null&&postAge.doubleValue()>0){
							totalPrice+=postAge;
						} 
					}
					gwork.setPostage(postAge); 
					gwork.setSubmittime(new Date());
					gwork.setCount(count);
					//参与团购活动的产品列表
					List<TiGroupactivityproducts> ActProlist=gactProMapper.findProductsByGActid(gwork.getGactid());
					if(ActProlist!=null&&ActProlist.size()>0){
						TiProductstyles style=styleMapper.selectByPrimaryKey(gwork.getSttyleid());
						for (TiGroupactivityproducts pp : ActProlist) {
							if(pp.getProductid().longValue()==gwork.getProductid().longValue()){
								double priceSingle=pp.getPrice().doubleValue();
								gwork.setPrice(style.getPrice().doubleValue());
								gwork.setActprice(priceSingle);
								totalPrice+=priceSingle*count;
								gwork.setTotalprice(totalPrice);
								//如果
								if(totalPrice<0.01d){
									gwork.setPaytime(new Date());
									gwork.setStatus(Integer.parseInt(GroupActWorkStatus.payed.toString()));  
								}
								gworkMapper.updateByPrimaryKeySelective(gwork);
								
								//创建支付订单
								OPayorder payorder=new OPayorder();
								payorder.setUserid(user.getUserId()); 
								payorder.setPayid(GenUtils.getOrderNo(user.getUserId()));
								payorder.setCreatetime(new Date());
								payorder.setOrdertype(Integer.parseInt(PayOrderTypeEnum.ti_groupAct.toString()));
								payorder.setTotalprice(totalPrice);
								payorder.setUserorderid(String.valueOf(workId));//
								if(postAge!=null&&postAge.doubleValue()>0){
									payorder.setExtobject(gwork.getAddressid().toString());  
								}
								if(totalPrice<0.01d){
									payorder.setStatus(Integer.parseInt(OrderStatusEnum.payed.toString()));
									payMapper.insert(payorder);
									
									Map<String, Object> resultMap=new HashMap<String, Object>();
									resultMap.put("payId", payorder.getPayid());
									resultMap.put("payed", 1);
									rq.setStatu(ReturnStatus.Success);
									rq.setBasemodle(resultMap); 
								}else {
									payorder.setStatus(Integer.parseInt(OrderStatusEnum.noPay.toString()));
									payMapper.insert(payorder);
									Map<String, Object> resultMap=new HashMap<String, Object>();
									resultMap.put("payId", payorder.getPayid());
									rq.setStatu(ReturnStatus.Success);
									rq.setBasemodle(resultMap); 
								}
								
								return JsonUtil.objectToJsonStr(rq);
							}
						}
					}else {
						rq.setStatu(ReturnStatus.SystemError);
						rq.setStatusreson("找不到相应的产品");
					}
				}else {
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("没有选择收货方式！");
				}
			}else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("团购记录不存在！");
			}

		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	

	@Autowired
	private TiPromotersMapper tipromoterMapper;
	/**
	 * 省市区
	 */
	@Resource(name = "regionServiceImpl")
	private IRegionService regionService;
	/**
	 * 用户地址
	 */
	@Resource(name = "baseUserAddressServiceImpl")
	private IBaseUserAddressService addressService;
	/**
	 * 影楼地址
	 * @param workId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getPromoterAddress")
	public String getPromoterAddress(@RequestParam(required = false, defaultValue = "0")long workId,@RequestParam(required = false, defaultValue = "0") long addrid)throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			TiGroupactivityworks gwork = gworkMapper.selectByPrimaryKey(workId);
			if (gwork != null&&gwork.getSttyleid()!=null) {
				TiGroupactivity gact= gactMapper.selectByPrimaryKey(gwork.getGactid());
				if (gact != null&&!ObjectUtil.isEmpty(gact.getProvince())&&gact.getProvince().intValue()>0) {
					Map<String, Object> mapResult = new HashMap<String, Object>(); 
					//是否可以寄到自己地址
					if(gact.getAddresstype()==null||gact.getAddresstype().intValue()==Integer.parseInt(ActivityAddressType.auto.toString())
							||gact.getAddresstype().intValue()==Integer.parseInt(ActivityAddressType.customerAddr.toString())){
						mapResult.put("myAddress", addressService.getUserAddressResult(user.getUserId(),addrid));
					}
					if(gact.getAddresstype()!=null&&(gact.getAddresstype().intValue()==Integer.parseInt(ActivityAddressType.auto.toString())||
							gact.getAddresstype().intValue()==Integer.parseInt(ActivityAddressType.promoterAddr.toString()))){
						mapResult.put("promoterName", gact.getCompanyname());
						mapResult.put("promoterUserId", gact.getPromoteruserid());
						UUserAddressResult userAddressResult = new UUserAddressResult();
						userAddressResult.setProvince(gact.getProvince());
						userAddressResult.setCity(gact.getCity());
						userAddressResult.setArea(gact.getArea());
						userAddressResult.setProvinceName(regionService.getProvinceName(gact.getProvince()));
						userAddressResult.setCityName(regionService.getCityName(gact.getCity()));
						userAddressResult.setAreaName(regionService.getAresName(gact.getArea()));
						userAddressResult.setStreetdetail(gact.getStreetdetails());
						userAddressResult.setPhone(gact.getMobilephone()); 
						mapResult.put("promoterAddress", userAddressResult);
					}
					rq.setBasemodle(mapResult); 
				}
				rq.setStatu(ReturnStatus.Success);
			}
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 * 获取团购业务 收货地址信息
	 * @param workId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getGroupWorkAddress")
	public String getGroupWorkAddress(@RequestParam(required = false, defaultValue = "0")long workId)throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			TiGroupactivityworks gwork = gworkMapper.selectByPrimaryKey(workId);
			if(gwork!=null){
				Map<String, Object> resultMap=new HashMap<String, Object>();
				resultMap.put("addressType", gwork.getAddresstype());
				UUserAddressResult userAddressResult=new UUserAddressResult();
				//寄到自己的地址
				if(gwork.getAddresstype()!=null&&gwork.getAddresstype().intValue()==Integer.parseInt(AddressTypeEnum.cusaddr.toString())){
					OOrderaddress address= orderaddressMapper.selectByPrimaryKey(gwork.getAddressid());
					if(address!=null){
						userAddressResult.setProvince(address.getProvincecode());
						userAddressResult.setCity(address.getCitycode());
						userAddressResult.setArea(address.getDistrictcode());
						userAddressResult.setProvinceName(address.getProvince());
						userAddressResult.setCityName(address.getCity());
						userAddressResult.setAreaName(address.getDistrict());
						userAddressResult.setStreetdetail(address.getStreetdetail());
						userAddressResult.setPhone(address.getPhone()); 
						userAddressResult.setReciver(address.getReciver());
					}
					
				}else {
					TiGroupactivity gact= gactMapper.selectByPrimaryKey(gwork.getGactid());
					if(gact!=null){
						userAddressResult.setProvince(gact.getProvince());
						userAddressResult.setCity(gact.getCity());
						userAddressResult.setArea(gact.getArea());
						userAddressResult.setProvinceName(regionService.getProvinceName(gact.getProvince()));
						userAddressResult.setCityName(regionService.getCityName(gact.getCity()));
						userAddressResult.setAreaName(regionService.getAresName(gact.getArea()));
						userAddressResult.setStreetdetail(gact.getStreetdetails());
						userAddressResult.setPhone(gact.getMobilephone()); 
						userAddressResult.setReciver(gact.getReciver());
					} 
				}
				resultMap.put("address", userAddressResult);
				rq.setBasemodle(resultMap);
				rq.setStatu(ReturnStatus.Success); 
			}
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 生成作品编号
	 * @return
	 */
	private long getNewWorkId(){
		// 2 生成 作品id(cartId=workId)
		PMyproducts cart=new PMyproducts();
		cart.setCreatetime(new Date());
		cart.setUserid(0l);
		myproductMapper.insertReturnId(cart);
		return cart.getCartid();
	}
}
