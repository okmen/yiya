package com.bbyiya.service.impl.calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.TiActivitysMapper;
import com.bbyiya.dao.TiActivityworksMapper;
import com.bbyiya.dao.TiMyworksMapper;
import com.bbyiya.dao.TiProductsMapper;
import com.bbyiya.dao.TiProductstylesMapper;
import com.bbyiya.dao.UAccountsMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.calendar.ActivityStatusEnums;
import com.bbyiya.enums.calendar.ActivityWorksStatusEnum;
import com.bbyiya.model.TiActivitys;
import com.bbyiya.model.TiActivityworks;
import com.bbyiya.model.TiMyworks;
import com.bbyiya.model.TiProductstyles;
import com.bbyiya.model.UAccounts;
import com.bbyiya.service.calendar.ItiAcitivityMgtService;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
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
	
	public void updateWorkTofailse(){
		//1需要积攒的活动
		//2、作品状态（已提交的作品、未提交的作品）
		//3、提交时间超过3天
		PageHelper.startPage(1, 50);
		//查询需要积攒的作品
		List<TiActivityworks> actworks=activityworksMapper.getActWorkListNeedShared();
		PageInfo<TiActivityworks> resultPage = new PageInfo<TiActivityworks>(actworks);
		if(resultPage!=null&&resultPage.getList()!=null&&resultPage.getList().size()>0){
			long nowtimelong=new Date().getTime();
			//作品
			List<Integer> actIds=new ArrayList<Integer>();
			for (TiActivityworks ww : resultPage.getList()) {
				long createTimelong=ww.getCreatetime().getTime(); 
				long betimeMinutes=(nowtimelong-createTimelong)/(1000*60);
				if(betimeMinutes>(7*24*60)){
					ww.setStatus(Integer.parseInt(ActivityWorksStatusEnum.fail.toString()));
					activityworksMapper.updateByPrimaryKeySelective(ww);
					actIds.add(ww.getActid());
				}
			}
			if(actIds!=null&&actIds.size()>0){
				for (Integer id : actIds) {
					updateActivitylimitCountByActId(id);
				}
			}
		}
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
			TiActivitys act=myactMapper.selectByPrimaryKey(myactWork.getActid());
			if(act!=null&&act.getApplylimitcount()!=null&&act.getApplylimitcount().intValue()>0){
				if(act.getProduceruserid().longValue()!=userId){
					rq.setStatusreson("非您开的活动，无权限！"); 
					return rq;
				}
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
							activityworksMapper.updateByPrimaryKeySelective(myactWork);
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
	private TiProductsMapper productMapper;
	@Autowired
	private TiProductstylesMapper styleMapper;
	
	public boolean checkAccount(long promoterUserId,long productId,int count){
		UAccounts account= accountMapper.selectByPrimaryKey(promoterUserId);
		if(account!=null&&account.getAvailableamount()!=null&&account.getAvailableamount()>0){
			//TODO
			TiProductstyles style= styleMapper.selectByPrimaryKey(productId);
			if(style!=null){
				
			}
		}
		return false;
	}
	
}
