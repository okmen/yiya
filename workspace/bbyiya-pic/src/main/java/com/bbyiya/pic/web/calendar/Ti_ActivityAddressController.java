package com.bbyiya.pic.web.calendar;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.OOrderproductsMapper;
import com.bbyiya.dao.TiActivitysMapper;
import com.bbyiya.dao.TiActivityworksMapper;
import com.bbyiya.dao.TiMyworksMapper;
import com.bbyiya.dao.TiProductsMapper;
import com.bbyiya.dao.TiPromotersMapper;
import com.bbyiya.dao.TiUserdiscountsMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.calendar.ActivityAddressType;
import com.bbyiya.enums.calendar.ActivityWorksStatusEnum;
import com.bbyiya.enums.calendar.AddressTypeEnum;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.model.TiActivitys;
import com.bbyiya.model.TiActivityworks;
import com.bbyiya.model.TiMyworks;
import com.bbyiya.model.TiPromoters;
import com.bbyiya.model.TiUserdiscounts;
import com.bbyiya.model.UUseraddress;
import com.bbyiya.pic.utils.Json2Objects;
import com.bbyiya.service.IRegionService;
import com.bbyiya.service.calendar.ITi_OrderMgtService;
import com.bbyiya.service.calendar.ItiAcitivityMgtService;
import com.bbyiya.service.pic.IBasePostMgtService;
import com.bbyiya.service.pic.IBaseUserAddressService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.calendar.TiActivityOrderSubmitParam;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.vo.user.UUserAddressResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/ti_actAddress")
public class Ti_ActivityAddressController extends SSOController {

	@Autowired
	private TiMyworksMapper workMapper;
	@Autowired
	private TiProductsMapper productMapper;
	@Autowired
	private TiUserdiscountsMapper userDisMapper;
	
	@Autowired
	private OOrderproductsMapper oproductOrderMapper;

	@Autowired
	private TiPromotersMapper promoterMapper;

	
	@Autowired
	private TiActivityworksMapper activityworkMapper;
	@Autowired
	private TiActivitysMapper actMapper;
	//订单处理service
	@Resource(name = "tiOrderMgtServiceImpl")
	private ITi_OrderMgtService orderMgtService;
	/**
	 * 活动
	 */
	@Resource(name = "ti_AcitivityMgtServiceImpl")
	private  ItiAcitivityMgtService actService;
	
	@Resource(name = "baseUserAddressServiceImpl")
	private IBaseUserAddressService addressService;

	@Resource(name = "regionServiceImpl")
	private IRegionService regionService;
	@Resource(name = "basePostMgtServiceImpl")
	private IBasePostMgtService postMgtService;
	
	/**
	 * 门店自提
	 * @param detailJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/submitReciverInfo")
	public String submitReciverInfo(long workId,  String addrJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			//联系方式信息
			UUseraddress address =  Json2Objects.getParam_UUseraddress(addrJson); //(UUseraddress) JsonUtil.jsonStrToObject(addrJson, UUseraddress.class);
			if (address != null) {
				if(ObjectUtil.isEmpty(address.getReciver())||!ObjectUtil.isMobile(address.getPhone())){
					rq.setStatusreson("姓名不能为空/手机号不正确");
					return JsonUtil.objectToJsonStr(rq);
				}
				address.setUserid(user.getUserId()); 
				rq = addressService.addOrEdit_UserAddressReturnAddressId(address);
			}else{
				rq.setStatusreson("参数有误！");
				return JsonUtil.objectToJsonStr(rq);
			}
			//活动作品
			TiActivityworks work=activityworkMapper.selectByPrimaryKey(workId);
			if(work!=null&&work.getUserid()!=null&&user.getUserId().longValue()==work.getUserid().longValue()){
				work.setReciever(address.getReciver());
				work.setMobiephone(address.getPhone());
				work.setAddresstype(-1); 
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
	 * 选择我的收货地址
	 * @param workId
	 * @param addressType 枚举AddressTypeEnum （0门店自提，1邮寄到家）
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/submitAddressChoose")
	public String submitAddress(long workId,  int addressType) throws Exception {
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			//活动作品
			TiActivityworks work=activityworkMapper.selectByPrimaryKey(workId);
			if(work!=null&&work.getUserid()!=null&&user.getUserId().longValue()==work.getUserid().longValue()){
				work.setAddresstype(addressType); 
				activityworkMapper.updateByPrimaryKey(work);
				
				//是否可以直接下单
				boolean canOrder=false; 
				TiActivitys activitys=actMapper.selectByPrimaryKey(work.getActid());
				if(activitys!=null&&work.getStatus()!=null&&work.getStatus().intValue()==Integer.parseInt(ActivityWorksStatusEnum.completeshare.toString()))
				{
					if(activitys.getHourseffective()!=null){
						long time=work.getSubmittime().getTime()+activitys.getHourseffective().intValue()*60*60*1000;
						if(time<(new Date()).getTime()){
							canOrder=true;
						}
					}
				}
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

	/**
	 * 展示用户收货地址选择
	 * @param workId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getAddressInfo")
	public String area(long workId) throws Exception {
		LoginSuccessResult user = super.getLoginUser();
		ReturnModel rq = new ReturnModel();
		if (user != null) {
			//是否可选邮寄到家
			boolean selfAddr=false;
			//是否可选门店自提
			boolean promoterAddr=false;
			//活动作品
			TiActivityworks work=activityworkMapper.selectByPrimaryKey(workId);
			//活动信息
			TiActivitys actInfo=null;
			if(work!=null) {
				actInfo= actMapper.selectByPrimaryKey(work.getActid());
				if(actInfo!=null&&actInfo.getAutoaddress()!=null&&(actInfo.getAutoaddress().intValue()==Integer.parseInt(ActivityAddressType.auto.toString())||actInfo.getAutoaddress().intValue()==Integer.parseInt(ActivityAddressType.customerAddr.toString()))){
					selfAddr=true;
				}else if(actInfo!=null&&actInfo.getAutoaddress()==null){
					selfAddr=true;
				}
			}else{//没有参加活动，是否用了优惠券购买
				selfAddr=true;
				OOrderproducts oproducts= oproductOrderMapper.getOProductsByWorkId(workId);
				if(oproducts!=null){
					TiUserdiscounts discount= userDisMapper.getMyDiscountsByUserOrderId(oproducts.getUserorderid());
					if(discount!=null&&discount.getActid()!=null){
						actInfo= actMapper.selectByPrimaryKey(discount.getActid());
					}
				}
			}
			//影楼用户userId
			long promoterUserId=0l;
			if(actInfo!=null&&actInfo.getAutoaddress()!=null&&(actInfo.getAutoaddress().intValue()==Integer.parseInt(ActivityAddressType.auto.toString())||actInfo.getAutoaddress().intValue()==Integer.parseInt(ActivityAddressType.promoterAddr.toString()))){
				promoterAddr=true;
				promoterUserId=actInfo.getProduceruserid();
			}else if(actInfo!=null&&actInfo.getAutoaddress()==null){
				promoterAddr=true;
				promoterUserId=actInfo.getProduceruserid();
			}
			Map<String, Object> mapResult = new HashMap<String, Object>(); 
			//如果已经选过收货地址
			if(work!=null&&work.getAddresstype()!=null&&work.getAddresstype().intValue()>=0){
				mapResult.put("postagePayed", 1);
				if(work.getAddresstype().intValue()==Integer.parseInt(AddressTypeEnum.cusaddr.toString())&&work.getOrderaddressid()!=null){
					promoterAddr=false;
				}else if(work.getAddresstype().intValue()==Integer.parseInt(AddressTypeEnum.promoteraddr.toString())&&!ObjectUtil.isEmpty(work.getReciever())){
					selfAddr=false;
				}
			}else{
				mapResult.put("postagePayed", 0);
			}
			//展示用户自己的收货地址，并计算运费
			if(selfAddr){
				//如果已经付过运费了=========================
				if(work!=null&&work.getAddresstype()!=null&&work.getAddresstype().intValue()==Integer.parseInt(AddressTypeEnum.cusaddr.toString())&&work.getOrderaddressid()!=null&&work.getOrderaddressid().longValue()>0){
					mapResult.put("selfAddress", 1);
					mapResult.put("myAddress", addressService.getUserAddressResultByOrderAddressId(work.getOrderaddressid()) );
					rq.setBasemodle(mapResult); 
				//========================end case1=============================================
				}else{
					//作品信息
					TiMyworks mywork=workMapper.selectByPrimaryKey(workId);
					UUserAddressResult selfAddress=addressService.getUserAddressResult(user.getUserId(),null);
					//如果运费还未付，计算邮寄到家的运费
					if((mywork!=null&&work!=null&&work.getOrderaddressid()==null)||work==null){
						if(selfAddress!=null){
							double postage= postMgtService.getPostAge_ti(selfAddress.getAddrid(), mywork.getProductid());
							selfAddress.setPostage(postage);
						}
					}
					mapResult.put("selfAddress", 1);
					mapResult.put("myAddress", selfAddress );
				}
			}
			//影楼收货地址
			if(promoterAddr&&promoterUserId>0){
				TiPromoters promoters = promoterMapper.selectByPrimaryKey(promoterUserId);
				if (promoters != null) {
					mapResult.put("address", addressService.getPromoterAddressResult(promoterUserId)); 
					mapResult.put("company", promoters.getCompanyname());
				}
			}
			rq.setBasemodle(mapResult); 
			rq.setStatu(ReturnStatus.Success); 
			
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	
}
