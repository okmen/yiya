package com.bbyiya.service.impl.calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.EErrorsMapper;
import com.bbyiya.dao.OOrderproductphotosMapper;
import com.bbyiya.dao.OOrderproductsMapper;
import com.bbyiya.dao.OProducerordercountMapper;
import com.bbyiya.dao.OUserorderpushMapper;
import com.bbyiya.dao.TiActivitysMapper;
import com.bbyiya.dao.TiActivityworksMapper;
import com.bbyiya.dao.TiMyworksMapper;
import com.bbyiya.dao.TiProductsMapper;
import com.bbyiya.dao.TiProductstylesMapper;
import com.bbyiya.dao.UAccountsMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.calendar.ActivityStatusEnums;
import com.bbyiya.enums.calendar.ActivityWorksStatusEnum;
import com.bbyiya.model.EErrors;
import com.bbyiya.model.OOrderproductphotos;
import com.bbyiya.model.OProducerordercount;
import com.bbyiya.model.OUserorderpush;
import com.bbyiya.model.TiActivitys;
import com.bbyiya.model.TiActivityworks;
import com.bbyiya.model.TiMyworks;
import com.bbyiya.service.calendar.ITi_OrderMgtService;
import com.bbyiya.service.calendar.ItiAcitivityMgtService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.HttpRequestHelper;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.calendar.TiActivityOrderSubmitParam;
import com.bbyiya.vo.order.RedpackgeOrderParam;
import com.bbyiya.vo.order.UserOrderRedpackgeResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("ti_AcitivityMgtServiceImpl")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class TiAcitivityMgtServiceImpl implements ItiAcitivityMgtService{
	@Autowired
	private TiMyworksMapper myworksMapper;
	@Autowired
	private TiActivitysMapper myactMapper;
	@Autowired
	private TiActivityworksMapper activityworksMapper;
	@Autowired
	private UAccountsMapper accountMapper;
	@Autowired 
	private TiProductsMapper productMapper;
	@Autowired
	private TiProductstylesMapper styleMapper;
	@Autowired
	private OOrderproductsMapper oproductMapper;
	@Autowired
	private OOrderproductphotosMapper ordercontentMapper;
	@Resource(name = "tiOrderMgtServiceImpl")
	private  ITi_OrderMgtService basetiorderService;
	/**
	 * 作品完成分享
	 * @param actId
	 */
	public void updateActivitylimitCountByActId(Integer actId){
		 TiActivitys actInfo=  myactMapper.selectByPrimaryKey(actId);
		 if(actInfo!=null&&actInfo.getStatus()!=null&&actInfo.getStatus()==Integer.parseInt(ActivityStatusEnums.ok.toString())){
			 List<Integer> statusArr=new ArrayList<Integer>();
			 statusArr.add(Integer.parseInt(ActivityWorksStatusEnum.imagesubmit.toString()));
			 statusArr.add(Integer.parseInt(ActivityWorksStatusEnum.completeshare.toString()));
			 statusArr.add(Integer.parseInt(ActivityWorksStatusEnum.completeorder.toString()));
			 int applyingCount=activityworksMapper.countByActIdAndStatus(actId, statusArr);
			 
			 //更新已经领取名额
			 if(applyingCount>0&&(actInfo.getApplyingcount()==null||applyingCount!=actInfo.getApplyingcount())){
				 actInfo.setApplyingcount(applyingCount);
				 if(actInfo.getApplylimitcount().intValue()<applyingCount){
					 actInfo.setApplylimitcount(applyingCount); 
				 }
				 myactMapper.updateByPrimaryKeySelective(actInfo);
			 }
		 }
	}
	
	
	public void timeToSubmitOrders(){
		//1需要积攒的活动
		//2、作品状态（已提交的作品、未提交的作品）
		//3、提交时间超过3天
		PageHelper.startPage(1, 100);
		//查询积攒时间到了的需要下单的作品
		List<TiActivityworks> actworks=activityworksMapper.findActworklistExpired();
		PageInfo<TiActivityworks> resultPage = new PageInfo<TiActivityworks>(actworks);
		if(resultPage!=null&&resultPage.getList()!=null&&resultPage.getList().size()>0){
			//作品
			List<Integer> actIds=new ArrayList<Integer>();
			for (TiActivityworks ww : resultPage.getList()) {
				//活动信息
				TiActivitys act= myactMapper.selectByPrimaryKey(ww.getActid());
				if(act!=null){
					if (act.getExtcount() != null && act.getExtcount().intValue() > 0) {
						// 需要积攒
						if (ww.getExtcount() != null && ww.getExtcount().intValue() >= act.getExtcount().intValue()) {
							if(ww.getAddresstype()==null||ww.getAddresstype().intValue()<0){
								// TUDO 打到目标但是 未填写收货地址的
								
							}else{
								// 完成积攒 --下单的数量 自己购买 +1
								TiActivityOrderSubmitParam orderParam = new TiActivityOrderSubmitParam();
								orderParam.setSubmitUserId(act.getProduceruserid());
								orderParam.setWorkId(ww.getWorkid());
								orderParam.setCount(1 + (ww.getCountmore() == null ? 0 : ww.getCountmore().intValue()));
								basetiorderService.submitOrder_ibs(orderParam);
							}
						} else if(ww.getCountmore()!=null&&ww.getCountmore().intValue()>0){// 没有完成
							if(ww.getAddresstype()==null||ww.getAddresstype().intValue()<0){
								// TUDO 打到目标但是 未填写收货地址的
								
							}else{
								TiActivityOrderSubmitParam orderParam = new TiActivityOrderSubmitParam();
								orderParam.setSubmitUserId(act.getProduceruserid());
								orderParam.setWorkId(ww.getWorkid());
								orderParam.setCount((ww.getCountmore() == null ? 0 : ww.getCountmore().intValue()));
								basetiorderService.submitOrder_ibs(orderParam);
							}
						}else {
							ww.setStatus(Integer.parseInt(ActivityWorksStatusEnum.fail.toString()));
							activityworksMapper.updateByPrimaryKeySelective(ww);
						}
					}
				}
				actIds.add(ww.getActid());
			}
			if(actIds!=null&&actIds.size()>0){
				for (Integer id : actIds) {
					updateActivitylimitCountByActId(id);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param workId
	 * @return
	 */
	public ReturnModel timeToSubmitOrder(long userId,long workId){
		ReturnModel rq=new ReturnModel();
		//我参与的活动作品
		TiActivityworks ww=activityworksMapper.selectByPrimaryKey(workId);
		if(ww==null){
			rq.setStatu(ReturnStatus.ParamError);
			return rq;
		}
		if(ww.getUserid().longValue()!=userId){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("不是本人，无权此操作！");
			return rq;
		} 
		if(ww.getAddresstype()==null||ww.getAddresstype().intValue()<0){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("还未选择收货地址！");
			return rq;
		}
		//活动信息
		TiActivitys act= myactMapper.selectByPrimaryKey(ww.getActid());
		if(act!=null){
			if (act.getExtcount() != null && act.getExtcount().intValue() > 0) {
				// 需要积攒
				if (ww.getExtcount() != null && ww.getExtcount().intValue() >= act.getExtcount().intValue()) {
					
					// 完成积攒 --下单的数量 自己购买 +1
					TiActivityOrderSubmitParam orderParam = new TiActivityOrderSubmitParam();
					orderParam.setSubmitUserId(act.getProduceruserid());
					orderParam.setWorkId(ww.getWorkid());
					orderParam.setCount(1 + (ww.getCountmore() == null ? 0 : ww.getCountmore().intValue()));
					basetiorderService.submitOrder_ibs(orderParam);
				} else if(ww.getCountmore()!=null&&ww.getCountmore().intValue()>0){// 没有完成
					TiActivityOrderSubmitParam orderParam = new TiActivityOrderSubmitParam();
					orderParam.setSubmitUserId(act.getProduceruserid());
					orderParam.setWorkId(ww.getWorkid());
					orderParam.setCount((ww.getCountmore() == null ? 0 : ww.getCountmore().intValue()));
					basetiorderService.submitOrder_ibs(orderParam);
				}else {
					rq.setStatu(ReturnStatus.SystemError);
					rq.setStatusreson("您还未获得产品！");
					return rq;
				}
			}
			updateActivitylimitCountByActId(act.getActid());
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("ok"); 
		return rq;
	}
	
	/**
	 * 如果七天未完成目标，置活动实效
	 */
	public ReturnModel updateActivityWorkTofailse(long userId,long workId){
		ReturnModel rq=new ReturnModel();
		TiActivityworks myactWork= activityworksMapper.selectByPrimaryKey(workId);
		if(myactWork!=null){
			TiActivitys act=myactMapper.selectByPrimaryKey(myactWork.getActid());
			if(act!=null&&act.getApplylimitcount()!=null&&act.getApplylimitcount().intValue()>0){
				if(act.getProduceruserid().longValue()!=userId){
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("非您开的活动，无权限！"); 
					return rq;
				}
				//将参与作品置为失效
				myactWork.setStatus(Integer.parseInt(ActivityWorksStatusEnum.fail.toString()));
				activityworksMapper.updateByPrimaryKeySelective(myactWork);
				//重新更新获取名额
				updateActivitylimitCountByActId(myactWork.getActid()); 
				rq.setStatusreson("ok!");
				rq.setStatu(ReturnStatus.Success);
			}else {
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("只有限制领取名额的活动，才可以作废当前名额！"); 
			}
		}else{
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("无效的活动作品！"); 
		}
		return rq;
	}
	
	/**
	 * 重新激活失效的活动名额
	 * @param userId
	 * @param workId
	 * @return
	 */
	public ReturnModel invokeActivityWorkStatus(long userId,long workId){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		TiActivityworks myactWork= activityworksMapper.selectByPrimaryKey(workId);
		if(myactWork!=null){
			//只能让失效的状态有效
			if(myactWork.getStatus()!=null&&myactWork.getStatus()!=Integer.parseInt(ActivityWorksStatusEnum.fail.toString())){
				rq.setStatusreson("此状态不可操作！"); 
				return rq;
			}
		    //活动信息
			TiActivitys act=myactMapper.selectByPrimaryKey(myactWork.getActid());
			if(act!=null&&act.getApplylimitcount()!=null&&act.getApplylimitcount().intValue()>0){
				if(act.getProduceruserid().longValue()!=userId){
					rq.setStatusreson("非您开的活动，无权限！"); 
					return rq;
				}
				//作品
				TiMyworks workInfo= myworksMapper.selectByPrimaryKey(workId);
				if(workInfo!=null&&!ObjectUtil.isEmpty(workInfo.getCompletetime())){
					//已经提交过作品
					if(act.getExtcount()!=null&&act.getExtcount().intValue()>0){
						//此时已经完成积攒
						if(myactWork.getExtcount()!=null&&myactWork.getExtcount().intValue()>=act.getExtcount().intValue()){
							myactWork.setStatus(Integer.parseInt(ActivityWorksStatusEnum.completeshare.toString()));
							activityworksMapper.updateByPrimaryKeySelective(myactWork);
						}else{//已经提交作品未完成积攒
							myactWork.setStatus(Integer.parseInt(ActivityWorksStatusEnum.imagesubmit.toString()));
							myactWork.setSubmittime(new Date()); 
							activityworksMapper.updateByPrimaryKeySelective(myactWork);
							//我的作品
							workInfo.setCompletetime(new Date());
							myworksMapper.updateByPrimaryKeySelective(workInfo);
						}
					}else{//不需要积攒
						rq.setStatusreson("不需要积攒的活动，暂时无法重新激活！"); 
						return rq;
					}
				}else{//未提交作品
					myactWork.setStatus(Integer.parseInt(ActivityWorksStatusEnum.apply.toString()));
					activityworksMapper.updateByPrimaryKeySelective(myactWork);
				}
				//重新更新获取名额
				updateActivitylimitCountByActId(myactWork.getActid()); 
				rq.setStatusreson("ok!");
				rq.setStatu(ReturnStatus.Success);
			}else {
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("只有限制领取名额的活动，才可以作废当前名额！"); 
			}
		}else{
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("无效的活动作品！"); 
		}
		return rq;
	}
	
	
	
	@Autowired
	private OUserorderpushMapper orderpushMapper;
	private Logger Log = Logger.getLogger(TiAcitivityMgtServiceImpl.class);
	
	public void sendOrderToRedpacket(){
		String msg="";
		PageHelper.startPage(1, 50);
		//查询积攒时间到了的活动
		List<UserOrderRedpackgeResult> orderlist=orderpushMapper.findUserorderForPushList();
		PageInfo<UserOrderRedpackgeResult> resultPage = new PageInfo<UserOrderRedpackgeResult>(orderlist);
		if(resultPage!=null&&resultPage.getList()!=null&&resultPage.getList().size()>0){
			msg+="doHongbaoOrderPush本次预计处理订单数："+resultPage.getList().size()+"条";
			int countReal=0;int falsecount=0;
			String errorOrder="";
			//作品
			for (UserOrderRedpackgeResult mod : resultPage.getList()) {
				try {
					 String paramJson=JsonUtil.objectToJsonStr(getSendParamJson(mod)) ;
					 String url= ConfigUtil.getPropertyVal("hongBaoOrderApiUrl");//"http://101.231.199.66/LyServices/HongBaoOrderApi.aspx";
					 String result= HttpRequestHelper.postJson(url, paramJson);
					 JSONObject model = JSONObject.fromObject(result);
					 if(model!=null){
						 if(model.get("ErrorCode").equals("0000")){
							 OUserorderpush ord= orderpushMapper.selectByPrimaryKey(mod.getUserorderid());
							 if(ord==null){
								 ord=new OUserorderpush();
								 ord.setUserorderid(mod.getUserorderid());
								 ord.setPushtime(new Date());
								 ord.setIspushed(1);
								 orderpushMapper.insert(ord);
								 countReal++;
							 } 
						 }else{
							 errorOrder+=mod.getUserorderid()+":"+model.get("ErrorCode")+",";
							 falsecount++;
						 }
					 }
//					 System.out.println(result); 
				} catch (Exception e) {
					addErrorLog(e.toString(), this.getClass().getName()); 
					msg+="报错单号："+mod.getUserorderid(); 
				}
				 
			}
			msg+="实际处理"+countReal+"条，返回结果不为000有"+falsecount+"条:"+errorOrder;
		}
		Log.info(msg);
	}

	@Autowired
	private OProducerordercountMapper oproducerOrderCountMapper;
	
	private RedpackgeOrderParam getSendParamJson(UserOrderRedpackgeResult mod){
		if(mod!=null){
			RedpackgeOrderParam param=new RedpackgeOrderParam();
			param.setAuthCode(ConfigUtil.getPropertyVal("AuthCode"));
			param.setClientCodeEn(ConfigUtil.getPropertyVal("ClientCodeEn"));
			//收货地址
			param.setAddress(mod.getStreetDetail());
			param.setProvince(mod.getProvince());
			param.setCity(mod.getCity());
			param.setTown(mod.getDistrict());
			//收货人信息
			param.setReceiverName(mod.getReciverName());
			param.setReceiverPhone(mod.getReciverPhone());
			param.setOrderCode(mod.getUserorderid());
			param.setType(mod.getStyleId().toString()); 
			param.setCopys(mod.getCopys()); 
			OProducerordercount ocount= oproducerOrderCountMapper.selectByPrimaryKey(mod.getUserorderid());
			String index=ocount==null?"1":(ocount.getPrintindex());
			param.setPrintCode(mod.getWorkId()+"-"+mod.getUserid()+"-"+index); 
			param.setRePrint("N");
			//是否是代发货 
			if(mod.getIspromoteraddress()!=null&&mod.getIspromoteraddress().intValue()==1){
				param.setDropShopping("N");
				param.setDeliveAddress(mod.getProvince()+mod.getCity()+mod.getDistrict()+mod.getStreetDetail()); 
				param.setDelivePhone(param.getReceiverPhone());
			}else{
				param.setDropShopping("Y");
			}
			if(!(ObjectUtil.isEmpty(mod.getKhName())||ObjectUtil.isEmpty(mod.getKhPhone()))){
				param.setReceiverName(mod.getKhName());
				param.setReceiverPhone(mod.getKhPhone());
			}
			if(!(ObjectUtil.isEmpty(mod.getKhName1())||ObjectUtil.isEmpty(mod.getKhPhone1()))){
				param.setReceiverName(mod.getKhName1());
				param.setReceiverPhone(mod.getKhPhone1());
			}
			TiActivityworks actwork= activityworksMapper.selectByPrimaryKey(mod.getWorkId());
			if(actwork!=null){
				TiActivitys actInfo= myactMapper.selectByPrimaryKey(actwork.getActid());
				if(actInfo!=null){
					if(!ObjectUtil.isEmpty(actInfo.getCompanyname())){
						param.setAgentBless(actInfo.getCompanyname());
					}
					if(!ObjectUtil.isEmpty(actInfo.getQrcode())){
						param.setUrl(actInfo.getQrcode());//二维码地址
					}
				}
			}
			
			List<Map<String, String>> customerBless=new ArrayList<Map<String,String>>();
			
			List<OOrderproductphotos> ophotos= ordercontentMapper.findOrderProductPhotosByProductOrderId(mod.getUserorderid());
			if(ophotos!=null&&ophotos.size()>0){
				int i=0;
				for (OOrderproductphotos oo : ophotos) {
					Map<String, String> map=new HashMap<String, String>();
					map.put("Sort", String.valueOf(i));
					map.put("CustomerBlessTitle", oo.getTitle());
					map.put("CustomerBlessContent", oo.getContent());
					customerBless.add(map);
					i++;
				}
				param.setCustomerBlessList(customerBless); 
			}
			return param;
		}
		return null;
	}

	
	@Autowired
	private EErrorsMapper logMapper;
	/**
	 * 插入错误信息
	 * @param msg
	 * @param className
	 */
	public void addErrorLog(String msg,String className) {
		EErrors errors = new EErrors();
		errors.setClassname(this.getClass().getName());
		errors.setMsg(msg);
		errors.setCreatetime(new Date());
		logMapper.insert(errors);
	}
}
