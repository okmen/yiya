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
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.calendar.ActivityWorksStatusEnum;
import com.bbyiya.enums.calendar.TiActivityTypeEnum;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.TiGroupactivity;
import com.bbyiya.model.TiGroupactivityproducts;
import com.bbyiya.model.TiGroupactivityworks;
import com.bbyiya.model.TiProducts;
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



@Service("ibs_CalendarActivityService")
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
		String redirct_url="groupBuy?groupId="+ti.getGactid();	
		try {
			String urlstr = ConfigUtil.getSingleValue("shareulr-base")+"uid="+URLEncoder.encode(userid.toString(),"utf-8")+"&redirct_url="+URLEncoder.encode(redirct_url,"utf-8");
			String codeurl="https://mpic.bbyiya.com/common/generateQRcode?urlstr="+URLEncoder.encode(urlstr,"utf-8");
			ti.setLinkurl(codeurl);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ti.setStatus(1);//默认就是已开启的活动	
		if(isadd){
			groupactMapper.insertReturnId(ti);
		}else{
			groupactMapper.updateByPrimaryKey(ti);
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
			ti.setActclickcount(0);
		}
		rq.setBasemodle(pageresult);
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
				
				ti.setSubmittimestr(DateUtil.getTimeStr(ti.getSubmittime(), "yyyy-MM-dd"));
				if(!ObjectUtil.isEmpty(ti.getUserorderid())){
					OUserorders order=orderMapper.selectByPrimaryKey(ti.getUserorderid());
					ti.setPostage(order.getPostage());

				}	
			}
		}
		rq.setBasemodle(pageresult);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("获取列表成功！");
		return rq;
	}
	
	
}
