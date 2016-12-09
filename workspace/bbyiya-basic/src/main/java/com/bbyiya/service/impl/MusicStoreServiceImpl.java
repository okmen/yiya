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
import com.bbyiya.model.UChildreninfo;
import com.bbyiya.service.IMusicStoreService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ObjectUtil;
import com.sdicons.json.validator.impl.predicates.Str;


@Service("musicStoreService")
@Transactional(rollbackFor={RuntimeException.class, Exception.class})
public class MusicStoreServiceImpl implements IMusicStoreService{

	@Autowired
	private UChildreninfoMapper childMapper;
	@Autowired
	private SMusicrecommendMapper sMusicrecommendMapper;
	/**
	 * 每日音乐 推荐
	 * @return
	 */
	public List<SMusicrecommend> find_SMusicrecommend(Long userId){
		List<SMusicrecommend> list=null;
		UChildreninfo child= childMapper.selectByPrimaryKey(userId);
		if(child!=null){
			try {
				int days= DateUtil.daysBetween(child.getBirthday(), new Date());
				list=sMusicrecommendMapper.findSMusicrecommendByDay(days);
				if(list!=null&&list.size()>0){
					return list;
				}else {
					list=new ArrayList<SMusicrecommend>();
					List<Map<String, String>> maplist= ConfigUtil.getMaplist("muscis");
					if(maplist!=null&&maplist.size()>0){
						for (Map<String, String> map : maplist) {
							SMusicrecommend mo=new SMusicrecommend();
							mo.setMusicid(ObjectUtil.parseInt(map.get("MusicId")));
							mo.setLinkurl(map.get("LinkUrl"));
							list.add(mo);
						}
					}
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}
}
