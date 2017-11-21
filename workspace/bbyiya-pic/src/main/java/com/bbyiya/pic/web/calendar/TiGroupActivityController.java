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
import com.bbyiya.dao.OPayorderMapper;
import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.TiGroupactivityMapper;
import com.bbyiya.dao.TiGroupactivityproductsMapper;
import com.bbyiya.dao.TiGroupactivityworksMapper;
import com.bbyiya.dao.TiMyartsdetailsMapper;
import com.bbyiya.dao.TiMyworksMapper;
import com.bbyiya.dao.TiProductsMapper;
import com.bbyiya.dao.TiProductstylesMapper;
import com.bbyiya.dao.TiPromotersMapper;
import com.bbyiya.enums.OrderStatusEnum;
import com.bbyiya.enums.PayOrderTypeEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.calendar.GroupActWorkStatus;
import com.bbyiya.model.OPayorder;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.TiGroupactivity;
import com.bbyiya.model.TiGroupactivityproducts;
import com.bbyiya.model.TiGroupactivityworks;
import com.bbyiya.model.TiMyartsdetails;
import com.bbyiya.model.TiMyworks;
import com.bbyiya.model.TiProducts;
import com.bbyiya.model.TiProductstyles;
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
		Map<String, Object> result=new HashMap<String, Object>();
		TiGroupactivity actInfo= gactMapper.selectByPrimaryKey(groupId);
		if(actInfo!=null){
			int browsecount=actInfo.getBrowsecount()==null?1:(actInfo.getBrowsecount().intValue()+1);
			actInfo.setBrowsecount(browsecount);
			gactMapper.updateByPrimaryKeySelective(actInfo);
		}
		result.put("gActInfo", actInfo);
		List<TiGroupactivityproducts> gprolist=gactProMapper.findProductsByGActid(groupId);
		if(gprolist!=null&&gprolist.size()>0){
			List<Long> proIds=new ArrayList<Long>();
			for (TiGroupactivityproducts tt : gprolist) {
				proIds.add(tt.getProductid());
			}
			List<TiProducts> proList=productMapper.findProductlistByProductIds(proIds);
			if(proList!=null&&proList.size()>0){
				for (TiGroupactivityproducts pro : gprolist) {
					for (TiProducts pp : proList) {
						if(pro.getProductid().longValue()==pp.getProductid().longValue()){
							pro.setTitle(pp.getTitle());
							pro.setOriginPrice(pp.getPrice());
							pro.setDefaultimg(pp.getDefaultimg());
						}
					}
				}
			}
			result.put("prolist", gprolist);
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
			long workId = getNewWorkId();
			TiGroupactivityworks gwork = new TiGroupactivityworks();
			gwork.setWorkid(workId);
			gwork.setUserid(user.getUserId());
			gwork.setGactid(groupId);
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
					Map<String, Object> map=new HashMap<String, Object>();
					map.put("gwork", gwork);
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
					rq.setBasemodle(map);
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
					if((gwork.getAddresstype().intValue()==0&&!(ObjectUtil.isEmpty(gwork.getName())||ObjectUtil.isEmpty(gwork.getMobilephone())))
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
								payorder.setStatus(Integer.parseInt(OrderStatusEnum.noPay.toString()));
								payMapper.insert(payorder);
								Map<String, Object> resultMap=new HashMap<String, Object>();
								resultMap.put("payId", payorder.getPayid());
								rq.setStatu(ReturnStatus.Success);
								rq.setBasemodle(resultMap); 
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
//				TiPromoters promoters = tipromoterMapper.selectByPrimaryKey(gact.getPromoteruserid());
				if (gact != null) {
					Map<String, Object> mapResult = new HashMap<String, Object>();
					mapResult.put("myAddress", addressService.getUserAddressResult(user.getUserId(),addrid));
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
					rq.setStatu(ReturnStatus.Success);
					rq.setBasemodle(mapResult); 
				}
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
