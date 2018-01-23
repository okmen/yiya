package com.bbyiya.pic.service.impl.calendar;


import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.common.vo.ImageInfo;
import com.bbyiya.dao.TiAdvertimgsMapper;
import com.bbyiya.dao.TiProductsMapper;
import com.bbyiya.dao.TiPromoteremployeesMapper;
import com.bbyiya.dao.TiPromotersMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.calendar.PromoterStatusEnum;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.model.TiAdvertimgs;
import com.bbyiya.model.TiProducts;
import com.bbyiya.model.TiPromoteremployees;
import com.bbyiya.model.TiPromoters;
import com.bbyiya.model.UUsers;
import com.bbyiya.pic.service.calendar.IIbs_TiAdvertimgsService;
import com.bbyiya.pic.service.calendar.IIbs_TiPromoterEmployeeService;
import com.bbyiya.service.IBaseUserCommonService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.calendar.TiEmployeeActOffVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;



@Service("ibs_TiAdvertimgsService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Ibs_TiAdvertimgsServiceImpl implements IIbs_TiAdvertimgsService{
	
	@Autowired
	private TiProductsMapper productMapper;
	@Autowired
	private TiAdvertimgsMapper advertimgMapper;

	/*-------------------用户信息------------------------------------------------*/
	@Autowired
	private UUsersMapper usersMapper;
	
	/**
	 * 添加推广商广告位
	 */
	public ReturnModel addOrEditAdvertimgs(Long promoterUserId,Long productid,String advertimgjson,String content ){
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.ParamError);
		TiAdvertimgs advert=advertimgMapper.getAdvertByProductIdAndPromoterId(productid, promoterUserId);
		boolean isadd=false;
		if(advert==null){
			advert=new TiAdvertimgs();
			isadd=true;
		}
		advert.setAdvertimgjson(advertimgjson);
		if(!ObjectUtil.isEmpty(content)){
			advert.setAdvertcontent(content);
		}
		advert.setProductid(productid);
		advert.setPromoteruserid(promoterUserId);
		if(isadd){
			advertimgMapper.insert(advert);
		}else{
			advertimgMapper.updateByPrimaryKey(advert);
		}
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("操作成功！");
		return rqModel;
	}
	
	/**
	 * 得到产品的广告位信息
	 * @param promoterUserId
	 * @param productid
	 * @return
	 */
	public ReturnModel getAdvertimgsByIds(Long promoterUserId,Long productid ){
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.ParamError);
		HashMap<String,Object> map=new HashMap<String, Object>();
		TiAdvertimgs advert=advertimgMapper.getAdvertByProductIdAndPromoterId(productid, promoterUserId);
		if(advert!=null){
			List<ImageInfo> imList= (List<ImageInfo>)JsonUtil.jsonToList(advert.getAdvertimgjson());
			map.put("advert", advert);
			map.put("imglist",imList);
		}
		//得到预览图
		TiProducts products=productMapper.selectByPrimaryKey(productid);
		if(products!=null){
			map.put("products", products);
		}
		rqModel.setBasemodle(map);
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("成功！");
		return rqModel;
	}
	

}
