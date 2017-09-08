package com.bbyiya.pic.web.calendar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.PStylecoordinateMapper;
import com.bbyiya.dao.PStylecoordinateitemMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.PStylecoordinate;
import com.bbyiya.model.PStylecoordinateitem;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/ti_style")
public class TiCoordinateController  extends SSOController{

	@Autowired
	private PStylecoordinateMapper styleCoordMapper;

	@Autowired
	private PStylecoordinateitemMapper styleCoordItemMapper;
	
	/**
	 * P12 款式坐标
	 * @param styleId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getCoordinates")
	public String getCoordinates(@RequestParam(required = false, defaultValue = "0") long styleId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=getStyleCoordResult(styleId);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	public ReturnModel getStyleCoordResult(Long styleId) {
		ReturnModel rq = new ReturnModel();
		List<PStylecoordinate> list = styleCoordMapper.findlistByStyleId(styleId);
		if (list != null && list.size() > 0) {
			List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();

			for (PStylecoordinate ss : list) {
				Map<String, Object> map = new HashMap<String, Object>();
				PStylecoordinateitem w_no = styleCoordItemMapper.selectByPrimaryKey(ss.getNocoordid().longValue());
				map.put("number", w_no);
				PStylecoordinateitem w_pic = styleCoordItemMapper.selectByPrimaryKey(ss.getPiccoordid().longValue());
				map.put("pic", w_pic);
				map.put("type", ss.getType());
				
				long type = styleId % 2;
				List<Map<String, String>> backMaps = ConfigUtil.getMaplist("backcoordinate");
				if (backMaps != null && backMaps.size() > 0) {
					for (Map<String, String> mapBacks : backMaps) {
						if (ObjectUtil.parseLong(mapBacks.get("type")) == type) {
							map.put("back-mod", mapBacks);
						}
					}
				}
				arrayList.add(map);
			}
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(arrayList);
		}
		return rq;
	}
}
