package com.bbyiya.pic.web.calendar;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.bbyiya.pic.vo.calendar.MyworkDetailsParam;
import com.bbyiya.utils.ImgDomainUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/ti_redpacket")
public class Ti_ActRedpakgeController extends SSOController {
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
		if(user!=null) {
			MyworkDetailsParam param=null;
			try{
				param=(MyworkDetailsParam)JsonUtil.jsonStrToObject(detailJson, MyworkDetailsParam.class);
			}catch(Exception e){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数错误！");
				return JsonUtil.objectToJsonStr(rq);
			}
			
			if(param!=null&&param.getWorkId()!=null&&param.getDetails()!=null&&param.getDetails().size()>0) {
				TiMyworks myworks= workMapper.selectByPrimaryKey(param.getWorkId());
				if(myworks!=null) {
					if(myworks.getUserid()!=null&&myworks.getUserid().longValue()==user.getUserId().longValue()){
						TiProducts product=productMapper.selectByPrimaryKey(myworks.getProductid());
						if(product!=null) {
							myworks.setStyleid(param.getStyleId()); 
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
									String title=param.getDetails().get(i).getTitle();
									String content=param.getDetails().get(i).getContent();
									if(!ObjectUtil.isEmpty(url)||(!ObjectUtil.isEmpty(title)&&!ObjectUtil.isEmpty(content))){
										url=ImgDomainUtil.getImageUrl_Full(url);
										TiMyartsdetails detail=new TiMyartsdetails();
										detail.setWorkid(param.getWorkId());
										detail.setTitle(title);
										detail.setContent(content); 
										detail.setImageurl(url);
										detail.setSort(param.getDetails().get(i).getSort()==null?i:param.getDetails().get(i).getSort());
										detail.setCreatetime(time); 
										detailMapper.insert(detail);
									}
								}
								//完成上传操作(提交操作)
								if(isOk>0&&style.getImgcount().intValue()==param.getDetails().size()) {
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
									
									//已经下单的作品
									OOrderproducts oproduct= oproductOrderMapper.getOProductsByWorkId(param.getWorkId());
									if(oproduct!=null) {
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
													detail.setContent(param.getDetails().get(i).getContent()); 
													detail.setTitle(param.getDetails().get(i).getTitle()); 
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
							} else {
								rq.setStatu(ReturnStatus.SystemError);
								rq.setStatusreson("内容不能超过"+style.getImgcount()+"张！"); 
							}
						}
						
					}else {
						rq.setStatu(ReturnStatus.SystemError);
						rq.setStatusreson("非本人操作！"); 
					}
					
				}else {
					rq.setStatu(ReturnStatus.SystemError);
					rq.setStatusreson("作品不存在！"); 
				}
			}else{
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数不全");
				return JsonUtil.objectToJsonStr(rq);
			}
		}else { 
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	
	
}
