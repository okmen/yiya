package com.bbyiya.pic.web.calendar;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.OOrderproductsMapper;
import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.TiActivityworksMapper;
import com.bbyiya.dao.TiMyartsdetailsMapper;
import com.bbyiya.dao.TiMyworksMapper;
import com.bbyiya.dao.TiProductstylesMapper;
import com.bbyiya.enums.OrderStatusEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.TiActivityworks;
import com.bbyiya.model.TiMyartsdetails;
import com.bbyiya.model.TiMyworks;
import com.bbyiya.model.TiProductstyles;
import com.bbyiya.pic.vo.calendar.MyworkDetailsParam;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
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
	
	/**
	 * 参与活动 -图片上传
	 * @param detailJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/editMyWork")
	public String editMyWork(String detailJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			MyworkDetailsParam param=(MyworkDetailsParam)JsonUtil.jsonStrToObject(detailJson, MyworkDetailsParam.class);
			if(param!=null&&param.getWorkId()!=null&&param.getDetails()!=null&&param.getDetails().size()>0){
				TiMyworks myworks= workMapper.selectByPrimaryKey(param.getWorkId());
				if(myworks!=null){
					TiProductstyles style=styleMapper.selectByPrimaryKey(myworks.getStyleid()==null?myworks.getProductid():myworks.getStyleid());
					if(style!=null&&style.getImgcount().intValue()<=param.getDetails().size()){
						Map<String, Object> mapResult=new HashMap<String, Object>();
						Date time=new Date();
						for(int i=0;i<param.getDetails().size();i++){
							TiMyartsdetails detail=new TiMyartsdetails();
							detail.setWorkid(param.getWorkId());
							detail.setImageurl(param.getDetails().get(0).getImageurl());
							detail.setSort(i);
							detail.setCreatetime(time); 
							detailMapper.insert(detail);
							i++;
						}
						//图片上传时间
						OOrderproducts oproduct= oproductOrderMapper.getOProductsByWorkId(param.getWorkId());
						if(oproduct!=null){
							OUserorders userorders= userOrderMapper.selectByPrimaryKey(oproduct.getUserorderid());
							if(userorders!=null&&userorders.getStatus().intValue()==Integer.parseInt(OrderStatusEnum.payed.toString())){
								userorders.setStatus(Integer.parseInt(OrderStatusEnum.waitFoSend.toString()));
								userorders.setUploadtime(new Date()); 
								userOrderMapper.updateByPrimaryKeySelective(userorders);
								mapResult.put("ordered", 1);
								mapResult.put("userOrderId", userorders.getUserorderid());
							}
						}
						rq.setBasemodle(mapResult); 
						rq.setStatu(ReturnStatus.Success);
						rq.setStatusreson("提交成功"); 
					}else {
						rq.setStatu(ReturnStatus.SystemError);
						rq.setStatusreson("图片不足"+style.getImgcount()+"张！"); 
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
			if(ObjectUtil.isEmpty(reciever)||ObjectUtil.isMobile(phone)){
				rq.setStatusreson("姓名不能为空/手机号不正确");
				return JsonUtil.objectToJsonStr(rq);
			}
			TiActivityworks work=activityworkMapper.selectByPrimaryKey(workId);
			if(work!=null){
				work.setReciever(reciever);
				work.setMobiephone(phone);
				activityworkMapper.updateByPrimaryKey(work);
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
	public String workInfo(long workId)throws Exception {
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			TiMyworks myworks= workMapper.selectByPrimaryKey(workId);
			if(myworks!=null){
				TiProductstyles style= styleMapper.selectByPrimaryKey(myworks.getStyleid()==null?myworks.getProductid():myworks.getStyleid());
				if(style!=null){
					List<TiMyartsdetails> details= detailMapper.findDetailsByWorkId(workId);
					Map<String, Object> map=new HashMap<String, Object>();
					map.put("details", details);
					map.put("imgCount", style.getImgcount()); 
					rq.setStatu(ReturnStatus.Success);
					rq.setBasemodle(map); 
				}
			}
		}else { 
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
//	/**
//	 * 订单图片上传
//	 * @param detailJson
//	 * @return
//	 * @throws Exception
//	 */
//	@ResponseBody
//	@RequestMapping(value = "/uploadImgsForOrder")
//	public String uploadImgsForOrder(String detailJson) throws Exception {
//		ReturnModel rq=new ReturnModel();
//		LoginSuccessResult user= super.getLoginUser();
//		if(user!=null){
//			MyworkDetailsParam param=(MyworkDetailsParam)JsonUtil.jsonStrToObject(detailJson, MyworkDetailsParam.class);
//			if(param!=null&&!ObjectUtil.isEmpty(param.getOrderproductId())&&param.getDetails()!=null&&param.getDetails().size()>0){
//				//图片上传时间
//				OOrderproducts oproduct= oproductOrderMapper.selectByPrimaryKey(param.getOrderproductId());
//				if(oproduct!=null&&oproduct.getCartid()!=null){
//					//作品id
//					TiMyworks myworks= workMapper.selectByPrimaryKey(oproduct.getCartid());
//					if(myworks!=null){
//						TiProductstyles style=styleMapper.selectByPrimaryKey(myworks.getStyleid()==null?myworks.getProductid():myworks.getStyleid());
//						if(style!=null&&style.getImgcount().intValue()<=param.getDetails().size()){
//							Date time=new Date();
//							for(int i=0;i<param.getDetails().size();i++){
//								TiMyartsdetails detail=new TiMyartsdetails();
//								detail.setWorkid(myworks.getWorkid());
//								detail.setImageurl(param.getDetails().get(0).getImageurl());
//								detail.setSort(i);
//								detail.setCreatetime(time); 
//								detailMapper.insert(detail);
//								i++;
//							}
//							
//							OUserorders userorders = userOrderMapper.selectByPrimaryKey(oproduct.getUserorderid());
//							if (userorders != null && userorders.getStatus().intValue() == Integer.parseInt(OrderStatusEnum.payed.toString())) {
//								userorders.setStatus(Integer.parseInt(OrderStatusEnum.waitFoSend.toString()));
//								userorders.setUploadtime(new Date());
//								userOrderMapper.updateByPrimaryKeySelective(userorders);
//							}
//							rq.setStatu(ReturnStatus.Success);
//							rq.setStatusreson("提交成功"); 
//						}else {
//							rq.setStatu(ReturnStatus.SystemError);
//							rq.setStatusreson("图片不足"+style.getImgcount()+"张！"); 
//						}
//					}
//				}
//				
//			}
//		}else { 
//			rq.setStatu(ReturnStatus.LoginError);
//			rq.setStatusreson("登录过期");
//			return JsonUtil.objectToJsonStr(rq);
//		}
//		return JsonUtil.objectToJsonStr(rq);
//	}
}
