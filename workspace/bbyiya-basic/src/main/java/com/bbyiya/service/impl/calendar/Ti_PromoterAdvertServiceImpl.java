package com.bbyiya.service.impl.calendar;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.TiPromoteradvertimgsMapper;
import com.bbyiya.dao.TiPromoteradvertinfoMapper;
import com.bbyiya.dao.TiPromoteradvertviewlogsMapper;
import com.bbyiya.model.TiPromoteradvertimgs;
import com.bbyiya.model.TiPromoteradvertinfo;
import com.bbyiya.model.TiPromoteradvertviewlogs;
import com.bbyiya.service.calendar.ITi_PromoterAdvertService;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("ti_PromoterAdvertService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Ti_PromoterAdvertServiceImpl implements ITi_PromoterAdvertService{
	@Autowired
	private TiPromoteradvertviewlogsMapper logsMapper;
	@Autowired
	private TiPromoteradvertinfoMapper adInfoMapper;

	@Autowired
	private TiPromoteradvertimgsMapper advertImgMapper;
	/**
	 * 新增广告预览记录
	 */
	public void addViews(LoginSuccessResult user,int advertId){
		TiPromoteradvertinfo adInfo= adInfoMapper.selectByPrimaryKey(advertId);
		if(adInfo!=null){
			int viewCount=addLogReturnViewCount(user, advertId);
			//更新广告的总体浏览数、送达人次
			if(viewCount<=1){
				adInfo.setViewpersoncount(adInfo.getViewpersoncount()==null?1:(adInfo.getViewpersoncount()+1));
			}
			adInfo.setViewcount(adInfo.getViewcount()==null?1:(adInfo.getViewcount().intValue()+1));
			adInfoMapper.updateByPrimaryKeySelective(adInfo); 
		}
		
	}
	
	/**
	 * 广告的点击记录
	 * @param user
	 * @param advertId
	 */
	public void addClicks(LoginSuccessResult user,int advertId){
		TiPromoteradvertinfo adInfo= adInfoMapper.selectByPrimaryKey(advertId);
		if(adInfo!=null){
			int viewCount=addLogReturnViewCount(user, advertId);
			//更新广告的总体浏览数、送达人次
			if(viewCount<=1){
				adInfo.setViewpersoncount(adInfo.getViewpersoncount()==null?1:(adInfo.getViewpersoncount()+1));
			}
			adInfo.setViewcount(adInfo.getViewcount()==null?1:(adInfo.getViewcount().intValue()+1));
			adInfo.setReadcount(adInfo.getReadcount()==null?1:(adInfo.getReadcount().intValue()+1));
			adInfoMapper.updateByPrimaryKeySelective(adInfo);  
		}
	}

	/**
	 * 广告详情(包含图片)
	 * @param advertId
	 * @return
	 */
	public TiPromoteradvertinfo getTiPromoteradvertinfo(int advertId){
		TiPromoteradvertinfo advertInfo= adInfoMapper.selectByPrimaryKey(advertId);
		if(advertInfo!=null){
			List<TiPromoteradvertimgs> advertImgs= advertImgMapper.findImgsByAdvertId(advertId);
			if(advertImgs!=null&&advertImgs.size()>0){
				advertInfo.setImglist(advertImgs);
			}
		}
		return advertInfo;
	}
	/**
	 * 新增广告曝光，并返回广告详情
	 * @param user
	 * @param advertId
	 * @return TiPromoteradvertinfo
	 */
	public TiPromoteradvertinfo addViewCountReurnTiPromoteradvertinfo(LoginSuccessResult user,Integer advertId){
		if(ObjectUtil.isEmpty(advertId)||user==null)
			return null;
		//广告基本信息
		TiPromoteradvertinfo advertInfo= this.getTiPromoteradvertinfo(advertId.intValue());
		if(advertInfo!=null){
			this.addViews(user, advertId);
		}
		return advertInfo;
	}
	/**
	 * 点击广告-返回广告详情
	 */
	public TiPromoteradvertinfo addClickCountReurnTiPromoteradvertinfo(LoginSuccessResult user,Integer advertId){
		if(ObjectUtil.isEmpty(advertId)||user==null)
			return null;
		//广告基本信息
		TiPromoteradvertinfo advertInfo= this.getTiPromoteradvertinfo(advertId.intValue());
		if(advertInfo!=null){
			this.addClicks(user, advertId);
		}
		return advertInfo;
	}
	
	/**
	 * 获取分享广告的浏览记录 翻页
	 * @param advertId
	 * @param index
	 * @param size
	 * @return
	 */
	public PageInfo<TiPromoteradvertviewlogs> findTiPromoteradvertviewlogsPage(int advertId,int index,int size){
		PageHelper.startPage(index, size);
		List<TiPromoteradvertviewlogs> logslist=logsMapper.findlistByAdvertId(advertId);
		PageInfo<TiPromoteradvertviewlogs> result=new PageInfo<TiPromoteradvertviewlogs>(logslist);
		if(result!=null&&result.getList()!=null&&result.getList().size()>0){
			for (TiPromoteradvertviewlogs log : result.getList()) {
				log.setViewtimeStr(DateUtil.getTimeStr(log.getViewtime(), "yyyy-MM-dd HH:mm"));
			}
		} 
		return result;
	}
	
	/**
	 * 新增浏览广告记录，并返回当前用户的浏览次数
	 * @param user
	 * @param advertId
	 * @return
	 */
	public int addLogReturnViewCount(LoginSuccessResult user,int advertId){
		TiPromoteradvertviewlogs logInfo= logsMapper.getByAdIdAndUid(user.getUserId(),advertId);
		int viewCount=1;
		if(logInfo!=null){
			viewCount+=(logInfo.getViewcount()==null?1:logInfo.getViewcount().intValue());
			logInfo.setViewcount(viewCount);
			logInfo.setViewtime(new Date());
			logsMapper.updateByPrimaryKeySelective(logInfo);
		}else{
			logInfo=new TiPromoteradvertviewlogs();
			logInfo.setAdvertid(advertId); 
			logInfo.setUserid(user.getUserId());
			logInfo.setHeadimg(user.getHeadImg());
			logInfo.setNickname(user.getNickName());
			logInfo.setViewcount(viewCount);
			logInfo.setViewtime(new Date());
			logsMapper.insert(logInfo);
		}
		return viewCount;
	}
}
