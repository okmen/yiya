package com.bbyiya.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.SMusicrecommendMapper;
import com.bbyiya.dao.UChildreninfoMapper;
import com.bbyiya.model.SMusicrecommend;
import com.bbyiya.service.IMusicStoreService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.music.DailyMusicResult;
import com.bbyiya.vo.user.LoginSuccessResult;

@Service("musicStoreService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class MusicStoreServiceImpl implements IMusicStoreService {

	@Autowired
	private UChildreninfoMapper childMapper;
	@Autowired
	private SMusicrecommendMapper sMusicrecommendMapper;

	
	public  List<DailyMusicResult> find_dailyMusiclist(LoginSuccessResult user) {
		List<SMusicrecommend> dailList=find_SMusicrecommend(user);
		if(dailList!=null&&dailList.size()>0){
			List<DailyMusicResult> results=new ArrayList<DailyMusicResult>();
			for (SMusicrecommend rec : dailList) {
				DailyMusicResult mo=new DailyMusicResult();
				mo.setAuthor(rec.getAuthor());
				mo.setName(rec.getName());
				mo.setMusicId(rec.getReid());
				mo.setLinkUrl(rec.getLinkurl());
				results.add(mo);
			}
			return results;
		}else {
			return findDayMusicDefault();
		}
	}
	
	/**
	 * 每日音乐 推荐
	 * 
	 * @return
	 */
	public List<SMusicrecommend> find_SMusicrecommend(LoginSuccessResult user) {
		List<SMusicrecommend> list = null;
		if (user != null && user.getBabyInfo() != null) {
			try {
				// 获取宝宝当前第几天
				int days = DateUtil.daysBetween(user.getBabyInfo().getBirthday(), new Date());
				list = sMusicrecommendMapper.findSMusicrecommendByDay(days);
				if (list != null && list.size() > 0) {
					return list;
				} 
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		return list;
	}
	
	

	/**
	 * 获取每日音乐推荐 默认列表
	 * 
	 * @return
	 */
	public List<DailyMusicResult> findDayMusicDefault() {
		List<DailyMusicResult> list = new ArrayList<DailyMusicResult>();
		List<Map<String, String>> maplist = ConfigUtil.getMaplist("muscis");
		if (maplist != null && maplist.size() > 0) {
			for (Map<String, String> map : maplist) {
				DailyMusicResult mo = new DailyMusicResult();
				mo.setMusicId(ObjectUtil.parseInt(map.get("musicId")));
				mo.setLinkUrl(map.get("linkUrl"));
				mo.setName(map.get("name"));
				mo.setAuthor(map.get("author"));
				mo.setDuration(map.get("duration"));
				list.add(mo);
			}
		}
		return list;
	}

}
