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
import com.bbyiya.dao.SMusicsMapper;
import com.bbyiya.dao.SMusicttypeMapper;
import com.bbyiya.dao.UChildreninfoMapper;
import com.bbyiya.model.SMusicrecommend;
import com.bbyiya.model.SMusicttype;
import com.bbyiya.service.IMusicStoreService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.music.MusicResult;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("musicStoreService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class MusicStoreServiceImpl implements IMusicStoreService {

	@Autowired
	private UChildreninfoMapper childMapper;
	@Autowired
	private SMusicrecommendMapper sMusicrecommendMapper;
	@Autowired
	private SMusicsMapper musicsMapper;
	@Autowired
	private SMusicttypeMapper musicttypeMapper;
	
	

	public List<MusicResult> find_dailyMusiclist(LoginSuccessResult user) {
		List<SMusicrecommend> dailList = find_SMusicrecommend(user);
		if (dailList != null && dailList.size() > 0) {
			return exchange_list_SMusicrecommendToDailyMusicResult(dailList);
		} else {
			return findDayMusicDefault();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public PageInfo<MusicResult> find_MusicResult(int typeId,int index,int size){ 
		PageHelper.startPage(index, size);
		List<MusicResult> list=musicsMapper.findMusiclistByTypeId(typeId);
		PageInfo<MusicResult> reuslt=new PageInfo<MusicResult>(list); 
		return reuslt;
	}
	
	public List<SMusicttype> fint_SMusicttypelist(){
		return musicttypeMapper.findMusictTypeAll();
	}

	/**
	 * 列表类型转换 （SMusicrecommend change to DailyMusicResult ）
	 * @param dailList
	 * @return
	 */
	public List<MusicResult> exchange_list_SMusicrecommendToDailyMusicResult(List<SMusicrecommend> dailList) {
		if (dailList != null && dailList.size() > 0) {
			List<MusicResult> results = new ArrayList<MusicResult>();
			for (SMusicrecommend rec : dailList) {
				MusicResult mo = new MusicResult();
				mo.setAuthor(rec.getAuthor());
				mo.setName(rec.getName());
				mo.setMusicId(rec.getReid());
				mo.setLinkUrl(rec.getLinkurl());
				mo.setDefaultImg(rec.getPic()); 
				results.add(mo);
			}
			return results;
		}
		return null;
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
	public List<MusicResult> findDayMusicDefault() {
		List<MusicResult> list = new ArrayList<MusicResult>();
		List<Map<String, String>> maplist = ConfigUtil.getMaplist("muscis");
		if (maplist != null && maplist.size() > 0) {
			for (Map<String, String> map : maplist) {
				MusicResult mo = new MusicResult();
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
