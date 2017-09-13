package com.bbyiya.pic.web.calendar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.bbyiya.model.TiStylecoordinate;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.calendar.product.TiStyleLayerResult;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/ti_style")
public class TiCoordinateController  extends SSOController{

//	@Autowired
//	private PStylecoordinateMapper styleCoordMapper;
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
	

	@Autowired
	private TiAdvertimgsMapper advertMapper;
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
				//订单生产商打印
				OProducerordercount oproducerModel=oproducerOrderCountMapper.selectByPrimaryKey(userOrderId);
				if(oproducerModel==null){
					oproducerModel= new OProducerordercount();
					oproducerModel.setUserorderid(userOrderId);
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
				}
				List<OOrderproductphotos> photoList=ophotoMapper.findOrderProductPhotosByProductOrderId(oproductlist.get(0).getOrderproductid());
				if(photoList!=null&&photoList.size()>0){
					long styleId=oproductlist.get(0).getStyleid();
					TiProductstyles style= styleMapper.selectByPrimaryKey(styleId);
					if(style==null||style.getImgcount()<photoList.size()||photoList.size()<=0){
						rq.setStatusreson("作品数量有误");
						return JsonUtil.objectToJsonStr(rq);
					}
					TiStylecoordinate stylecoordinate = styleCoordMapper.selectByPrimaryKey(styleId);
					List<TiStyleLayerResult> layerList=layerMapper.findLayerlistByStyleId(styleId);
					if(style!=null&&stylecoordinate!=null&&layerList!=null&&layerList.size()>0){
						
						Map<String, Object> map = new HashMap<String, Object>();
						//打印号坐标
						PStylecoordinateitem print_no = styleCoordItemMapper.selectByPrimaryKey(stylecoordinate.getPrintcoordid().longValue());
						//内页图片坐标
						PStylecoordinateitem in_pic = styleCoordItemMapper.selectByPrimaryKey(stylecoordinate.getImgcoordid().longValue());
						//封面图片坐标
						PStylecoordinateitem front_pic = styleCoordItemMapper.selectByPrimaryKey(stylecoordinate.getFrontimgcoordid().longValue());
						
						for(int i=0;i<layerList.size();i++){
							if(i==0){
								layerList.get(i).setImgCoordMod(front_pic);
								if(products.getCateid()==1||products.getCateid()==2||products.getCateid()==3){
									layerList.get(i).setWidthhight(1d);
									layerList.get(i).setIsround(1);
								}else {
									double widthhight= (style.getWidth()*front_pic.getPointwidth())/(style.getHight()*front_pic.getPointhight());
									layerList.get(i).setWidthhight(widthhight); 
								} 
							}else {
								layerList.get(i).setImgCoordMod(in_pic);
								double widthhight= (style.getWidth()*in_pic.getPointwidth())/(style.getHight()*in_pic.getPointhight());
								layerList.get(i).setWidthhight(widthhight); 
							}
							if(photoList.size()>i){
								layerList.get(i).setWorkImgUrl(photoList.get(i).getImgurl()); 
								//---打印号---
								layerList.get(i).setPrintNo(workId+"-"+userorders.getUserid()+"-"+(i+1)+"-"+oproducerModel.getPrintindex()); 
							}
						}
						//是否有广告位
						if(products.getAdvertcount()!=null&&products.getAdvertcount().intValue()>0){
							 List<TiStyleLayerResult> adverlist=new ArrayList<TiStyleLayerResult>();
							 List<ImageInfo> imglist=null;
							 if(userorders.getBranchuserid()!=null&&userorders.getBranchuserid()>0){
								 TiAdvertimgs advertlist= advertMapper.getAdvertByProductIdAndPromoterId(products.getProductid(),userorders.getBranchuserid() );
								 if(advertlist!=null&&!ObjectUtil.isEmpty(advertlist.getAdvertimgjson())){
									 imglist=(List<ImageInfo>)JsonUtil.jsonToList(advertlist.getAdvertimgjson());
								 }
							 }
							 for(int i=0;i<products.getAdvertcount();i++){
								 TiStyleLayerResult advertMap=new TiStyleLayerResult();
								 advertMap.setIsAdvert(1);
								 if(imglist==null||imglist.get(i)==null){
									 advertMap.setHaveAdvert(0);
									 advertMap.setBackImg("http://document.bbyiya.com/tiadvert-p2401-0911.jpg"); 
								 }else {
									 advertMap.setHaveAdvert(1); 
									 advertMap.setAdImg(imglist.get(i).getUrl());
									 advertMap.setImgCoordMod(styleCoordItemMapper.selectByPrimaryKey(29l));
									 advertMap.setBackImg("http://document.bbyiya.com/tiadvert-back-p2401-0911.png"); 
								 }
//								 advertMap.set("printNo", workId+"-"+userorders.getUserid()+"-"+(layerList.size()+i+1)+"-"+oproducerModel.getPrintindex());
								 adverlist.add(advertMap);
							 }
//							 map.put("advertList", adverlist); 
							 //最终打印图片列表
							 List<TiStyleLayerResult> resultslist=new ArrayList<TiStyleLayerResult>();
							 int index=1;
							 for (TiStyleLayerResult layer : layerList) {
								layer.setPrintNo(workId+"-"+userorders.getUserid()+"-"+(index)+"-"+oproducerModel.getPrintindex()); 
								resultslist.add(layer);
								if(index==1){
									if(adverlist!=null&&adverlist.get(adverlist.size()-1)!=null){
										index++;
										adverlist.get(adverlist.size()-1).setPrintNo(workId+"-"+userorders.getUserid()+"-"+(index)+"-"+oproducerModel.getPrintindex());
										resultslist.add(adverlist.get(adverlist.size()-1));  
									}
								}
								index++;
							 }
							 for(int i=(adverlist.size()-2);i>=0;i--){ 
								 adverlist.get(i).setPrintNo(workId+"-"+userorders.getUserid()+"-"+(index)+"-"+oproducerModel.getPrintindex());
								 resultslist.add(adverlist.get(i));  
								 index++;
							 }
							 map.put("imgLayList", resultslist);
						}else {
							map.put("imgLayList", layerList);
						}
//						map.put("imgLayList", layerList);
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
			//打印号坐标
//			PStylecoordinateitem print_no = styleCoordItemMapper.selectByPrimaryKey(stylecoordinate.getPrintcoordid().longValue());
			//内页图片坐标
			PStylecoordinateitem in_pic = styleCoordItemMapper.selectByPrimaryKey(stylecoordinate.getImgcoordid().longValue());
			//封面图片坐标
			PStylecoordinateitem front_pic = styleCoordItemMapper.selectByPrimaryKey(stylecoordinate.getFrontimgcoordid().longValue());
			
			for(int i=0;i<layerList.size();i++){
				if(i==0){
					layerList.get(i).setImgCoordMod(front_pic);
					if(products.getCateid()==1||products.getCateid()==2||products.getCateid()==3){
						layerList.get(i).setWidthhight(1d);
						layerList.get(i).setIsround(1);
					}else {
						double widthhight= (style.getWidth()*front_pic.getPointwidth())/(style.getHight()*front_pic.getPointhight());
						layerList.get(i).setWidthhight(widthhight); 
					} 
				}else {
					layerList.get(i).setImgCoordMod(in_pic);
					double widthhight= (style.getWidth()*in_pic.getPointwidth())/(style.getHight()*in_pic.getPointhight());
					layerList.get(i).setWidthhight(widthhight); 
				}
				if(details!=null&&details.size()>0&&details.size()>i){
					layerList.get(i).setWorkImgUrl(details.get(i).getImageurl()); 
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
