package com.bbyiya.pic.web.cts;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.PStylecoordinateMapper;
import com.bbyiya.dao.PStylecoordinateitemMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.PStylecoordinate;
import com.bbyiya.model.PStylecoordinateitem;
import com.bbyiya.pic.vo.product.StyleCoordinateEditParam;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/cts/style")
public class StyleCoordinateController extends SSOController {
	/*---------------------坐标模板---------------------------------*/
	@Autowired
	private PStylecoordinateMapper styleCoordMapper;
	@Autowired
	private PStylecoordinateitemMapper styleCoordItemMapper;

	/**
	 * P06 款式坐标修改
	 * @param paramJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/editCoordinate")
	public String editCoordinate(String paramJson) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			StyleCoordinateEditParam param = (StyleCoordinateEditParam) JsonUtil.jsonStrToObject(paramJson, StyleCoordinateEditParam.class);			
			if (param != null && param.getCoId() != null) {
				PStylecoordinate stylecoordinate = styleCoordMapper.selectByPrimaryKey(param.getCoId());
				if (stylecoordinate != null) {
					if (param.getNumberMod() != null) {
						if (param.getNumberMod().getCoordid() != null) {
							PStylecoordinateitem item = styleCoordItemMapper.selectByPrimaryKey(param.getNumberMod().getCoordid());
							if (item != null) {
								styleCoordItemMapper.updateByPrimaryKeySelective(param.getNumberMod());
							} else {
								rq.setStatu(ReturnStatus.ParamError);
								rq.setStatusreson("找不到相应的打印号坐标 coordJson:" + JsonUtil.objectToJsonStr(param.getNumberMod()));
								return JsonUtil.objectToJsonStr(rq);
							}
						} else {
							styleCoordItemMapper.insertReturnId(param.getNumberMod());
							stylecoordinate.setNocoordid(param.getNumberMod().getCoordid().intValue());
						}
					}
					if (param.getContentMod() != null) {
						if (param.getContentMod().getCoordid() != null) {
							PStylecoordinateitem item = styleCoordItemMapper.selectByPrimaryKey(param.getContentMod().getCoordid());
							if (item != null) {
								styleCoordItemMapper.updateByPrimaryKeySelective(param.getContentMod());
							} else {
								rq.setStatu(ReturnStatus.ParamError);
								rq.setStatusreson("找不到相应的打印号坐标 coordJson:" + JsonUtil.objectToJsonStr(param.getNumberMod()));
								return JsonUtil.objectToJsonStr(rq);
							}
						} else {
							styleCoordItemMapper.insertReturnId(param.getContentMod());
							stylecoordinate.setWordcoordid(param.getContentMod().getCoordid().intValue());
						}
					}
					if (param.getPicMod() != null) {
						if (param.getPicMod().getCoordid() != null) {
							PStylecoordinateitem item = styleCoordItemMapper.selectByPrimaryKey(param.getPicMod().getCoordid());
							if (item != null) {
								styleCoordItemMapper.updateByPrimaryKeySelective(param.getPicMod());
							} else {
								rq.setStatu(ReturnStatus.ParamError);
								rq.setStatusreson("找不到相应的打印号坐标 coordJson:" + JsonUtil.objectToJsonStr(param.getNumberMod()));
								return JsonUtil.objectToJsonStr(rq);
							}
						} else {
							styleCoordItemMapper.insertReturnId(param.getPicMod());
							stylecoordinate.setPiccoordid(param.getPicMod().getCoordid().intValue());
						}
					}
					rq.setStatu(ReturnStatus.Success);
					rq.setStatusreson("设置成功");
				} else {
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("找不到坐标信息");
				}
			} else {
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数有误");
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 获取款式坐标
	 * @param styleId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getStyleCoordinate")
	public String getStyleCoordinate(Long styleId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			List<StyleCoordinateEditParam> resultList=new ArrayList<StyleCoordinateEditParam>();
			List<PStylecoordinate> list=styleCoordMapper.findlistByStyleId(styleId);
			if(list!=null&&list.size()>0){
				for (PStylecoordinate co : list) {
					StyleCoordinateEditParam vo=new StyleCoordinateEditParam();
					vo.setCoId(co.getId());
					vo.setStyleId(styleId);
					vo.setType(co.getType());
					vo.setTypeName(co.getType().intValue()==1?"横构图坐标":"竖构图坐标"); 
					vo.setContentMod(styleCoordItemMapper.selectByPrimaryKey(co.getWordcoordid().longValue()));
					vo.setNumberMod(styleCoordItemMapper.selectByPrimaryKey(co.getNocoordid().longValue()));
					vo.setPicMod(styleCoordItemMapper.selectByPrimaryKey(co.getPiccoordid().longValue()));
					resultList.add(vo);
				}
				rq.setStatu(ReturnStatus.Success);
				rq.setBasemodle(resultList); 
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}

}
