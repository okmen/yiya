package com.bbyiya.pic.web;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.RegionMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.RAreas;
import com.bbyiya.model.RCity;
import com.bbyiya.model.RProvince;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/region")
public class RegionController extends SSOController {

	@Autowired
	private RegionMapper regionMapper;
	
	

	/**
	 * 
	 * 获取地区列表
	 * @param province
	 * @param city
	 * @param area
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getlist")
	public String area(@RequestParam(required = false, defaultValue = "0") int type, String code) throws Exception {
		ReturnModel rq = new ReturnModel();
		switch (type) {
		case 0:
			if (!ObjectUtil.isEmpty(code)) {// 获取省
				rq.setBasemodle(regionMapper.getProvinceByCode(ObjectUtil.parseInt(code)));
			} else {
				String keyCity="cityArea_province_list1";
				List<RProvince> provincelist=(List<RProvince>)RedisUtil.getObject(keyCity);
				if(provincelist!=null&&provincelist.size()>0){
					rq.setBasemodle(provincelist);
				}else {
					provincelist=regionMapper.findProvincelistAll();
					if(provincelist!=null&&provincelist.size()>0){
						RedisUtil.setObject(keyCity, provincelist,36000); 
					}
					rq.setBasemodle(provincelist); 
				}
			}
			break;
		case 1:
			if (!ObjectUtil.isEmpty(code)) {//市 列表
//				rq.setBasemodle(regionMapper.findCitylistBy_ProvinceCode(ObjectUtil.parseInt(code)));
				
				String keyCity="cityArea_province1";
				Map<String, List<RCity>> mapCity= (Map<String, List<RCity>>)RedisUtil.getObject(keyCity); //new HashMap<Integer, List<RCity>>();
				if(mapCity!=null){//已经有缓存信息了
					if(mapCity.containsKey(code)){//有该省的所有市级缓存
						rq.setBasemodle(mapCity.get(code));  
					}else {
						List<RCity> list=regionMapper.findCitylistBy_ProvinceCode(ObjectUtil.parseInt(code));
						if(list!=null&&list.size()>0){
							mapCity.put(code, list);
							RedisUtil.setObject(keyCity, mapCity,36000);
						}
						rq.setBasemodle(list); 
					}
				}else {
					mapCity=new HashMap<String, List<RCity>>();
					List<RCity> list=regionMapper.findCitylistBy_ProvinceCode(ObjectUtil.parseInt(code));
					if(list!=null&&list.size()>0){
						mapCity.put(code, list);
						RedisUtil.setObject(keyCity, mapCity);
					}
					rq.setBasemodle(list); 
				}
			}
			break;
		case 2:
			if (!ObjectUtil.isEmpty(code)) {// 区 列表
//				rq.setBasemodle(regionMapper.findArealistBy_CityCode(ObjectUtil.parseInt(code)));
				
				String keyCity="cityArea_city1";
				Map<String, List<RAreas>> mapCity= (Map<String, List<RAreas>>)RedisUtil.getObject(keyCity);
				if(mapCity!=null){//已经有缓存信息了
					if(mapCity.containsKey(code)){//有该省的所有市级缓存
						rq.setBasemodle(mapCity.get(code));  
					}else {
						List<RAreas> list=regionMapper.findArealistBy_CityCode(ObjectUtil.parseInt(code));
						if(list!=null&&list.size()>0){
							mapCity.put(code, list);
							RedisUtil.setObject(keyCity, mapCity,36000);
						}
						rq.setBasemodle(list); 
					}
				}else {
					mapCity=new HashMap<String, List<RAreas>>();
					List<RAreas> list=regionMapper.findArealistBy_CityCode(ObjectUtil.parseInt(code));
					if(list!=null&&list.size()>0){
						mapCity.put(code, list);
						RedisUtil.setObject(keyCity, mapCity,36000);
					}
					rq.setBasemodle(list); 
				}
			}
			break;
		default:
			break;
		}
		rq.setStatu(ReturnStatus.Success);  
		return JsonUtil.objectToJsonStr(rq);
	}
}
