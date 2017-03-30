package com.bbyiya.service.impl.pic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.PPostmodelMapper;
import com.bbyiya.dao.PPostmodelareasMapper;
import com.bbyiya.dao.UUseraddressMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.PPostmodel;
import com.bbyiya.model.PPostmodelareas;
import com.bbyiya.model.UUseraddress;
import com.bbyiya.service.pic.IBasePostMgtService;
import com.bbyiya.vo.ReturnModel;


@Service("basePostMgtServiceImpl")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class BasePostMgtServiceImpl implements IBasePostMgtService{
	@Autowired
	private PPostmodelareasMapper postmodelareasMapper;
	@Autowired
	private PPostmodelMapper postmodelMapper;
	@Autowired
	private UUseraddressMapper addressMapper;
	
	public ReturnModel find_postagelist(Long addressId){
		ReturnModel rq=new ReturnModel();
		List<PPostmodel> list=null;
		if(addressId!=null&&addressId>0){
			UUseraddress addr= addressMapper.get_UUserAddressByKeyId(addressId);
			if(addr!=null){
				 list=find_postlist(addr.getArea());
			}
		}
		if(list==null||list.size()<=0)
			list=find_postlist(null);
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(list); 
		return rq;
	}
	
	
	public List<PPostmodel> find_postlist(Integer area) {
		List<PPostmodel> postList = postmodelMapper.findAllModels();
		if (postList != null && postList.size() > 0) {
			if(area!=null&&area>0){
				for (PPostmodel post : postList) {
					PPostmodelareas areamod = postmodelareasMapper.getPostAreaModel(post.getPostmodelid(), area);
					if (areamod != null) {
						post.setAmount(areamod.getAmount());
					}
				}
			}
		}
		return postList;
	}
	
	public PPostmodel getPostmodel(Integer postModelId,Integer areaId){
		PPostmodel model=postmodelMapper.selectByPrimaryKey(postModelId);
		if(model!=null){
			PPostmodelareas areamod = postmodelareasMapper.getPostAreaModel(postModelId, areaId);
			if(areamod!=null){
				model.setAmount(areamod.getAmount());
			}
		}
		return model;
	}
	
	
}
