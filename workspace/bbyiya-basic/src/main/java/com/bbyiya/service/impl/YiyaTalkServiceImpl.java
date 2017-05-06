package com.bbyiya.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.MYiyabannerMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.MYiyabanner;
import com.bbyiya.service.IYiyaTalkService;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.talks.YiyaTalkBannerModel;

@Service("yiyaTalkService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class YiyaTalkServiceImpl implements IYiyaTalkService{
	@Autowired
	private MYiyabannerMapper yiyabannerMapper;
	/**
	 * 首页 咿呀说 banner 缓存KEY  
	 */
	private static String kEY_TALKS_BANNER="YIYA_TALK_BANNERS_temp";
	
	public List<YiyaTalkBannerModel> find_talkBanners(){
		List<YiyaTalkBannerModel> result=(List<YiyaTalkBannerModel>)RedisUtil.getObject(kEY_TALKS_BANNER);
		if(result==null||result.size()<=0){
			result=new ArrayList<YiyaTalkBannerModel>();
			List<MYiyabanner> banners=yiyabannerMapper.find_MYiyabannerByCurrentTime(new Date());
			if(banners!=null&&banners.size()>0){
				for (MYiyabanner bb : banners) {
					YiyaTalkBannerModel mo=new YiyaTalkBannerModel();
					mo.setId(bb.getId());
					mo.setImageUrl(bb.getImageurl());
					mo.setTitle(bb.getTitle());
					mo.setWapUrl(bb.getWapurl());
					result.add(mo);
				}
//				RedisUtil.setObject(kEY_TALKS_BANNER, result, 600);
			}
		}
		return result;
	}
	
	public ReturnModel addOrEdit_yiyaTalkBanner(Integer adminId,MYiyabanner param){
		ReturnModel rq=new ReturnModel();
		if(param!=null&&param.getId()!=null){
			MYiyabanner banner=yiyabannerMapper.selectByPrimaryKey(param.getId());
			if(banner!=null){
				if(param.getIson()!=null){
					banner.setIson(param.getIson());
				}
				if(!ObjectUtil.isEmpty(param.getWapurl()) ){
					banner.setWapurl(param.getWapurl());
				}
				if(!ObjectUtil.isEmpty(param.getImageurl())){
					banner.setImageurl(param.getImageurl());
				}
				if(!ObjectUtil.isEmpty(param.getTitle())){
					banner.setTitle(param.getTitle());
				}
				yiyabannerMapper.updateByPrimaryKey(banner);
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("ok!");
				RedisUtil.delete(kEY_TALKS_BANNER);//清楚首页缓存
			}
		}
		else if (param!=null) {
			yiyabannerMapper.insert(param);
			RedisUtil.delete(kEY_TALKS_BANNER);//清楚首页缓存
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("ok!");
		}
		else {
			rq.setStatu(ReturnStatus.ParamError_1);
			rq.setStatusreson("参数有误");
		}
		return rq;
	}
}
