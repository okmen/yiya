package com.bbyiya.pic.web.calendar;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.TiActivitysMapper;
import com.bbyiya.dao.TiActivityworksMapper;
import com.bbyiya.dao.TiDiscountmodelMapper;
import com.bbyiya.dao.TiMyworkcustomersMapper;
import com.bbyiya.dao.TiMyworksMapper;
import com.bbyiya.dao.TiUserdiscountsMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.calendar.ActivityWorksStatusEnum;
import com.bbyiya.model.TiActivitys;
import com.bbyiya.model.TiActivityworks;
import com.bbyiya.model.TiDiscountmodel;
import com.bbyiya.model.TiMyworkcustomers;
import com.bbyiya.model.TiMyworks;
import com.bbyiya.model.TiUserdiscounts;
import com.bbyiya.pic.vo.calendar.GetDiscountParam;
import com.bbyiya.service.calendar.ITi_OrderMgtService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.calendar.TiActivityOrderSubmitParam;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;
import com.sdicons.json.mapper.MapperException;

@Controller
@RequestMapping(value = "/tiDiscount")
public class TiDiscountController extends SSOController {

	@Autowired
	private TiUserdiscountsMapper userDisMapper;
	@Autowired
	private TiDiscountmodelMapper dismodelMapper;
	@Autowired
	private TiMyworkcustomersMapper workcustomerMapper;

	@Autowired
	private TiActivitysMapper actMapper;
	@Autowired
	private TiActivityworksMapper activityworksMapper;
	@Autowired
	private TiMyworksMapper myworkMapper;
	
	@Resource(name = "tiOrderMgtServiceImpl")
	private ITi_OrderMgtService orderMgtService;
	/**
	 * 领取优惠券--代客制作优惠券
	 * @param workId
	 * @return
	 * @throws MapperException
	 */
	@ResponseBody
	@RequestMapping(value = "/getDiscount")
	public String getDiscount(String paramJson, Long workId) throws MapperException {
		ReturnModel rq = new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		if(ObjectUtil.isEmpty(paramJson)){
			rq.setStatusreson("参数有误");
			return JsonUtil.objectToJsonStr(rq);
		}
		//参数 param
		GetDiscountParam param=(GetDiscountParam)JsonUtil.jsonStrToObject(paramJson, GetDiscountParam.class);
		if(param==null||param.getSourceWorkId()==null){
			rq.setStatusreson("参数有误");
			return JsonUtil.objectToJsonStr(rq);
		}
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			//代客制作作品 分享 领取优惠券
			if (param.getSourceType() == 2) {
				List<TiUserdiscounts> mydislist= userDisMapper.findMyDiscountsByWorkId(user.getUserId(),workId);
				if(mydislist!=null&&mydislist.size()>0){
					rq.setStatusreson("您已经领取过优惠券！");
					return JsonUtil.objectToJsonStr(rq);
				}
				TiMyworkcustomers actMyworkcustomers = workcustomerMapper.selectByPrimaryKey(workId);
				if (actMyworkcustomers != null) {
					// 获取优惠券
					getMyDiscounts(param, actMyworkcustomers.getPromoteruserid(), user.getUserId());
					int shareCount = actMyworkcustomers.getSharedcount() == null ? 1 : (actMyworkcustomers.getSharedcount().intValue() + 1);
					actMyworkcustomers.setSharedcount(shareCount);
					if (actMyworkcustomers.getNeedsharecount() != null && actMyworkcustomers.getNeedsharecount().intValue() > 0) {
						if (shareCount >= actMyworkcustomers.getNeedsharecount().intValue()) {
							actMyworkcustomers.setStatus(Integer.parseInt(ActivityWorksStatusEnum.completeshare.toString()));
							workcustomerMapper.updateByPrimaryKeySelective(actMyworkcustomers);
							// TODO 自动下单操作

						}
					} else {
						workcustomerMapper.updateByPrimaryKeySelective(actMyworkcustomers);
					}
					rq.setStatu(ReturnStatus.Success);
					rq.setStatusreson("恭喜获得3张5折优惠券（下单时自动使用）");
				}
			} else if (param.getSourceType() == 1) {// 直接参与活动领取优惠券
				TiActivitys actInfo = actMapper.selectByPrimaryKey(param.getActityId());
				if (actInfo == null) {
					rq.setStatusreson("活动不存在！");
					return JsonUtil.objectToJsonStr(rq);
				}
				List<TiUserdiscounts> mydislist= userDisMapper.findMyDiscountsByActId(user.getUserId(),param.getActityId());
				if(mydislist!=null&&mydislist.size()>0){
					rq.setStatusreson("您已经领取过优惠券！");
					return JsonUtil.objectToJsonStr(rq);
				}
				getMyDiscounts( param,actInfo.getProduceruserid(),user.getUserId());
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("恭喜获得3张5折优惠券（下单时自动使用）");

			} else {// 活动作品分享后领取
				List<TiUserdiscounts> mydislist= userDisMapper.findMyDiscountsByWorkId(user.getUserId(),param.getSourceWorkId());
				if(mydislist!=null&&mydislist.size()>0){
					rq.setStatusreson("您已经领取过优惠券！");
					return JsonUtil.objectToJsonStr(rq);
				}
				TiMyworks myworks=myworkMapper.selectByPrimaryKey(param.getSourceWorkId());
				if(myworks!=null){
					if(myworks.getActid()!=null){
						param.setActityId(myworks.getActid()); 
						TiActivityworks activityworks=activityworksMapper.selectByPrimaryKey(param.getSourceWorkId());
						TiActivitys actInfo = actMapper.selectByPrimaryKey(param.getActityId());
						if(activityworks!=null&&actInfo!=null){
							getMyDiscounts( param, actInfo.getProduceruserid(),user.getUserId());
							
							activityworks.setExtcount((activityworks.getExtcount()==null?0:activityworks.getExtcount().intValue())+1); 
							
							//如果活动目标达到，直接下单
							int extcount=actInfo.getExtcount()==null?0:actInfo.getExtcount();
							if(activityworks.getExtcount().intValue()>=extcount&&activityworks.getStatus().intValue()!=Integer.parseInt(ActivityWorksStatusEnum.completeorder.toString())){
								//更新参与活动状态
								activityworks.setStatus(Integer.parseInt(ActivityWorksStatusEnum.completeshare.toString())); 
								activityworksMapper.updateByPrimaryKeySelective(activityworks);
								//自动下单
								TiActivityOrderSubmitParam OrderParam=new TiActivityOrderSubmitParam();
								OrderParam.setCount(1);
								OrderParam.setSubmitUserId(actInfo.getProduceruserid());
								OrderParam.setWorkId(workId);
								orderMgtService.submitOrder_ibs(OrderParam); 
							}else {
								activityworksMapper.updateByPrimaryKeySelective(activityworks);
							}
							
							rq.setStatu(ReturnStatus.Success);
							rq.setStatusreson("恭喜获得3张5折优惠券（下单时自动使用）");
						}
					}else {
//						getMyDiscounts( param, 75L,user.getUserId());
						rq.setStatu(ReturnStatus.SystemError);
						rq.setStatusreson("不好意思， 非活动作品无法领取优惠券！");
					}
				}
			}

		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	private boolean getMyDiscounts(GetDiscountParam param,Long promoterUserId,Long userId){
		//获取优惠券
		List<TiDiscountmodel> modlist= dismodelMapper.findDiscountList();
		if(modlist!=null&&modlist.size()>0){
			for(int i=0;i<3;i++){
				TiUserdiscounts model=new TiUserdiscounts();
				model.setWorkid(param.getSourceWorkId());
				model.setActid(param.getActityId()); 
				model.setPromoteruserid(promoterUserId); 
				model.setCreatetime(new Date());
				model.setDiscountid(modlist.get(0).getDiscountid());
				model.setStatus(0);
				model.setUserid(userId);
				userDisMapper.insert(model);
			}
		}
		return true;
	}
	

	
}
