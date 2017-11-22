package com.bbyiya.pic.service.impl.calendar;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.TiGroupactivityMapper;
import com.bbyiya.dao.TiGroupactivityproductsMapper;
import com.bbyiya.dao.TiGroupactivityworksMapper;
import com.bbyiya.dao.TiProductsMapper;
import com.bbyiya.dao.TiProductstylesMapper;
import com.bbyiya.dao.TiPromoteradvertinfoMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.calendar.ActivityWorksStatusEnum;
import com.bbyiya.enums.calendar.AddressTypeEnum;
import com.bbyiya.enums.calendar.TiActivityTypeEnum;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.TiActivitys;
import com.bbyiya.model.TiGroupactivity;
import com.bbyiya.model.TiGroupactivityproducts;
import com.bbyiya.model.TiGroupactivityworks;
import com.bbyiya.model.TiProducts;
import com.bbyiya.model.TiProductstyles;
import com.bbyiya.model.TiPromoteradvertinfo;
import com.bbyiya.pic.service.calendar.IIbs_GroupActivityService;
import com.bbyiya.pic.vo.calendar.GroupActivityAddParam;
import com.bbyiya.service.IRegionService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.calendar.TiActivitysVo;
import com.bbyiya.vo.calendar.TiGroupActivitysWorksVo;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;



@Service("ibs_GroupActivityService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Ibs_GroupActivityServiceImpl implements IIbs_GroupActivityService{
	
	@Autowired
	private TiGroupactivityMapper groupactMapper;
	@Autowired
	private TiGroupactivityproductsMapper groupproductMapper;
	@Autowired
	private TiGroupactivityworksMapper groupactworkMapper;
	@Autowired
	private OUserordersMapper orderMapper;
	@Autowired
	private TiPromoteradvertinfoMapper advertinfoMapper;
	@Autowired
	private TiProductsMapper productMapper;
	@Autowired
	private TiProductstylesMapper styleMapper;
	
	
	/*-------------------用户信息------------------------------------------------*/
	@Autowired
	private UUsersMapper usersMapper;
	@Resource(name = "regionServiceImpl")
	private IRegionService regionService;
	/**
	 * 添加分销活动
	 * 
	 * */
	public ReturnModel addorEditGroupActivity(Long userid,GroupActivityAddParam param){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);	
		for (TiGroupactivityproducts pp : param.getProductlist()) {
			TiProductstyles style=styleMapper.selectByPrimaryKey(pp.getProductid());
//			//所有产品只能输入高于惊爆价的2倍，低于全价
//			if(pp.getPrice().doubleValue()>=style.getPrice()){
//				rq.setStatu(ReturnStatus.ParamError);
//				rq.setStatusreson("产品价格必须低于全价！");
//				return rq;
//			}
//			if(pp.getPrice().doubleValue()<=style.getPromoterprice().doubleValue()*2){
//				rq.setStatu(ReturnStatus.ParamError);
//				rq.setStatusreson("产品价格必须高于惊爆价的2倍！");
//				return rq;
//			}
		}
		boolean isadd=false;
		TiGroupactivity ti=null;
		if(!ObjectUtil.isEmpty(param.getGactid())){
			ti=groupactMapper.selectByPrimaryKey(param.getGactid());
		}
		if(ti==null){
			ti=	new TiGroupactivity();
			isadd=true;
		}	
		ti.setTitle(param.getTitle());
		ti.setDescription(param.getDescription());
		ti.setCompanyname(param.getCompanyname());
		ti.setLogo(param.getLogo());
		ti.setReciver(param.getReciver());
		ti.setMobilephone(param.getMobilephone());
		ti.setProvince(param.getProvince());
		ti.setCity(param.getCity());
		ti.setArea(param.getArea());
		ti.setStreetdetails(param.getStreetdetails());
		ti.setAddress(regionService.getProvinceName(param.getProvince())+regionService.getCityName(param.getCity())+regionService.getAresName(param.getArea())+param.getStreetdetails());
		ti.setPromoteruserid(userid);
		ti.setCreatetime(new Date());
		ti.setStatus(1);
		ti.setPraisecount(5);
		ti.setTimespare(3L);
		ti.setStatus(1);//默认就是已开启的活动	
		if(isadd){
			groupactMapper.insertReturnId(ti);
		}else{
			groupactMapper.updateByPrimaryKey(ti);
		}
		
		String redirct_url="groupBuy?groupId="+ti.getGactid();	
		try {
			String urlstr = ConfigUtil.getSingleValue("shareulr-base")+"uid="+URLEncoder.encode(userid.toString(),"utf-8")+"&redirct_url="+URLEncoder.encode(redirct_url,"utf-8");
			String codeurl="https://mpic.bbyiya.com/common/generateQRcode?urlstr="+URLEncoder.encode(urlstr,"utf-8");
			ti.setLinkurl(codeurl);
			groupactMapper.updateByPrimaryKey(ti);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//先删除
		List<TiGroupactivityproducts> productList=groupproductMapper.findProductsByGActid(ti.getGactid());
		if(productList!=null&&productList.size()>0){
			for (TiGroupactivityproducts op : productList) {
				groupproductMapper.deleteByPrimaryKey(op.getId());
			}
		}
		if(param.getProductlist()!=null&&param.getProductlist().size()>0){
			for (TiGroupactivityproducts pro : param.getProductlist()) {
				TiGroupactivityproducts newproduct=new TiGroupactivityproducts();
				newproduct.setGactid(ti.getGactid());
				newproduct.setPrice(pro.getPrice());
				newproduct.setProductid(pro.getProductid());
				newproduct.setStatus(1);
				groupproductMapper.insert(newproduct);
			}
		}
		HashMap<String, Object> map=new HashMap<String, Object>();
		map.put("actid", ti.getGactid());
		rq.setBasemodle(map);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("添加分销活动成功！");
		return rq;
	}
	
	/**
	 * 分销活动列表
	 */
	public ReturnModel findGroupActivityList(int index,int size,Long userid,Integer status,String keywords){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);
		
		PageHelper.startPage(index, size);
		List<TiGroupactivity> activitylist=groupactMapper.findGroupActivityList(userid,status,keywords);
		PageInfo<TiGroupactivity> pageresult=new PageInfo<TiGroupactivity>(activitylist);
		for (TiGroupactivity ti : pageresult.getList()) {
			ti.setCreatetimestr(DateUtil.getTimeStr(ti.getCreatetime(), "yyyy-MM-dd"));
			Integer sellcount=groupactworkMapper.getCountByGActStatus(ti.getGactid(), 1);
			ti.setSellercount(sellcount);
			if(ti.getAdvertid()!=null){
				TiPromoteradvertinfo advertinfo=advertinfoMapper.selectByPrimaryKey(ti.getAdvertid());
				if(advertinfo!=null){
					//广告分享页的浏览数x2倍
					Integer readcount=(advertinfo.getReadcount()==null?0:advertinfo.getReadcount().intValue())*2;
					advertinfo.setReadcount(readcount);
					ti.setAdvertbrowsecount(readcount);
				}
			}
			
		}
		rq.setBasemodle(pageresult);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("获取列表成功！");
		return rq;
	}
	
	public ReturnModel getGroupActivityByGactid(Long userid,Integer gactid){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);
		
		HashMap<String, Object> map=new HashMap<String, Object>();
		TiGroupactivity act=groupactMapper.selectByPrimaryKey(gactid);
		List<TiGroupactivityproducts> productList=groupproductMapper.findProductsByGActid(gactid);
		map.put("act", act);
		map.put("products", productList);
		rq.setBasemodle(map);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("获取列表成功！");
		return rq;
	}
	
	
	/**
	 * 活动制作进度列表
	 */
	public ReturnModel getGroupActWorkListByGactid(int index,int size,Integer gactid,Integer addresstype,String keywords){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);
		PageHelper.startPage(index, size);
		List<TiGroupActivitysWorksVo> activitylist=groupactworkMapper.findGroupActWorkListByActId(gactid, addresstype, keywords);
		PageInfo<TiGroupActivitysWorksVo> pageresult=new PageInfo<TiGroupActivitysWorksVo>(activitylist);
		if(pageresult!=null&&pageresult.getList()!=null){
			for (TiGroupActivitysWorksVo ti : pageresult.getList()) {
				if(!ObjectUtil.isEmpty(ti.getPaytime())){
					ti.setSubmittimestr(DateUtil.getTimeStr(ti.getPaytime(), "yyyy-MM-dd"));
				}
//				//如果是上门自提
//				if(ti.getAddresstype()!=null&&ti.getAddresstype().intValue()==Integer.parseInt(AddressTypeEnum.promoteraddr.toString())){
//					if(!ObjectUtil.isEmpty(ti.getUserorderid())){
//						OUserorders order=orderMapper.selectByPrimaryKey(ti.getUserorderid());
//						ti.setPostage(order.getPostage());
//					}	
//				}
			}
		}
		rq.setBasemodle(pageresult);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("获取列表成功！");
		return rq;
	}

	//得到上门自提的总费用
	public ReturnModel getSumPostAgeByGactid(Integer gactid){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);
		Double postage=groupactworkMapper.getSumPostAgeByGactid(gactid, Integer.parseInt(AddressTypeEnum.promoteraddr.toString()));
		if(postage==null) postage=0.0;
		HashMap<String, Double> map=new HashMap<String, Double>();
		map.put("sumpostage", postage);
		rq.setBasemodle(map);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("获取列表成功！");
		return rq;
	}
		
	
	/**
	 * 设置活动的分享广告
	 */
	public ReturnModel setActsShareAdvert(Long promoterUserId,Integer gactid,Integer advertid){
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.ParamError);
		TiGroupactivity act=groupactMapper.selectByPrimaryKey(gactid);
		if(act!=null){
			act.setAdvertid(advertid);
			groupactMapper.updateByPrimaryKey(act);
			rqModel.setStatu(ReturnStatus.Success);
			rqModel.setStatusreson("配置成功！");
		}else{
			rqModel.setStatu(ReturnStatus.ParamError);
			rqModel.setStatusreson("活动ID["+gactid+"]不存在!");
		}
		return rqModel;
	}
	
}
