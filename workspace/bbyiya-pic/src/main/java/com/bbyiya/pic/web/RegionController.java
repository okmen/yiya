package com.bbyiya.pic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.RegionMapper;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
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
				rq.setBasemodle(regionMapper.findProvincelistAll());
			}
			break;
		case 1:
			if (!ObjectUtil.isEmpty(code)) {
				rq.setBasemodle(regionMapper.findCitylistBy_ProvinceCode(ObjectUtil.parseInt(code)));
			}
			break;
		case 2:
			if (!ObjectUtil.isEmpty(code)) {
				rq.setBasemodle(regionMapper.findArealistBy_CityCode(ObjectUtil.parseInt(code)));
			}
			break;
		default:
			break;
		}

		return JsonUtil.objectToJsonStr(rq);
	}
}
