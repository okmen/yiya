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
import com.bbyiya.vo.user.LoginSuccessResult;

@Service("musicStoreService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class MusicStoreServiceImpl implements IMusicStoreService {

	@Autowired
	private UChildreninfoMapper childMapper;
	@Autowired
	private SMusicrecommendMapper sMusicrecommendMapper;

	/**
	 * 每日音乐 推荐
	 * 
	 * @return
	 */
	public List<SMusicrecommend> find_SMusicrecommend(LoginSuccessResult user) {
		List<SMusicrecommend> list = null;
		if (user != null && user.getBabyInfo() != null) {
			try {
				//获取宝宝当前第几天
				int days = DateUtil.daysBetween(user.getBabyInfo().getBirthday(), new Date());
				list = sMusicrecommendMapper.findSMusicrecommendByDay(days);
				if (list != null && list.size() > 0) {
					return list;
				} else {
					return findDayMusicDefault();
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			return findDayMusicDefault();
		}
		return list;
	}
	
	/**
	 * 获取每日音乐推荐 默认列表
	 * @return
	 */
	public List<SMusicrecommend> findDayMusicDefault(){
		List<SMusicrecommend> list = new ArrayList<SMusicrecommend>();
		List<Map<String, String>> maplist = ConfigUtil.getMaplist("muscis");
		if (maplist != null && maplist.size() > 0) {
			for (Map<String, String> map : maplist) {
				SMusicrecommend mo = new SMusicrecommend();
				mo.setMusicid(ObjectUtil.parseInt(map.get("MusicId")));
				mo.setLinkurl(map.get("LinkUrl"));
				mo.setName(map.get("Name"));
				mo.setAuthor(map.get("Author"));
				list.add(mo);
			}
		}
		return list;
	}

}
