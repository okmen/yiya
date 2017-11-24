package com.bbyiya.service.impl.pic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.PPostmodelMapper;
import com.bbyiya.dao.PPostmodelareasMapper;
import com.bbyiya.dao.PProductsMapper;
import com.bbyiya.dao.TiProductsMapper;
import com.bbyiya.dao.UUseraddressMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.PPostmodel;
import com.bbyiya.model.PPostmodelareas;
import com.bbyiya.model.PProducts;
import com.bbyiya.model.TiProducts;
import com.bbyiya.model.UUseraddress;
import com.bbyiya.service.pic.IBasePostMgtService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.vo.PostInfoVo;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.product.PPostModelAreasVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


@Service("basePostMgtServiceImpl")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class BasePostMgtServiceImpl implements IBasePostMgtService{
	@Autowired
	private PPostmodelareasMapper postmodelareasMapper;
	@Autowired
	private PPostmodelMapper postmodelMapper;
	@Autowired
	private UUseraddressMapper addressMapper;
	@Autowired
	private PProductsMapper productsMapper;
	
	@Autowired
	private TiProductsMapper tiProductsMapper;
	
//	public ReturnModel find_postagelist(Long addressId){
//		ReturnModel rq=new ReturnModel();
//		List<PPostmodel> list=null;
//		if(addressId!=null&&addressId>0){
//			UUseraddress addr= addressMapper.get_UUserAddressByKeyId(addressId);
//			if(addr!=null){
//				 list=find_postlist(addr.getArea());
//			}
//		}
//		if(list==null||list.size()<=0)
//			list=find_postlist(null);
//		rq.setStatu(ReturnStatus.Success);
//		rq.setBasemodle(list); 
//		return rq;
//	}
	/**
	 * 获取运费模块
	 * @param addressId
	 * @param productId
	 * @return
	 */
	public ReturnModel find_postagelist(Long addressId,Long productId){
		ReturnModel rq=new ReturnModel();
		List<PPostmodel> list=new ArrayList<PPostmodel>();
		PProducts products=productsMapper.selectByPrimaryKey(productId);
		if(products!=null){
			UUseraddress addr= addressMapper.get_UUserAddressByKeyId(addressId);
			if(addr!=null){
				PPostmodel model =getPostmodel(products.getPostmodelid(),addr.getArea());
				if(model!=null){
					list.add(model);
				}
			}
			rq.setBasemodle(list);
		}
		rq.setStatu(ReturnStatus.Success);
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
	
	public ReturnModel find_postlist(Integer area,Long productId) {
		ReturnModel rq=new ReturnModel();
		List<PPostmodel> list=new ArrayList<PPostmodel>();
		PProducts products=productsMapper.selectByPrimaryKey(productId);
		if(products!=null){
			PPostmodel model = getPostmodel(products.getPostmodelid(), area);
			if (model != null) {
				list.add(model);
			}
			rq.setBasemodle(list);
		}
		rq.setStatu(ReturnStatus.Success);
		return rq;
	}
	
	/**
	 * 根据产品获取运费情况
	 */
//	public List<PPostmodel> find_postlist_ti(Integer area,Long productId) {
//		List<PPostmodel> postlist=new ArrayList<PPostmodel>();
//		TiProducts products= tiProductsMapper.selectByPrimaryKey(productId);
//		if(products!=null){
//			PPostmodel post = postmodelMapper.selectByPrimaryKey(products.getPostmodelid());
//			if (post != null) {
//				if (area != null && area > 0) {
//					PPostmodelareas areamod = postmodelareasMapper.getPostAreaModel(post.getPostmodelid(), area);
//					if (areamod != null) {
//						post.setAmount(areamod.getAmount());
//					}
//				}
//				postlist.add(post);
//			}
//			return postlist;
//		}
//		return null;
//	}
	
	public ReturnModel find_postlist_ti(Integer area,Long productId) {
		ReturnModel rq=new ReturnModel();
		List<PPostmodel> postlist=new ArrayList<PPostmodel>();
		TiProducts products= tiProductsMapper.selectByPrimaryKey(productId);
		if(products!=null){
			PPostmodel post = postmodelMapper.selectByPrimaryKey(products.getPostmodelid());
			if (post != null) {
				if (area != null && area > 0) {
					PPostmodelareas areamod = postmodelareasMapper.getPostAreaModel(post.getPostmodelid(), area);
					if (areamod != null) {
						post.setAmount(areamod.getAmount());
					}
				}
				postlist.add(post);
			}
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(postlist); 
		}
		return rq;
	}
	
	public ReturnModel find_postlist_ti(Long addressId,Long productId) {
		ReturnModel rq=new ReturnModel();
		List<PPostmodel> postlist=new ArrayList<PPostmodel>();
		TiProducts products= tiProductsMapper.selectByPrimaryKey(productId);
		if(products!=null){
			PPostmodel post = postmodelMapper.selectByPrimaryKey(products.getPostmodelid());
			if (post != null) {
				UUseraddress addr= addressMapper.get_UUserAddressByKeyId(addressId);
				if(addr!=null){
					PPostmodelareas areamod = postmodelareasMapper.getPostAreaModel(post.getPostmodelid(), addr.getArea());
					if (areamod != null) {
						post.setAmount(areamod.getAmount());
					}
				}
				postlist.add(post);
			}
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(postlist); 
		}
		return rq;
		
	}
	
	public Double getPostAge_ti(Long addressId,Long productId){
		TiProducts products= tiProductsMapper.selectByPrimaryKey(productId);
		if(products!=null){
			PPostmodel post = postmodelMapper.selectByPrimaryKey(products.getPostmodelid());
			if (post != null) {
				UUseraddress addr= addressMapper.get_UUserAddressByKeyId(addressId);
				if(addr!=null){
					PPostmodelareas areamod = postmodelareasMapper.getPostAreaModel(post.getPostmodelid(), addr.getArea());
					if (areamod != null) {
						return areamod.getAmount();
					}
				}
			}
			return post.getAmount();
		}
		return 10d;
	}
	
	public PPostmodel getPostmodel(Integer postModelId,Integer areaId){
		PPostmodel model=postmodelMapper.selectByPrimaryKey(postModelId);
		if(model!=null){
			if(areaId!=null&&areaId.intValue()>0){
				PPostmodelareas areamod = postmodelareasMapper.getPostAreaModel(postModelId, areaId);
				if(areamod!=null){
					model.setAmount(areamod.getAmount());
				}
			}
		}
		return model;
	}

	public ReturnModel addPostmodel(Long userid,String name,Double amount){
		ReturnModel rq=new ReturnModel();
		PPostmodel model=new PPostmodel();
		model.setAmount(amount);
		model.setName(name);
		model.setCreatetime(new Date());
		model.setType(1);
		model.setUserid(userid);
		postmodelMapper.insertSelective(model);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("");
		return rq;
	}
	
	public ReturnModel editPostmodel(int postModelId,String name,Double amount){
		ReturnModel rq=new ReturnModel();
		PPostmodel model=postmodelMapper.selectByPrimaryKey(postModelId);
		model.setAmount(amount);
		model.setName(name);
		postmodelMapper.updateByPrimaryKeySelective(model);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("");
		return rq;
	}
	

	public ReturnModel find_PostModelAreaslist(int index,int size,Integer areacode,String areaname) {
		ReturnModel rq=new ReturnModel();
		PageHelper.startPage(index, size);
		List<PPostModelAreasVo> postList = postmodelareasMapper.findAllPostModelAreas(areacode, areaname);
		PageInfo<PPostModelAreasVo> result=new PageInfo<PPostModelAreasVo>(postList);
		
		if(result!=null&&result.getList()!=null&&result.getList().size()>0){
			for (PPostModelAreasVo pvo : result.getList()) {
				PPostmodel model=postmodelMapper.selectByPrimaryKey(pvo.getPostmodelid());
				if(model!=null)
					pvo.setPostModelName(model.getName());
			}
		}
		rq.setBasemodle(result);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("获取列表成功");
		return rq;
	}
	
	public ReturnModel addPostModelAreas(String postModelJson){
		ReturnModel rq=new ReturnModel();
		JSONObject obj = JSONObject.fromObject(postModelJson);
		if(obj==null){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("抱歉！参数错误");
			return rq;
		}
		//判断areacode，areaname是否存在
		PPostmodelareas modelarea=postmodelareasMapper.getPostAreaModel(obj.getInt("postmodelid"), obj.getInt("areacode"));
		if(modelarea!=null){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("已存在该区域的邮费模板!");
			return rq;
		}
		PPostmodelareas areas=new PPostmodelareas();
		areas.setAmount(obj.getDouble("amount"));
		areas.setAreacode(obj.getInt("areacode"));
		areas.setAreaname(obj.getString("areaname"));
		areas.setCreatetime(new Date());
		areas.setPostmodelid(obj.getInt("postmodelid"));
		postmodelareasMapper.insert(areas);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("保存成功！");
		return rq;
		
	}
	

	public ReturnModel editPostModelAreas(String postModelJson){
		ReturnModel rq=new ReturnModel();
		JSONObject obj = JSONObject.fromObject(postModelJson);
		if(obj==null){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("抱歉！参数错误");
			return rq;
		}
		PPostmodelareas areas=new PPostmodelareas();
		areas.setAmount(obj.getDouble("amount"));
		areas.setAreacode(obj.getInt("areacode"));
		areas.setAreaname(obj.getString("areaname"));
		areas.setCreatetime(new Date());
		areas.setPostmodelid(obj.getInt("postmodelid"));
		postmodelareasMapper.updateByPrimaryKeySelective(areas);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("修改成功！");
		return rq;
		
	}
	
	public ReturnModel delPostModelAreas(Integer postId){
		ReturnModel rq=new ReturnModel();
		postmodelareasMapper.deleteByPrimaryKey(postId);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("删除成功！");
		return rq;
		
	}
	/**
	 * 配置文件中读取物流公司信息
	 * @return
	 */
	public ReturnModel getPostInfo(){
		List<PostInfoVo> list=new ArrayList<PostInfoVo>();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, String>> mapList=ConfigUtil.getMaplist("postInfo");
		if(mapList!=null&&mapList.size()>0){
			for (Map<String, String> mapStyle : mapList) {
				PostInfoVo vo=new PostInfoVo();
				vo.setCode(mapStyle.get("code"));
				vo.setName(mapStyle.get("name"));
				list.add(vo);
			}
		}
		ReturnModel rq = new ReturnModel();
		map.put("list", list);
		rq.setBasemodle(map);
		rq.setStatu(ReturnStatus.Success);
		return rq;
	}
	
}
