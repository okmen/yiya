package com.bbyiya.pic.service.impl.calendar;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.TiProductsMapper;
import com.bbyiya.dao.TiPromoteradvertcoustomerMapper;
import com.bbyiya.dao.TiPromoteradvertimgsMapper;
import com.bbyiya.dao.TiPromoteradvertinfoMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.TiPromoteradvertcoustomer;
import com.bbyiya.model.TiPromoteradvertimgs;
import com.bbyiya.model.TiPromoteradvertinfo;
import com.bbyiya.pic.service.calendar.IIbs_TiPromoterAdvertShareService;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;



@Service("ibs_TiPromoterAdvertShareService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Ibs_TiPromoterAdvertShareServiceImpl implements IIbs_TiPromoterAdvertShareService{
	
	@Autowired
	private TiProductsMapper productMapper;
	@Autowired
	private TiPromoteradvertinfoMapper advertinfoMapper;
	@Autowired
	private TiPromoteradvertimgsMapper advertimgsMapper;
	
	@Autowired
	private TiPromoteradvertcoustomerMapper coustomerMapper;
	/*-------------------用户信息------------------------------------------------*/
	@Autowired
	private UUsersMapper usersMapper;
	
	/**
	 * 推广商分享广告报名客户列表
	 */
	public ReturnModel getPromoteradvertCoustomer(Long promoterUserId,int index,int size){
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.Success);
		TiPromoteradvertinfo advertinfo=advertinfoMapper.getModelByPromoterUserId(promoterUserId);
		if(advertinfo!=null){
			PageHelper.startPage(index, size);
			List<TiPromoteradvertcoustomer> coustomerlist=coustomerMapper.selectListByAdvertId(advertinfo.getAdvertid());
			PageInfo<TiPromoteradvertcoustomer> result=new PageInfo<TiPromoteradvertcoustomer>(coustomerlist);
			rqModel.setBasemodle(result);
		}
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("获取成功！");
		return rqModel;
	}
	/**
	 * 加载推广商分享广告信息
	 */
	public ReturnModel getPromoterShareAdvert(Long promoterUserId){
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.Success);

		TiPromoteradvertinfo advertinfo=advertinfoMapper.getModelByPromoterUserId(promoterUserId);
		
		if(advertinfo!=null){
			if(advertinfo.getUpdatetime()!=null){
				advertinfo.setUpdatetimestr(DateUtil.getTimeStr(advertinfo.getUpdatetime(), "yyyy-MM-dd HH:mm:ss"));
			}else{
				advertinfo.setUpdatetimestr(DateUtil.getTimeStr(advertinfo.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
				
			}
			List<TiPromoteradvertimgs> advertimgslist=advertimgsMapper.findImgsByAdvertId(advertinfo.getAdvertid());
			advertinfo.setImglist(advertimgslist);
		}
		rqModel.setBasemodle(advertinfo);		
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("获取成功！");
		return rqModel;
	}
	/**
	 * 分享广告设置
	 */
	public ReturnModel addOrEditShareAdvert(Long promoterUserId,TiPromoteradvertinfo advertinfo,List<TiPromoteradvertimgs> advertimgs ){
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.ParamError);
		if(ObjectUtil.isEmpty(advertinfo.getDescription())){
			rqModel.setStatu(ReturnStatus.ParamError);
			rqModel.setStatusreson("广告主题不能为空，请填写广告主题！");
			return rqModel;
		}
		if(ObjectUtil.isEmpty(advertinfo.getDefaultimg())){
			rqModel.setStatu(ReturnStatus.ParamError);
			rqModel.setStatusreson("顶部广告图不能为空，请上传顶部广告图！");
			return rqModel;
		}
		if(advertimgs==null||advertimgs.size()<=0){
			rqModel.setStatu(ReturnStatus.ParamError);
			rqModel.setStatusreson("详情图不能为空，请至少上传一张详情图！");
			return rqModel;
		}
		boolean isadd=false;
		TiPromoteradvertinfo advert=null;
		if(!ObjectUtil.isEmpty(promoterUserId)){
			advert=advertinfoMapper.getModelByPromoterUserId(promoterUserId);
		}
		if(advert==null){
			advert=new TiPromoteradvertinfo();
			advert.setCreatetime(new Date());
			advert.setUpdatetime(new Date());
			isadd=true;
		}
		advert.setDefaultimg(advertinfo.getDefaultimg());
		advert.setDescription(advertinfo.getDescription());
		advert.setOpenapplication(advertinfo.getOpenapplication());
		advert.setStatus(1);
		advert.setUpdatetime(new Date());
		advert.setPromoteruserid(promoterUserId);		
		if(isadd){
			advertinfoMapper.insertReturnId(advert);
		}else{
			advertinfoMapper.updateByPrimaryKey(advert);
		}
		//先删除后插入
		List<TiPromoteradvertimgs> advertimgslist=advertimgsMapper.findImgsByAdvertId(advert.getAdvertid());
		if(advertimgslist!=null&&advertimgslist.size()>0){
			for (TiPromoteradvertimgs img : advertimgslist) {
				advertimgsMapper.deleteByPrimaryKey(img.getId());
			}
		}
		
		if(advertimgs!=null&&advertimgs.size()>0){
			int sort=1;
			for (TiPromoteradvertimgs img : advertimgs) {
				img.setCreatetime(new Date());
				img.setAdvertid(advert.getAdvertid());
				img.setSort(sort);
				advertimgsMapper.insert(img);
				sort++;
			}
		}
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("操作成功！");
		return rqModel;
	}
	
	/**
	 * 清除设置
	 */
	public ReturnModel resetAdvertInfo(Long promoterUserId){
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.ParamError);
		TiPromoteradvertinfo advertinfo=advertinfoMapper.getModelByPromoterUserId(promoterUserId);
		if(advertinfo!=null){
			//advertinfoMapper.deleteByPrimaryKey(advertinfo.getAdvertid());
			advertinfo.setDefaultimg("");
			advertinfo.setDescription("");
			advertinfo.setImglist(null);
			advertinfo.setOpenapplication(0);
			advertinfo.setStatus(0);
			advertinfoMapper.updateByPrimaryKey(advertinfo);
			List<TiPromoteradvertimgs> advertimgslist=advertimgsMapper.findImgsByAdvertId(advertinfo.getAdvertid());
			if(advertimgslist!=null&&advertimgslist.size()>0){
				for (TiPromoteradvertimgs img : advertimgslist) {
					advertimgsMapper.deleteByPrimaryKey(img.getId());
				}
			}
			rqModel.setStatu(ReturnStatus.Success);
			rqModel.setStatusreson("清除成功！");
		}

		return rqModel;
	}

}
