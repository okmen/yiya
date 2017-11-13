package com.bbyiya.pic.web.calendar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jsqlparser.statement.select.Select;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.common.vo.ImageInfo;
import com.bbyiya.dao.OOrderproductphotosMapper;
import com.bbyiya.dao.OOrderproductsMapper;
import com.bbyiya.dao.OProducerordercountMapper;
import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.PStylecoordinateitemMapper;
import com.bbyiya.dao.TiActivityworksMapper;
import com.bbyiya.dao.TiAdvertimgsMapper;
import com.bbyiya.dao.TiMyartsdetailsMapper;
import com.bbyiya.dao.TiMyworksMapper;
import com.bbyiya.dao.TiProductsMapper;
import com.bbyiya.dao.TiProductstylesMapper;
import com.bbyiya.dao.TiProductstyleslayersMapper;
import com.bbyiya.dao.TiStyleadvertsMapper;
import com.bbyiya.dao.TiStylecoordinateMapper;
import com.bbyiya.enums.OrderTypeEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.OOrderproductphotos;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.model.OProducerordercount;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.PStylecoordinateitem;
import com.bbyiya.model.TiActivityworks;
import com.bbyiya.model.TiAdvertimgs;
import com.bbyiya.model.TiMyartsdetails;
import com.bbyiya.model.TiMyworks;
import com.bbyiya.model.TiProducts;
import com.bbyiya.model.TiProductstyles;
import com.bbyiya.model.TiStyleadverts;
import com.bbyiya.model.TiStylecoordinate;
import com.bbyiya.pic.utils.Json2Objects;
import com.bbyiya.utils.ImgDomainUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.calendar.product.TiStyleLayerResult;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/ti_style")
public class TiCoordinateController  extends SSOController{

	@Autowired
	private TiStylecoordinateMapper styleCoordMapper;
	@Autowired
	private PStylecoordinateitemMapper styleCoordItemMapper;

	@Autowired
	private TiProductstyleslayersMapper layerMapper;
	@Autowired
	private TiMyworksMapper workMapper;
	@Autowired
	private TiMyartsdetailsMapper detailMapper;

	@Autowired
	private TiProductstylesMapper styleMapper;
	@Autowired
	private TiActivityworksMapper actworkMapper;
	@Autowired
	private OOrderproductsMapper oproductMapper;
	@Autowired
	private OOrderproductphotosMapper ophotoMapper;
	@Autowired
	private OUserordersMapper userorderMapper;
	@Autowired
	private OProducerordercountMapper oproducerOrderCountMapper;

	@Autowired
	private TiAdvertimgsMapper advertMapper;
	@Autowired
	private TiStyleadvertsMapper styleAdevertMapper;
	/**
	 * P12 款式坐标
	 * @param styleId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getCoordinates")
	public String getCoordinates(@RequestParam(required = false, defaultValue = "0") long styleId,@RequestParam(required = false, defaultValue = "0") long workId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			TiMyworks myworks= workMapper.selectByPrimaryKey(workId);
			if(myworks!=null){
				List<TiMyartsdetails> detailsList= detailMapper.findDetailsByWorkId(workId);
				if(detailsList!=null&&detailsList.size()>0){
					rq=getStyleCoordResult(myworks.getStyleid(),detailsList);
				}
			}else if (styleId>0) {
				rq=getStyleCoordResult(styleId,null);
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	

//	oproducerModel= new OProducerordercount();
//	oproducerModel.setUserorderid(userOrderId);
//	oproducerModel.setUserid(userorders.getUserid());
//	oproducerModel.setProduceruserid(userorders.getProduceruserid());
//	Integer indexCount=oproducerOrderCountMapper.getMaxOrderIndexByProducerIdAndUserId(userorders.getProduceruserid(),userorders.getUserid());
//	int orderIndex=indexCount==null?1:(indexCount+1);
//	oproducerModel.setOrderindex(orderIndex);
//	if(userorders.getOrdertype()!=null&&userorders.getOrdertype()==Integer.parseInt(OrderTypeEnum.ti_branchOrder.toString())){
//		TiActivityworks actwork= actworkMapper.selectByPrimaryKey(workId);
//		if(actwork!=null&&actwork.getOrderaddressid()!=null&&actwork.getOrderaddressid().longValue()>0){
//			oproducerModel.setPrintindex(orderIndex+"A"); 
//		}else {
//			oproducerModel.setPrintindex(String.valueOf(orderIndex));
//		}
//	}else {
//		oproducerModel.setPrintindex(orderIndex+"A");
//	}
//	oproducerOrderCountMapper.insert(oproducerModel);
	
	/**
	 * 订单图片
	 * @param userOrderId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getOrderPhotoCoord")
	public String getCoordinates(String userOrderId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			OUserorders userorders=userorderMapper.selectByPrimaryKey(userOrderId);
			List<OOrderproducts> oproductlist=oproductMapper.findOProductsByOrderId(userOrderId);
			if(oproductlist!=null&&oproductlist.size()>0&&userorders!=null&&userorders.getProduceruserid()!=null) {
				TiProducts products=productsMapper.selectByPrimaryKey(oproductlist.get(0).getProductid());
				long workId=oproductlist.get(0).getCartid();
				//订单生产商打印序列表
				OProducerordercount oproducerModel=oproducerOrderCountMapper.selectByPrimaryKey(userOrderId);
				if(oproducerModel==null){
					insertOProducerordercount(oproducerModel,userorders,workId);
				}
				//订单图片
				List<OOrderproductphotos> photoList=ophotoMapper.findOrderProductPhotosByProductOrderId(oproductlist.get(0).getOrderproductid());
				if(photoList!=null&&photoList.size()>0){
					//订单款式styleId（目前每单只有一个产品）
					long styleId=oproductlist.get(0).getStyleid();
					TiProductstyles style= styleMapper.selectByPrimaryKey(styleId);
					if(style==null||style.getImgcount()<photoList.size()||photoList.size()<=0){
						rq.setStatusreson("作品数量有误");
						return JsonUtil.objectToJsonStr(rq);
					}
					//款式各个图层的坐标信息model
					TiStylecoordinate stylecoordinate = styleCoordMapper.selectByPrimaryKey(styleId);
					//款式模板背景列表
					List<TiStyleLayerResult> layerList=layerMapper.findLayerlistByStyleId(styleId);
					if(style!=null&&stylecoordinate!=null&&layerList!=null&&layerList.size()>0){
						Map<String, Object> map = new HashMap<String, Object>();
						//打印号坐标
						PStylecoordinateitem print_no = styleCoordItemMapper.selectByPrimaryKey(stylecoordinate.getPrintcoordid().longValue());
						//打印号 的字号大小
						switch (products.getCateid()) {
						case 1:
						case 2:
							print_no.setWordSize(8);
							break;
						default:
							print_no.setWordSize(6);
							break;
						}
						//内页图片坐标
						PStylecoordinateitem in_pic = styleCoordItemMapper.selectByPrimaryKey(stylecoordinate.getImgcoordid().longValue());
						//封面图片坐标
						PStylecoordinateitem front_pic = styleCoordItemMapper.selectByPrimaryKey(stylecoordinate.getFrontimgcoordid().longValue());
						//影楼广告图列表
						List<ImageInfo> advertImglist=null;
						//是否有广告位
						if(products.getAdvertcount()!=null&&products.getAdvertcount().intValue()>0){
							 if(userorders.getBranchuserid()!=null&&userorders.getBranchuserid()>0){
								 TiAdvertimgs advertlist= advertMapper.getAdvertByProductIdAndPromoterId(products.getProductid(),userorders.getBranchuserid() );
								 if(advertlist!=null&&!ObjectUtil.isEmpty(advertlist.getAdvertimgjson())){
									 advertImglist=Json2Objects.getImageInfosList(advertlist.getAdvertimgjson());
								 }
							 }
						}
						//
						for(int i=0;i<layerList.size();i++){
							if (layerList.get(i).getIsround() != null && layerList.get(i).getIsround() == 1) {// 如果是圆形图（宽高比为																											// 1:1）
								layerList.get(i).setWidthhight(1d);
							}
							// 图片的坐标位置model
							if (i == 0) {
								layerList.get(i).setImgCoordMod(front_pic);
								double widthhight = (style.getWidth() * front_pic.getPointwidth()) / (style.getHight() * front_pic.getPointhight());
								layerList.get(i).setWidthhight(widthhight);
							} else {
								layerList.get(i).setImgCoordMod(in_pic);
								double widthhight = (style.getWidth() * in_pic.getPointwidth()) / (style.getHight() * in_pic.getPointhight());
								layerList.get(i).setWidthhight(widthhight);
							}
							
							
							//是否当前页面上有嵌套广告
							if(advertImglist!=null&&advertImglist.size()>0&&layerList.get(i).getAdvertcoordid()!=null&&layerList.get(i).getAdvertcoordid().intValue()>0){
								PStylecoordinateitem advertMod = styleCoordItemMapper.selectByPrimaryKey(layerList.get(i).getAdvertcoordid().longValue());
								layerList.get(i).setAdImg(advertImglist.get(0).getUrl()); 
								layerList.get(i).setAdvertCoordMod(advertMod); 
								layerList.get(i).setHaveAdvert(1); 
							}
							if(photoList.size()>i){
								layerList.get(i).setWorkImgUrl(ImgDomainUtil.getImageUrl_Full(photoList.get(i).getImgurl()));  
								//---打印号---
								layerList.get(i).setPrintNo(getPrintNu(workId, userorders.getUserid(), oproducerModel.getPrintindex(), i+1));
								layerList.get(i).setPrintNo(workId+"-"+(i+1)+"-"+userorders.getUserid()+"-"+oproducerModel.getPrintindex()); 
							}
						}
						//如果是台历，单独广告页
						if(products.getAdvertcount()!=null&&products.getCateid()!=null&&products.getCateid().intValue()==3) {
							 //是否有广告位
							 TiStyleadverts styleadverts= styleAdevertMapper.selectByPrimaryKey(styleId);
							 if(styleadverts!=null) {
								 //产品广告页列表
								 List<TiStyleLayerResult> adverlist=new ArrayList<TiStyleLayerResult>();
								 //根据产品广告页的数量
								 for(int i=0;i<products.getAdvertcount();i++){
									 TiStyleLayerResult advertMap=new TiStyleLayerResult();
									 advertMap.setIsAdvert(1);
									 if(advertImglist==null||advertImglist.size()<=i||advertImglist.get(i)==null||ObjectUtil.isEmpty(advertImglist.get(i).getUrl())){ 
										 advertMap.setHaveAdvert(0);
										 advertMap.setBackImg(styleadverts.getBlankimg()); 
									 }else{
										 advertMap.setHaveAdvert(1); 
										 advertMap.setAdImg(advertImglist.get(i).getUrl());
										 advertMap.setImgCoordMod(styleCoordItemMapper.selectByPrimaryKey(styleadverts.getImgcoordid().longValue()));
										 advertMap.setBackImg(styleadverts.getBackimg());  
									 }
									 adverlist.add(advertMap);
								 }
								 //最终打印图片列表
								 List<TiStyleLayerResult> resultslist=new ArrayList<TiStyleLayerResult>();
								 int index=1;
								 for (TiStyleLayerResult layer : layerList) {
									layer.setPrintNo(getPrintNu(workId, userorders.getUserid(), oproducerModel.getPrintindex(), index));
									resultslist.add(layer);
									if(index==1){//第一页
										//第一张影楼广告放在 首页的背面（也就是 第二页）
										if(adverlist!=null&&adverlist.size()>0){
											index++;
											adverlist.get(0).setPrintNo( getPrintNu(workId, userorders.getUserid(), oproducerModel.getPrintindex(), index));
											resultslist.add(adverlist.get(0));   
										}
									}
									index++;
								 }
								 if(adverlist.size()>1){
									 //排除第一张广告
									 for (int j=1;j<adverlist.size(); j++) {
										 adverlist.get(j).setPrintNo( getPrintNu(workId, userorders.getUserid(), oproducerModel.getPrintindex(), index));
										 resultslist.add(adverlist.get(j)); 
										 index++;
									}
								 }
//								 for(int i=(adverlist.size()-2);i>=0;i--){ 
//									 adverlist.get(i).setPrintNo( getPrintNu(workId, userorders.getUserid(), oproducerModel.getPrintindex(), index));
//									 resultslist.add(adverlist.get(i));  
//									 index++;
//								 } 
								 map.put("imgLayList", resultslist);
							 }else {
								 map.put("imgLayList", layerList);
							 }
							
						}else {
							map.put("imgLayList", layerList);
						}
						map.put("print_Mod", print_no);
						rq.setBasemodle(map);
						rq.setStatu(ReturnStatus.Success);
						return JsonUtil.objectToJsonStr(rq);
					}
				}
			}
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("参数有误userorder:"+userOrderId);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	private OProducerordercount  insertOProducerordercount(OProducerordercount oproducerModel,OUserorders userorders,long workId){
		if(oproducerModel==null&&userorders!=null){
			oproducerModel= new OProducerordercount();
			oproducerModel.setUserorderid(userorders.getUserorderid());
			oproducerModel.setUserid(userorders.getUserid());
			oproducerModel.setProduceruserid(userorders.getProduceruserid());
			Integer indexCount=oproducerOrderCountMapper.getMaxOrderIndexByProducerIdAndUserId(userorders.getProduceruserid(),userorders.getUserid());
			int orderIndex=indexCount==null?1:(indexCount+1);
			oproducerModel.setOrderindex(orderIndex);
			if(userorders.getOrdertype()!=null&&userorders.getOrdertype()==Integer.parseInt(OrderTypeEnum.ti_branchOrder.toString())){
				TiActivityworks actwork= actworkMapper.selectByPrimaryKey(workId);
				if(actwork!=null&&actwork.getOrderaddressid()!=null&&actwork.getOrderaddressid().longValue()>0){
					oproducerModel.setPrintindex(orderIndex+"A"); 
				}else {
					oproducerModel.setPrintindex(String.valueOf(orderIndex));
				}
			}else {
				oproducerModel.setPrintindex(orderIndex+"A");
			}
			oproducerOrderCountMapper.insert(oproducerModel);
		}
		return oproducerModel;
	}
	
	/**
	 * 打印号
	 * @param workId
	 * @param userId
	 * @param orderIndex
	 * @param index
	 * @return
	 */
	private String getPrintNu(long workId,long userId,String orderIndex,int index){
		return workId+"-"+userId+"-"+orderIndex+"-"+index; 
	}
	
	
	@Autowired
	private TiProductsMapper productsMapper;
	
	public ReturnModel getStyleCoordResult(Long styleId,List<TiMyartsdetails> details) {
		ReturnModel rq = new ReturnModel();
		TiProductstyles style= styleMapper.selectByPrimaryKey(styleId);
		if(style==null){
			rq.setStatusreson("产品不存在");
			return rq;
		}
		if(details!=null){
			if(style.getImgcount()<details.size()||details.size()<=0){
				rq.setStatusreson("作品数量有误");
				return rq;
			}
		}
		TiProducts products=productsMapper.selectByPrimaryKey(style.getProductid());
		TiStylecoordinate stylecoordinate = styleCoordMapper.selectByPrimaryKey(styleId);
		List<TiStyleLayerResult> layerList=layerMapper.findLayerlistByStyleId(styleId);
		if(style!=null&&stylecoordinate!=null&&layerList!=null&&layerList.size()>0){
			
			Map<String, Object> map = new HashMap<String, Object>();
			//内页图片坐标
			PStylecoordinateitem in_pic = styleCoordItemMapper.selectByPrimaryKey(stylecoordinate.getImgcoordid().longValue());
			//封面图片坐标
			PStylecoordinateitem front_pic = styleCoordItemMapper.selectByPrimaryKey(stylecoordinate.getFrontimgcoordid().longValue());
			
			for(int i=0;i<layerList.size();i++){
				if (layerList.get(i).getIsround() != null && layerList.get(i).getIsround() == 1) {// 如果是圆形图（宽高比为																											// 1:1）
					layerList.get(i).setWidthhight(1d);
				}
				// 图片的坐标位置model
				if (i == 0) {
					layerList.get(i).setImgCoordMod(front_pic);
					double widthhight = (style.getWidth() * front_pic.getPointwidth()) / (style.getHight() * front_pic.getPointhight());
					layerList.get(i).setWidthhight(widthhight);
				} else {
					layerList.get(i).setImgCoordMod(in_pic);
					double widthhight = (style.getWidth() * in_pic.getPointwidth()) / (style.getHight() * in_pic.getPointhight());
					layerList.get(i).setWidthhight(widthhight);
				}
				if(details!=null&&details.size()>0&&details.size()>i){
					layerList.get(i).setWorkImgUrl(ImgDomainUtil.getImageUrl_Full(details.get(i).getImageurl())); 
				}
			}
			map.put("imgLayList", layerList);
			map.put("cateId", products.getCateid()); 
			rq.setBasemodle(map);
			rq.setStatu(ReturnStatus.Success);
		}
		rq.setStatu(ReturnStatus.Success);
		return rq;
	}
	
	
}
