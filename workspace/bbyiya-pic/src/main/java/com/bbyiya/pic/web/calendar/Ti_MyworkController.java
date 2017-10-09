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
import com.bbyiya.enums.OrderStatusEnum;
import com.bbyiya.enums.OrderTypeEnum;
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
import com.bbyiya.pic.vo.calendar.MyworkDetailsParam;
import com.bbyiya.utils.ImgDomainUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
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
				work.setStatus(Integer.parseInt(ActivityWorksStatusEnum.imagesubmit.toString()));
				activityworkMapper.updateByPrimaryKey(work);
				
				TiMyworks myworks=workMapper.selectByPrimaryKey(workId);
				if(myworks!=null){
					myworks.setCompletetime(new Date());
					workMapper.updateByPrimaryKeySelective(myworks);
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
	@Autowired
	private TiProductsMapper productMapper;
	/**
	 * 作品详情
	 * @param workId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/workInfo")
	public String workInfo(long workId)throws Exception {
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
						map.put("title", style.getDescription()); 
						map.put("cateId", products.getCateid());
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
					if(actInfo!=null){
						Map<String, Object> map=new HashMap<String, Object>();
						map.put("needCount", actInfo.getExtcount());
						map.put("extCount", myactInfo.getExtcount());
						map.put("userId", myactInfo.getUserid());
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
	private EErrorsMapper logMapper;
	public void addlog(String msg) {
		EErrors errors = new EErrors();
		errors.setClassname(this.getClass().getName());
		errors.setMsg("下单失败：" + msg);
		errors.setCreatetime(new Date());
		logMapper.insert(errors);
	}
}
